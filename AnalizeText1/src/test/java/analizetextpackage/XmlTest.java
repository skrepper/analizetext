package analizetextpackage;

import org.junit.Assert;
import org.junit.Test;
 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

import java.io.StringReader;

public class XmlTest {
    private static String TEST_XML="<model>" + 
    		"  <rules>" + 
    		"    <rule>" + 
    		"      <factExpression>spring</factExpression>" + 
    		"      <resultingFact>" + 
    		"        rain" + 
    		"      </resultingFact>" + 
    		"    </rule>" + 
    		"    <rule>" + 
    		"      <andExpression>" + 
    		"        <factExpression>spring</factExpression>" + 
    		"        <factExpression>winter</factExpression>" + 
    		"      </andExpression>" + 
    		"      <resultingFact>" + 
    		"        autumn" + 
    		"      </resultingFact>" + 
    		"    </rule>" + 
    		"    <rule>" + 
    		"      <orExpression>" + 
    		"        <factExpression>spring</factExpression>" + 
    		"        <factExpression>winter</factExpression>" + 
    		"        <andExpression>" + 
    		"          <factExpression>summer</factExpression>" + 
    		"          <factExpression>rain</factExpression>" + 
    		"        </andExpression>" + 
    		"      </orExpression>" + 
    		"      <resultingFact>" + 
    		"        autumn" + 
    		"      </resultingFact>" + 
    		"    </rule>" + 
    		"  </rules>" + 
    		"  <approvedFacts>" + 
    		"    <approvedFact fact=\"spring\"/>" + 
    		"    <approvedFact fact=\"summer\"/>" + 
    		"  </approvedFacts>" + 
    		"</model>" + 
    		"";
     
    @Test
    public void test() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(
        		Model.class, 
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class,
        		OrExpression.class
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model result = (Model)
                unmarshaller.unmarshal(new StringReader(TEST_XML));
        
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(result, System.out);
        Assert.assertEquals(1, 1);
    }

}
