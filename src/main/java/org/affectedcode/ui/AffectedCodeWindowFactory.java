package org.affectedcode.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

/**
 * @author daniil.timashov on 20.07.2020
 */
public class AffectedCodeWindowFactory implements ToolWindowFactory {

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        AffectedTestsWindow affectedTestsWindow = new AffectedTestsWindow(toolWindow, project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(affectedTestsWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
