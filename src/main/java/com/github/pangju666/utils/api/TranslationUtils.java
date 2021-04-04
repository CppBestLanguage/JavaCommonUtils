package com.github.pangju666.utils.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 英汉翻译工具，基于有道云api
 *
 * @author nullptr
 * @version 1.0 2020-10-9
 * @since 1.0 2020-10-9
 */
public class TranslationUtils {
    private static final String API_URL = "https://openapi.youdao.com/api";
    private static String APP_KEY;
    private static String APP_SECRET;
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static void initConfig(String appKey, String appSecret) {
        APP_KEY = appKey;
        APP_SECRET = appSecret;
    }

    public JSONObject toChinese(String word) {
        return doRequest(initParams("en", "zh_CN", word));
    }

    public JSONObject toEnglish(String word) {
        return doRequest(initParams("zh_CN", "en", word));
    }

    private List<NameValuePair> initParams(String from, String to, String word) {
        List<NameValuePair> paramList = new ArrayList<>();
        String salt = String.valueOf(System.currentTimeMillis());
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
        paramList.add(new BasicNameValuePair("from", from));
        paramList.add(new BasicNameValuePair("to", to));
        paramList.add(new BasicNameValuePair("signType", "v3"));
        paramList.add(new BasicNameValuePair("curtime", currentTime));
        paramList.add(new BasicNameValuePair("appKey", APP_KEY));
        paramList.add(new BasicNameValuePair("q", word));
        paramList.add(new BasicNameValuePair("salt", salt));
        String signStr = APP_KEY + truncate(word) + salt + currentTime + APP_SECRET;
        paramList.add(new BasicNameValuePair("sign", getDigest(signStr)));

        return paramList;
    }

    private JSONObject doRequest(List<NameValuePair> paramList) {
        // 创建HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // httpPost
        HttpPost httpPost = new HttpPost(API_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity, "UTF-8");
            httpResponse.close();
            return parseJSON(JSONObject.fromObject(json));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject parseJSON(JSONObject object) {
        JSONObject result = new JSONObject();
        System.out.println(object);
        int errorCode = Integer.parseInt(object.getString("errorCode"));
        if (errorCode != 0) {
            result.put("error", "请输入正确的单词");
            return result;
        }
        String word = object.getString("query");
        JSONArray explanations = object.getJSONArray("translation");
        if (word.contentEquals(explanations.getString(0))) {
            result.put("error", "请输入正确的单词");
            return result;
        }
        if (object.containsKey("basic")) {
            String phonetic = object.getJSONObject("basic").getString("phonetic");
            result.put("label", "[" + phonetic + "]");
        }
        result.put("word", word);
        Object[] translations = explanations.toArray();
        result.put("explanations", StringUtils.join(translations, ","));
        return result;
    }

    /**
     * 生成加密字段
     */
    private String getDigest(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String truncate(String word) {
        if (word == null || word.isEmpty()) {
            return null;
        }
        int length = word.length();
        return length <= 20 ? word : (word.substring(0, 10) + length + word.substring(length - 10, length));
    }
}