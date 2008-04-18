package pairwisetesting.metaparameterprovider;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.exception.MetaParameterException;

public class XMLMetaParameterProvider implements IMetaParameterProvider {
	private String xmlData;
	public XMLMetaParameterProvider(String xmlData) {
		this.xmlData = xmlData;
	}

	public MetaParameter get() throws MetaParameterException {

		MetaParameter metaParameter = null; 
		Builder parser = new Builder(false);
		try {
			Document doc = parser.build(this.xmlData, null);
			Element root = doc.getRootElement();
			Element strength = root.getFirstChildElement("strength");
			
			metaParameter = new MetaParameter();
			// Strength
			metaParameter.setStrength(Integer.parseInt(strength.getValue()));
			
			// Factors
			Elements factors = root.getChildElements("factor");
			for (int i = 0; i < factors.size(); i++) {
				// Factor
				Element factorElement = factors.get(i);
				Factor factor = new Factor();
				// Name
				Element name = factorElement.getFirstChildElement("name");
				factor.setName(name.getValue());
				// Levels
				Elements levels = factorElement.getChildElements("level");
				for (int j = 0; j < levels.size(); j++) {
					// Level
					Element levelElement = levels.get(j);
					factor.addLevel(levelElement.getValue());
				}
				metaParameter.addFactor(factor);
			}
			
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
		return metaParameter;
	}

}
