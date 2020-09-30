package org.affectedcode.psi.processing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.affectedcode.psi.find.FoundUsages;
import org.affectedcode.psi.TestIdInfo;

import java.util.Set;

/**
 * @author daniil.timashov on 07.08.2020
 */
public class MethodProcessing extends AbstractProcessing<PsiMethod> {

    private TestIdInfo testIdInfo;
    private FoundUsages<PsiMethod> methodFoundUsages;

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
