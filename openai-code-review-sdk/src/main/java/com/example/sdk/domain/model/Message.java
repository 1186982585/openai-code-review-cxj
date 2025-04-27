package com.example.sdk.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String touser = "osj_x7De4VJR_l117Ncf2J2Y4XoU";
    private String template_id = "c_Teadl9D8dt4l-vv5J4-GxXzoM4W4UT18lwafME_qQ";
    private String url = "https://github.com/1186982585/openai-code-review-log/blob/master/2025-04-27/jlR98ZRpJq1q.md";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
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
