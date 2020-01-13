package com.coder.nettychat.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/4 18:18
 */
public class JsonUtil {

    private static GsonBuilder builder = new GsonBuilder();
    static {
        // 禁用Html的序列化
        builder.disableHtmlEscaping();
    }

    /**
     * POJO的序列化
     * @return 使用Google的Gson框架
     */
    public static String convertToJson(Object obj){
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

    /**
     * 将json反序列化为POJO
     * 使用阿里的FastJson
     * @param json 要反序列化的json
     * @param clazz 要反序列化的原型
     * @param <T> 泛型
     * @return 反序列化后的POJO
     */
    public static <T>T jsonToPojo(String json, Class<T> clazz){
        return JSONObject.parseObject(json, clazz);
    }

    /**
     * 将JSON反序列化为POJO对象列表
     * @param json 要反序列化的JSON字符串
     * @param clazz POJO类型
     * @return 对象列表
     */
    public static <T>List<T> jsonToList(String json, Class<T> clazz){
        return JSONObject.parseArray(json, clazz);
    }
}