package com.wrike.affectedcode.psi;

import com.intellij.psi.PsiElement;
import com.wrike.affectedcode.AffectedCodePluginException;
import com.wrike.affectedcode.vcs.AffectedCodeUnit;

import java.util.Set;

/**
 * @author daniil.timashov on 27.08.2020
 */
public interface AffectedPsiElement<T extends AffectedCodeUnit> {

    Set<PsiElement> affectedCodeToAffectedElements(Set<T> affectedCode) throws AffectedCodePluginException;

}
