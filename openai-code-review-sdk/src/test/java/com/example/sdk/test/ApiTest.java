package com.example.sdk.test;

import com.alibaba.fastjson2.JSON;
import com.example.sdk.domain.model.ChatCompletionSyncResponse;
import com.example.sdk.types.utils.BearerTokenUtils;
import com.example.sdk.types.utils.WXAccessTokenUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    @Test
    public void test_wx(){
        String accessToken = WXAccessTokenUtils.getAccessToken();
        System.out.println(accessToken);

        Message message = new Message();
        message.put("project","big-market");
        message.put("review","feat: 新加功能");

        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
        sendPostRequest(url, JSON.toJSONString(message));
    }

    private static void sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                String response = scanner.useDelimiter("\\A").next();
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Message {
        private String touser = "osj_x7De4VJR_l117Ncf2J2Y4XoU";
        private String template_id = "c_Teadl9D8dt4l-vv5J4-GxXzoM4W4UT18lwafME_qQ";
        private String url = "https://github.com/1186982585/openai-code-review-log/blob/master/2025-04-27/jlR98ZRpJq1q.md";
        private Map<String, Map<String, String>> data = new HashMap<>();

        public void put(String key, String value){
            data.put(key, new HashMap<String, String>(){
                {put("value", value);}
            });
        }

        public String getTouser() {
            return touser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, Map<String, String>> getData() {
            return data;
        }

        public void setData(Map<String, Map<String, String>> data) {
            this.data = data;
        }
    }

}
