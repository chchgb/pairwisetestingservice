package st;
public class Argument {

    private String value;
    private String type;

    public Argument(String value, String type) {
		this.value = value; 
		this.type = type;
	}

    public String getValue() {
		return value;
	}

    public String getType() {
		return type;
	}
}