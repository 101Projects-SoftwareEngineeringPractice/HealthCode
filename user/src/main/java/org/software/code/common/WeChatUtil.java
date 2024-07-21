package org.software.code.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class WeChatUtil {
    private static String appId = "your_app_id";
    private static String secret = "your_app_secret";

    public static String getOpenIDFromWX(String code) throws RuntimeException {
        String wxApiUrl = "https://api.weixin.qq.com/sns/jscode2session";
        String grantType = "authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=%s", wxApiUrl, appId, secret, code, grantType);
        try {
            String response = restTemplate.getForObject(url, String.class); // 发送 GET 请求，获取微信接口返回的数据
            String openid = ""; // 初始化 OpenID
            // 解析 JSON 数据，获取 openid
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            // openid = jsonNode.get("openid").asText();
            openid = "openid-" + code;
            return openid;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("用户异常，请稍后重试");
        }

    }
}
