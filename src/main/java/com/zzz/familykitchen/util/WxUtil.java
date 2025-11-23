package com.zzz.familykitchen.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
// 必须导入Spring的@Value注解
import org.springframework.beans.factory.annotation.Value;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 【重要】调用微信API
 */
@Slf4j
@Component
public class WxUtil {

    @Value("${wx.miniapp.app-id}")
    private String appId;

    @Value("${wx.miniapp.app-secret}")
    private String appSecret;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 微信API地址
    private static final String WECHAT_API_BASE = "https://api.weixin.qq.com";

    /**
     * code换session_key和openid
     * 文档: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     */
    /**
     * 登录凭证校验 - 获取用户openid和session_key
     * @param code 小程序端调用wx.login()获取的code
     * @return 包含openid和session_key的Map
     */
    public Map<String, String> code2Session(String code) throws IOException {
        String url = String.format(
                "%s/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WECHAT_API_BASE, appId, appSecret, code
        );

        String response = doGet(url);
        JsonNode jsonNode = objectMapper.readTree(response);

        if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
            throw new IOException("登录失败: " + jsonNode.get("errmsg").asText());
        }

        Map<String, String> result = new HashMap<>();
        result.put("openid", jsonNode.get("openid").asText());
        result.put("session_key", jsonNode.get("session_key").asText());

        return result;
    }

    /**
     * 获取Access Token
     */
    public String getAccessToken() throws IOException {
        String url = String.format(
                "%s/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                WECHAT_API_BASE, appId, appSecret
        );

        String response = doGet(url);
        JsonNode jsonNode = objectMapper.readTree(response);

        if (jsonNode.has("errcode")) {
            throw new IOException("获取token失败: " + response);
        }

        return jsonNode.get("access_token").asText();
    }

    /**
     * 上传图片到微信服务器(临时素材)
     * @param file 图片文件
     * @return 微信media_id
     */
    public String uploadImage(MultipartFile file) throws IOException {
        String token = getAccessToken();
        String url = WECHAT_API_BASE + "/cgi-bin/media/upload?access_token="
                + token + "&type=image";

        RequestBody fileBody = RequestBody.create(
                file.getBytes(),
                MediaType.parse("image/*")
        );

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("media", file.getOriginalFilename(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseStr = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseStr);

            if (jsonNode.has("errcode")) {
                throw new IOException("上传失败: " + responseStr);
            }

            return jsonNode.get("media_id").asText();
        }
    }

    /**
     * 发送GET请求
     */
    private String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败: " + response);
            }
            return response.body().string();
        }
    }
}