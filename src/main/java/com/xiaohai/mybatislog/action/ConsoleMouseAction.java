package com.xiaohai.mybatislog.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.xiaohai.mybatislog.ConvertFilter;
import org.jetbrains.annotations.NotNull;
/**
 * @author xiaohai
 */
public class ConsoleMouseAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
                System.out.println("Action performed: " + selectedText);
//                Messages.showMessageDialog("你选中的文本是: " + selectedText, "信息", Messages.getInformationIcon());
                Project project = event.getProject();
                ToolWindow toolWindow = ToolWindowFactory.getToolWindow(project);
                if (toolWindow == null) {
                    Messages.showMessageDialog(project, "Tool window not found.", "Error", Messages.getErrorIcon());
                    return;
                }
                // 弹出工具窗口
                toolWindow.show(() -> {
                    // 工具窗口弹出后，可以在这里进行其他操作
                    // 如果需要处理工具窗口中的内容，可以在此处实现
                    String convertedLogs = ConvertFilter.convertLogs(selectedText);
                    ToolWindowFactory.setInputText(selectedText);
                    ToolWindowFactory.setResultText(convertedLogs);
                    ToolWindowFactory.copyToClipboard(convertedLogs);
                });
            }
        }
    }
}
