package com.wrike.affectedcode.psi.processing;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

/**
 * @author daniil.timashov on 30.09.2020
 */
public class CommentProcessing extends AbstractProcessing<PsiComment>{

    public CommentProcessing() {
        typeOfAncestor = PsiComment.class;
    }

    @Override
    public <E extends PsiElement> void process(E checkedElement) {
        findAncestor(checkedElement);
    }
}
