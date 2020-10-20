package com.wrike.affectedcode.psi.processing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wrike.affectedcode.psi.find.FoundUsages;
import com.wrike.affectedcode.psi.TestIdInfo;

/**
 * @author daniil.timashov on 07.08.2020
 */
public class MethodProcessing extends AbstractProcessing<PsiMethod> {

    private final TestIdInfo testIdInfo;
    private final FoundUsages<PsiMethod> methodFoundUsages;

    public MethodProcessing(TestIdInfo testIdInfo, FoundUsages<PsiMethod> methodFoundUsages) {
        this.testIdInfo = testIdInfo;
        this.methodFoundUsages = methodFoundUsages;
        typeOfAncestor = PsiMethod.class;
    }

    @Override
    public <E extends PsiElement> void process(E checkedElement) {
        clearFoundPsiReferences();
        findAncestor(checkedElement);
        if (hasAncestor()) {
            if (methodFoundUsages.shouldProceedSearch(ancestor)) {
                methodFoundUsages.search(ancestor);
                psiReferences = methodFoundUsages.foundElements();
            }
            testIdInfo.addInfoIfExists(ancestor);
        }
    }
}
