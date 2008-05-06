package pairwisetesting.metaparameterparser;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

public class XMLMetaParameterProvider implements IMetaParameterProvider {
	private String xmlData;
	
	private String schemaPath = "";

	public XMLMetaParameterProvider(String xmlData, String schemaPath) {
		this.xmlData = xmlData;
		this.schemaPath = schemaPath;
	}

	public MetaParameter get() throws MetaParameterException {
		MetaParameter metaParameter = null;
		try {
		   	// Get Schema
	        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Source schemaSource = new StreamSource(schemaPath);
	        Schema schema = schemaFactory.newSchema(schemaSource);
	    	
	        // Get SAX Parser Factory and configure it
	    	SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setValidating(false);
	        factory.setNamespaceAware(true);
	        factory.setSchema(schema);
	        
	        // Get SAX Parser
	        SAXParser parser = factory.newSAXParser();
	        
	        // Build XOM document
	        Builder builder = new Builder(parser.getXMLReader(), false);
			Document doc = builder.build(xmlData, null);
			
			// Without XML Schema validation
			//Document doc = new Builder().build(xmlData, null);
			
			metaParameter = new MetaParameter();
			
			Element root = doc.getRootElement();
			Element strength = root.getFirstChildElement("strength");			
			
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
			
			// Constraints
			Elements constraintElements = root.getChildElements("constraint");
			for (int i = 0; i < constraintElements.size(); i++) {
				metaParameter.addConstraint(constraintElements.get(i).getValue());
			}
			
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
		return metaParameter;
	}

}
