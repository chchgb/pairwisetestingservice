package pairwisetesting.plug.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import pairwisetesting.coredomain.MetaParameter;
import testingngservices.client.PairwiseClient;
import testingngservices.testcasetemplate.core.Parameter;

public class WizardPage3 extends WizardPage {

	private Table table;
	private MetaParameter metaParameter;
	private String engineName;
	//private ArrayList<String> returnTypeList;
	private ArrayList<ArrayList<String>> tableInput;
	private ArrayList<String> expectancyValueList;
	private String returnType;
	private Object methodName;

	/**
	 * Create the wizard
	 */
	public WizardPage3() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		metaParameter = new MetaParameter();
		expectancyValueList = new ArrayList<String>();
	}

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		//
		setControl(container);


		final TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				String message = "";
				int index = table.getSelectionIndex();
				ArrayList<String> testCase = tableInput.get(index);
				for(int i=1;i<testCase.size();i++){
					message += (testCase.get(i) +", ");
				}
				
				message = message.substring(0, message.length()-2);
				
				String initValue = testCase.get(0);
				message = methodName + " ( "+ message +" )";
				//InputDialog input = new InputDialog(null, "Input Dialog", message, initValue, null);
				ExpectancyValueDialog input = new ExpectancyValueDialog(null);
				input.setMessage(message);
				input.setReturnType(returnType);
				input.setExpectancyValue(initValue);
				//input.
				
				if(input.open() == Window.OK){
					System.out.println(input.getExpectancyValue());
					
					String result = input.getExpectancyValue();
					testCase.set(0, result);
					expectancyValueList.set(index,result);
					tableViewer.setInput(tableInput);
				}
				
				
			}
		});
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		//table.setSize(-1, 100);
		//initTable(table);
		
		WizardPage2 page2 = (WizardPage2) getPreviousPage();
		metaParameter = page2.getMetaParameter();
		engineName = page2.getEngineName();
		
		WizardPage1 page1 = (WizardPage1) page2.getPreviousPage();
		List<Parameter> parameterList = Arrays.asList(page1.getParameters());
		returnType = page1.getReturnType();
		methodName = page1.getMethodName();
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("Expected");

		for (Parameter parameter : parameterList) {
			final TableColumn newColumnTableColumn = new TableColumn(table,
					SWT.NONE);
			newColumnTableColumn.setWidth(100);
			newColumnTableColumn.setText(parameter.getName());
		}
		
		

		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new WizardPage3TableViewerLabelProvider());
		tableInput = getPairwiseList();
		tableViewer.setInput(tableInput);

	}

	private ArrayList<ArrayList<String>> getPairwiseList() {

		MyWizard wizard = (MyWizard) getWizard();
		PairwiseClient client = new PairwiseClient(engineName, wizard
				.getWebSerivcesURL());
		String xmlData = client.getPairwiseXML(metaParameter);

		wizard.setPairwiseXML(xmlData);
		return parserPairwiseResult(xmlData);

	}

	private ArrayList<ArrayList<String>> parserPairwiseResult(String xmlData) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		expectancyValueList = new ArrayList<String>();
		try {
			Document doc = new Builder().build(xmlData, null);
			Element root = doc.getRootElement();

			Elements runs = root.getChildElements("run");

			for (int i = 0; i < runs.size(); i++) {
				Element run = runs.get(i);
				Elements levels = run.getChildElements("level");
				ArrayList<String> levelList = new ArrayList<String>();
				
				for(int index = 0;index<levels.size();index++){
					String level = levels.get(index).getValue();
					levelList.add(level);
				}
				
				result.add(levelList);

			}
			
			//returnTypeList = new ArrayList<String>(result.size());
			String initValue = "";
			if(returnType.equals("boolean")){
				initValue = "false";
			}else if(returnType.equals("int") || returnType.equals("double")){
				initValue = "0";
			}else if(returnType.equals("float")){
				initValue = "0.0";
			}else if(returnType.equals("String")){
				initValue = "";
			}
			
			
			for(int i=0;i<result.size();i++){
				result.get(i).add(0,initValue);
				expectancyValueList.add(initValue);
			}
			

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

		return result;
	}
	
	public ArrayList<String> getExpectancyValueList(){
		return this.expectancyValueList;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

}
