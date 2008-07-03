package pairwisetesting.plug.util;

import java.util.ArrayList;
import java.util.HashMap;


public class FunctionInfo {
	
	private String filePath;
	
	private String packageNameTextValue;
	
	private String classUnderTextValue;
	
	private ArrayList<String> methodTextValueList;
	
	private ArrayList<String> constructorArgumentTextValueList;
	
	private String singletonMethodTextValue;
	
	private String checkStateMethodTextValue;
	
	private HashMap<String, HashMap> methodDetail;
	
	
	
	public String getPackageNameTextValue() {
		return packageNameTextValue;
	}

	public void setPackageNameTextValue(String packageNameTextValue) {
		this.packageNameTextValue = packageNameTextValue;
	}

	public String getClassUnderTextValue() {
		return classUnderTextValue;
	}

	public void setClassUnderTextValue(String classUnderTextValue) {
		this.classUnderTextValue = classUnderTextValue;
	}

	public String getSingletonMethodTextValue() {
		return singletonMethodTextValue;
	}

	public void setSingletonMethodTextValue(String singletonMethodTextValue) {
		this.singletonMethodTextValue = singletonMethodTextValue;
	}

	public String getCheckStateMethodTextValue() {
		return checkStateMethodTextValue;
	}

	public void setCheckStateMethodTextValue(String checkStateMethodTextValue) {
		this.checkStateMethodTextValue = checkStateMethodTextValue;
	}

	public ArrayList<String> getMethodTextValueList() {
		return methodTextValueList;
	}

	public void setMethodTextValueList(ArrayList<String> methodTextValueList) {
		this.methodTextValueList = methodTextValueList;
	}

	public ArrayList<String> getConstructorArgumentTextValueList() {
		return constructorArgumentTextValueList;
	}

	public void setConstructorArgumentTextValueList(
			ArrayList<String> constructorArgumentTextValueList) {
		this.constructorArgumentTextValueList = constructorArgumentTextValueList;
	}

	public HashMap<String, HashMap> getMethodDetail() {
		return methodDetail;
	}

	public void setMethodDetail(HashMap<String, HashMap> methodDetail) {
		this.methodDetail = methodDetail;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
