package analizetextpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParsing {

	private Set<String> knownFacts;
	private Set<String> unknownFacts = new HashSet<String>();
	private ArrayList<RuleAnalysis> allRules;

	public RuleParsing(Set<String> knownFacts, Set<String> unknownFacts,
			ArrayList<RuleAnalysis> allRules) {
		this.knownFacts = knownFacts;
		this.unknownFacts = unknownFacts;
		this.allRules = allRules;
	}

	public void parseRule(String strLine) {
		// -> - ������ EQ
		String[] strArr = strLine.split(OperationEnum.EQ.getVal());
		if (strArr.length < 2) {
			throw new RuntimeException("������ ��������� ����� - �������� ���������� �������.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("������ ��������� ����� - ������� ����� ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� �����.");
		}
		if (Pattern.compile(Operation.GetAllTokensRegExpr()).matcher(strArr[1]).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������.");
		}
		// ��� ����� � ������ �������������� ����
		unknownFacts.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(Operation.GetAllTokensRegExpr());
		for (String i : strArrLeft) unknownFacts.add(i.trim());

		// ������ ������� ����
		RuleAnalysis rule = new RuleAnalysis(knownFacts, unknownFacts);
		rule.makeAnaliz(strArr); // ���������� ������ ���� � �������
		allRules.add(rule);

	}

	public void parseKnownFacts(String strLine) {
		// ��������� ������ �����
		String[] strArr = strLine.split(",", -1);
		if (strLine.length() < 1) {
			throw new RuntimeException("������ ��������� ����� - �������� ������ � ����� �����.");
		}
		for (String i : strArr) {
			if (i.trim().length() == 0)
				throw new RuntimeException("� ��������� ������ ����� ���� ������ ����������.");
			Fact.checkToErrorsFact(i.trim());
			knownFacts.add(i.trim());
		}
	}

}
