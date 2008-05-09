package test.dependency;

import java.util.ArrayList;

public class B {
	C[] c;
	A a;
	String[] s;
	
	public C[][] foo(String[] g) {
		return new ArrayList<C[]>().toArray(new C[0][0]);
	}
}
