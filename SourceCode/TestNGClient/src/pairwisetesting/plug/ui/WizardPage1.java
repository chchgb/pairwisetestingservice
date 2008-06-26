package pairwisetesting.plug.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pairwisetesting.plug.util.FunctionInfo;
import testingngservices.testcasetemplate.MethodSignatureFinder;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;
import testingngservices.testcasetemplate.core.MethodSignature;
import testingngservices.testcasetemplate.core.Parameter;

public class WizardPage1 extends WizardPage {

	private FunctionInfo functionInfo;

	private Text text_6;
	private Text text_5;
	private Text text;
	private Combo combo;
	private Text text_4;
	private Text text_3;
	private Text text_2;
	private Text text_1;
	private Button button;

	// private ArrayList<String> paramList;
	private Parameter[] parameters;
	private String returnType;

	private String methodName;
	private String classUnderTest;
	private String webServicesURL;
	

	/**
	 * Create the wizard
	 */
	public WizardPage1() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		this.functionInfo = new FunctionInfo();
		// this.paramList = new ArrayList<String>();
		this.webServicesURL = "localhost";
		this.parameters = new Parameter[0];

	}

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {

		final Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());

		//
		setControl(container);

		setPageComplete(false);

		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		group.setLayout(gridLayout);

		final Label label = new Label(group, SWT.NONE);
		label.setText("被测方法：");

		final ComboViewer comboViewer = new ComboViewer(group, SWT.BORDER);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ArrayList<String> methodTestValue = new ArrayList<String>();
		methodTestValue = functionInfo.getMethodTextValueList();

		System.out.println("methodTestValue " + methodTestValue);

		if (methodTestValue != null && methodTestValue.size() > 0) {
			combo.setItems(methodTestValue.toArray(new String[0]));
		}

		combo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setPageComplete(false);
				clearWidgets();
				reflash();
				container.layout();
			}
		});

		final Label label_5 = new Label(group, SWT.NONE);
		label_5.setText("静态方法：");

		button = new Button(group, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				button.setSelection(button.getSelection());
			}
		});

		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("所属包：");

		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		text_1.setText(functionInfo.getPackageNameTextValue());

		final Label label_6 = new Label(group, SWT.NONE);
		label_6.setText("所属类：");

		text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setText(functionInfo.getClassUnderTextValue());

		final Label sigleLabel = new Label(group, SWT.NONE);
		sigleLabel.setText("单例方法：");

		text_2 = new Text(group, SWT.BORDER);

		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text_2.setText("");

		final Label label_7 = new Label(group, SWT.NONE);
		label_7.setText("Delta：");

		text_5 = new Text(group, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text_5.setText("");

		final Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("返回植类型：");

		text_3 = new Text(group, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label label_8 = new Label(group, SWT.NONE);
		label_8.setText("状态检测方法：");

		text_6 = new Text(group, SWT.BORDER);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("构造函数参数：");

		text_4 = new Text(group, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label label_9 = new Label(group, SWT.NONE);

		label_9.setText("Web Services 地址：");

		final Label localhostLabel = new Label(group, SWT.NONE);
		localhostLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		localhostLabel.setText(this.webServicesURL);
		localhostLabel.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				InputDialog input = new InputDialog(null, "Input Dialog",
						"Input the Web Services IP", webServicesURL, null);
				if (input.open() == input.OK) {
					webServicesURL = input.getValue();
					localhostLabel.setText(webServicesURL);

					localhostLabel.update();
				}
			}
		});
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);

		final Button button_1 = new Button(group, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				TestCaseTemplateParameter tp = new TestCaseTemplateParameter();

				HashMap method = functionInfo.getMethodDetail().get(
						combo.getText());

				tp.setPackageName(functionInfo.getPackageNameTextValue());
				tp.setClassUnderTest(functionInfo.getClassUnderTextValue());
				tp.setMethodUnderTest(combo.getText());

				tp.setStaticMethod(button.getSelection());
				tp.setReturnType(text_3.getText());

				for (Parameter parameter : parameters) {
					tp.addMethodParameter(parameter.getType(), parameter
							.getName());
				}

				String constructor = text_4.getText().trim();
				if (!constructor.equals("")) {
					String[] constructorArgus = constructor.split(",");
					if (constructorArgus != null && constructorArgus.length > 0) {
						int i = 0;
						for (String argu : constructorArgus) {

							String[] arguArray = argu.trim().split(" ");
							tp.addConstructorArgument(arguArray[0],
									arguArray[1]);
							i++;
						}
					}
				}

				tp.setSingletonMethod(text_2.getText());
				tp.setCheckStateMethod(text_6.getText());
				if (!text_5.getText().equals("")) {
					tp.setDelta(Double.valueOf(text_5.getText()));
				}

				MyWizard wizard = (MyWizard) getWizard();
				wizard.setTestCaseTemplateParameter(tp);
				
				InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(classUnderTest);
				finder.setScopeByMethodSignature(returnType, methodName);
				wizard.setInvocationSequenceFinder(finder);
				
				
				
				

				IWizardPage page = getNextPage();
				if (page.getControl() != null) {
					page.dispose();
				}

				setPageComplete(true);

			}
		});
		final GridData gd_button_1 = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false);
		gd_button_1.widthHint = 80;
		button_1.setLayoutData(gd_button_1);
		button_1.setText("提交");

		combo.select(0);

		reflash();

	}

	public void setFunctionInfo(FunctionInfo info) {
		this.functionInfo = info;
	}

	public FunctionInfo getFunctionInfo() {
		return this.functionInfo;
	}

	public void setWebServicesURL(String webServicesURL) {
		this.webServicesURL = webServicesURL;

	}

	private void reflash() {

		HashMap show = functionInfo.getMethodDetail().get(combo.getText());

		Object object = show.get("returnType");
		String returnType = "";
		if (object != null) {
			returnType = object.toString();
		}

		text_3.setText(returnType);

		ArrayList<String> accessModifiers = (ArrayList<String>) show
				.get("accessModifiers");
		boolean isStatic = false;

		if (accessModifiers != null && accessModifiers.size() > 0) {
			for (String access : accessModifiers) {
				if (access.equals("static"))
					isStatic = true;
			}

		}
		button.setSelection(isStatic);
		parameters = getParametersFromSource();

	}

	private void clearWidgets() {
		button.setSelection(false);
		text_2.setText("");
		text_3.setText("");
		text_4.setText("");
		text_5.setText("");
		text_6.setText("");
	}

	public Parameter[] getParameters() {
		return this.parameters;
	}

	private Parameter[] getParametersFromSource() {
		methodName = combo.getText();

		HashMap show = functionInfo.getMethodDetail().get(methodName);

		System.out.println("methodName : " + methodName);

		returnType = (String) show.get("returnType");

		classUnderTest = functionInfo.getFilePath() + "src/"
				+ functionInfo.getPackageNameTextValue().replace(".", "/")
				+ "/" + functionInfo.getClassUnderTextValue() + ".java";

		System.out.println("returnType : " + returnType);
		System.out.println("file : " + classUnderTest);
		MethodSignatureFinder methodSignatureFinder = new MethodSignatureFinder(classUnderTest);
		MethodSignature ms = methodSignatureFinder.getMethodSignature(returnType, methodName);
		return ms.getParameters();

	}

	public String getReturnType() {
		return this.returnType;
	}

	public String getMethodName() {
		return functionInfo.getPackageNameTextValue() + "."
				+ functionInfo.getClassUnderTextValue() + "\n"
				+ this.methodName;
	}

}
