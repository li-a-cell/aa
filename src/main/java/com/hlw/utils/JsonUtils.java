package com.hlw.utils;

import org.json.JSONObject;

public class JsonUtils {

    /**
     * 从JSON字符串中获取指定键的值
     * @param jsonString JSON格式的字符串
     * @param key 要查询的键
     * @return 键对应的值，如果键不存在则返回null
     */
    public static String getValueFromJson(String jsonString, String key) {
        try {
            // 解析JSON字符串
            JSONObject jsonObject = new JSONObject(jsonString);
            // 获取键值
            return jsonObject.getString(key);
        } catch (Exception e) {
            // 处理可能的异常，如键不存在或解析错误
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}