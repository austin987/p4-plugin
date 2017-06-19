package org.jenkinsci.plugins.p4.client;

import org.jenkinsci.plugins.p4.DefaultEnvironment;
import org.jenkinsci.plugins.p4.SampleServerRule;
import org.jenkinsci.plugins.p4.credentials.P4PasswordImpl;
import org.jenkinsci.plugins.p4.workflow.source.AbstractSource;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.logging.Logger;

/**
 * Test class for testing org.jenkinsci.plugins.p4.workflow.source.AbstractSource implementation
 */
public class GraphWorkFlowTest extends DefaultEnvironment {

	private static Logger logger = Logger.getLogger(ConnectionTest.class.getName());
	private static final String P4ROOT = "tmp-WorkflowTest-p4root";
	private static P4PasswordImpl auth;

	@ClassRule
	public static JenkinsRule jenkins = new JenkinsRule();

	@Rule
	public SampleServerRule p4d = new SampleServerRule(P4ROOT, R17_1);

	@Before
	public void buildCredentials() throws Exception {
		auth = createCredentials("jenkins", "Password", p4d);
	}

	/**
	 * Test for syncing a graph source using script with source specified
	 *
	 * @throws Exception
	 */
	@Test
	public void testSyncGraphSources() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', source: [$class: 'GraphSource', graph: '''//graph/docker-plugin\n" +
				"//graph/scm-api-plugin''']," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "graph-sources-sync");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: //graph/scm-api-plugin.git@5631932f5cdf6c3b829911b6fe5ab42d436d74da", run);
	}

	@Test
	public void testSyncGraphSource() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', source: [$class: 'GraphSource', graph: '//graph/scm-api-plugin']," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "graph-source-sync");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: //graph/scm-api-plugin.git@5631932f5cdf6c3b829911b6fe5ab42d436d74da", run);
	}

	/**
	 * Test for syncing a graph source with no source specified. i.e legacy script with new changes.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSyncGraphSourceWithLegacyScript() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', depotPath: '//graph/scm-api-plugin'," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "graph-source-sync-legacy");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: //graph/scm-api-plugin.git@5631932f5cdf6c3b829911b6fe5ab42d436d74da", run);
	}

	/**
	 * Test for syncing a depot source using script with source specified
	 *
	 * @throws Exception
	 */
	@Test
	public void testSyncDepotSource() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', source: [$class: 'DepotSource', depot: '//depot/...']," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "depot-source-sync");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: 10306", run);
	}

	/**
	 * Test for syncing a depot source with no source specified. i.e legacy script with new changes.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSyncDepotSourceWithLegacyScript() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', depotPath: '//depot/...'," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "depot-source-sync-legacy");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: 10306", run);
	}

	/**
	 * Test for syncing a depot source with no source specified. i.e legacy script with new changes.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSyncDepotSourceJava() throws Exception {

		String id = auth.getId();
		String pipelineScript = "pipeline{\nagent any \nstages{\nstage('l'){\n" +
				"steps{" +
				"p4sync charset: 'none', credential: '" + id + "', depotPath: '//graph/scm-api-plugin/....java'," +
				"populate: [$class: 'GraphHybridImpl', parallel: [enable: false, minbytes: '1024', minfiles: '1', threads: '4'], pin: '', quiet: false]" +
				"\n}\n}\n}\n}";

		WorkflowJob job = jenkins.jenkins.createProject(WorkflowJob.class, "depot-java-source-sync-legacy");
		job.setDefinition(new CpsFlowDefinition(pipelineScript, false));
		WorkflowRun run = jenkins.assertBuildStatusSuccess(job.scheduleBuild2(0));
		jenkins.assertBuildStatusSuccess(run);
		jenkins.assertLogContains("P4 Task: syncing files at change: 1030", run);
	}

	@Test
	public void testGetClientView() {
		String clientView = AbstractSource.getClientView("//depot", "job1");
		Assert.assertEquals("//depot/... //job1/...", clientView);

		clientView = AbstractSource.getClientView("//depot/", "job1");
		Assert.assertEquals("//depot/... //job1/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src", "job1");
		Assert.assertEquals("//depot/src/... //job1/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/", "job1");
		Assert.assertEquals("//depot/src/... //job1/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/...", "job1");
		Assert.assertEquals("//depot/src/... //job1/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/....", "job1");
		Assert.assertEquals("//depot/src/.... //job1/....", clientView);

		clientView = AbstractSource.getClientView("//depot/src/....java", "job1");
		Assert.assertEquals("//depot/src/....java //job1/....java", clientView);

		clientView = AbstractSource.getClientView("//depot/src\n//depot/tgt", "job1");
		Assert.assertEquals("//depot/src/... //job1/depot/src/...\n//depot/tgt/... //job1/depot/tgt/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/\n//depot/tgt", "job1");
		Assert.assertEquals("//depot/src/... //job1/depot/src/...\n//depot/tgt/... //job1/depot/tgt/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/\n//depot/tgt/", "job1");
		Assert.assertEquals("//depot/src/... //job1/depot/src/...\n//depot/tgt/... //job1/depot/tgt/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/...\n//depot/tgt/...", "job1");
		Assert.assertEquals("//depot/src/... //job1/depot/src/...\n//depot/tgt/... //job1/depot/tgt/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/...\n//depot/tgt/", "job1");
		Assert.assertEquals("//depot/src/... //job1/depot/src/...\n//depot/tgt/... //job1/depot/tgt/...", clientView);

		clientView = AbstractSource.getClientView("//depot/src/....java\n//depot/tgt/", "job1");
		Assert.assertEquals("//depot/src/....java //job1/depot/src/....java\n//depot/tgt/... //job1/depot/tgt/...", clientView);
	}
}
