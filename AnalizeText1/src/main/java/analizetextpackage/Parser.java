package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Parser {

	// символьные состояния
	enum CharacterState {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, OperatorAnd1, OperatorAnd2, OperatorOr1, OperatorOr2, Equal1,
		Equal2, ResultingFact, UnderscoreResultingFact, SpaceAfterResultingFact
	};

	// словесные состояния
	enum OperationState {
		Begin, Fact, AndOperation, OrOperation, EqualOperation, ResultingFact
	};

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException {
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
					rules.add(parseRuleBySymbol(strLine));
					break;
				case KNOWN_FACTS:
					parseKnownFacts(resultingFacts, strLine);
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

	private void parseKnownFacts(Set<String> resultingFacts, String strLine) {
		String[] knownFacts;
		knownFacts = strLine.split(",", -1);
		for (String i : knownFacts) {
			String s = i.trim();
			validateFact(s);
			resultingFacts.add(s);
		}
	}

	private Rule parseRule(String strLine) {
		String[] ruleParts;
		ruleParts = strLine.split("->", -1);
		if (ruleParts.length != 2) {
			throw new RuntimeException("Ошибка синтаксиса правила " + strLine);
		}
		String resultingFact = ruleParts[1].trim();
		validateFact(resultingFact);
		return new Rule(parseExpression(ruleParts[0].trim()), resultingFact); // парсинг и построение выражений
	}

	private Expression parseExpression(String expressionString) {
		ArrayList<Expression> arrExpr = new ArrayList<>();
		String arrStr[] = expressionString.split("\\|\\|", -1);
		if (arrStr.length == 1)
			return parseFactExpr(arrStr[0]);
		for (int i = 0; i < arrStr.length; i++)
			arrExpr.add(parseAndExpr(arrStr[i]));
		return new OrExpression(arrExpr);
	}

	private Expression parseAndExpr(String expressionString) {
		ArrayList<FactExpression> arrExpr = new ArrayList<>();
		String arrStr[] = expressionString.split("&&", -1);
		if (arrStr.length == 1)
			return parseFactExpr(arrStr[0]);
		for (int i = 0; i < arrStr.length; i++)
			arrExpr.add(parseFactExpr(arrStr[i]));
		return new AndExpression(arrExpr);
	}

	private FactExpression parseFactExpr(String str) {
		str = str.trim();
		validateFact(str);
		return new FactExpression(str);
	}

	private void validateFact(String factToken) {
		if (!Pattern.compile("^_*[a-zA-Z]+\\w*").matcher(factToken).matches()) {
			throw new RuntimeException("Неверное имя факта. '" + factToken + "'");
		} else {
			// System.out.println("yes fact " + factToken);
		}
	}

	private enum FilePositionState {
		RULE, KNOWN_FACTS, FINISH
	}

	private Rule parseRuleBySymbol(String strLine) {
		CharacterState characterState = CharacterState.Begin;
		OperationState operationState = OperationState.Begin;
		Expression resultExpression = null;
		FactExpression factExpression = null;
		Collection<FactExpression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		String fact = "";
		String resultingFact = "";

		for (char iat : strLine.toCharArray()) {
//		for (int i = 0; i < strLine.length(); i++) {
//			char iat = strLine.charAt(i);
			// System.out.println("Operation state = " + operationState);
			switch (characterState) {
			case Begin:
				if (Character.isWhitespace(iat))
					break;

				if (iat == '_') {
					characterState = CharacterState.UnderscoreFact;
					fact = fact + iat;
					break;
				}
				if (Character.isLetter(iat)) {
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
				if (iat != '>')
					throw new RuntimeException("Неверное имя факта. После дефиса нечто " + strLine);

				if (operationState == OperationState.OrOperation)
					orExpressions.add(factExpression);
				else if (operationState == OperationState.AndOperation)
					andExpressions.add(factExpression);
				operationState = OperationState.EqualOperation;
				characterState = CharacterState.Equal2;
				break;
				
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
					if (operationState == OperationState.AndOperation)
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					if (operationState == OperationState.OrOperation) {
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					}
					if (operationState == OperationState.Fact)
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					operationState = OperationState.AndOperation;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого & нечто " + strLine);
			case OperatorOr1:
				if (iat == '|') {
					characterState = CharacterState.OperatorOr2;
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.OrOperation)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndOperation) {
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
						((ArrayList<Expression>) orExpressions).add(new AndExpression(andExpressions));
						andExpressions = new ArrayList<>();
					}
					operationState = OperationState.OrOperation;
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

	public static Collection<FactExpression> cloneList(Collection<FactExpression> list) {
		Collection<FactExpression> clone = new ArrayList<FactExpression>(list.size());
		for (FactExpression item : list) {
			try {
				clone.add(item.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return clone;
	}

}
