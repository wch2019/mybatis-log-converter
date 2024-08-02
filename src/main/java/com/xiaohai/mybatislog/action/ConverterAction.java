package com.xiaohai.mybatislog.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
/**
 * @author xiaohai
 */
public class ConverterAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        ToolWindow toolWindow = ToolWindowFactory.getToolWindow(project);
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
}
