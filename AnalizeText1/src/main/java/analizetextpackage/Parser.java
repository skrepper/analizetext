package analizetextpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Parser {

	// ������� � �����
	private enum FilePositionState {
		RULE, KNOWN_FACTS, FINISH
	}

	// ���������� ���������
	private enum CharacterState {
		BeforeFact, Fact, LetterlessFact, BeforeOperation, OperationAnd, OperationOr, Equal, BeforeResultingFact,
		ResultingFact, LetterlessResultingFact, EndingSpace
	};

	private enum CharacterStateKnownFacts {
		BeforeFact, Fact, UnderscoreFact, SpaceAfterFact 
	};
	

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException, JAXBException, SAXException {
		String extension = getFileExtension(new File(filePathName));
		
		if (extension.equals(".xml"))	
			return getXMLModel(filePathName);
		
		Collection<Rule> rules = new ArrayList<>();
		Set<String> resultingFacts = new HashSet<String>();
		final String FILE_END_DELIMITER = "----------------------------------------------------------------"; // 64
		FilePositionState parsingState = FilePositionState.RULE;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				switch (parsingState) {
				case RULE:
					if (strLine.equals(FILE_END_DELIMITER)) {
						parsingState = FilePositionState.KNOWN_FACTS;
						break;
					}
					rules.add(parseRule(strLine));
					break;
				case KNOWN_FACTS:
					parseKnownFacts(resultingFacts, strLine);
					parsingState = FilePositionState.FINISH;
					break;
				case FINISH:
				default:
					throw new RuntimeException("������ ��������� �������� ����� ������.");
				}
			}
			return new Model(rules, resultingFacts);
		} catch (IOException e) {
			throw new RuntimeException("������ ������ �����. " + e.getMessage(), e);
		}
	}
	
	private Model getXMLModel(String filePathName) throws JAXBException, SAXException, IOException {

		JAXBContext jc = JAXBContext.newInstance(
        		Model.class,
        		/*Expression.class,*/
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model model = (Model)
                unmarshaller.unmarshal(new FileReader(new File(filePathName) ));

        JAXBSource source = new JAXBSource(jc, model);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File("src/main/resources/func_xml.xsd")); 
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new MyErrorHandler());
        validator.validate(source);		

