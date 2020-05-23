package info.palinc

import hudson.model.queue.QueueTaskFuture
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Specification

class VarsSpec extends Specification {
    // A rule implementation can intercept test method execution and
    // alter the behaviour of these tests, or add cleanup work as it was done in
    // JUnit's @Before, @After, @BeforeClass and @AfterClass or
    // Spock’s setup, cleanup, setupSpec and cleanupSpec methods
    @Rule
    public JenkinsRule rule = new JenkinsRule()

    // runs before every feature method
    def setup() {
        rule.timeout = 30
        final LibraryRetriever retriever = new LocalLibraryRetriever()
        final LibraryConfiguration localLibrary =
                new LibraryConfiguration('testLibrary', retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'unused'
        localLibrary.allowVersionOverride = false
        //GlobalLibraries.get().setLibraries(Collections.singletonList(localLibrary))
        GlobalLibraries.get().libraries = [localLibrary]
    }

    def "the test for a simple vars groovy script which should print Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world1')
        workflowJob.definition = new CpsFlowDefinition('''
            echo "Hello World!" //Fails currently
        '''.stripIndent(), false)

        expect:
        final WorkflowRun run = rule.buildAndAssertSuccess(workflowJob)
    }
}
