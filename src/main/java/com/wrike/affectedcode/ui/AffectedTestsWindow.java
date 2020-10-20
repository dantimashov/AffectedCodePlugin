package com.wrike.affectedcode.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.wrike.affectedcode.actions.AffectedCodeFindAction;

import javax.swing.*;

import static java.lang.String.format;

/**
 * @author daniil.timashov on 23.07.2020
 */
public class AffectedTestsWindow {

    private JPanel panel;
    private JPanel resultPanel;
    private JPanel controlPanel;
    private JPanel foundIdsPanel;
    private JPanel foundModulesPanel;
    private JTextArea foundIdsText;
    private JLabel numberIdsLabel;
    private JTextArea foundModulesText;
    private JLabel foundModulesLabel;
    private JButton clearButton;
    private JButton findButton;

    private String numberFoundIdsMessage = "Found %s ids";

    public AffectedTestsWindow(ToolWindow toolWindow, Project project) {
        clearButton.addActionListener(e -> {
            foundIdsText.setText("");
            foundModulesText.setText("");
            numberIdsLabel.setText(format(numberFoundIdsMessage, 0));
        });

        findButton.addActionListener(e -> {
            DisplayedData displayedData = AffectedCodeFindAction.affectedTestsIds(project);
            foundIdsText.setText(displayedData.formattedFoundIds());
            numberIdsLabel.setText(format(numberFoundIdsMessage, displayedData.foundIdsNumber()));
            foundModulesText.setText(displayedData.formattedFoundModules());
        });

    }

    public JPanel getContent() {
        return panel;
    }
}
