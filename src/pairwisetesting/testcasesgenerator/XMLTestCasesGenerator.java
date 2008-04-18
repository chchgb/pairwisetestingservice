package pairwisetesting.testcasesgenerator;

import nu.xom.Document;
import nu.xom.Element;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

public class XMLTestCasesGenerator implements ITestCasesGenerator {

	public String generate(MetaParameter mp, String[][] testData) {
		
		Element root = new Element("testcases");
		
		// Factors
		for (Factor factor : mp.getFactors()) {
			Element factorElement = new Element("factor");
			factorElement.appendChild(factor.getName());
			root.appendChild(factorElement);
		}
	
		// Runs
		for (String[] row : testData) {
			Element runElement = new Element("run");
			// Levels
			for (String level : row) {
				Element levelElement = new Element("level");
				levelElement.appendChild(level);
				runElement.appendChild(levelElement);
			}
			root.appendChild(runElement);
		}
			
		Document doc = new Document(root);
		return doc.toXML();
	}

}
