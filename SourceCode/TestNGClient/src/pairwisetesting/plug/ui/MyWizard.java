package pairwisetesting.plug.ui;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.plug.util.FunctionInfo;
import testingngservices.client.LibManager;
import testingngservices.client.TestNGClient;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;



public class MyWizard extends Wizard {

	private FunctionInfo functionInfo;
	private TestNGClient testNGClient;
	private String pairwiseXML;
	private TestCaseTemplateParameter tp;
	
	private String webServicesURL;
	private InvocationSequenceFinder finder;
	
	
	
	
	private WizardPage1 page1;
	private WizardPage2 page2;
	private WizardPage3 page3;
	private WizardPage4 page4;
	private WizardPage5 page5;
	private WizardPage6 page6;
	private LibManager libManager;
	
	public MyWizard(){
		//this.metaParameter = new MetaParameter();
		this.functionInfo = new FunctionInfo();
		this.webServicesURL = "localhost";
		this.pairwiseXML="";
		this.tp = new TestCaseTemplateParameter();
	}
	
	@Override
	public void createPageControls(Composite pageContainer) {
	    //super.createPageControls(pageContainer);
	}
	
	public void setLibManager(LibManager lib){
		this.libManager = lib;
	}
	
	public LibManager getLibManager(){
		return this.libManager;
	}
	
	public void setTestCaseTemplateParameter(TestCaseTemplateParameter tp){
		this.tp =tp;
	}
	
	public TestCaseTemplateParameter getTestCaseTemplateParameter(){
		return this.tp;
	}
	
	public void setPairwiseXML(String xml){
		this.pairwiseXML =xml;
	}
	
	public String getPairwiseXML(){
		return this.pairwiseXML;
	}
	public void setTestNGClient(TestNGClient client){
		this.testNGClient = client;
	}
	
	public TestNGClient getTestNGClient(){
		return this.testNGClient;
	}
	
	public void setWebServicesURL(String url){
		this.webServicesURL = url;
	}
	
	public String getWebSerivcesURL(){
		return this.webServicesURL;
	}
	
	@Override
	public void addPages(){
		page1 = new WizardPage1();
		page2 = new WizardPage2();
		page3 = new WizardPage3();
		page4 = new WizardPage4();
		page5 = new WizardPage5();
		page6 = new WizardPage6();
		
		page1.setFunctionInfo(this.functionInfo);
		page1.setWebServicesURL(this.webServicesURL);
		
		addPage(page1);
		addPage(page2);
		addPage(page3);
		addPage(page4);
		addPage(page5);
		addPage(page6);
		
	}

	@Override
	public boolean performFinish() {
		
		// TODO Auto-generated method stub
		MessageDialog.openInformation(null,"PairwiseTesting","Test Finish!");
		return true;
	}
	
	@Override
	public boolean canFinish(){
		if(this.getContainer().getCurrentPage() != page6){
			return false;
		}else{
			return super.canFinish();
		}
	}
	
	public void setFunctionInfo(FunctionInfo info){
		this.functionInfo = info;
	}
	
	public FunctionInfo getFunctionInfo(){
		return this.functionInfo;
	}
	
	public InvocationSequenceFinder getInvocationSequenceFinder() {
		return this.finder;
	}
	
	public void setInvocationSequenceFinder(InvocationSequenceFinder finder) {
		this.finder =finder;
	}
	
	
	
}
