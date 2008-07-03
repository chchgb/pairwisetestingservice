package pairwisetesting.plug.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExpectancyValueDialog extends Dialog {

	private Combo combo;
	private Text text;
	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */

	private String expectancyValue;
	private String returnType;
	
	private String message = "Message";

	public ExpectancyValueDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout());
		
		if(returnType.equals("boolean")){

			final Label label = new Label(container, SWT.NONE);
			GridData data = new GridData(SWT.CENTER, SWT.BOTTOM, true, true);
			data.widthHint = 160;
			data.heightHint = 42;
			
			label.setLayoutData(data);
			label.setText(message);
			final ComboViewer comboViewer = new ComboViewer(container, SWT.BORDER);
			combo = comboViewer.getCombo();
			final GridData gd_combo = new GridData(SWT.CENTER, SWT.FILL, false, true);
			gd_combo.widthHint = 136;
			combo.setLayoutData(gd_combo);
			combo.add("false");
			combo.add("true");
			if(expectancyValue.equals("false")){
				combo.select(0);
			}else{
				combo.select(1);
			}
			
		}else{
			text = new Text(container, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			text.setText(expectancyValue);
			text.selectAll();
		}

		return container;
	}

	/**
	 * Create contents of the button bar
	 * 
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
		return new Point(500, 256);
	}

	public void setExpectancyValue(String value) {
		this.expectancyValue = value;
	}

	public String getExpectancyValue() {
		return this.expectancyValue;
	}

	public void setReturnType(String type) {
		this.returnType = type;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if(returnType.equals("boolean")){
				expectancyValue = combo.getItem(combo.getSelectionIndex());
			}else{
				expectancyValue = text.getText();
			}
			
			//return;
		}
		super.buttonPressed(buttonId);
	}
}
