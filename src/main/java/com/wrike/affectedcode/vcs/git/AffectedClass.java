package com.wrike.affectedcode.vcs.git;

import com.wrike.affectedcode.vcs.AffectedCodeUnit;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author daniil.timashov on 27.07.2020
 */
public class AffectedClass implements AffectedCodeUnit {

    private final String moduleName;
    private final String fileName;
    private final String pathToClass;
    private final String pathToClassForLog;
    private boolean isNewClass = false;
    private final List<ChangedLines> changedLines = new LinkedList<>();

    public AffectedClass(String classPath) {
        this.moduleName = parseModuleName(classPath);
        this.fileName = StringUtils.substringAfterLast(classPath, "/");
        this.pathToClassForLog = classPath.substring(2); // remove `/b` git prefix
        this.pathToClass = pathToClassForLog.replace(".java", "").replace(".", "/") + ".java";
    }

    private String parseModuleName(String classPath) {
        String allModules = StringUtils.substringBetween(classPath, "b/", "/src/");
        return allModules.contains("/") ? StringUtils.substringAfterLast(allModules, "/") : allModules;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isNewClass() {
        return isNewClass;
    }

    public void setNewClass(boolean newClass) {
        isNewClass = newClass;
    }

    public void addChangedLine (int firstChangedLine, int numberOfChangedLines) {
        changedLines.add(new ChangedLines(firstChangedLine, numberOfChangedLines));
    }

    public List<ChangedLines> getChangedLines() {
        return changedLines;
    }

    public String getPathToClass() {
        return pathToClass;
    }

    public String getPathToClassForLog() {
        return pathToClassForLog;
    }

    public static class ChangedLines {

        private final int firstChangedLine;
        private final int numberOfChangedLines;

        ChangedLines(int firstChangedLine, int numberOfChangedLines) {
            this.firstChangedLine = firstChangedLine;
            this.numberOfChangedLines = numberOfChangedLines;
        }

        public int getFirstChangedLine() {
            return firstChangedLine;
        }

        public int getNumberOfChangedLines() {
            return numberOfChangedLines;
        }

        public boolean isDeletedLine() {
            return numberOfChangedLines == 0;
        }
    }

}
