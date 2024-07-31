package com.xiaohai;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class MyBatisLoggerConsoleMouseAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null) {
                Messages.showMessageDialog("Selected text: " + selectedText, "Information", Messages.getInformationIcon());
            }
        } else {
            ConsoleViewImpl console = event.getData(PlatformDataKeys.CONSOLE_VIEW);
            if (console != null) {
                String selectedText = console.getSelectionModel().getSelectedText();
                if (selectedText != null) {
                    Messages.showMessageDialog("Selected text: " + selectedText, "Information", Messages.getInformationIcon());
                }
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        boolean enabled = editor != null && editor.getSelectionModel().hasSelection();
        if (!enabled) {
            ConsoleViewImpl console = event.getData(PlatformDataKeys.CONSOLE_VIEW);
            enabled = console != null && console.getSelectionModel().hasSelection();
        }
        event.getPresentation().setEnabledAndVisible(enabled);
    }
}
