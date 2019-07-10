package analizetextpackage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import analizetextpackage.XmlTest.MyErrorHandler;

public class AnalizeTextTest {

	private String[] actualArray;

    private final PrintStream systemOut = System.out, systemErr = System.err;

    private ByteArrayOutputStream testOut, errOut;
	
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        errOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setErr(new PrintStream(errOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
        System.setErr(systemErr);
    }
    
	@Test
	public void testContent1() throws IOException {
		//������� ��������
		Main.main(new String[] { "-f target/test-classes/func_text_1.txt" });
		actualArray = testOut.toString().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(
								equalTo("autumn"), 
						        equalTo("winter"), 
						        equalTo("rain"),
				                equalTo("summer"))));
		assertThat(actualArray.length, equalTo(4)); 
	}

	@Test
	public void testContent2() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_2.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent3() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_3.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent4() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_4.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent5() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_5.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent6() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_6.txt" }); 
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent7() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_7.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent8() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_8.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	

	@Test
	public void testContent9() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_9.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	

	@Test
	public void testContent10() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_10.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	
	@Test
	public void testContent11() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_11.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	
	@Test
	public void testContent12() throws IOException {
		//�������� �� ��������� ���������� ���������
		Main.main(new String[] { "-f target/test-classes/func_text_12.txt" });
		actualArray = testOut.toString().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(
								equalTo("autumn"), 
						        equalTo("winter"), 
						        equalTo("tornado"), 
						        equalTo("water"), 
						        equalTo("rain"),
				                equalTo("summer"))));
		assertThat(actualArray.length, equalTo(6)); 
	}
	
	@Test
	public void testContent13() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_13.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	
	@Test
	public void testContent14() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_14.txt" });
		assertThat(errOut.toString(), equalTo("������ ��������� �������� ����� ������."));
	}
	
	@Test
	public void testContent15() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_15.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent16() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_16.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent17() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_17.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent18() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_18.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}

	@Test
	public void testContent19() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_19.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	@Test
	public void testContent20() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_20.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}
	@Test 
	public void testContent21() throws IOException {
		Main.main(new String[] { "-f target/test-classes/func_text_21.txt" });
		actualArray = testOut.toString().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(  
								equalTo("ocean"),   
								equalTo("_ss1winter1"),   
						        equalTo("__s1winter1"), 
						        equalTo("_f2s"), 
						        equalTo("winter1"),
						        equalTo("summer"),
						        equalTo("dirt"),
						        equalTo("mmer"),
				                equalTo("autumn"))));
		assertThat(actualArray.length, equalTo(9)); 
	}

	@Test
	public void testArgs1() throws IOException {
		Main.main(new String[] {  });
		assertThat(errOut.toString(), startsWith("�������� �������� �����."));
	}
	
	@Test
	public void testArgs2() throws IOException {
		Main.main(new String[] { "wrong name" });
		assertThat(errOut.toString(), startsWith("�������� �������� �����."));
	}

	@Test 
	public void testRule() throws IOException, ReflectiveOperationException, IllegalArgumentException, InvocationTargetException, JAXBException, SAXException {
		Parser ruleParser = new Parser();
		Model testModel = ruleParser.parseFile("target/test-classes/testRule.txt", false);

		Class modelClass = testModel.getClass(); 
		Field fieldRules = modelClass.getDeclaredField("rules");
		fieldRules.setAccessible(true);
		Collection<Rule> testRules = (Collection<Rule>) fieldRules.get(testModel);
		Rule rule = testRules.iterator().next();

		Class ruleClass = rule.getClass(); 
		Field fieldExpression = ruleClass.getDeclaredField("expression");
		fieldExpression.setAccessible(true);
		OrExpression resultExpression = (OrExpression) fieldExpression.get(rule);
		
		Class expressionClass = resultExpression.getClass(); 
		Field fieldOperands = expressionClass.getDeclaredField("operands");
		fieldOperands.setAccessible(true);
		ArrayList<Expression> operands = (ArrayList<Expression>) fieldOperands.get(resultExpression);
		
		assertThat(operands.size(), equalTo(2)); //������ �������

		AndExpression andExpression = (AndExpression) operands.get(1); //������!
		Class andExpressionClass = andExpression.getClass(); 
		Field fieldAndOperands = andExpressionClass.getDeclaredField("operands");
		fieldAndOperands.setAccessible(true);
		ArrayList<Expression> andOperands = (ArrayList<Expression>) fieldAndOperands.get(andExpression);
	
		assertThat(andOperands.size(), equalTo(4)); //������ ������� (������ �������)
		
	}
	
	@Test
	public void testContent22() throws IOException {
		//������� ��������
		Main.main(new String[] { "-f target/test-classes/func_text_22.txt" });
		assertThat(errOut.toString(), startsWith("�������� ��� �����."));
	}


	@Test 
	public void testRuleXML() throws IOException, ReflectiveOperationException, IllegalArgumentException, InvocationTargetException, JAXBException, SAXException {
		Parser ruleParser = new Parser();
        JAXBContext jc = JAXBContext.newInstance(
        		Model.class, 
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model testModel = (Model)
                unmarshaller.unmarshal(new FileReader(new File("target/test-classes/func_xml_1.xml") ));


        JAXBSource source = new JAXBSource(jc, testModel);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File("target/test-classes/func_xml.xsd")); 

        Validator validator = schema.newValidator();
        validator.setErrorHandler(new MyErrorHandler());
        validator.validate(source);

		Class modelClass = testModel.getClass(); 
		Field fieldRules = modelClass.getDeclaredField("rules");
		fieldRules.setAccessible(true);
		Collection<Rule> testRules = (Collection<Rule>) fieldRules.get(testModel);
		Iterator<Rule> testRulesIterator = testRules.iterator();
		testRulesIterator.next();
		testRulesIterator.next();
		Rule rule = testRulesIterator.next();

		Class ruleClass = rule.getClass(); 
		Field fieldExpression = ruleClass.getDeclaredField("expression");
		fieldExpression.setAccessible(true);
		OrExpression resultExpression = (OrExpression) fieldExpression.get(rule);
		
		Class expressionClass = resultExpression.getClass(); 
		Field fieldOperands = expressionClass.getDeclaredField("operands");
		fieldOperands.setAccessible(true);
		ArrayList<Expression> operands = (ArrayList<Expression>) fieldOperands.get(resultExpression);
		
		assertThat(operands.size(), equalTo(3)); //������ �������

		AndExpression andExpression = (AndExpression) operands.get(2); 
		Class andExpressionClass = andExpression.getClass(); 
		Field fieldAndOperands = andExpressionClass.getDeclaredField("operands");
		fieldAndOperands.setAccessible(true);
		ArrayList<Expression> andOperands = (ArrayList<Expression>) fieldAndOperands.get(andExpression);
	
		assertThat(andOperands.size(), equalTo(2)); //������ ������� (������ �������)
		
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


