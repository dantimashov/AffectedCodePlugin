package org.affectedcode.vcs.git.parser;

import org.affectedcode.vcs.git.AffectedClass;
import org.apache.commons.lang.StringUtils;

/**
 * @author daniil.timashov on 06.09.2020
 */
public class GitDiffClassInfo {

    private String classDiff;
    private boolean isParsed;
    private AffectedClass affectedClass;

    GitDiffClassInfo(String classDiff) {
        this.classDiff = classDiff;
        this.isParsed = true;
    }

    public AffectedClass affectedClass() {
        return affectedClass;
    }

    public boolean isParsed() {
        return isParsed;
    }

    public void parse() {
        String[] classDiffLines = classDiff.split("\n");
        String destinationFile =  StringUtils.substringAfterLast(classDiffLines[0], " ");
        if (isFileNotChanged() || isNotJavaFile(destinationFile)) {
            isParsed = false;
        } else {
            affectedClass = new AffectedClass(destinationFile);
            parseClassDiffLines(classDiffLines);
        }
    }

    private void parseClassDiffLines(String[] classDiffLines) {
        for (String line : classDiffLines) {
            GitDiffCodeLine gitDiffCodeLine = new GitDiffCodeLine(line);
            if (gitDiffCodeLine.isFromDeletedFile()) {
                isParsed = false;
                break;
            } else if (gitDiffCodeLine.isFromNewFile()) {
                affectedClass.setNewClass(true);
                break;
            } else {
                gitDiffCodeLine.addChangedLine(affectedClass);
            }
        }
    }

    private boolean isNotJavaFile(String destinationFile) {
        return !destinationFile.endsWith(".java");
    }

    private boolean isFileNotChanged() {
        String metaInfo = StringUtils.substringBefore(classDiff, "\n@@");
        return metaInfo.contains("similarity index 100%");
    }
}
