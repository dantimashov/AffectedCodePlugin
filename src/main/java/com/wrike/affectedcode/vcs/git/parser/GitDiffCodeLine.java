package com.wrike.affectedcode.vcs.git.parser;

import com.wrike.affectedcode.vcs.git.AffectedClass;
import org.apache.commons.lang.StringUtils;

/**
 * @author daniil.timashov on 06.09.2020
 */
public class GitDiffCodeLine {

    private final String codeLine;

    GitDiffCodeLine(String codeLine) {
        this.codeLine = codeLine;
    }

    public void addChangedLine(AffectedClass affectedClass) {
        if (codeLine.startsWith("@@")) {
            String changedLineMetaInfo = StringUtils.substringBetween(codeLine, " +", " @@");
            if (isAddedMoreOneLine(changedLineMetaInfo)) {
                affectedClass.addChangedLine(Integer.parseInt(StringUtils.substringBefore(changedLineMetaInfo, ",")),
                        Integer.parseInt(StringUtils.substringAfter(changedLineMetaInfo, ",")));
            } else {
                affectedClass.addChangedLine(Integer.parseInt(changedLineMetaInfo), 1);
            }
        }
    }

    public boolean isFromDeletedFile() {
        if (codeLine.startsWith("+++")) {
            String destinationFileName = StringUtils.substringAfter(codeLine, " ");
            return isNotRealFile(destinationFileName);
        }
        return false;
    }

    public boolean isFromNewFile() {
        if (codeLine.startsWith("---")) {
            String sourceFileName = StringUtils.substringAfter(codeLine, " ");
            return isNotRealFile(sourceFileName);
        }
        return false;
    }

    private boolean isNotRealFile(String fileName) {
        return "/dev/null".equals(fileName);
    }

    private boolean isAddedMoreOneLine(String changedLineMetaInfo) {
        return changedLineMetaInfo.contains(",");
    }

}
