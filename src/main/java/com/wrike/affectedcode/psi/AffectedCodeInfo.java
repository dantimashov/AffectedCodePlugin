package com.wrike.affectedcode.psi;

import com.intellij.psi.PsiMember;

/**
 * @author daniil.timashov on 02.08.2020
 */
public abstract class AffectedCodeInfo<E> {

    E foundInfo;

    public E foundInfo() {
        return foundInfo;
    }

    protected abstract <T extends PsiMember> boolean hasInfo(T element);

    protected abstract <T extends PsiMember> void addInfo(T element);
}
