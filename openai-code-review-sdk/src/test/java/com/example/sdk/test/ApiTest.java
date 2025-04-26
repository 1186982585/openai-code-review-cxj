package com.example.sdk.test;

import com.alibaba.fastjson2.JSON;
import com.example.sdk.domain.model.ChatCompletionSyncResponse;
import com.example.sdk.types.utils.BearerTokenUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @Author cxj
 * @Date 2025/4/21 20:40
 * @Description:
 */
public class ApiTest {

    public static void main(String[] args) {
        String apikeySecret = "1cbf7e694e644fd28965d163faac8572.DixpIV4y86LtigjI";

        String token = BearerTokenUtils.getToken(apikeySecret);
        System.out.println(token);

    }

    @Test
    public void test_http() throws IOException {
        String apikeySecret = "1cbf7e694e644fd28965d163faac8572.DixpIV4y86LtigjI";
        String token = BearerTokenUtils.getToken(apikeySecret);

        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        String code = "1+1";

        String jsonInputString = "{" +
                "\"model\":\"glm-4-flash\"," +
                "\"messages\": [" +
                "   {" +
                "       \"role\":\"user\"," +
                "       \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + code + "\"" +
                "   }" +
                "]" +
                "}";

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        System.out.println(content);

        ChatCompletionSyncResponse response = JSON.parseObject(content.toString(), ChatCompletionSyncResponse.class);
        System.out.println(response.getChoices().get(0).getMessage().getContent());
    }

}
