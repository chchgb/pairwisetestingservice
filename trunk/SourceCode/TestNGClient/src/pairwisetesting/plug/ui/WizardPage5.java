package pairwisetesting.plug.ui;

import java.io.IOException;
import java.util.ArrayList;

import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import pairwisetesting.util.TextFile;

import testingngservices.client.TestNGClient;
import testingngservices.client.TestNGCore;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;

public class WizardPage5 extends WizardPage {

	private StyledText styledText;
	private String testcase;
	private String testcasePath;

	/**
	 * Create the wizard
	 */
	public WizardPage5() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);
		//
		setControl(container);

		final SourceViewer sourceViewer = new SourceViewer(container, null,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		styledText = sourceViewer.getTextWidget();
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sourceViewer.setEditable(true);

		// styledText.setSize(300, 200);

		initWidget();

		IDocument document = new Document(testcase);
		sourceViewer.setDocument(document);

		final Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				IDocument document = sourceViewer.getDocument();
				testcase = document.get();
//				try {
//					testcase = document.get(0, 100);
//				} catch (BadLocationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
				//System.out.println(testcase);
				//System.out.println("testcasePath : "+ testcasePath);
				TextFile.write(testcasePath,testcase);
			}
		});
		final GridData gd_button = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gd_button.widthHint = 80;
		button.setLayoutData(gd_button);
		button.setText("Ã·Ωª");
	}

	private void initWidget() {
		MyWizard wizard = (MyWizard) getWizard();
		String pairwiseXML = wizard.getPairwiseXML();

		WizardPage3 page3 = (WizardPage3) this.getPreviousPage()
				.getPreviousPage();

		String expectancyXML = this.addExpectancyElement(pairwiseXML, page3
				.getExpectancyValueList());

		//System.out.println(expectancyXML);

		TestNGClient testNGClient = wizard.getTestNGClient();

		testNGClient.setPairwiseResult(expectancyXML);

		testcase = testNGClient.getTestCase();
		testcasePath = testNGClient.getTestCasePath();

	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

	private String addExpectancyElement(String pairwiseXML,
			ArrayList<String> expectancyList) {
		nu.xom.Document doc = null;
		try {
			doc = new Builder().build(pairwiseXML, null);
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();

		Elements runs = root.getChildElements("run");

		for (int i = 0; i < runs.size(); i++) {
			Element run = runs.get(i);
			Element expectedElement = new Element("expected");
			expectedElement.appendChild(expectancyList.get(i));
			run.appendChild(expectedElement);
		}
		return doc.toXML();
	}

}
