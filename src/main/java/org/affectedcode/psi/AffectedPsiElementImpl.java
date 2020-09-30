package org.affectedcode.psi;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.util.PsiTreeUtil;
import org.affectedcode.AffectedCodePluginException;
import org.affectedcode.PluginLogger;
import org.affectedcode.vcs.git.AffectedClass;
import com.intellij.openapi.module.Module;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * @author daniil.timashov on 27.08.2020
 */
public class AffectedPsiElementImpl implements AffectedPsiElement<AffectedClass> {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    private final Project project;
    private Set<PsiElement> foundElements;
    private Module parsedClassModule;

    AffectedPsiElementImpl(Project project) {
        this.project = project;
    }

    @Override
    public Set<PsiElement> affectedCodeToAffectedElements(Set<AffectedClass> affectedCode) throws AffectedCodePluginException {
        foundElements = new HashSet<>();
        for (AffectedClass affectedClass : affectedCode) {
            ModuleManager moduleManager = ModuleManager.getInstance(project);
            parsedClassModule = Optional.ofNullable(moduleManager.findModuleByName(affectedClass.getModuleName()))
                                        .orElseThrow(() -> new AffectedCodePluginException(format("Not found module with name `%s`", affectedClass.getModuleName())));
            if (affectedClass.isNewClass()) {
                addAffectedElementsForNewClass(affectedClass);
            } else {
                addAffectedElementsByChangedLines(affectedClass);
            }
        }
        return foundElements;
    }

    private void addAffectedElementsForNewClass(AffectedClass affectedClass) throws AffectedCodePluginException {
        String packageName = ((PsiJavaFileImpl) findPsiFileByNameInModule(affectedClass)).getPackageName();
        String qualifiedClassName = affectedClass.getFileName().replace(".java", "");
        if (!packageName.isEmpty()) {
            qualifiedClassName = packageName + "." + qualifiedClassName;
        }
        Optional<PsiClass> newClass = Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(qualifiedClassName, parsedClassModule.getModuleScope()));
        newClass.ifPresentOrElse(
                clazz -> foundElements.addAll(Arrays.asList(clazz.getMethods())),
                () -> PluginLogger.warn(format("Not found class `%s` in module `%s`", affectedClass.getFileName(), parsedClassModule.getName()))
        );
    }

    private void addAffectedElementsByChangedLines(AffectedClass affectedClass) throws AffectedCodePluginException {
        PsiFile file = findPsiFileByNameInModule(affectedClass);
        Document doc = PsiDocumentManager.getInstance(project).getDocument(file);
        for (AffectedClass.ChangedLines changedLine : affectedClass.getChangedLines()) {
            if (changedLine.isDeletedLine()) {
                PsiElement foundElement = findAnyPsiElementOfLine(doc, file, changedLine.getFirstChangedLine());
                if (isInsideOfPsiElement(foundElement, PsiMethod.class) || isInsideOfPsiElement(foundElement, PsiAnnotation.class)) {
                    foundElements.add(foundElement);
                }
            }
            for (int i = changedLine.getFirstChangedLine(); i < changedLine.getFirstChangedLine() + changedLine.getNumberOfChangedLines(); i++) {
                PsiElement foundElement = findAnyPsiElementOfLine(doc, file, i);
                foundElements.add(foundElement);
            }
        }
    }

    private static <T extends PsiElement> boolean isInsideOfPsiElement(PsiElement psiElement, Class<T> typeOfWrappingElement) {
        T wrappingElement = PsiTreeUtil.getParentOfType(psiElement, typeOfWrappingElement);
        return Optional.ofNullable(wrappingElement).isPresent();
    }

    private PsiElement findAnyPsiElementOfLine(Document doc, PsiFile file, int lineNumber) {
        int safeForBoundsFileLineNumber = processLineNumber(lineNumber, doc.getLineCount());
        int startOffset = doc.getLineStartOffset(safeForBoundsFileLineNumber - 1); // line in doc started with 0, but line in git diff started with 1
        int endOffset = doc.getLineEndOffset(safeForBoundsFileLineNumber - 1);
        return file.findElementAt((startOffset + endOffset) / 2);
    }

    private int processLineNumber(int lineNumber, int fileLineCount) {
        if (lineNumber <= 0) {
            return 1;
        }
        if (lineNumber >= fileLineCount) {
            return fileLineCount - 1;
        }
        return lineNumber;
    }

    private PsiFile findPsiFileByNameInModule(AffectedClass clazz) throws AffectedCodePluginException {
        String pathToClass = IS_WINDOWS ? clazz.getPathToClass().replace("/", "\\") : clazz.getPathToClass();
        return Stream.of(FilenameIndex.getFilesByName(project, clazz.getFileName(), parsedClassModule.getModuleScope()))
                     .filter(psiFile -> (psiFile.getVirtualFile().getPresentableUrl().contains(pathToClass)))
                     .findFirst()
                     .orElseThrow(() -> new AffectedCodePluginException(format("Not found file by name `%s` in module `%s`", clazz.getFileName(), clazz.getModuleName())));
    }
}
