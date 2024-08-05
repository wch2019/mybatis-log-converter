package com.xiaohai.mybatislog.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * @author xiaohai
 */
public class DemoTool extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
       Project project = e.getProject();
        if (project != null) {
            Messages.showDialog("Hello world", "Title", new String[]{"Yes", "No"}, 0, Messages.getQuestionIcon());
        }

    }
}
