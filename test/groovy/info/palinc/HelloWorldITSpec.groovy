package info.palinc

import hudson.model.queue.QueueTaskFuture
import org.jenkinsci.plugins.workflow.actions.LogAction
import org.jenkinsci.plugins.workflow.graph.FlowGraphWalker
import org.jenkinsci.plugins.workflow.graph.FlowNode
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever

import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import spock.lang.Specification

import java.util.regex.Matcher

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
        localLibrary.defaultVersion = 'unused'
        localLibrary.allowVersionOverride = false
        //GlobalLibraries.get().setLibraries(Collections.singletonList(localLibrary))
        GlobalLibraries.get().libraries = [localLibrary]
    }

    def "A pipeline from a Jenkinsfile prints Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world')
        def script = new File('test/resources/jobs/Jenkinsfile')
        workflowJob.definition = new CpsFlowDefinition(script.text, true)

        when:
        final QueueTaskFuture<WorkflowRun> futureRun = workflowJob.scheduleBuild2(0)

        then:
        final WorkflowRun run = rule.assertBuildStatusSuccess(futureRun)
        rule.assertLogContains('Hello World!', run)
    }

    def "runMethod() prints Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world1')
        workflowJob.definition = new CpsFlowDefinition('''
            //@Library('testLibrary') _ //The @Library is non-functional. Without it, the 'testLibrary' is still available for testing.

            import info.palinc.HelloWorld
        
            def hW = new HelloWorld(this)
            hW.runMethod()
        '''.stripIndent(), false)

        when:
        final QueueTaskFuture<WorkflowRun> futureRun = workflowJob.scheduleBuild2(0)

        then:
        final WorkflowRun run = rule.assertBuildStatusSuccess(futureRun)
        rule.assertLogContains('Hello World!', run)
    }

    def "Importing directory, vars/helloWord prints Hello World!"() {
        given:
        final WorkflowJob workflowJob = rule.createProject(WorkflowJob, 'test-hello-world2')
        final CpsFlowDefinition flow = new CpsFlowDefinition('''
            import helloWorld //without this line in ITSpec, thee vars/*.groovy scripts are not Serializable and errors out.

            node(){
                helloWorld
            }
        '''.stripIndent(), false)

        when:
        workflowJob.definition = flow

        then:
        final WorkflowRun run = rule.buildAndAssertSuccess(workflowJob)
        println run.log
        // TODO: test for console output
        //rule.assertLogContains('Hello World!', run)
        List<LogAction> logActions = new ArrayList<LogAction>();
        for (FlowNode n : new FlowGraphWalker(run.getExecution())) {
            LogAction la = n.getAction(LogAction.class);
            if (la != null) {
                logActions.add(la);
            }
        }
        1 == logActions.size();
        StringWriter w = new StringWriter();
        logActions.get(0).getLogText().writeLogTo(0, w);
        println(w.toString())
    }

    def "example from echosteptest"() {
        when:
        WorkflowJob p = rule.jenkins.createProject(WorkflowJob.class, "p");
        p.setDefinition(new CpsFlowDefinition("echo 'hello there'", true));

        then:
        WorkflowRun b = rule.assertBuildStatusSuccess(p.scheduleBuild2(0));
        List<LogAction> logActions = new ArrayList<LogAction>();
        for (FlowNode n : new FlowGraphWalker(b.getExecution())) {
            LogAction la = n.getAction(LogAction.class);
            if (la != null) {
                logActions.add(la);
            }
        }
        1 == logActions.size();
        StringWriter w = new StringWriter();
        logActions.get(0).getLogText().writeLogTo(0, w);
        "hello there" == w.toString().trim();
//        Matcher m = Pattern.compile("hello there").matcher(JenkinsRule.getLog(b));
//        assertTrue("message printed once", m.find());
//        assertFalse("message not printed twice", m.find());
    }

}
