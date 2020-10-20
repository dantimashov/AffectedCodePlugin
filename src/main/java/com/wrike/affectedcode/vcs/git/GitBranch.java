package com.wrike.affectedcode.vcs.git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import git4idea.commands.GitCommand;
import com.wrike.affectedcode.vcs.VcsBranch;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author daniil.timashov on 24.07.2020
 */
public class GitBranch implements VcsBranch {

    private final GitClient gitClient;

    public GitBranch(@NotNull Project project) {
        this.gitClient = new GitClient(project);
    }

    @Override
    public String diffBetweenBranches(String src, String target) throws VcsException {
        String onlyOneCodeLineInDiffResult = "-U0";
        String rangeDiff = String.format("%s...%s", target, src);
        return gitClient.executeCommand(GitCommand.DIFF, onlyOneCodeLineInDiffResult, rangeDiff);
    }

    @Override
    public String currentLocalBranch() throws VcsException {
        List<String> localBranches = Arrays.asList(gitClient.executeCommand(GitCommand.BRANCH).split("\n"));
        String currentLocalBranch = localBranches.stream().filter(branch -> branch.contains("*")).findFirst().orElse("");
        return currentLocalBranch.replace("*", "").trim();
    }

    @Override
    public String remoteBranch(String localBranch) throws VcsException {
        String allInfoAboutBranch = "-vv";
        List<String> localBranchesInfo = Arrays.asList(gitClient.executeCommand(GitCommand.BRANCH, allInfoAboutBranch).split("\n"));
        String localBranchInfo = localBranchesInfo.stream().filter(branchInfo -> branchInfo.contains(localBranch)).findFirst().orElse("");
        String remoteBranchInfo = StringUtils.substringBetween( localBranchInfo, "[", "]");
        return Objects.isNull(remoteBranchInfo)
                ? "origin/master"
                : onlyRemoteBranchName(remoteBranchInfo);
    }

    private String onlyRemoteBranchName(String remoteBranchInfo) {
        return StringUtils.substringBefore(remoteBranchInfo, ":");
    }
}
