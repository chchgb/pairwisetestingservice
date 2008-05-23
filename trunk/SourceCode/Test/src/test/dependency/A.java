package test.dependency;

public class A {
	B[] b;
	G g;
	Object[] obj;

	public void foo() {

		String string = "";

		while (string.endsWith("[]")) {
			string = string.substring(0, string.length() - 2);
		}
	}
}
