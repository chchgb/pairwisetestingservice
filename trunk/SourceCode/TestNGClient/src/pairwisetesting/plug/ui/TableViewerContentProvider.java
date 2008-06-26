package pairwisetesting.plug.ui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewerContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		}else{
			return null;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
