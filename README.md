# maven-spock-integration-testing-of-jenkins-shared-library
This is a pared down demonstration of spock based integration testing of a Jenkins Pipeline Shared Library using Maven project.

Use Mike Kobit's [Gradle Plugin](https://github.com/mkobit/jenkins-pipeline-shared-libraries-gradle-plugin) for better support of testing of Jenkins Pipeline Shared Library.

* Uses pom.xml to add jenkins core, war, test-harness, sshd module, and some of the pipeline plugins.
* Uses LocalLibraryRetriever.groovy, unlike the dynamically generated code in mkobit's gradle plugin

TODO:

* Need to research why the /vars scripts are not inheriting the steps from plugins.
* Need to research why the sandbox CpsFlowDefinition asks for script approval for the shared library. 