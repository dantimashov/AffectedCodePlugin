package com.wrike.affectedcode.vcs;

import com.intellij.openapi.vcs.VcsException;

/**
 * @author daniil.timashov on 23.07.2020
 */
public interface VcsBranch {

    String diffBetweenBranches(String src, String target) throws VcsException;

    String currentLocalBranch() throws VcsException;

    String remoteBranch(String localBranch) throws VcsException;
}
