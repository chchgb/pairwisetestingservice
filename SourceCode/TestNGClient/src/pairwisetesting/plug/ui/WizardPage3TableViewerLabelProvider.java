package pairwisetesting.plug.ui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class WizardPage3TableViewerLabelProvider implements ITableLabelProvider {
	

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ArrayList<String> testCase = (ArrayList<String>)element;
		if(columnIndex < testCase.size()){
			return testCase.get(columnIndex);
		}else{
			return "";
		}
		
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}