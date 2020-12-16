# AffectedCodePlugin

Open source version of plugin to find unit tests id affected by code changes. It was created on the basis of similar plugin used in QAA department of Wrike Inc. 
It's planned to develop and test new features out of the original goals.

Current version of the plugin uses the Git as VCS system. If you use plugin for a project then the root folder of the project should be under the version control of Git.

## For contributors

There are variables **ID_ANNOTATION** and **ID_VALUE** in class *CurrentProject*. Current values are linked with special [project](https://github.com/dantimashov/DummyAutotestProject) for testing. If you plan to use plugin differently then change this part of the code first.

You can read [a description](https://github.com/JetBrains/gradle-intellij-plugin) about *gradle-intellij-plugin* tasks.
Especially interesting:<br>
<code>runIde</code> - start IntelliJ IDEA with the plugin under development<br> 
<code>buildPlugin</code> - create zip archive in *./build/distributions*. You can use this archive [to install plugin from disk](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk) <br> 
