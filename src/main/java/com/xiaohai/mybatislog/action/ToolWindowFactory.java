package com.xiaohai.mybatislog.action;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.xiaohai.mybatislog.ConvertFilter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author xiaohai
 */
public class ToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private static JTextArea inputTextArea;
    private static JEditorPane resultTextArea;

    /**
     * 获取工具窗口
     *
     * @param project
     * @return
     */
    public static ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow("MyBatis Log Convert");
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
        // 创建主面板，使用BorderLayout布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输入文本区域
        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
//        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.GRAY), "Input Data"));

        // 创建结果文本区域
        resultTextArea = new JEditorPane();
        resultTextArea.setContentType("text/html");
        resultTextArea.setEditable(false);
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.GRAY), "Converted SQL"));

        // 使用JSplitPane创建可调节大小的分隔面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScrollPane, resultScrollPane);
        // 初始分割位置
        splitPane.setResizeWeight(0.3);

        // 创建按钮
        JButton convertButton = new JButton("转换");
        JButton clearButton = new JButton("清空");

        // 转换按钮动作
        convertButton.addActionListener(e -> compress(inputTextArea));
        // 清空按钮动作
        clearButton.addActionListener(e -> clear(inputTextArea, resultTextArea));

        // 创建按钮面板，并将按钮居中显示
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(clearButton);
        buttonPanel.add(convertButton);

        // 将可调节的输入/输出面板放置在中心位置，将按钮面板放置在底部
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建并设置工具窗口内容
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 设置输入文本
     *
     * @param text 文本
     */
    public static void setInputText(String text) {
        if (inputTextArea != null) {
            inputTextArea.setText(text);
        }
    }

    /**
     * 设置结果文本
     *
     * @param text 文本
     */
    public static void setResultText(String text) {
        if (resultTextArea != null) {
            if (StringUtils.isNotBlank(text)) {
                resultTextArea.setText(formatAsHtml(text));
            } else {
                resultTextArea.setText("格式有误，无法转换");
            }
        }
    }

    /**
     * 格式化HTML
     *
     * @param text 文本
     * @return 格式化后的HTML
     */
    private static String formatAsHtml(String text) {
        return "<html><body style='font-family:monospace;color:#00FF00;'><pre>" + text + "</pre></body></html>";
    }

    private static String formatSql(String sql) {
        // 简单SQL格式化逻辑，可以根据需要增强
        return SqlFormatter.format(sql);
    }

    /**
     * 转换
     *
     * @param inputTextArea
     */
    public static void compress(JTextArea inputTextArea) {
        String inputText = inputTextArea.getText();
        if (inputText != null && !inputText.trim().isEmpty()) {
            String convertedLogs = ConvertFilter.convertLogs(inputText);
            setResultText(convertedLogs);
            copyToClipboard(convertedLogs);
        } else {
            JOptionPane.showMessageDialog(null, "输入数据为空或无效", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 清空输入和结果
     */
    public static void clear(JTextArea inputTextArea, JEditorPane resultTextArea) {
        inputTextArea.setText("");
        resultTextArea.setText("");
    }

    /**
     * 复制结果到剪贴板
     */
    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
