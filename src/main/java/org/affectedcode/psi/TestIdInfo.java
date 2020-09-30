package org.affectedcode.psi;

import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMember;
import org.affectedcode.PluginLogger;
import org.affectedcode.ui.DisplayedData;
import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import static java.lang.String.format;

/**
 * @author daniil.timashov on 02.08.2020
 */
public class TestIdInfo extends AffectedCodeInfo<DisplayedData>{

    private String annotationToFind;
    private String shortAnnotationToFindName;
    private String annotationValueToFind;

    TestIdInfo(String annotationToFind, String annotationValueToFind) {
        this.annotationToFind = annotationToFind;
        this.shortAnnotationToFindName = StringUtils.substringAfterLast(annotationToFind, ".");
        this.annotationValueToFind = annotationValueToFind;
        this.foundInfo = new DisplayedData();
    }

    @Override
    protected <T extends PsiMember> boolean hasInfo(T element) {
        return element.hasAnnotation(annotationToFind);
    }

    @Override
    protected <T extends PsiMember> void addInfo(T element) {
        Optional<PsiAnnotationMemberValue> testCaseIdValue = Optional.ofNullable(element.getAnnotation(annotationToFind).findAttributeValue(annotationValueToFind));
        testCaseIdValue.ifPresentOrElse(
                value -> {
                    foundInfo.addFoundId(value.getText());
                    foundInfo.addFoundModule(
                            element.getContainingFile().getVirtualFile().getPath(),
                            element.getContainingClass().getProject().getBasePath()
                    );
                },
                () -> PluginLogger.warn(notFoundAnnotationAttributeWarning(element.getContainingClass().getQualifiedName()))
        );
    }

    public <T extends PsiMember> void addInfoIfExists(T element) {
        if(hasInfo(element)) {
            addInfo(element);
        }
    }

    private String notFoundAnnotationAttributeWarning(String className) {
        return format("Could not find attribute `%s` for annotation `%s` in class %s", annotationValueToFind, shortAnnotationToFindName, className);
    }
}
