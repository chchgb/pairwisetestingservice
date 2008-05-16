package testingngservices.testcasetemplate.core;

public class Invocation {
	private String content;
	private boolean hasReturnType;
	private InvocationCount count;
	
	public Invocation(String content, boolean hasReturnType, InvocationCount count) {
		this.content = content;
		this.hasReturnType = hasReturnType;
		this.count = count;
	}
	
	public Invocation() {
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean hasReturnType() {
		return hasReturnType;
	}

	public void hasReturnType(boolean hasReturnType) {
		this.hasReturnType = hasReturnType;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

	public InvocationCount getCount() {
		return count;
	}

	public void setCount(InvocationCount count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + (hasReturnType ? 1231 : 1237);
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
		final Invocation other = (Invocation) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (hasReturnType != other.hasReturnType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.hasReturnType + " " + this.content + " " + this.count;
	}

}
