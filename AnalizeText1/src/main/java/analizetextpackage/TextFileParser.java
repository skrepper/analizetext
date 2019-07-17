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

public class TextFileParser implements FileParser{

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
	
}

