package pairwisetesting.plug.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import testingngservices.client.LibManager;
import testingngservices.client.TestNGClient;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;

public class WizardPage4 extends WizardPage {

	private Table table_3;
	private Table table_2;
	private Table table_1;
	private Table table;
	private TableViewer tableViewer;
	private TableViewer tableViewer_1;
	private TableViewer tableViewer_2;
	private TableViewer tableViewer_3;

	private ArrayList<String> dependencyLib;
	private ArrayList<String> classPathLib;
	private Map<String, String> userLibFullPath;

	/**
	 * Create the wizard
	 */
	public WizardPage4() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		this.userLibFullPath = new TreeMap<String, String>();
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

		setPageComplete(false);

		final SashForm sashForm = new SashForm(container, SWT.NONE);

		final Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout());

		final Label dependencyLabel = new Label(composite, SWT.NONE);
		dependencyLabel.setText("依赖库：");

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new WizardPage4TableViewerLabelProvider());

		final Label label = new Label(composite, SWT.NONE);
		label.setText("AutoMock 信息：");

		tableViewer_3 = new TableViewer(composite, SWT.BORDER);
		table_3 = tableViewer_3.getTable();
		table_3.setLinesVisible(true);
		table_3.setHeaderVisible(true);
		table_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer_3.setContentProvider(new TableViewerContentProvider());
		tableViewer_3.setLabelProvider(new WizardPage4TableViewerLabelProvider());
		

		final Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout());

		final Label classpathLibLabel = new Label(composite_1, SWT.NONE);
		classpathLibLabel.setText("ClassPath Lib :");

		tableViewer_1 = new TableViewer(composite_1, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		table_1 = tableViewer_1.getTable();
		table_1.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				TableItem[] items = table_1.getSelection();
				String fileName = items[0].getText(0);
				// MessageDialog.openInformation(null,"information",name);

				String partName = fileName.substring(
						fileName.lastIndexOf("/") + 1, fileName.length());
				userLibFullPath.put(partName, fileName);

				// userLibFullPath.add(name);
				tableViewer_2.setInput(new ArrayList<String>(userLibFullPath
						.keySet()));
				tableViewer_2.refresh();
				setPageComplete(false);
			}
		});

		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tableViewer_1.setContentProvider(new TableViewerContentProvider());
		tableViewer_1
				.setLabelProvider(new WizardPage4TableViewerLabelProvider());

		final Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new GridLayout());

		final Label userLibLabel = new Label(composite_2, SWT.NONE);
		userLibLabel.setText("User Lib :");

		tableViewer_2 = new TableViewer(composite_2, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		table_2 = tableViewer_2.getTable();
		table_2.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				TableItem[] items = table_2.getSelection();
				String fileName = items[0].getText(0);
				// MessageDialog.openInformation(null,"information",name);
				String partName = fileName.substring(
						fileName.lastIndexOf("/") + 1, fileName.length());
				userLibFullPath.remove(partName);
				tableViewer_2.setInput(new ArrayList<String>(userLibFullPath
						.keySet()));
				tableViewer_2.refresh();
				setPageComplete(false);
			}
		});
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		table_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tableViewer_2.setContentProvider(new TableViewerContentProvider());
		tableViewer_2
				.setLabelProvider(new WizardPage4TableViewerLabelProvider());

		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sashForm.setLayout(new GridLayout());

		final Composite composite_4 = new Composite(container, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 3;
		composite_4.setLayout(gridLayout_2);

		final Button button_2 = new Button(composite_4, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(composite_2.getParent()
						.getShell(), SWT.OPEN);
				String fileName = fileDialog.open();
				if (fileName.contains("\\")) {
					fileName = fileName.replace("\\", "/");
				}
				String partName = fileName.substring(
						fileName.lastIndexOf("/") + 1, fileName.length());
				userLibFullPath.put(partName, fileName);

				// userLibFullPath.add(name);
				tableViewer_2.setInput(new ArrayList<String>(userLibFullPath
						.keySet()));
				tableViewer_2.refresh();
				setPageComplete(false);

			}
		});
		button_2.setText("添加");

		final Button button_3 = new Button(composite_4, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				int index = tableViewer_2.getTable().getSelectionIndex();
				if (index != -1) {
					String removeLib = tableViewer_2.getTable().getItem(index)
							.getText();

					userLibFullPath.remove(removeLib);
					tableViewer_2.setInput(new ArrayList<String>(
							userLibFullPath.keySet()));
					tableViewer_2.refresh();
					setPageComplete(false);

				}

			}
		});
		button_3.setText("删除");

		// final Button button_5 = new Button(composite_3, SWT.NONE);
		// button_5.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(final SelectionEvent e) {
		// userLibFullPath.clear();
		// tableViewer_2.setInput(new
		// ArrayList<String>(userLibFullPath.keySet()));
		// tableViewer_2.refresh();
		// }
		// });
		// button_5.setText("清空");

		final Button button_4 = new Button(composite_4, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				MyWizard wizard = (MyWizard) getWizard();
				LibManager libManager = wizard.getLibManager();
				libManager.setFoundedSet(new HashSet<String>(userLibFullPath
						.values()));
				//wizard.setLibManager(libManager);
				setPageComplete(true);

				IWizardPage page = getNextPage();
				if (page.getControl() != null) {
					page.dispose();
				}
				page = page.getNextPage();
				if (page.getControl() != null) {
					page.dispose();
				}

			}
		});
		button_4.setText("提交");
		sashForm.setWeights(new int[] { 1, 1, 1 });
		initWidget();
	}

	private void initWidget() {
		MyWizard wizard = (MyWizard) getWizard();
		TestCaseTemplateParameter tp = wizard.getTestCaseTemplateParameter();
		
		String endPath = wizard.getFunctionInfo().getFilePath();
		TestNGClient testNGClient = new TestNGClient();
		testNGClient.initTestNGClient(tp, wizard.getWebSerivcesURL());
		testNGClient.setClassPath("bin");
		testNGClient.setEndPath(endPath);
		
		testNGClient.libManager.addFoundedLibFromClasspath(endPath
				+ ".classpath");

		testNGClient.libManager.getNotFoundLib();

		LibManager lib = testNGClient.getDependency();

		this.dependencyLib = new ArrayList<String>(lib.getDependencyLib());
		tableViewer.setInput(this.dependencyLib);

		InvocationSequenceFinder finder = wizard.getInvocationSequenceFinder();

		for (String mock : lib.getMockList()) {

			String instanceName = finder.getFieldName(mock);

			tp.addImport(mock);
			tp.addClassToMockInstanceName(mock, instanceName);
			String[] jMockInvocations = finder.getJMockInvocations(mock);
			tp.addJMockInvocationSequence(mock, jMockInvocations);
		}
		
		ArrayList<String> mockList = lib.getMockList();
		
		tableViewer_3.setInput(mockList);

		System.out.println("MockList : \n" + mockList);

		this.classPathLib = lib.getAllLocalLib();
		tableViewer_1.setInput(this.classPathLib);

		Set<String> libSet = lib.getFoundedLibSet();
		for (String fileName : libSet) {
			String partName = fileName.substring(fileName.lastIndexOf("/") + 1,
					fileName.length());
			this.userLibFullPath.put(partName, fileName);
		}
		tableViewer_2.setInput(new ArrayList<String>(userLibFullPath.keySet()));

		
	//	System.out.println(tp.toXML());
		
		
		wizard.setLibManager(lib);
		//wizard.setTestCaseTemplateParameter(tp);
		wizard.setTestNGClient(testNGClient);

	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

}
