package pairwisetesting.plug.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
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

public class Sample extends Dialog {

	private Text text_2;
	private Text text_1;
	private Text text;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public Sample(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Group rangeGroup = new Group(container, SWT.NONE);
		rangeGroup.setText("Range");
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 3;
		rangeGroup.setLayout(gridLayout_1);

		final Button randomButton = new Button(rangeGroup, SWT.RADIO);
		randomButton.setText("Random");

		final Button button_1 = new Button(rangeGroup, SWT.RADIO);
		button_1.setText(">=0");

		final Button button_2 = new Button(rangeGroup, SWT.RADIO);
		button_2.setText("Radio Button");

		final Group detailGroup = new Group(container, SWT.NONE);
		detailGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		detailGroup.setText("Detail");
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 4;
		detailGroup.setLayout(gridLayout_2);

		final Label numberLabel = new Label(detailGroup, SWT.NONE);
		numberLabel.setText("Number :");

		text = new Text(detailGroup, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new Label(detailGroup, SWT.NONE);
		new Label(detailGroup, SWT.NONE);

		final Label minLabel = new Label(detailGroup, SWT.NONE);
		minLabel.setText("Min :");

		text_1 = new Text(detailGroup, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label maxLabel = new Label(detailGroup, SWT.NONE);
		maxLabel.setText("Max :");

		text_2 = new Text(detailGroup, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		//
		return container;
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

}
