package info.palinc

import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever

import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import spock.lang.Specification

class HelloWorldITSpec extends Specification {

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
        //TODO: Override the methods of LibraryRetriever
        //TODO: maven to copy the library into 'testLibrary' directory before integration testing phase
        final LibraryConfiguration localLibrary =
                new LibraryConfiguration('testLibrary', retriever)
        localLibrary.implicit = true
        localLibrary.defaultVersion = 'master'
        localLibrary.allowVersionOverride = true
        GlobalLibraries.get().setLibraries(Collections.singletonList(localLibrary))
    }

    def "runMethod() returns success"() {
        given:
        WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world')
        def script = new File('test/resources/jobs/Jenkinsfile')
        // TODO: try with sandbox: true
        workflowJob.definition = new CpsFlowDefinition(script.text, false)

        when:
        WorkflowRun result = rule.buildAndAssertSuccess(workflowJob)

        then:
        rule.assertLogContains('Hello World!', result)
    }


}
