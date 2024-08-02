package com.xiaohai.mybatislog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class ToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private static JTextArea inputTextArea;
    private static JEditorPane resultTextArea;

    @Override
    public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
        // 创建主面板  10px间距
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        // 四周10px间距
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输入区域面板
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));

        // 创建输入文本区域
        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        // 设置字体
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // 设置内部边距
        inputTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.GRAY), "Input Data"));

        // 创建按钮
        JButton convertButton = new JButton("转换");
        convertButton.setPreferredSize(new Dimension(80, 30));
        JButton clearButton = new JButton("清空");
        clearButton.setPreferredSize(new Dimension(80, 30));
        JButton copyButton = new JButton("复制");
        copyButton.setPreferredSize(new Dimension(80, 30));

        // 设置按钮动作
        convertButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText != null && !inputText.trim().isEmpty()) {
                String convertedLogs = ConverterAction.convertLogs(inputText);
                setResultText(convertedLogs);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "输入数据为空或无效", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        clearButton.addActionListener(e -> {
            inputTextArea.setText("");
            resultTextArea.setText("");
        });
        copyButton.addActionListener(e -> {
            String resultText = getResultText();
            if (resultText != null && !resultText.trim().isEmpty()) {
                StringSelection stringSelection = new StringSelection(resultText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                JOptionPane.showMessageDialog(mainPanel, "SQL已复制到剪贴板", "信息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "无可复制内容", "警告", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 创建按钮面板，使用右对齐的FlowLayout布局
        // 10px间距
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(copyButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(convertButton);

        // 将输入文本区域和按钮面板添加到输入区域面板中
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建结果文本区域
        resultTextArea = new JEditorPane();
        resultTextArea.setContentType("text/html");
        resultTextArea.setEditable(false);
        // 设置字体
//        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // 设置内部边距
        resultTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(JBColor.GRAY), "Converted SQL"));

        // 添加到主面板
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);

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

    public static String getResultText() {
        // 去掉HTML标签
        return resultTextArea != null ? resultTextArea.getText().replaceAll("<[^>]+>", "") : "";
    }

    public static ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow("MyBatis Log Convert");
    }

    private static String formatAsHtml(String text) {
        return "<html><body style='font-family:monospace;color:#00FF00;'><pre>" + text + "</pre></body></html>";
    }
}
