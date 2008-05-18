package test.dependency;

import java.util.ArrayList;

public class B {
	A[] a;
	String[] s;
	
	public void foo(String[] g) {
		s = new String[0];
		a = new A[0];
		C[][] c = new ArrayList<C[]>().toArray(null);
	}
}
