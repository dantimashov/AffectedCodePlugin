package com.wrike.affectedcode.psi.find;

import com.intellij.psi.PsiElement;

import java.util.Set;

/**
 * @author daniil.timashov on 02.08.2020
 */
public interface FoundUsages<T extends PsiElement> {

    boolean shouldProceedSearch(T element);

    void search(T element);

    Set<PsiElement> foundElements();
}
