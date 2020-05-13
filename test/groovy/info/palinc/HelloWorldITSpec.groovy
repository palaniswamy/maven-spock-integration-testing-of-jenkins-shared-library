package info.palinc

import hudson.model.queue.QueueTaskFuture
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

    def "runMethod() prints Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world')
        def script = new File('test/resources/jobs/Jenkinsfile')
        // TODO: try with sandbox: true
        workflowJob.definition = new CpsFlowDefinition(script.text, false)

        when:
        final QueueTaskFuture<WorkflowRun> futureRun = workflowJob.scheduleBuild2(0)

        then:
        final WorkflowRun run = rule.assertBuildStatusSuccess(futureRun)
        rule.assertLogContains('Hello World!', run)
    }

    def "vars/helloWord prints Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world2')
        final CpsFlowDefinition flow = new CpsFlowDefinition('''
            import helloWorld

            helloWorld
        '''.stripIndent(), false)

        when:
        workflowJob.definition = flow

        then:
        final WorkflowRun run = rule.buildAndAssertSuccess(workflowJob)
        println run.log
        // TODO: test for console output
        //rule.assertLogContains('Hello World!', run)
    }

}
