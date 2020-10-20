package com.wrike.affectedcode.psi.processing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.wrike.affectedcode.psi.find.FoundUsages;

/**
 * @author daniil.timashov on 07.08.2020
 */
public class FieldProcessing extends AbstractProcessing<PsiField> {

    private final FoundUsages<PsiField> fieldFoundUsages;

    public FieldProcessing(FoundUsages<PsiField> fieldFoundUsages) {
        this.fieldFoundUsages = fieldFoundUsages;
        typeOfAncestor = PsiField.class;
    }

    @Override
    public <E extends PsiElement> void process(E checkedElement) {
        clearFoundPsiReferences();
        findAncestor(checkedElement);
        if (hasAncestor() && fieldFoundUsages.shouldProceedSearch(ancestor)) {
            fieldFoundUsages.search(ancestor);
            psiReferences = fieldFoundUsages.foundElements();
        }
    }

}
