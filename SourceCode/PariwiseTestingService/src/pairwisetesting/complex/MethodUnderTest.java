package pairwisetesting.complex;

import java.util.ArrayList;

public class MethodUnderTest {

	private String returnType;
	private String name;
	private ArrayList<Parameter> paramlist = new ArrayList<Parameter>();
	
	public MethodUnderTest(String returnType, String name) {
		this.returnType = returnType;
		this.name = name;
	}

	public void add(Parameter p) {
		paramlist.add(p);
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Parameter[] getParameters() {
		return paramlist.toArray(new Parameter[0]);
	}

	public String toXML() {
		return null;
	}
	
}
