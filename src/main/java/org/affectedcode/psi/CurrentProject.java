package org.affectedcode.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.affectedcode.AffectedCodePluginException;
import org.affectedcode.PluginLogger;
import org.affectedcode.psi.find.FieldFoundUsages;
import org.affectedcode.psi.find.FoundUsages;
import org.affectedcode.psi.find.MethodFoundUsages;
import org.affectedcode.psi.processing.AbstractProcessing;
import org.affectedcode.psi.processing.AnnotationProcessing;
import org.affectedcode.psi.processing.CommentProcessing;
import org.affectedcode.psi.processing.FieldProcessing;
import org.affectedcode.psi.processing.MethodProcessing;
import org.affectedcode.ui.DisplayedData;
import org.affectedcode.vcs.git.AffectedClass;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author daniil.timashov on 02.08.2020
 */
public class CurrentProject {

    private List<AbstractProcessing> processing = List.of();
    private Project project;
    private TestIdInfo testIdInfo;
    private static final String ID_ANNOTATION = "annotations.TestId";
    private static final String ID_VALUE = "value";

    public CurrentProject(Project project) {
        this.project = project;
        this.testIdInfo = new TestIdInfo(ID_ANNOTATION, ID_VALUE);
    }

    public void searchAffectedTestsIds(Set<AffectedClass> affectedClasses) throws AffectedCodePluginException {
        processing = createProcessingList();
        affectedTestsIdsSearch(affectedByUserElements(affectedClasses));
    }

    public DisplayedData affectedTestsIds() {
        return testIdInfo.foundInfo();
    }

    private List<AbstractProcessing> createProcessingList() {
        Set<PsiMethod> checkedMethods = new HashSet<>();
        Set<PsiField> checkedFields = new HashSet<>();

        FoundUsages<PsiMethod> methodFoundUsages = new MethodFoundUsages(checkedMethods);
        MethodProcessing methodProcessing = new MethodProcessing(testIdInfo, methodFoundUsages);

        FoundUsages<PsiField> fieldFoundUsages = new FieldFoundUsages(checkedFields);
        FieldProcessing fieldProcessing = new FieldProcessing(fieldFoundUsages);

        AnnotationProcessing annotationProcessing = new AnnotationProcessing(methodProcessing, fieldProcessing);
        CommentProcessing commentProcessing = new CommentProcessing();
        return List.of(commentProcessing, methodProcessing, fieldProcessing, annotationProcessing);
    }

    private Set<PsiElement> affectedByUserElements(Set<AffectedClass> affectedClasses) throws AffectedCodePluginException {
        AffectedPsiElement<AffectedClass> affectedPsiElement = new AffectedPsiElementImpl(project);
        return affectedPsiElement.affectedCodeToAffectedElements(affectedClasses);
    }

    private void affectedTestsIdsSearch(Set<PsiElement> elements) {
        int iterationNumber = 1;
        while (!elements.isEmpty()) {
            PluginLogger.info(format("Affected tests ids search %s iteration for %s code elements", iterationNumber++, elements.size()));
            elements = elements.stream()
                               .flatMap(element -> affectedTestsIdsSearch(element).stream())
                               .collect(Collectors.toSet());
        }
    }

    private Set<PsiElement> affectedTestsIdsSearch(PsiElement element) {
        for (AbstractProcessing processing : processing) {
            processing.process(element);
            if(processing.isSuccessfulProcess()) {
                return processing.psiReferences();
            }
        }
        return Collections.emptySet();
    }
}
