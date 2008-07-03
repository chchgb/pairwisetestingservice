package pairwisetesting.plug.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import testingngservices.testcasetemplate.core.Parameter;

public class WizardPage2 extends WizardPage {

	//private Table table;
	//private ArrayList<String> paramList;
	private Combo combo;
	private Text text;
	private Parameter[] parameters;
	private ArrayList<Text> paramText;
	
	private MetaParameter metaParameter;

	/**
	 * Create the wizard
	 */
	public WizardPage2() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		//metaParameter = new MetaParameter(2);
		
		
		
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

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 4;
		composite.setLayout(gridLayout_1);

		final Label pairwiseEngineLabel = new Label(composite, SWT.NONE);
		pairwiseEngineLabel.setText("Pairwise Engine :");

		final ComboViewer comboViewer = new ComboViewer(composite, SWT.BORDER);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				setPageComplete(false);
			}
		});
		combo = comboViewer.getCombo();
		final GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_combo.widthHint = 133;
		combo.setLayoutData(gd_combo);
		combo.add("PICTEngine");
		combo.add("JennyEngine");
		combo.add("AMEngine");
		//combo.add("TVGEngine");
		combo.select(0);
		

		final Label strenLabel = new Label(composite, SWT.NONE);
		strenLabel.setText("Strength :");

		text = new Text(composite, SWT.BORDER);
		final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text.widthHint = 26;
		text.setLayoutData(gd_text);
		text.setText("2");
		text.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setPageComplete(false);
			}
		});

		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);

		initWidgets(group);

		final Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				
				metaParameter = new MetaParameter(Integer.valueOf(text.getText()));
				Iterator<Parameter> ite_list = Arrays.asList(parameters).iterator();
				Iterator<Text> ite_text = paramText.iterator();
				while(ite_list.hasNext()){
					String[] level = ite_text.next().getText().split(",");
					Factor factor = new Factor(ite_list.next().getName(),level);
					metaParameter.addFactor(factor);					
				}
				
				
				IWizardPage page = getNextPage();
				if (page.getControl() != null){
					page.dispose();
				}
				
				if(page.getNextPage().getControl() != null){
					page.getNextPage().dispose();
				}
				
				if(page.getNextPage().getNextPage().getControl() != null){
					page.getNextPage().getNextPage().dispose();
				}
				
				if(page.getNextPage().getNextPage().getNextPage().getControl() != null){
					page.getNextPage().getNextPage().getNextPage().dispose();
				}
				
				setPageComplete(true);
				
				
			}
		});
		final GridData gd_button = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gd_button.widthHint = 80;
		button.setLayoutData(gd_button);
		button.setText("Ã·Ωª");
		

	}
	
	private void initWidgets(final Group group){
		
		//this.paramList = ((WizardPage1) this.getPreviousPage()).getParamList();
		this.parameters = ((WizardPage1) this.getPreviousPage()).getParameters();
		this.paramText = new ArrayList<Text>();
		
		//System.out.println("paramList : "+ paramList);
		
		
		for(final Parameter parameter : parameters){
			final Label label = new Label(group, SWT.NONE);
			label.setText(parameter.getName()+"("+parameter.getType()+")"+" £∫");
			
			final Text paramText  = new Text(group, SWT.BORDER);
			
			paramText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			paramText.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setPageComplete(false);
				}
			});
			paramText.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					ParamInputDialog input = new ParamInputDialog(group.getParent().getShell());
					//String type = label.getText();
					input.setParamType(parameter.getType());
					if(input.open() == Window.OK){
						paramText.setText(input.getResult());
						
					}
					
				}
			});
			this.paramText.add(paramText);
		}
		
	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}
	
	public MetaParameter getMetaParameter(){
		return this.metaParameter;
	}

	public String getEngineName() {
		// TODO Auto-generated method stub
		return combo.getItem(combo.getSelectionIndex());
	}
	
//	public ArrayList<String> getParamList(){
//		return this.paramList;
//	}
}
