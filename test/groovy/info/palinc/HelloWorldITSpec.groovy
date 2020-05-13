package info.palinc

//import com.mkobit.jenkins.pipelines.codegen.LocalLibraryRetriever
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

    // runs once -  before each feature method
    def setup() {
        rule.timeout = 30
        //final LibraryRetriever retriever = new LocalLibraryRetriever()
//        final LibraryConfiguration localLibrary =
//                new LibraryConfiguration('info.palinc', retriever)
//        localLibrary.implicit = true
//        localLibrary.defaultVersion = 'master'
//        localLibrary.allowVersionOverride = true
//        GlobalLibraries.get().setLibraries(Collections.singletonList(localLibrary))
    }

    def "runMethod() returns success"() {
        given:
        WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world')
        def script = new File('test/resources/jobs/Jenkinsfile')
        workflowJob.definition = new CpsFlowDefinition(script.text, true)

        when:
        WorkflowRun result = rule.buildAndAssertSuccess(workflowJob)

        then:
        rule.assertLogContains('Hello World!', result)
    }


}
