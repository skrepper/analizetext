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
		Begin, Fact, UnderscoreFact, SpaceAfterFact, Comma 
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
					throw new RuntimeException("������ ��������� �������� ����� ������.");
				}
			}
			return new Model(rules, resultingFacts);
		} catch (IOException e) {
			throw new RuntimeException("������ ������ �����. " + e.getMessage(), e);
		}
	}

	private Rule parseRule(String strLine) {
		CharacterState characterState = CharacterState.BeforeFact;
		Expression resultExpression = null;
		FactExpression factExpression = null;
		Collection<Expression> andExpressions = new ArrayList<>();
		Collection<Expression> orExpressions = new ArrayList<>();
		StringBuilder fact = new StringBuilder();
		String resultingFact = "";

		for (char iat : strLine.toCharArray()) {
			switch (characterState) {
			case BeforeFact:
				if (Character.isWhitespace(iat))
					break;

				fact = fact.append(iat);
				if (iat == '_') {
					characterState = CharacterState.LetterlessFact;
					break;
				}
				if (Character.isLetter(iat)) {
					characterState = CharacterState.Fact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
			case LetterlessFact:
				fact = fact.append(iat);
				if (Character.isLetter(iat)) {
					characterState = CharacterState.Fact;
					break;
				}
				if (iat == '_') 
					break;
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreFact ����� " + strLine);
			case Fact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					fact = fact.append(iat);
					break;
				}
				factExpression = new FactExpression(fact.toString());
				fact.setLength(0);
				if (Character.isWhitespace(iat)) {
					characterState = CharacterState.BeforeOperation;
					break;
				}
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
				
/*				if (andExpressions.size() > 0 && orExpressions.size() > 0) {
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
				}*/
				
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
				resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
				if (iat == '_') {
					characterState = CharacterState.LetterlessResultingFact;
					break;
				}
				if (Character.isLetter(iat)) {
					characterState = CharacterState.ResultingFact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� > ����� " + strLine);
			case LetterlessResultingFact:
				resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
				if (Character.isLetter(iat)) {
					characterState = CharacterState.ResultingFact;
					break;
				}
				if (iat == '_') break;
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreResultingFact ����� " + strLine);
			case ResultingFact:
				if (Character.isLetterOrDigit(iat) || iat == '_') {
					resultingFact = new StringBuilder().append(resultingFact).append(iat).toString();
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

		return new Rule(resultExpression, resultingFact);
	}
	
	private void parseKnownFacts(Set<String> resultingFacts, String strLine) {
		CharacterStateKnownFacts characterState = CharacterStateKnownFacts.Begin;
		String fact = "";
		for (int i = 0; i < strLine.length(); i++) {
			Character iat = Character.valueOf(strLine.charAt(i));
			switch (characterState) {
			case Begin:
				if (iat == ' ')
					break;
				fact = fact + iat;
				if (iat == '_') {
					characterState = CharacterStateKnownFacts.UnderscoreFact;
					break;
				}
				if (Character.isLetter(iat)) {
					characterState = CharacterStateKnownFacts.Fact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
			case UnderscoreFact:
				fact = fact + iat;
				if (Character.isLetter(iat)) {
					characterState = CharacterStateKnownFacts.Fact;
					break;
				}
				if (iat == '_') 
					break;
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreFact ����� " + strLine);
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
				if ((iat == '_') || Character.isLetterOrDigit(iat)) {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ����� ����� " + strLine);
			case Comma:
				if (iat == ' ')
					break;
				fact = fact + iat;
				if (iat == '_') {
					characterState = CharacterStateKnownFacts.UnderscoreFact;
					break;
				}
				if (Character.isLetter(iat)) {
					characterState = CharacterStateKnownFacts.Fact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
			case SpaceAfterFact:
				if (iat == ' ')
					break;
				throw new RuntimeException("�������� ��� �����. ����� ������� ����� ����� ����� " + strLine);
			default:
				throw new RuntimeException("������ �������� - �� ������ � ����������� ��������� " + strLine);
			}
		}
        if (!fact.equals("")) {
			resultingFacts.add(fact);
			return;
        }
        throw new RuntimeException("�������� ��� �����. " + strLine);
	}
}
