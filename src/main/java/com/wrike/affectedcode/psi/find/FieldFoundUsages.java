package com.wrike.affectedcode.psi.find;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldFoundUsages implements FoundUsages<PsiField> {

    private Set<PsiElement> foundElements;
    Set<PsiField> checkedFields;

    public FieldFoundUsages(Set<PsiField> checkedFields) {
        this.checkedFields = checkedFields;
    }

    @Override
    public boolean shouldProceedSearch(PsiField field) {
        return !checkedFields.contains(field);
    }

    @Override
    public void search(PsiField field) {
        Collection<PsiReference> psiReferences = ReferencesSearch.search(field).findAll();
        foundElements = psiReferences.stream().map(PsiReference::getElement).collect(Collectors.toSet());
        checkedFields.add(field);
    }

    @Override
    public Set<PsiElement> foundElements() {
        return foundElements;
    }

}
