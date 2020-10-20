package com.wrike.affectedcode.psi.processing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Optional;
import java.util.Set;

/**
 * @author daniil.timashov on 07.08.2020
 */
public abstract class AbstractProcessing<T extends PsiElement> {

    protected T ancestor;
    protected Class<T> typeOfAncestor;
    protected Set<PsiElement> psiReferences = Set.of();

    protected void findAncestor(PsiElement childElement) {
        boolean childElementHasAncestorType = typeOfAncestor.isInstance(childElement);
        ancestor = childElementHasAncestorType ? (T) childElement : PsiTreeUtil.getParentOfType(childElement, typeOfAncestor);
    }

    protected boolean hasAncestor() {
        return Optional.ofNullable(ancestor).isPresent();
    }

    protected void clearFoundPsiReferences() {
        psiReferences = Set.of();
    }

    public boolean isSuccessfulProcess() {
        return hasAncestor();
    }

    public abstract <E extends PsiElement> void process(E checkedElement);

    public Set<PsiElement> psiReferences() {
        return psiReferences;
    }
}
