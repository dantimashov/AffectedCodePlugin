package org.affectedcode.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import org.affectedcode.AffectedCodePluginException;
import org.affectedcode.PluginLogger;
import org.affectedcode.psi.CurrentProject;
import org.affectedcode.ui.DisplayedData;
import org.affectedcode.vcs.DiffParser;
import org.affectedcode.vcs.VcsBranch;
import org.affectedcode.vcs.git.AffectedClass;
import org.affectedcode.vcs.git.GitBranch;
import org.affectedcode.vcs.git.parser.GitDiffParser;

import static java.lang.String.format;

/**
 * @author daniil.timashov on 24.07.2020
 */
public class AffectedCodeFindAction {

    public static DisplayedData affectedTestsIds(Project project) {
        PluginLogger.info("Start searching for affected tests ids");
        VcsBranch gitBranch = new GitBranch(project);
        DiffParser<AffectedClass> diffParser = new GitDiffParser();
        CurrentProject currentProject = new CurrentProject(project);

        try {
            PluginLogger.info("Start getting branches info");
            String localBranch = gitBranch.currentLocalBranch();
            String remoteBranch = gitBranch.remoteBranch(localBranch);

            String diff = gitBranch.diffBetweenBranches(localBranch, remoteBranch);
            PluginLogger.info(format("Got git diff between branches: %s and %s", localBranch, remoteBranch));
            diffParser.parse(diff);

            PluginLogger.info("Start searching for ids");
            currentProject.searchAffectedTestsIds(diffParser.parsedDiff());
            PluginLogger.info("End searching for ids");
        } catch (VcsException e) {
            PluginLogger.error("Error while running git module");
            PluginLogger.error(e.getMessage());
            e.printStackTrace();
        } catch (AffectedCodePluginException e) {
            PluginLogger.error("Error while running psi module");
            PluginLogger.error(e.getMessage());
            e.printStackTrace();
        }
        return currentProject.affectedTestsIds();
    }
}
