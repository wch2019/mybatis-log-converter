package com.xiaohai;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyBatisLoggerConverterAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        ToolWindow toolWindow = MyBatisLoggerToolWindowFactory.getToolWindow(project);
        if (toolWindow == null) {
            Messages.showMessageDialog(project, "Tool window not found.", "Error", Messages.getErrorIcon());
            return;
        }

        // 弹出工具窗口
        toolWindow.show(() -> {
            // 工具窗口弹出后，可以在这里进行其他操作
            // 如果需要处理工具窗口中的内容，可以在此处实现
        });
    }

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
                String[] parts = param.split("(?<=\\d)(?=\\D)", 2); // 分割为值和类型
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
