package com.origin.generation.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 公共工具类
 *
 * @author alex
 * @date 2019-09-07
 */
public class CommonUtils {

    /**
     * 格式化字段名
     *
     * @param columnName 字段名
     * @return 返回格式化后的字符串
     */
    public static String fromColumnName(String columnName) {

        String[] split = columnName.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                sb.append(StringUtils.capitalize(split[i]));
            }
        }
        return sb.toString();
    }

    /**
     * 格式化类名
     *
     * @param columnName 类名
     * @return 返回格式化类名
     */
    public static String formatterClassName(String columnName) {

        String[] split = columnName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }

    /**
     * 根据数据库类型转换成JAVA类型
     *
     * @param type 数据库类型
     * @return 对应的Java类型
     */
    public static String getJavaType(String type) {

        type = type.toLowerCase();
        if ("char".equals(type) || "varchar".equals(type) || "mediumtext".equals(type) || "text".equals(type)) {
            return "String";
        } else if ("int".equals(type) || "mediumint".equals(type) || "tinyint".equals(type)) {
            return "Integer";
        } else if ("float".equals(type)) {
            return "Float";
        } else if ("double".equals(type)) {
            return "Double";
        } else if ("bigint".equals(type)) {
            return "Long";
        } else if ("decimal".equals(type)) {
            return "BigDecimal";
        } else if ("bit".equals(type)) {
            return "Boolean";
        } else if ("timestamp".equals(type) || "date".equals(type) || "datetime".equals(type)) {
            return "Date";
        }
        return null;
    }
}
