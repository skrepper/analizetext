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

<<<<<<< HEAD
	// позиция в файле
	private enum FilePositionState {
		RULE, KNOWN_FACTS, FINISH
	}

	// символьные состояния
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
		
=======
	private enum FilePositionState {
		RULE, KNOWN_FACTS, FINISH
	}
	
	private int currentPosition;
	private String currentLine;

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException {
>>>>>>> master
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
<<<<<<< HEAD
					rules.add(parseRule(strLine));
=======
					rules.add(parseRuleBySymbol(strLine, 0));
>>>>>>> master
					break;
				case KNOWN_FACTS:
					parseKnownFactsBySymbol(resultingFacts, strLine);
					parsingState = FilePositionState.FINISH;
					break;
				case FINISH:
				default:
					throw new RuntimeException("Ошибка структуры входного файла данных.");
				}
			}
			return new Model(rules, resultingFacts);
		} catch (IOException e) {
			throw new RuntimeException("Ошибка чтения файла. " + e.getMessage(), e);
		}
	}
	
	private Model getXMLModel(String filePathName) throws JAXBException, SAXException, IOException {

<<<<<<< HEAD
		JAXBContext jc = JAXBContext.newInstance(
        		Model.class,
        		Rule.class, 
        		FactExpression.class,
        		AndExpression.class ,
        		OrExpression.class
        		);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        Model model = (Model)
                unmarshaller.unmarshal(new FileReader(new File(filePathName) ));

/*        JAXBSource source = new JAXBSource(jc, model);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File("src/main/resources/func_xml.xsd")); 
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new MyErrorHandler());
        validator.validate(source);	*/	

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
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
			case LetterlessFact:
				if (Character.isLetter(iat)) {
					fact = fact.append(iat);
					characterState = CharacterState.Fact;
					break;
				}
				if (iat == '_') { 
					fact = fact.append(iat);
=======
	// словесные состояния
	enum OperationState {
		Begin, Fact, CommonExpression, AndExpression, OrExpression, EqualOperation, ResultingFact
	};
	
	// символьные состояния
	enum CharacterState {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, EndParentheses,
		OperatorAnd1, OperatorAnd2, OperatorOr1, OperatorOr2, Equal1,
		Equal2, ResultingFact, UnderscoreResultingFact, SpaceAfterResultingFact
	};

	enum ExpressionState {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, EndParentheses,
		OperatorAnd1, OperatorAnd2, OperatorOr1, OperatorOr2, Equal1,
		Equal2, ResultingFact, UnderscoreResultingFact, SpaceAfterResultingFact
	};

	enum ResulitingFactState {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, EndParentheses,
		OperatorAnd1, OperatorAnd2, OperatorOr1, OperatorOr2, Equal1,
		Equal2, ResultingFact, UnderscoreResultingFact, SpaceAfterResultingFact
	};

	
	private Rule parseRuleBySymbol(String strLine, int currentI) {
		CharacterState characterState = CharacterState.Begin;
		OperationState operationState = OperationState.Begin;
		Expression resultExpression = null;
		FactExpression factExpression = null;
		Collection<Expression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		String fact = "";
		String resultingFact = "";
		for (int i = currentI; i < strLine.length(); i++) {
			currentI = i;
			Character iat = Character.valueOf(strLine.charAt(i));
			switch (characterState) {
			case Begin:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					operationState = OperationState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
			case UnderscoreFact:
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					if (operationState == OperationState.Begin)
						operationState = OperationState.Fact;
					fact = fact + iat;
					break;
				}
				if (iat == '_') {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreFact нечто " + strLine);
			case Fact:
				if (iat == ' ') {
					characterState = CharacterState.SpaceAfterFact;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '|') {
					characterState = CharacterState.OperatorOr1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '-') {
					characterState = CharacterState.Equal1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После факта нечто " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd1;
					break;
				}
				if (iat == '|') {
					characterState = CharacterState.OperatorOr1;
					break;
				}
				if (iat == '-') {
					characterState = CharacterState.Equal1;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После пробела после факта нечто " + strLine);
			case Equal1:
				if (iat == '>') {
					characterState = CharacterState.Equal2;
					if (operationState == OperationState.OrExpression)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndExpression)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					operationState = OperationState.EqualOperation;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После дефиса нечто " + strLine);
			case Equal2:
				if (iat == ' ') {
					break;
				}
				if (iat == '_') {
					characterState = CharacterState.UnderscoreResultingFact;
					resultingFact = resultingFact + iat;
					operationState = OperationState.ResultingFact;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.ResultingFact;
					operationState = OperationState.ResultingFact;
					resultingFact = resultingFact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После > нечто " + strLine);
			case UnderscoreResultingFact:
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.ResultingFact;
					resultingFact = resultingFact + iat;
					break;
				}
				if (iat == '_') {
					resultingFact = resultingFact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreResultingFact нечто " + strLine);
			case ResultingFact:
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					resultingFact = resultingFact + iat;
					break;
				}
				if (iat == ' ') {
					characterState = CharacterState.SpaceAfterResultingFact;
					break;
				}
				throw new RuntimeException("Неверное имя факта. В результирующем факте нечто " + strLine);
			case SpaceAfterResultingFact:
				if (iat == ' ') {
					break;
				}
				throw new RuntimeException(
						"Неверное имя факта. После пробела после результирующего факта нечто " + strLine);
			case OperatorAnd1:
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd2;
					if (operationState == OperationState.AndExpression)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					if (operationState == OperationState.OrExpression) {
						andExpressions.clear();
						((ArrayList<Expression>) andExpressions).add(factExpression);
					}
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					operationState = OperationState.AndExpression;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого & нечто " + strLine);
			case OperatorOr1:
				if (iat == '|') {
					characterState = CharacterState.OperatorOr2;
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.OrExpression)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndExpression) {
						((ArrayList<Expression>) andExpressions).add(factExpression);
						Collection<Expression> copyAndExpression = cloneList(andExpressions);
						((ArrayList<Expression>) orExpressions).add(new AndExpression(copyAndExpression));
					}
					operationState = OperationState.OrExpression;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого | нечто " + strLine);
			case OperatorAnd2:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После && нечто " + strLine);
			case OperatorOr2:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После || нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("Неверное имя факта. Не было результирующего факта " + strLine);

		if (orExpressions.size() > 0) {
			resultExpression = new OrExpression(orExpressions);
			return new Rule(resultExpression, resultingFact);
		}

		if (andExpressions.size() > 0) {
			resultExpression = new AndExpression(andExpressions);
			return new Rule(resultExpression, resultingFact);
		}

		if (factExpression != null) {
			resultExpression = factExpression;
			return new Rule(resultExpression, resultingFact);
		}
		throw new RuntimeException("It is impossible " + strLine);
	}

	public static Collection<Expression> cloneList(Collection<Expression> list) {
		Collection<Expression> clone = new ArrayList<Expression>(list.size());
		for (Expression item : list) {
			try {
				clone.add(item.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return clone;
	}

	
	private Expression parseExpressionBySymbol() {
		CharacterState characterState = CharacterState.Begin;
		OperationState operationState = OperationState.Begin;
		Expression resultExpression = null;
		Expression tempExpression = null;
		FactExpression factExpression = null;
		Collection<Expression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		String fact = "";
		for (int i = currentPosition; i < currentLine.length(); i++) {
			Character iat = Character.valueOf(currentLine.charAt(i));
			switch (characterState) {
			case Begin:
				if (iat == ' ')
					break;
				if (iat == '(') {
					tempExpression = parseExpressionBySymbol();
					characterState = CharacterState.EndParentheses;
					break;
				}
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					operationState = OperationState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
			case UnderscoreFact:
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					if (operationState == OperationState.Begin)
						operationState = OperationState.Fact;
					fact = fact + iat;
					break;
				}
				if (iat == '_') {
					fact = fact + iat;
>>>>>>> master
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreFact нечто " + strLine);
			case Fact:
<<<<<<< HEAD
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
				throw new RuntimeException("Неверное имя факта. После факта нечто " + strLine);
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
				throw new RuntimeException("Неверное имя факта. После пробела после факта нечто " + strLine);
			case OperationAnd:
				if (iat == '&') {
					andExpressions.add(factExpression);
					characterState = CharacterState.BeforeFact;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого & нечто " + strLine);
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
				throw new RuntimeException("Неверное имя факта. После первого | нечто " + strLine);
			case Equal:
				if (iat != '>')
					throw new RuntimeException("Неверное имя факта. После дефиса нечто " + strLine);
				
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
				throw new RuntimeException("Неверное имя факта. После > нечто " + strLine);
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
				throw new RuntimeException("Неверное имя факта. После UnderscoreResultingFact нечто " + strLine);
			case ResultingFact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					resultingFact = resultingFact.append(iat);
					break;
				}
				if (Character.isWhitespace(iat)) {
					characterState = CharacterState.EndingSpace;
					break;
				}
				throw new RuntimeException("Неверное имя факта. В результирующем факте нечто " + strLine);
			case EndingSpace:
				if (Character.isWhitespace(iat)) 
					break;
				throw new RuntimeException(
						"Неверное имя факта. После пробела после результирующего факта нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("Неверное имя факта. Не было результирующего факта " + strLine);

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
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
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
				throw new RuntimeException("Неверное имя факта. После UnderscoreFact нечто " + strLine);
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
				throw new RuntimeException("Неверное имя факта. После факта нечто " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				throw new RuntimeException("Неверное имя факта. После пробела после факта нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
		
        if (characterState == CharacterStateKnownFacts.Fact)
        {
        	resultingFacts.add(fact.toString());
        	return;
        }

        if (characterState == CharacterStateKnownFacts.SpaceAfterFact)
        	return;
        	
        throw new RuntimeException("Неверное имя факта. " + strLine);
        
 	}
=======
				if (iat == ' ') {
					characterState = CharacterState.SpaceAfterFact;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '|') {
					characterState = CharacterState.OperatorOr1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (iat == '-') {
					characterState = CharacterState.Equal1;
					factExpression = new FactExpression(fact);
					fact = "";
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После факта нечто " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd1;
					break;
				}
				if (iat == '|') {
					characterState = CharacterState.OperatorOr1;
					break;
				}
				if (iat == '-') {
					characterState = CharacterState.Equal1;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После пробела после факта нечто " + strLine);
			case Equal1:
				if (iat == '>') {
					characterState = CharacterState.Equal2;
					if (operationState == OperationState.OrExpression)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndExpression)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					operationState = OperationState.EqualOperation;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После дефиса нечто " + strLine);
			case Equal2:
				if (iat == ' ') {
					break;
				}
				if (iat == '_') {
					characterState = CharacterState.UnderscoreResultingFact;
					resultingFact = resultingFact + iat;
					operationState = OperationState.ResultingFact;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.ResultingFact;
					operationState = OperationState.ResultingFact;
					resultingFact = resultingFact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После > нечто " + strLine);
			case UnderscoreResultingFact:
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.ResultingFact;
					resultingFact = resultingFact + iat;
					break;
				}
				if (iat == '_') {
					resultingFact = resultingFact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreResultingFact нечто " + strLine);
			case ResultingFact:
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					resultingFact = resultingFact + iat;
					break;
				}
				if (iat == ' ') {
					characterState = CharacterState.SpaceAfterResultingFact;
					break;
				}
				throw new RuntimeException("Неверное имя факта. В результирующем факте нечто " + strLine);
			case SpaceAfterResultingFact:
				if (iat == ' ') {
					break;
				}
				throw new RuntimeException(
						"Неверное имя факта. После пробела после результирующего факта нечто " + strLine);
			case OperatorAnd1:
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd2;
					if (operationState == OperationState.AndExpression)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					if (operationState == OperationState.OrExpression) {
						andExpressions.clear();
						((ArrayList<Expression>) andExpressions).add(factExpression);
					}
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) andExpressions).add(factExpression);
					operationState = OperationState.AndExpression;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого & нечто " + strLine);
			case OperatorOr1:
				if (iat == '|') {
					characterState = CharacterState.OperatorOr2;
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.OrExpression)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndExpression) {
						((ArrayList<Expression>) andExpressions).add(factExpression);
						Collection<Expression> copyAndExpression = cloneList(andExpressions);
						((ArrayList<Expression>) orExpressions).add(new AndExpression(copyAndExpression));
					}
					operationState = OperationState.OrExpression;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого | нечто " + strLine);
			case OperatorAnd2:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После && нечто " + strLine);
			case OperatorOr2:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterState.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После || нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("Неверное имя факта. Не было результирующего факта " + strLine);

		if (orExpressions.size() > 0) {
			resultExpression = new OrExpression(orExpressions);
			return new ParseExpressionObject(resultExpression, currentI);
		}

		if (andExpressions.size() > 0) {
			resultExpression = new AndExpression(andExpressions);
			return new ParseExpressionObject(resultExpression, currentI);
		}

		if (factExpression != null) {
			resultExpression = factExpression;
			return new ParseExpressionObject(resultExpression, currentI);
		}
		throw new RuntimeException("It is impossible " + strLine);
	}

	enum CharacterStateKnownFacts {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, Comma 
	};
	
	private void parseKnownFactsBySymbol(Set<String> resultingFacts, String strLine) {
		CharacterStateKnownFacts characterState = CharacterStateKnownFacts.Begin;
		String fact = "";
		for (int i = 0; i < strLine.length(); i++) {
			Character iat = Character.valueOf(strLine.charAt(i));
			switch (characterState) {
			case Begin:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterStateKnownFacts.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterStateKnownFacts.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
			case UnderscoreFact:
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterStateKnownFacts.Fact;
					fact = fact + iat;
					break;
				}
				if (iat == '_') {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreFact нечто " + strLine);
			case Fact:
				if (iat == ' ') {
					characterState = CharacterStateKnownFacts.SpaceAfterFact;
					resultingFacts.add(fact);
					fact = "";
					break;
				}
				if (iat == ',') {
					characterState = CharacterStateKnownFacts.Comma;
					resultingFacts.add(fact);
					fact = "";
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После факта нечто " + strLine);
			case Comma:
				if (iat == ' ')
					break;
				if (iat == '_') {
					characterState = CharacterStateKnownFacts.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.toString(iat).matches("[a-zA-Z]")) {
					characterState = CharacterStateKnownFacts.Fact;
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("Неверное имя факта. Впереди нечто " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				throw new RuntimeException("Неверное имя факта. После пробела после факта нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
        if (!fact.equals("")) {
			resultingFacts.add(fact);
			return;
        }
        throw new RuntimeException("Неверное имя факта. " + strLine);
	}
>>>>>>> master
	
    private static String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
        	throw new RuntimeException("Ошибка чтения файла. " + e.getMessage(), e);
        }
 
        return extension;
 
    }

}

<<<<<<< HEAD
=======

>>>>>>> master
