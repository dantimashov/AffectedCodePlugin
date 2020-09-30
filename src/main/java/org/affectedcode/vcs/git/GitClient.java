package org.affectedcode.vcs.git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.commands.Git;
import git4idea.commands.GitCommand;
import git4idea.commands.GitLineHandler;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * @author daniil.timashov on 24.07.2020
 */
public class GitClient {

    private final Project project;
    private VirtualFile vcsDirectory;

    GitClient(@NotNull Project project) {
        this.project = project;
    }

    private void initVcsDirectory() throws VcsException {
        this.vcsDirectory = Optional.ofNullable(ProjectLevelVcsManager.getInstance(project).getAllVcsRoots()[0].getPath())
                               .orElseThrow(() -> new VcsException(format("Not found vcs root for project %s", project.getName())));
    }

    String executeCommand(GitCommand gitCommand, @NonNls @NotNull String... parameters) throws VcsException {
        if (isNull(vcsDirectory)) {
            initVcsDirectory();
        }
        GitLineHandler handler = new GitLineHandler(project, vcsDirectory, gitCommand);
        handler.addParameters(parameters);
        return Git.getInstance().runCommand(handler).getOutputOrThrow();
    }
}
