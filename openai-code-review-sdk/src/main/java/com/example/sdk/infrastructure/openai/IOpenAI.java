package com.example.sdk.infrastructure.openai;

import com.example.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import com.example.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

/**
 * @Author cxj
 * @Date 2025/4/27 14:32
 * @Description:
 */
public interface IOpenAI {

    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception;

}
