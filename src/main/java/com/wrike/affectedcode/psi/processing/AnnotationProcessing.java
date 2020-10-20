package com.wrike.affectedcode.psi.processing;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author daniil.timashov on 28.08.2020
 */
public class AnnotationProcessing extends AbstractProcessing<PsiAnnotation>{

    private final FieldProcessing fieldProcessing;
    private final MethodProcessing methodProcessing;

    public AnnotationProcessing(MethodProcessing methodProcessing, FieldProcessing fieldProcessing) {
        this.methodProcessing = methodProcessing;
        this.fieldProcessing = fieldProcessing;
        typeOfAncestor = PsiAnnotation.class;
    }

    @Override
    public <E extends PsiElement> void process(E checkedElement) {
        clearFoundPsiReferences();
        findAncestor(checkedElement);
        if (hasAncestor()) {
            Optional.ofNullable(PsiTreeUtil.getParentOfType(ancestor, PsiClass.class)).ifPresent(clazz -> {
                Stream.of(clazz.getMethods()).forEach(methodProcessing::process);
                Stream.of(clazz.getFields()).forEach(fieldProcessing::process);
            });
        }
    }
}
