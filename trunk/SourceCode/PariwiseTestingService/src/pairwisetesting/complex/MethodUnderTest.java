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
				+ ((returnType == null) ? 0 : returnType.hashCode());
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
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
	
}
