package testingngservices.test;

import org.junit.*;

import testingngservices.core.TestWorkflow;
import testingngservices.sourcefile.TestingMetaParameter;

public class TestNGServiceTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void workFlow() {
		TestingMetaParameter temp = new TestingMetaParameter();
		temp.addFile("src/Util.java");

		temp.setTestCase("src/UnitTest.java");

		TestWorkflow workflow = new TestWorkflow(temp);

		workflow.testWorkflow();

	}

}
