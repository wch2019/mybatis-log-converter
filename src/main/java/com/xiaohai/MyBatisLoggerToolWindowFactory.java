package com.xiaohai;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyBatisLoggerToolWindowFactory implements ToolWindowFactory {
    private static JTextArea inputTextArea;
    private static JEditorPane resultTextArea;
    private static JButton convertButton;
    private static JButton clearButton;

    @Override
    public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        // 创建输入区域面板
        JPanel inputPanel = new JPanel(new BorderLayout());

        // 创建输入文本区域
        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // 设置字体
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Input Data"));

        // 创建转换按钮
        convertButton = new JButton("转换");
        convertButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText != null && !inputText.trim().isEmpty()) {
                String convertedLogs = MyBatisLoggerConverterAction.convertLogs(inputText);
                setResultText(convertedLogs);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "输入数据为空或无效", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        // 创建清空按钮
        clearButton = new JButton("清空");
        clearButton.addActionListener(e -> {
            inputTextArea.setText("");
            resultTextArea.setText("");
        });
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(convertButton);
        // 添加水平间距
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearButton);

        // 将输入文本区域和按钮面板添加到输入区域面板中
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建结果文本区域
        resultTextArea = new JEditorPane();
        resultTextArea.setContentType("text/html");
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.GRAY), "Converted SQL"));

        // 添加到主面板
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);

        // 创建并设置工具窗口内容
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static void setInputText(String text) {
        if (inputTextArea != null) {
            inputTextArea.setText(text);
        }
    }

    public static void setResultText(String text) {
        if (resultTextArea != null) {
            resultTextArea.setText(formatAsHtml(text));
        }
    }

    public static String getInputText() {
        return inputTextArea != null ? inputTextArea.getText() : "";
    }

    public static ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow("MyBatisLogger");
    }
    private static String formatAsHtml(String text) {
        return "<html><body style='font-family:monospace;color:#00FF00;'><pre>" + text + "</pre></body></html>";
    }
}
