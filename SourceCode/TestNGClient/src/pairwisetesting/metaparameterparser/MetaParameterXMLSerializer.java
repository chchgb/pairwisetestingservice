package pairwisetesting.metaparameterparser;

import nu.xom.Document;
import nu.xom.Element;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;

public class MetaParameterXMLSerializer {

	public String serialize(MetaParameter mp) {
		
		Element root = new Element("metaparameter");

		// Strength
		Element strengthElement = new Element("strength");
		strengthElement.appendChild("" + mp.getStrength());
		root.appendChild(strengthElement);
		
		// Factors
		for (Factor factor : mp.getFactors()) {
			
			Element factorElement = new Element("factor");
			
			// Name
			Element nameElement = new Element("name");
			nameElement.appendChild(factor.getName());
			factorElement.appendChild(nameElement);
			
			// Levels
			for (String level : factor.getLevels()) {
				Element levelElement = new Element("level");
				levelElement.appendChild(level);
				factorElement.appendChild(levelElement);
			}
			
			root.appendChild(factorElement);
		}
		
		// Constraints
		for (String constraint : mp.getConstraints()) {
			Element constraintElement = new Element("constraint");
			constraintElement.appendChild(constraint);
			root.appendChild(constraintElement);
		}
		
		Document doc = new Document(root);
		return doc.toXML();
	}
}
