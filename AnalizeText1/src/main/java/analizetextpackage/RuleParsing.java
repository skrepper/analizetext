package analizetextpackage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RuleParsing {

	private Set<String> deducedFacts;
	private Set<String> unknownFacts = new HashSet<String>();
	private ArrayList<RuleAnalysis> allRules;

	public RuleParsing(Set<String> deducedFacts, Set<String> unknownFacts, ArrayList<RuleAnalysis> allRules) {
		this.deducedFacts = deducedFacts;
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
		if (strArr[0].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ����� ����� �����.");
		}
		String allOperationsRegularExpressions = String.join("|", EnumSet.allOf(OperationEnum.class).stream().map(i->i.getRegExp()).collect(Collectors.toList())); 
		if (Pattern.compile(allOperationsRegularExpressions).matcher(strArr[1]).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������.");
		}
		// ��� ����� � ������ �������������� ����
		unknownFacts.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(allOperationsRegularExpressions);
		for (String i : strArrLeft) unknownFacts.add(i.trim());

		// ������ ������� ����
		RuleAnalysis rule = new RuleAnalysis(deducedFacts, unknownFacts);
		rule.makeRuleAnalisys(strArr); // ���������� ������ ���� � �������
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
			(new Fact(i.trim(), deducedFacts)).checkToErrorsFact(i.trim()); 
			deducedFacts.add(i.trim());
		}
	}

}
