package com.chaos.util;

import java.lang.reflect.Field;
import java.util.*;

public class MapUtils {

    /**
     * Map转换为 Entity
     *
     * @param params 包含参数的Map
     * @param t      需要赋值的实体
     * @param <T>    类型
     */
    public static <T> T mapToEntity(Map<String, Object> params, T t) {
        if (null == params) {
            return t;
        }
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                if (null != params.get(name)) {
                    declaredField.set(t, params.get(name));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("属性设置失败！");
        }
        return t;
    }

    /**
     * 将对象转换为HashMap
     *
     * @param t   转换为Map的对象
     * @param <T> 转换为Map的类
     * @return Map
     */
    public static <T> Map<String, Object> entityToMap(T t) {
        Class<?> clazz = t.getClass();
        List<Field> allField = getAllField(clazz);
        Map<String, Object> hashMap = new LinkedHashMap<>(allField.size());
        try {
            for (Field declaredField : allField) {
                declaredField.setAccessible(true);
                Object o = declaredField.get(t);
                if (null != o) {
                    hashMap.put(declaredField.getName(), o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("属性获取失败！");
        }
        return hashMap;
    }

    /**
     * 获取所有属性
     *
     * @param clazz class
     * @param <T>   泛型
     * @return List<Field>
     */
    public static <T> List<Field> getAllField(Class<T> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> superClazz = clazz;
        while (null != superClazz) {
            fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));
            superClazz = superClazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 将Map参数转换为字符串
     *
     * @param map
     * @return
     */
    public static String mapToString(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        map.forEach((key, value) -> {
            sb.append(key).append("=").append(value.toString()).append("&");
        });
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * 将Bean对象转换Url请求的字符串
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String getUrlByBean(T t) {
        String pre = "?";
        Map<String, Object> map = entityToMap(t);
        return pre + mapToString(map);
    }

}
