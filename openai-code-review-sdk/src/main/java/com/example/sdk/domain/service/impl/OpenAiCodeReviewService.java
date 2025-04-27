package com.example.sdk.domain.service.impl;

import com.example.sdk.domain.model.Model;
import com.example.sdk.domain.service.AbstractOpenAiCodeReviewService;
import com.example.sdk.infrastructure.git.GitCommand;
import com.example.sdk.infrastructure.openai.IOpenAI;
import com.example.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import com.example.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import com.example.sdk.infrastructure.weixin.WeiXin;
import com.example.sdk.infrastructure.weixin.dto.TemplateMessageDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author cxj
 * @Date 2025/4/27 16:24
 * @Description:
 */
public class OpenAiCodeReviewService extends AbstractOpenAiCodeReviewService {

    public OpenAiCodeReviewService(GitCommand gitCommand, IOpenAI openAI, WeiXin weiXin) {
        super(gitCommand, openAI, weiXin);
    }

    @Override
    protected String getDiffCode() throws IOException, InterruptedException {
        return gitCommand.diff();
    }

    @Override
    protected String codeReview(String diffCode) throws Exception {
        ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
        chatCompletionRequest.setModel(Model.GLM_4_FLASH.getCode());

        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>(){{
            add(new ChatCompletionRequestDTO.Prompt("user", "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码如下:"));
            add(new ChatCompletionRequestDTO.Prompt("user", diffCode));

        }});

        ChatCompletionSyncResponseDTO completions = openAI.completions(chatCompletionRequest);
        ChatCompletionSyncResponseDTO.Message message = completions.getChoices().get(0).getMessage();
        return message.getContent();
    }

    @Override
    protected String recordCodeReview(String recommend) throws Exception {
        return gitCommand.commitAndPush(recommend);
    }

    @Override
    protected void pushMessage(String logUrl) throws IOException {
        Map<String, Map<String, String>> data = new HashMap<>();
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.REPO_NAME, gitCommand.getProject());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.BRANCH_NAME, gitCommand.getBranch());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_AUTHOR, gitCommand.getAuthor());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_MESSAGE, gitCommand.getMessage());
        weiXin.sentTemplateMessage(logUrl, data);
    }
}
