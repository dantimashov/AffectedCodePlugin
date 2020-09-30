package org.affectedcode.vcs;

import org.affectedcode.vcs.git.AffectedClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AffectedClassTest {

    @Test
    public void checkOneModule(){
        AffectedClass affectedClass = new AffectedClass(mavenClassPath("simplemodule"));
        assertThat(affectedClass.getModuleName(), is("simplemodule"));
    }

    @Test
    public void checkSeveralModules(){
        AffectedClass affectedClass = new AffectedClass(mavenClassPath("firstmodule/secondmodule/thirdmodule"));
        assertThat(affectedClass.getModuleName(), is("thirdmodule"));
    }

    private String mavenClassPath(String modules) {
        String gitDiffClassPath = "b/%s/src/main/java/org/mycompany/package/MyClass.java";
        return String.format(gitDiffClassPath, modules);
    }

}
