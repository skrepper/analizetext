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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class XmlTest2 {
     
    @Test
    public void test() throws JAXBException, SAXException, IOException, ReflectiveOperationException, RuntimeException {
		Parser ruleParser = new Parser();
		Model testModel = ruleParser.parseFile("target/test-classes/testRule.txt", true);

        JAXBContext jc = JAXBContext.newInstance(
        		Model.class, 
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class
        		);

       
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(testModel, System.out);
        
        Assert.assertEquals(1, 1);
    }
}    
