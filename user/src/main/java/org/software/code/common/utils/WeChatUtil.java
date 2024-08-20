package org.software.code.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class WeChatUtil {

    @Value("${spring.wechat.appid}")
    private String appIdNonStatic;

    @Value("${spring.wechat.secret}")
    private String secretNonStatic;

    private static String appId;
    private static String secret;

    @PostConstruct
    public void init() {
        appId = this.appIdNonStatic;
        secret = this.secretNonStatic;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getSecret() {
        return secret;
    }


//    private static String appId = "wxdad176e26bf8dce0";
//    private static String secret = "cd8995dbd0fb88a1731381ad0ceb3ca9";

//    @Value("${spring.wechat.appid}")
//    private static String appId ;
//    @Value("${spring.wechat.secret}")
//    private static String secret;

    public static String getOpenIDFromWX(String code) throws RuntimeException {
        try {
            String wxApiUrl = "https://api.weixin.qq.com/sns/jscode2session";
            String grantType = "authorization_code";

            RestTemplate restTemplate = new RestTemplate();
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=%s", wxApiUrl, appId, secret, code, grantType);
            String openid = ""; // 初始化 openid
            String response = restTemplate.getForObject(url, String.class); // 发送 GET 请求，获取微信接口返回的数据
            ObjectMapper objectMapper = new ObjectMapper(); //解析 JSON 数据，获取 openid
            JsonNode jsonNode = objectMapper.readTree(response);
            System.out.println(url);
            System.out.println(jsonNode);
            openid = jsonNode.get("openid").asText(); // 实际环境中应从jsonNode中获取实际的openid
//            openid = "openid-" + code; // 暂时用code生成的模拟openid（例如在开发或测试环境中使用）
            return openid;
        } catch (Exception e) {
            throw new RuntimeException("用户异常，请稍后重试");
        }
    }

    public static void main(String[] args) {
        String code = "0b3dKr200cC7uS1UvL300wI1a73dKr2g";
        getOpenIDFromWX(code);
    }
}

