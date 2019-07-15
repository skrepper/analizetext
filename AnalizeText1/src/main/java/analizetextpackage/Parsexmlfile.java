package analizetextpackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.util.JAXBSource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Parsexmlfile {

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException, JAXBException, SAXException, XMLStreamException {

		XMLInputFactory xif = XMLInputFactory.newInstance();
		FileInputStream fis = new FileInputStream(filePathName);
		XMLStreamReader xsr = xif.createXMLStreamReader(fis);
		xsr.nextTag();
		String schemaLocation = xsr.getAttributeValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "schemaLocation");
		schemaLocation = schemaLocation.substring(schemaLocation.lastIndexOf(" ")).trim();
		
		JAXBContext jc = JAXBContext.newInstance(
        		Model.class,  
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class 
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        unmarshaller.setEventHandler(new MyValidationEventHandler());
 
        Model model = (Model)  unmarshaller.unmarshal(new FileReader(new File(filePathName)));

        JAXBSource source = new JAXBSource(jc, model);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(Thread.currentThread().getContextClassLoader().getResource("func_xml.xsd"));
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new MyErrorHandler());
        validator.validate(source);		

        return model;
	}

    private class MyValidationEventHandler implements ValidationEventHandler {

		@Override
		public boolean handleEvent(ValidationEvent event) {
            System.out.println("\nWARNING in UNMarshall");
			return false;
		}
    }

    private class MyErrorHandler implements ErrorHandler {
   	 
		@Override
        public void warning(SAXParseException exception) throws SAXException {
            System.out.println("\nWARNING");
            exception.printStackTrace();
        }
     
		@Override
        public void error(SAXParseException exception) throws SAXException {
            System.out.println("\nERROR");
            exception.printStackTrace();
        }
     
		@Override
        public void fatalError(SAXParseException exception) throws SAXException {
            System.out.println("\nFATAL ERROR");
            exception.printStackTrace();
        }
    }
}


/*        Marshaller marshaller = jc.createMarshaller();
marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
marshaller.marshal(model, System.out);*/

