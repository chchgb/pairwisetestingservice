package com.john.actions;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class TestGenerationAction implements IObjectActionDelegate {

	private ISelection selection;
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object select = structuredSelection.getFirstElement();
			
			//save the result
			String result = "";
			
			if (select instanceof IMethod) {
				//get ICompilation : .java file object
				IMethod method = (IMethod)select;
				System.out.println(method.getElementName());
				for (String parameter : method.getParameterTypes()) {
					System.out.print(parameter);
				}

				try {
					System.out.println(method.getReturnType());
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(method.toString());
				
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		this.selection = selection;
	}


}
