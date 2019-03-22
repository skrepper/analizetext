package analizetextpackage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleAnalysis {

	private Set<String> deducedFacts;
	private Set<String> unknownFacts;
	// ��� �������
	public ArrayList<FactOrOperationOrExpression> rule = new ArrayList<>();

	public RuleAnalysis(Set<String> deducedFacts, Set<String> unknownFacts) {
		this.deducedFacts = deducedFacts;
		this.unknownFacts = unknownFacts;
	}

	/*
	 * ������ ������
	 */
	public void makeRuleAnalisys(String[] strArr) {

		rule.add(new Fact(strArr[0].trim(), deducedFacts));
		rule.add(new Operation(OperationEnum.EQ.getVal()));
		rule.add(new Fact(strArr[1].trim(), deducedFacts));

		String allOperationsRegularExpression = "(" + String.join("|",
				EnumSet.allOf(OperationEnum.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())) + ")";
		String[] leftFacts = splitPreserveDelimiter(strArr[0].trim(), allOperationsRegularExpression);
		int indexOfAdd = -1; // ����� ������� ������� ���� ��������� ����� ����� ��� ��������
		AnalisysRuleStateEnum analisysRuleState = AnalisysRuleStateEnum.BEFORE_ANALISYS;
		for (String i : leftFacts) {
			analisysRuleState = analisysRuleState
					.nextState(Pattern.compile(allOperationsRegularExpression).matcher(i).matches());
			switch (analisysRuleState) {
			case FIRST_FACT:
				rule.set(++indexOfAdd, new Fact(i.trim(), deducedFacts));
				break;
			case FACT:
				rule.add(++indexOfAdd, new Fact(i.trim(), deducedFacts));
				break;
			case OPERATION:
				rule.add(++indexOfAdd, new Operation(i.trim()));
				break;
			case BEFORE_ANALISYS:
				break;
			default:
				break;
			}
		}
		if (analisysRuleState == AnalisysRuleStateEnum.OPERATION)
			throw new RuntimeException("� ����� ����� ������ ��������.");

		// ���� �� ���� ���������� ��������� � ������� ����������
		Map<OperationEnum, Integer> OperationEnumPriority = EnumSet.allOf(OperationEnum.class).stream()
				.collect(Collectors.toMap(enumVal -> enumVal, enumVal -> enumVal.getPriority()));
		for (Integer i : OperationEnumPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { // ���������� ������ ���� �� ������ ������ �� �������� ���������
				for (int j = 0; j < rule.size() - 2; j++) { // ops.size()-2 - ������ ����� ���� -> � ���������
					if ((rule.get(j) instanceof Operation)
							&& OperationEnumPriority.get(((Operation) rule.get(j)).getOperation()) == i) {
						Expression expression = new Expression();
						foundOperation = true;
						if (j > 0 && (rule.get(j - 1) instanceof FactOrExpression) && j < (rule.size() - 1)
								&& (rule.get(j + 1) instanceof FactOrExpression)) {
							expression.setOperand1((FactOrExpression) rule.get(j - 1));
							expression.setOperand2((FactOrExpression) rule.get(j + 1));
							expression.setOperation((Operation) rule.get(j));
							rule.set(j, expression);
							Set<FactOrOperationOrExpression> remove = new HashSet<FactOrOperationOrExpression>();
							remove.add(rule.get(j - 1));
							remove.add(rule.get(j + 1));
							rule.removeAll(remove);
						}
						break;
					} else {
						foundOperation = false;
					}
				}
			}
		}
	}

	/*
	 * ������� ���������� true, ���� �� ���� ���������� � ������ ���� �� ���� ����
	 * �� ���� ���������� � ������, �� ����������� false
	 */
	public boolean checkAllLineExpressionsDeduced() {
		boolean result = true;
		for (FactOrOperationOrExpression i : rule.stream().collect(Collectors.toList())) {
			if (!i.getIsDefined()) {
				if (i.deduceAndGetIsDefined()) {
					// ���� ����� ������� ���� isDefined ���� false, � ����� true, �� ���� ��������� ����������
					deducedFacts.add(((Fact) rule.get(rule.size() - 1)).getFact());
					unknownFacts.remove(((Fact) rule.get(rule.size() - 1)).getFact());
					result = false;
				}
			}
		}
		return result;
	}

	/*
	 * ������� split � �������������
	 */
	public String[] splitPreserveDelimiter(String data, String regexp) {
		LinkedList<String> splitted = new LinkedList<String>();
		int last_match = 0;
		Matcher m = Pattern.compile(regexp).matcher(data);
		while (m.find()) {
			if (last_match < m.start())
				splitted.add(data.substring(last_match, m.start()));
			splitted.add(m.group());
			last_match = m.end();
		}
		if (last_match < data.length())
			splitted.add(data.substring(last_match));
		return splitted.toArray(new String[splitted.size()]);
	}

}