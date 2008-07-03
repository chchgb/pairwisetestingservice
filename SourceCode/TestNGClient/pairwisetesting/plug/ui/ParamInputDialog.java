package pairwisetesting.plug.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pairwisetesting.plug.util.ParamerterGenerator;

public class ParamInputDialog extends Dialog {
	private String paramType;
	private String result;
	private Combo combo;
	private Text text;
	private Text text_1;
	private Text text_2;

	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public ParamInputDialog(Shell parentShell) {
		super(parentShell);
		this.paramType = "";
		this.result = "";
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);
		final Label generationLabel = new Label(container, SWT.NONE);
		generationLabel.setText("Generate mode :");

		final ComboViewer comboViewer = new ComboViewer(container, SWT.BORDER);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				if(paramType.equals("int") && combo.getSelectionIndex() == 0){
					
				}
			}
		});
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData());
		if(paramType.equals("int")){
			initIntDialog();
		}else if(paramType.equals("float")){
			initFloatDialog();
			
		}else if(paramType.equals("String")){
			initStringDialog();
		}
		initIntRandom(container);
		//
		return container;
	}


	private void initIntDialog() {
		// TODO Auto-generated method stub
		combo.add("Random");
		combo.add("mode one");
		combo.select(0);
	}
	

	private void initFloatDialog() {
		// TODO Auto-generated method stub
		combo.add("Random");
		combo.add("mode one");
	}

	private void initStringDialog() {
		// TODO Auto-generated method stub
		combo.add("Random");
		combo.add("mode one");
		combo.select(0);
	}


	
	private void initIntRandom(Composite parent){
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Group detailGroup = new Group(container, SWT.NONE);
		detailGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		detailGroup.setText("Detail");
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 4;
		detailGroup.setLayout(gridLayout_2);

		final Label numberLabel = new Label(detailGroup, SWT.NONE);
		numberLabel.setText("Number :");

		text = new Text(detailGroup, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setText("10");
		new Label(detailGroup, SWT.NONE);
		new Label(detailGroup, SWT.NONE);

		final Label minLabel = new Label(detailGroup, SWT.NONE);
		minLabel.setText("Min :");

		text_1 = new Text(detailGroup, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text_1.setText("1");

		final Label maxLabel = new Label(detailGroup, SWT.NONE);
		maxLabel.setText("Max :");

		text_2 = new Text(detailGroup, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text_2.setText("100");
		parent.layout();
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}
	
	public void setParamType(String type){
		this.paramType = type;
	}

	public String getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if(paramType.equals("int") && combo.getSelectionIndex() == 0){
				this.result = ParamerterGenerator.intRandomGenerator(Integer.valueOf(text.getText()),Integer.valueOf(text_1.getText()),Integer.valueOf(text_2.getText()));
			}
		}
		super.buttonPressed(buttonId);
	}

}
