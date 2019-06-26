package analizetextpackage;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class XmlTest {
     
    @Test
    public void test() throws JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(
        		Model.class/*, 
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class*/
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model result = (Model)
                unmarshaller.unmarshal(new FileReader(new File("src/test/resources/func_xml_1.xml") ));

        JAXBSource source = new JAXBSource(jc, result);
        
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File("src/main/resources/func_xml.xsd")); 

        Validator validator = schema.newValidator();
        validator.setErrorHandler(new MyErrorHandler());
        validator.validate(source);
        
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(result, System.out);
        Assert.assertEquals(1, 1);
    }
    
    
    
    public class MyErrorHandler implements ErrorHandler {
    	 
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
