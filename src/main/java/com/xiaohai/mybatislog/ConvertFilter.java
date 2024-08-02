package com.xiaohai.mybatislog;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaohai
 * @date 2024-08-02 13:55
 **/
public class ConvertFilter {
    public static String convertLogs(String logs) {
        // 定义提取 SQL 和参数的正则表达式
        String sqlPattern = "Preparing: (.+)";
        String paramsPattern = "Parameters: (.+)";

        // 初始化变量
        String sql = "";
        String params = "";

        // 提取 SQL 语句
        Pattern pattern = Pattern.compile(sqlPattern);
        Matcher matcher = pattern.matcher(logs);
        if (matcher.find()) {
            sql = matcher.group(1).trim();
        }

        // 提取参数
        pattern = Pattern.compile(paramsPattern);
        matcher = pattern.matcher(logs);
        if (matcher.find()) {
            params = matcher.group(1).trim();
        }

        // 替换 SQL 中的占位符
        if (!params.isEmpty()) {
            // 移除参数中的括号和引号
            params = params.replaceAll("[\\[\\](){}]", "").replace("'", "");

            // 分割参数并替换占位符
            String[] paramsArray = params.split(", ");
            for (String param : paramsArray) {
                // 分割为值和类型
                String[] parts = param.split("(?<=\\d)(?=\\D)", 2);
                if (parts.length == 2) {
                    String value = parts[0].trim();
                    sql = sql.replaceFirst("\\?", value);
                }
            }
        }

        // 可选：格式化 SQL 以提高可读性
        return SqlFormatter.format(sql);
    }
}
