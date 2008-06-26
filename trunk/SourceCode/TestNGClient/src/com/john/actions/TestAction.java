package com.john.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import pairwisetesting.plug.ui.MyWizard;
import pairwisetesting.plug.util.FunctionInfo;
import pairwisetesting.plug.util.MethodParser;


public class TestAction implements IObjectActionDelegate {


	private ISelection selection;
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	@Override
	public void run(IAction action) {

//		WinMain winMain = new WinMain();
		FunctionInfo info = new FunctionInfo();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object select = structuredSelection.getFirstElement();
			
			//save the result
			String result = "";
			
			if (select instanceof ICompilationUnit) {
				//get ICompilation : .java file object
				ICompilationUnit javaFile = (ICompilationUnit)select;
				info.setClassUnderTextValue(javaFile.getElementName().substring(0, javaFile.getElementName().indexOf(".java")));
				
				//get package declarations
				try {
					IPackageDeclaration[] packageDeclaration = javaFile.getPackageDeclarations();
					IJavaProject iJavaProject = javaFile.getJavaProject();
					String filePath = ResourcesPlugin.getWorkspace().getRoot().getLocationURI().toString();
					filePath = filePath.substring(filePath.indexOf("/") + 1, filePath.length()) + "/" + iJavaProject.getElementName() + "/";
					info.setFilePath(filePath);
					

					for (IPackageDeclaration item : packageDeclaration) {
						info.setPackageNameTextValue(item.getElementName());
					}
					
				} catch (JavaModelException e) {

					e.printStackTrace();
				}
				

				try {
					IType[] types = javaFile.getTypes();
					for (IType item : types) {
						IMethod[] methods = item.getMethods();
						HashMap<String, HashMap> methodDetails = new HashMap<String, HashMap>();
						ArrayList<String> methodList = new ArrayList<String>(); 
						ArrayList<String> constructorParameterList = new ArrayList<String>();
						for (IMethod subitem : methods) {
							HashMap map = (HashMap)MethodParser.parse(subitem.toString().substring(0, subitem.toString().indexOf(" [")));

							methodDetails.put(subitem.getElementName(), map);
							methodList.add(subitem.getElementName());
							

							ArrayList<String> parameterList = (ArrayList<String>)map.get("parameters");

							String returnType = (String)map.get("returnType");
							if (returnType == null || returnType.equals(""))
								constructorParameterList = parameterList;

							ArrayList<String> accessModifiers = (ArrayList<String>)map.get("accessModifiers");
							boolean isStatic = false;

							if (accessModifiers != null && accessModifiers.size() > 0) {
								for (String access : accessModifiers) {
									if (access.equals("static")) 
										isStatic = true;
								}
								
							}

							
						}
						info.setMethodDetail(methodDetails);
						info.setConstructorArgumentTextValueList(constructorParameterList);
						info.setMethodTextValueList(methodList);
						
					}
				} catch (JavaModelException e) {

					e.printStackTrace();
				}
				
//				WinMain winMain = new WinMain();
//				winMain.setFunctionInfo(info);
//				winMain.setBlockOnOpen(true);
//				winMain.open();
				
				MyWizard wizard = new MyWizard();
				wizard.setFunctionInfo(info);
				
				
				WizardDialog dialog = new WizardDialog(null,wizard);
				dialog.setMinimumPageSize(300, 200);
				dialog.open();
				
			}
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	
		this.selection = selection;
	}

}
