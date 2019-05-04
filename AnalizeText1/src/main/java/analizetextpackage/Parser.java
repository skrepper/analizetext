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

	// ���������� ���������
	enum CharacterState {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, OperatorAnd1, OperatorAnd2, OperatorOr1, OperatorOr2, Equal1,
		Equal2, ResultingFact, UnderscoreResultingFact, SpaceAfterResultingFact
	};

	enum CharacterStateKnownFacts {
		Begin, Fact, UnderscoreFact, SpaceAfterFact, Comma 
	};

	// ��������� ���������
	enum OperationState {
		Begin, Fact, AndOperation, OrOperation, EqualOperation, ResultingFact
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
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
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
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					fact = fact + iat;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ����� ����� " + strLine);
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
					parseKnownFactsBySymbol(resultingFacts, strLine);
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

	private void validateFact(String factToken) {
		if (!Pattern.compile("^_*[a-zA-Z]+\\w*").matcher(factToken).matches()) {
			throw new RuntimeException("�������� ��� �����. '" + factToken + "'");
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
		for (int i = 0; i < strLine.length(); i++) {
			Character iat = Character.valueOf(strLine.charAt(i));
			// System.out.println("Operation state = " + operationState);
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
				throw new RuntimeException("�������� ��� �����. ������� ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreFact ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� ����� ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� ������� ����� ����� ����� " + strLine);
			case Equal1:
				if (iat == '>') {
					characterState = CharacterState.Equal2;
					if (operationState == OperationState.OrOperation)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndOperation)
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					operationState = OperationState.EqualOperation;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������ ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� > ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� UnderscoreResultingFact ����� " + strLine);
			case ResultingFact:
				if (Character.toString(iat).matches("[a-zA-Z0-9_]")) {
					resultingFact = resultingFact + iat;
					break;
				}
				if (iat == ' ') {
					characterState = CharacterState.SpaceAfterResultingFact;
					break;
				}
				throw new RuntimeException("�������� ��� �����. � �������������� ����� ����� " + strLine);
			case SpaceAfterResultingFact:
				if (iat == ' ') {
					break;
				}
				throw new RuntimeException(
						"�������� ��� �����. ����� ������� ����� ��������������� ����� ����� " + strLine);
			case OperatorAnd1:
				if (iat == '&') {
					characterState = CharacterState.OperatorAnd2;
					if (operationState == OperationState.AndOperation)
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					if (operationState == OperationState.OrOperation) {
						andExpressions.clear();
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					}
					if (operationState == OperationState.Fact)
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
					operationState = OperationState.AndOperation;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������� & ����� " + strLine);
			case OperatorOr1:
				if (iat == '|') {
					characterState = CharacterState.OperatorOr2;
					if (operationState == OperationState.Fact)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.OrOperation)
						((ArrayList<Expression>) orExpressions).add(factExpression);
					if (operationState == OperationState.AndOperation) {
						((ArrayList<FactExpression>) andExpressions).add(factExpression);
						Collection<FactExpression> copyAndExpression = cloneList(andExpressions);
						((ArrayList<Expression>) orExpressions).add(new AndExpression(copyAndExpression));
					}
					operationState = OperationState.OrOperation;
					break;
				}
				throw new RuntimeException("�������� ��� �����. ����� ������� | ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� && ����� " + strLine);
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
				throw new RuntimeException("�������� ��� �����. ����� || ����� " + strLine);
			default:
				throw new RuntimeException("������ �������� - �� ������ � ����������� ��������� " + strLine);
			}
		}
		if (resultingFact.length() == 0)
			throw new RuntimeException("�������� ��� �����. �� ���� ��������������� ����� " + strLine);

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
