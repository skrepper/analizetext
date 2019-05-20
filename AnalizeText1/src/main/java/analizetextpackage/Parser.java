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

	// позиция в файле
	private enum FilePositionState {
		RULE, KNOWN_FACTS, FINISH
	}

	// символьные состояния
	private enum CharacterState {
		BeforeFact, Fact, LetterlessFact, BeforeOperation, OperationAnd, OperationOr, Equal, BeforeResultingFact,
		ResultingFact, LetterlessResultingFact, EndingSpace
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
					rules.add(parseRule(strLine));
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

	private void validateFact(String factToken) {
		if (!Pattern.compile("^_*[a-zA-Z]+\\w*").matcher(factToken).matches()) {
			throw new RuntimeException("Неверное имя факта. '" + factToken + "'");
		} else {
			// System.out.println("yes fact " + factToken);
		}
	}

	private Rule parseRule(String strLine) {
		CharacterState characterState = CharacterState.BeforeFact;
		Expression resultExpression = null;
		FactExpression factExpression = null;
		Collection<FactExpression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		StringBuilder fact = new StringBuilder();
		String resultingFact = "";

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
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreFact нечто " + strLine);
			case Fact:
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
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					fact = fact.append(iat);
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
					if (andExpressions.size() > 0) {
						andExpressions.add(factExpression);
						orExpressions.add(new AndExpression(andExpressions));
						andExpressions = new ArrayList<>();
					} else {
						orExpressions.add(factExpression);
					}
					characterState = CharacterState.BeforeFact;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После первого | нечто " + strLine);
			case Equal:
				if (iat != '>')
					throw new RuntimeException("Неверное имя факта. После дефиса нечто " + strLine);

				if (andExpressions.size() > 0 && orExpressions.size() > 0) {
					andExpressions.add(factExpression);
					orExpressions.add(new AndExpression(andExpressions));
					resultExpression = new OrExpression(orExpressions);  
				}
				else if (orExpressions.size() > 0 && andExpressions.size() == 0) {
					orExpressions.add(factExpression);
					resultExpression = new OrExpression(orExpressions);
				}
				else if (orExpressions.size() == 0 && andExpressions.size() > 0) {
					andExpressions.add(factExpression);
					resultExpression = new AndExpression(andExpressions);
				}
				else if (factExpression != null && andExpressions.size() == 0 && orExpressions.size() == 0) {
					resultExpression = factExpression;
				}
				characterState = CharacterState.BeforeResultingFact;
				break;
			case BeforeResultingFact:
				if (Character.isWhitespace(iat)) {
					break;
				}
				if (iat == '_') {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
					characterState = CharacterState.LetterlessResultingFact;
					break;
				}
				if (Character.isLetter(iat)) {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
					characterState = CharacterState.ResultingFact;
					break;
				}
				throw new RuntimeException("Неверное имя факта. После > нечто " + strLine);
			case LetterlessResultingFact:
				if (Character.isLetter(iat)) {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
					characterState = CharacterState.ResultingFact;
					break;
				}
				if (iat == '_') {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
					break;
				}
				throw new RuntimeException("Неверное имя факта. После UnderscoreResultingFact нечто " + strLine);
			case ResultingFact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
					break;
				}
				if (Character.isWhitespace(iat)) {
					characterState = CharacterState.EndingSpace;
					break;
				}
				throw new RuntimeException("Неверное имя факта. В результирующем факте нечто " + strLine);
			case EndingSpace:
				if (Character.isWhitespace(iat)) {
					break;
				}
				throw new RuntimeException(
						"Неверное имя факта. После пробела после результирующего факта нечто " + strLine);
			default:
				throw new RuntimeException("Ошибка автомата - мы попали в невозможное состояние " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("Неверное имя факта. Не было результирующего факта " + strLine);

		return new Rule(resultExpression, resultingFact);
	}

}
