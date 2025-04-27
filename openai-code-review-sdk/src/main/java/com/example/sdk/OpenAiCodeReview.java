package com.example.sdk;

import com.alibaba.fastjson2.JSON;
import com.example.sdk.domain.service.impl.OpenAiCodeReviewService;
import com.example.sdk.infrastructure.git.GitCommand;
import com.example.sdk.infrastructure.openai.IOpenAI;
import com.example.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import com.example.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import com.example.sdk.infrastructure.openai.impl.ChatGLM;
import com.example.sdk.infrastructure.weixin.WeiXin;
import com.example.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import com.example.sdk.domain.model.Model;
import com.example.sdk.types.utils.BearerTokenUtils;
import com.example.sdk.types.utils.WXAccessTokenUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * @Author cxj
 * @Date 2025/4/21 17:10
 * @Description:
 */
public class OpenAiCodeReview {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

    // 配置配置
    private String weixin_appid = "wxe1c69630fb5ab489";
    private String weixin_secret = "749910b9d373bd4c43573ebdb0b73df9";
    private String weixin_touser = "osj_x7De4VJR_l117Ncf2J2Y4XoU";
    private String weixin_template_id = "q9sUzLg0CebeZfHq2lGAw1mng9FzmoKTya3w4pwDhCc";

    // ChatGLM 配置
    private String chatglm_apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private String chatglm_apiKeySecret = "1cbf7e694e644fd28965d163faac8572.DixpIV4y86LtigjI";

    // Github 配置
    private String github_review_log_uri;
    private String github_token;

    // 工程配置 - 自动获取
    private String github_project;
    private String github_branch;
    private String github_author;

    //

    public static void main(String[] args) throws IOException, InterruptedException, GitAPIException {
        GitCommand gitCommand = new GitCommand(
                getEvn("GITHUB_REVIEW_LOG_URI"),
                getEvn("GITHUB_TOKEN"),
                getEvn("COMMIT_PROJECT"),
                getEvn("COMMIT_BRANCH"),
                getEvn("COMMIT_AUTHOR"),
                getEvn("COMMIT_MESSAGE")
        );

        WeiXin weiXin = new WeiXin(
                getEvn("WEIXIN_APPID"),
                getEvn("WEIXIN_SECRET"),
                getEvn("WEIXIN_TOUSER"),
                getEvn("WEIXIN_TEMPLATE_ID")
        );

        IOpenAI openAI = new ChatGLM(getEvn("CHATGLM_APIHOST"), getEvn("CHATGLM_APIKEYSECRET"));

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, openAI, weiXin);
        openAiCodeReviewService.exec();

        logger.info("openai-code-review done!");

//        System.out.println("openai 代码评审，测试执行");
//
//        String token = System.getenv("GITHUB_TOKEN");
//        if (null == token || token.isEmpty()) {
//            throw new RuntimeException("token is null");
//        }
    }

    private static String getEvn(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException("value is null");
        }
        return value;
    }

//    public static void main(String[] args) throws IOException, InterruptedException, GitAPIException {
//        System.out.println("openai 代码评审，测试执行");
//
//        String token = System.getenv("GITHUB_TOKEN");
//        if (null == token || token.isEmpty()){
//            throw new RuntimeException("token is null");
//        }
//
//        // 1.代码检出
//        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
//        processBuilder.directory(new File("."));
//
//        Process process = processBuilder.start();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//
//        StringBuilder diffCode = new StringBuilder();
//        while ((line = reader.readLine()) != null) {
//            diffCode.append(line);
//        }
//
//        int exitCode = process.waitFor();
//        System.out.println("Exited with code:" + exitCode);
//
//        System.out.println("diff code："+diffCode.toString());
//
//        // 2. chatglm 代码评审
//        String log = codeReview(diffCode.toString());
//        System.out.println("code review:" + log);
//
//        // 3.写入评审日志
//        String logUrl = writeLog(token, log);
//        System.out.println("writeLog: " + logUrl);
//
//        // 4.消息通知
//        System.out.println("pushMessage: " + logUrl);
//        pushMessage(logUrl);
//
//    }
//
//    private static void pushMessage(String logUrl) {
//        String accessToken = WXAccessTokenUtils.getAccessToken();
//        System.out.println(accessToken);
//
//        TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO();
//        templateMessageDTO.put("project", "big-market");
//        templateMessageDTO.put("review", logUrl);
//        templateMessageDTO.setUrl(logUrl);
//
//        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
//        sendPostRequest(url, JSON.toJSONString(templateMessageDTO));
//    }
//
//    private static void sendPostRequest(String urlString, String jsonBody) {
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
//                String response = scanner.useDelimiter("\\A").next();
//                System.out.println(response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String codeReview(String diffCode) throws IOException {
//        String apikeySecret = "1cbf7e694e644fd28965d163faac8572.DixpIV4y86LtigjI";
//        String token = BearerTokenUtils.getToken(apikeySecret);
//
//        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
//        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Authorization", "Bearer " + token);
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        connection.setDoOutput(true);
//
////        String code = "1+1";
//
//        String jsonInputString = "{" +
//                "\"model\":\"glm-4-flash\"," +
//                "\"messages\": [" +
//                "   {" +
//                "       \"role\":\"user\"," +
//                "       \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + diffCode + "\"" +
//                "   }" +
//                "]" +
//                "}";
//
//        ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
//        chatCompletionRequest.setModel(Model.GLM_4_FLASH.getCode());
//
//        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>(){{
//            add(new ChatCompletionRequestDTO.Prompt("user", "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码如下:"));
//            add(new ChatCompletionRequestDTO.Prompt("user", diffCode));
//
//        }});
//
//        try(OutputStream os = connection.getOutputStream()){
//            byte[] input = JSON.toJSONString(chatCompletionRequest).getBytes(StandardCharsets.UTF_8);
//            os.write(input);
//        }
//
//        int responseCode = connection.getResponseCode();
//        System.out.println(responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null){
//            content.append(inputLine);
//        }
//
//        in.close();
//        connection.disconnect();
//
//        System.out.println(content);
//
//        ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
////        System.out.println(response.getChoices().get(0).getMessage().getContent());
//        return response.getChoices().get(0).getMessage().getContent();
//    }
//
//    private static String writeLog(String token, String log) throws GitAPIException, IOException {
//
//        Git git = Git.cloneRepository()
//                .setURI("https://github.com/1186982585/openai-code-review-log")
//                .setDirectory(new File("repo"))
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
//                .call();
//
//        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        File dateFolder = new File("repo/" + dateFolderName);
//        if (!dateFolder.exists()) {
//            dateFolder.mkdirs();
//        }
//
//        String fileName = generateRandomString(12) + ".md";
//        File newFile = new File(dateFolder, fileName);
//        try(FileWriter writer = new FileWriter(newFile)) {
//            writer.write(log);
//        }
//
//        git.add().addFilepattern(dateFolderName + "/" + fileName).call();
//        git.commit().setMessage("Add new file").call();
//        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();
//
//        return "https://github.com/1186982585/openai-code-review-log/blob/master/"+dateFolderName+"/"+fileName;
//    }
//
//    private static String generateRandomString(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            sb.append(characters.charAt(random.nextInt(characters.length())));
//        }
//        return sb.toString();
//    }

}
