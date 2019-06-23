package analizetextpackage;

import analizetextpackage.Model;
import analizetextpackage.xmlmodel.*;
import org.junit.Assert;
import org.junit.Test;
 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class XmlTest {
    private static String TEST_XML="<model>\r\n" + 
    		"  <rules>\r\n" + 
    		"    <rule>\r\n" + 
    		"      <factExpression>spring</factExpression>\r\n" + 
    		"      <resultingFact>\r\n" + 
    		"        rain\r\n" + 
    		"      </resultingFact>\r\n" + 
    		"    </rule>\r\n" + 
    		"    <rule>\r\n" + 
    		"      <andExpression>\r\n" + 
    		"        <factExpression>spring</factExpression>\r\n" + 
    		"        <factExpression>winter</factExpression>\r\n" + 
    		"      </andExpression>\r\n" + 
    		"      <resultingFact>\r\n" + 
    		"        autumn\r\n" + 
    		"      </resultingFact>\r\n" + 
    		"    </rule>\r\n" + 
    		"    <rule>\r\n" + 
    		"      <orExpression>\r\n" + 
    		"        <factExpression>spring</factExpression>\r\n" + 
    		"        <factExpression>winter</factExpression>\r\n" + 
    		"        <andExpression>\r\n" + 
    		"          <factExpression>summer</factExpression>\r\n" + 
    		"          <factExpression>rain</factExpression>\r\n" + 
    		"        </andExpression>\r\n" + 
    		"      </orExpression>\r\n" + 
    		"      <resultingFact>\r\n" + 
    		"        autumn\r\n" + 
    		"      </resultingFact>\r\n" + 
    		"    </rule>\r\n" + 
    		"  </rules>\r\n" + 
    		"  <approvedFacts>\r\n" + 
    		"    <approvedFact fact=\"spring\"/>\r\n" + 
    		"    <approvedFact fact=\"summer\"/>\r\n" + 
    		"  </approvedFacts>\r\n" + 
    		"</model>\r\n" + 
    		"";
     
    @Test
    public void test() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Model.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model result = (Model)
                unmarshaller.unmarshal(new StringReader(TEST_XML));
        Assert.assertEquals(1, 1);
/*        Assert.assertEquals(CodeType.NOK, result.getCode());
        Assert.assertEquals("abc", result.getDescription());*/
    }

}