/*        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(model, System.out);*/
        
        return model;
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
	
	
	private Rule parseRule(String strLine) {
		CharacterState characterState = CharacterState.BeforeFact;
		Expression resultExpression = null;
		FactExpression factExpression = null;
		Collection<Expression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		StringBuilder fact = new StringBuilder();
		StringBuilder resultingFact = new StringBuilder();

		for (char iat : strLine.toCharArray()) {
			switch (characterState) {
			case BeforeFact:
				if (Character.isWhitespace(iat))
					break;

				if (iat == '_') {
					fact = fact.append(iat);
					characterState = CharacterState.LetterlessFact;
					break;
				}
				if (Character.isLetter(iat)) {
					fact = fact.append(iat);
					characterState = CharacterState.Fact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
			case LetterlessFact:
				if (Character.isLetter(iat)) {
					fact = fact.append(iat);
					characterState = CharacterState.Fact;
					break;
				}
				if (iat == '_') { 
					fact = fact.append(iat);
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreFact ����� " + strLine);
			case Fact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					fact = fact.append(iat);
					break;
				}
				if (Character.isWhitespace(iat)) {
					factExpression = new FactExpression(fact.toString());
					fact.setLength(0);
					characterState = CharacterState.BeforeOperation;
					break;
				}
				if (iat == '&') {
					factExpression = new FactExpression(fact.toString());
					fact.setLength(0);
					characterState = CharacterState.OperationAnd;
					break;
				}
				if (iat == '|') {
					factExpression = new FactExpression(fact.toString());
					fact.setLength(0);
					characterState = CharacterState.OperationOr;
					break;
				}
				if (iat == '-') {
					factExpression = new FactExpression(fact.toString());
					fact.setLength(0);
					characterState = CharacterState.Equal;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ����� ����� " + strLine);
			case BeforeOperation:
				if (Character.isWhitespace(iat))
					break;
				if (iat == '&') {
					characterState = CharacterState.OperationAnd;
					break;
				}
				if (iat == '|') {
					characterState = CharacterState.OperationOr;
					break;
				}
				if (iat == '-') {
					characterState = CharacterState.Equal;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������� ����� ����� ����� " + strLine);
			case OperationAnd:
				if (iat == '&') {
					andExpressions.add(factExpression);
					characterState = CharacterState.BeforeFact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������� & ����� " + strLine);
			case OperationOr:
				if (iat == '|') {
					Expression expr = factExpression;
					if (andExpressions.size() > 0) {
						andExpressions.add(factExpression);
						expr = new AndExpression(andExpressions);
						andExpressions = new ArrayList<>();
					}

					orExpressions.add(expr);
					characterState = CharacterState.BeforeFact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������� | ����� " + strLine);
			case Equal:
				if (iat != '>')
					throw new RuntimeException("�������� ��� �����. ����� ������ ����� " + strLine);
				
				resultExpression = factExpression;
				if (andExpressions.size() > 0) {
					andExpressions.add(resultExpression);
					resultExpression = new AndExpression(andExpressions);
				}
				if (orExpressions.size() > 0) {
					orExpressions.add(resultExpression);
					resultExpression = new OrExpression(orExpressions);  
				}

				characterState = CharacterState.BeforeResultingFact;
				break;
			case BeforeResultingFact:
				if (Character.isWhitespace(iat)) 
					break;
				if (iat == '_') {
					resultingFact = resultingFact.append(iat);
					characterState = CharacterState.LetterlessResultingFact;
					break;
				}
				if (Character.isLetter(iat)) {
					resultingFact = resultingFact.append(iat);
					characterState = CharacterState.ResultingFact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� > ����� " + strLine);
			case LetterlessResultingFact:
				if (Character.isLetter(iat)) {
					resultingFact = resultingFact.append(iat);
					characterState = CharacterState.ResultingFact;
					break;
				}
				if (iat == '_') {
					resultingFact = resultingFact.append(iat);
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreResultingFact ����� " + strLine);
			case ResultingFact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					resultingFact = resultingFact.append(iat);
					break;
				}
				if (Character.isWhitespace(iat)) {
					characterState = CharacterState.EndingSpace;
					break;
				}
				throw new RuntimeException("�������� ��� �����. � �������������� ����� ����� " + strLine);
			case EndingSpace:
				if (Character.isWhitespace(iat)) 
					break;
				throw new RuntimeException(
						"�������� ��� �����. ����� ������� ����� ��������������� ����� ����� " + strLine);
			default:
				throw new RuntimeException("������ �������� - �� ������ � ����������� ��������� " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("�������� ��� �����. �� ���� ��������������� ����� " + strLine);

		return new Rule(resultExpression, resultingFact.toString());
	}
	
	private void parseKnownFacts(Set<String> resultingFacts, String strLine) {
		CharacterStateKnownFacts characterState = CharacterStateKnownFacts.BeforeFact;
		StringBuilder fact = new StringBuilder();
		for (int i = 0; i < strLine.length(); i++) {
			char iat = strLine.charAt(i);
			switch (characterState) {
			case BeforeFact:
				if (iat == ' ')
					break;
				if (iat == '_') {
					fact = fact.append(iat);
					characterState = CharacterStateKnownFacts.UnderscoreFact;
					break;
				}
				if (Character.isLetter(iat)) {
					fact = fact.append(iat);
					characterState = CharacterStateKnownFacts.Fact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
			case UnderscoreFact:
				if (Character.isLetter(iat)) {
					fact = fact.append(iat);
					characterState = CharacterStateKnownFacts.Fact;
					break;
				}
				if (iat == '_') { 
					fact = fact.append(iat);
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreFact ����� " + strLine);
			case Fact:
				if (iat == ' ') {
					characterState = CharacterStateKnownFacts.SpaceAfterFact;
					resultingFacts.add(fact.toString());
					fact.setLength(0);
					break;
				}
				if (iat == ',') {
					characterState = CharacterStateKnownFacts.BeforeFact;
					resultingFacts.add(fact.toString());
					fact.setLength(0);
					break;
				}
				if ((iat == '_') || Character.isLetterOrDigit(iat)) {
					fact = fact.append(iat);
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ����� ����� " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				throw new RuntimeException("�������� ��� �����. ����� ������� ����� ����� ����� " + strLine);
			default:
				throw new RuntimeException("������ �������� - �� ������ � ����������� ��������� " + strLine);
			}
		}
		
        if (characterState == CharacterStateKnownFacts.Fact)
        {
        	resultingFacts.add(fact.toString());
        	return;
        }

        if (characterState == CharacterStateKnownFacts.SpaceAfterFact)
        	return;
        	
        throw new RuntimeException("�������� ��� �����. " + strLine);
        
 	}
	
    private static String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
        	throw new RuntimeException("������ ������ �����. " + e.getMessage(), e);
        }
 
        return extension;
 
    }

}

