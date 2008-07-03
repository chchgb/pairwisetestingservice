package pairwisetesting.plug.ui;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import pairwisetesting.plug.util.FunctionInfo;


public class SampleWizard{
	public static void main(String[] args){
		Shell shell = new Shell();
		MyWizard wizard = new MyWizard();
		
		FunctionInfo info = new FunctionInfo();
		
		info.setClassUnderTextValue("Range");
		info.setFilePath("");
		info.setPackageNameTextValue("test.math");
		//info.setMethodDetail(methodDetails);
		
		//info.setConstructorArgumentTextValueList(constructorParameterList);
		//info.setMethodTextValueList(methodList);
		
		wizard.setFunctionInfo(info);
		
		
		WizardDialog dialog = new WizardDialog(shell,wizard);
		dialog.setMinimumPageSize(300, 200);
		dialog.open();
		
		
	}
	

}
