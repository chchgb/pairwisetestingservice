package testingngservices.testcasetemplate;

public class Invoke {
	private String statement;
	private String returnTypeName;
	
	public Invoke(String statement, String returnTypeName) {
		this.statement = statement;
		this.returnTypeName = returnTypeName;
	}
	
	public Invoke() {
	}
	
	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public String getReturnTypeName() {
		return returnTypeName;
	}
	
	public void setReturnTypeName(String returnTypeName) {
		this.returnTypeName = returnTypeName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((returnTypeName == null) ? 0 : returnTypeName.hashCode());
		result = prime * result
				+ ((statement == null) ? 0 : statement.hashCode());
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
		final Invoke other = (Invoke) obj;
		if (returnTypeName == null) {
			if (other.returnTypeName != null)
				return false;
		} else if (!returnTypeName.equals(other.returnTypeName))
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.returnTypeName + " " + this.getStatement();
	}
}
