import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.testcasesgenerator.TXTTestCasesGenerator;

public class PairwisetestingDemo {

	public static void main(String[] args) throws Exception {
		PairwiseTestingToolkit toolkit = new PairwiseTestingToolkit();
		toolkit.setMetaParameterProvider(new IMetaParameterProvider() {
			public MetaParameter get() {
				Factor f1 = new Factor("OS");
				f1.addLevel("Windows XP");
				f1.addLevel("Solaris 10");
				f1.addLevel("Red Hat 9");
				Factor f2 = new Factor("Browser", new String[] { "IE",
						"Firefox",
						"Opera" });
				Factor f3 = new Factor("Memory", new String[] { "255M", "1G",
						"2G" });
				Factor f4 = new Factor("DB", new String[] { "MySQL", "Oracle",
						"DB2" });
				MetaParameter mp = new MetaParameter(2);
				mp.addFactor(f1);
				mp.addFactor(f2);
				mp.addFactor(f3);
				mp.addFactor(f4);
				return mp;
			}
		});

		toolkit.setEngine(new AMEngine());
		toolkit.setTestCasesGenerator(new TXTTestCasesGenerator());
		System.out.println(toolkit.generateTestCases());

	}

}
