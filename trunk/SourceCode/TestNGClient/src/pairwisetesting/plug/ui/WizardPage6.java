package pairwisetesting.plug.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import testingngservices.client.TestNGClient;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;

public class WizardPage6 extends WizardPage {
	
	private String testResult;

	/**
	 * Create the wizard
	 */
	public WizardPage6() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		//
		setControl(container);

		final Browser browser = new Browser(container, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//browser.setUrl("http://www.eclipse.org");
		initWidget();
		browser.setText(testResult);
	}
	
	public void initWidget(){
		MyWizard wizard = (MyWizard) getWizard();

		TestNGClient testNGClient = wizard.getTestNGClient();

		testNGClient.setLibManager(wizard.getLibManager());
		
		testNGClient.setTestCaseTemplateParameter(wizard.getTestCaseTemplateParameter());
		
		
		
		testResult = testNGClient.getTestResult();
		

		
		wizard.setTestNGClient(testNGClient);
		
		
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

}
