package pairwisetesting.complex;

import java.util.ArrayList;

public class MethodUnderTest {

	private ArrayList<Parameter> paramlist = new ArrayList<Parameter>();
	private Parameter returnValueParameter;
	private String name = "";

	public MethodUnderTest() {
	}
	
	public MethodUnderTest(String returnType, String name) {
		this.returnValueParameter = new SimpleParameter(returnType, "ReturnValue");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add(Parameter p) {
		paramlist.add(p);
	}

	public Parameter[] getParameters() {
		return paramlist.toArray(new Parameter[0]);
	}
	
	public Parameter getReturnValueParameter() {
		return returnValueParameter;
	}

	public void setReturnValueRarameter(Parameter returnValueParameter) {
		this.returnValueParameter = returnValueParameter;
	}
	
	public String getReturnType() {
		return this.returnValueParameter.getType();
	}
	
	public void accept(IParameterVisitor pv) {
		for (Parameter p : this.paramlist) {
			p.accept(pv);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((paramlist == null) ? 0 : paramlist.hashCode());
		result = prime * result
				+ ((returnValueParameter == null) ? 0 : returnValueParameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodUnderTest other = (MethodUnderTest) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (paramlist == null) {
			if (other.paramlist != null)
				return false;
		} else if (!paramlist.equals(other.paramlist))
			return false;
		if (returnValueParameter == null) {
			if (other.returnValueParameter != null)
				return false;
		} else if (!returnValueParameter.equals(other.returnValueParameter))
			return false;
		return true;
	}
	
}
