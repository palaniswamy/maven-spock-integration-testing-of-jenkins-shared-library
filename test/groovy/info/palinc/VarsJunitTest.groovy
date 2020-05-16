package info.palinc

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule

class VarsJunitTest {

    @Rule
    public JenkinsRule rule = new JenkinsRule()

    @Before
    void configureGlobalGitLibraries() {
        rule.timeout = 30
        final LibraryRetriever retriever = new LocalLibraryRetriever()
        //TODO: Override the methods of LibraryRetriever
        //TODO: maven to copy the library into 'testLibrary' directory before integration testing phase
        final LibraryConfiguration localLibrary =
                new LibraryConfiguration('testLibrary', retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'unused'
        localLibrary.allowVersionOverride = false
        //GlobalLibraries.get().setLibraries(Collections.singletonList(localLibrary))
        GlobalLibraries.get().libraries = [localLibrary]
    }

//    @Test
//    void "testing /vars scripts"() {
//        final CpsFlowDefinition flow = new CpsFlowDefinition('''
//        import helloWorld
//
//        node {
//          helloWorld()
//        }
//    '''.stripIndent(), false)
//        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'project')
//        workflowJob.definition = flow
//
//        final WorkflowRun run = rule.buildAndAssertSuccess(workflowJob)
//        //rule.assertLogContains('Hello World!', run)
//    }
}
