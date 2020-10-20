package com.wrike.affectedcode.vcs.git.parser;

import com.wrike.affectedcode.vcs.DiffParser;
import com.wrike.affectedcode.vcs.git.AffectedClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author daniil.timashov on 27.07.2020
 */
public class GitDiffParser implements DiffParser<AffectedClass> {

    private final Set<AffectedClass> affectedClasses;

    public GitDiffParser() {
        this.affectedClasses = new HashSet<>();
    }

    @Override
    public void parse(String diff) {
        List<String> classesDiff = Stream.of(diff.split("diff --git")).skip(1).collect(toList());
        for (String classDiff : classesDiff) {
            GitDiffClassInfo gitDiffClassInfo = new GitDiffClassInfo(classDiff);
            gitDiffClassInfo.parse();
            if(gitDiffClassInfo.isParsed()) {
                affectedClasses.add(gitDiffClassInfo.affectedClass());
            }
        }
    }

    @Override
    public Set<AffectedClass> parsedDiff() {
        return affectedClasses;
    }
}
