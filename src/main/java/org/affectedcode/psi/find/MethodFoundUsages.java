package org.affectedcode.psi.find;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.MethodReferencesSearch;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author daniil.timashov on 22.08.2020
 */
public class MethodFoundUsages implements FoundUsages<PsiMethod> {

    private static final String BEFORE_EACH_ANNOTATION = "org.junit.jupiter.api.BeforeEach";
    private static final String BEFORE_ALL_ANNOTATION = "org.junit.jupiter.api.BeforeAll";
    private static final String AFTER_EACH_ANNOTATION = "org.junit.jupiter.api.AfterEach";
    private static final String AFTER_ALL_ANNOTATION = "org.junit.jupiter.api.AfterAll";
    private static final List<String> AFFECTED_ALL_CLASS_ANNOTATIONS_LIST = Arrays.asList(
            BEFORE_EACH_ANNOTATION, BEFORE_ALL_ANNOTATION, AFTER_EACH_ANNOTATION, AFTER_ALL_ANNOTATION
    );

    private Set<PsiElement> foundElements;
    Set<PsiMethod> checkedMethods;

    public MethodFoundUsages(Set<PsiMethod> checkedMethods) {
        this.checkedMethods = checkedMethods;
    }

    @Override
    public boolean shouldProceedSearch(PsiMethod method) {
        return !checkedMethods.contains(method);
    }

    @Override
    public void search(PsiMethod method) {
        Collection<PsiReference> psiReferences = MethodReferencesSearch.search(method).findAll();
        foundElements = psiReferences.stream().map(PsiReference::getElement).collect(Collectors.toSet());
        checkedMethods.add(method);

        if (hasAffectedAllClassAnnotations(method)) {
            PsiMethod[] affectedMethods = method.getContainingClass().getMethods();
            Stream.of(affectedMethods).filter(affectedMethod -> !checkedMethods.contains(affectedMethod)).forEach(affectedMethod -> {
                checkedMethods.add(affectedMethod);
                foundElements.add(affectedMethod);
            });
        }
    }

    private boolean hasAffectedAllClassAnnotations(PsiMethod method) {
        return Stream.of(method.getAnnotations()).anyMatch(psiAnnotation -> AFFECTED_ALL_CLASS_ANNOTATIONS_LIST.contains(psiAnnotation.getQualifiedName()));
    }

    @Override
    public Set<PsiElement> foundElements() {
        return foundElements;
    }
}
