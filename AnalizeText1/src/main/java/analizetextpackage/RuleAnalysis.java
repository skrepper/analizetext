package analizetextpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleAnalysis {

	private Set<String> knownFacts;
	private Set<String> unknownFacts = new HashSet<String>();
	// ��� ������
	public ArrayList<FactOrOperationOrExpression> line = new ArrayList<>();
	
	public RuleAnalysis(Set<String> knownFacts, Set<String> unknownFacts) {
		this.knownFacts = knownFacts; 
		this.unknownFacts = unknownFacts; 
	}
	
	/*
	 * ������ ������
	 */
	public void makeRuleAnalisys(String [] strArr) {

		line.add(new Fact(strArr[0].trim(), knownFacts));
		line.add(new Operation(OperationEnum.EQ.getVal()));
		line.add(new Fact(strArr[1].trim(), knownFacts));
		
		
		String sl = null;
		if (line.get(0) instanceof Fact) {
			sl = ((Fact) line.get(0)).getFact();
		} else {
			// ��� ����� �� ��� ����������, ��� ��� �� �������� ��� ������� ������
			throw new RuntimeException("������ �������");
		}

		String allOperationsRegularExpression = "(" + OperationEnum.GET_ALL_OPERATIONS_REGULAR_EXPRESSION() + ")"; // "(&&|\\|\\|)";
		String[] res = splitPreserveDelimiter(sl, allOperationsRegularExpression);
		Pattern token_pattern = Pattern.compile(allOperationsRegularExpression);
		if (token_pattern.matcher(res[0]).matches() || token_pattern.matcher(res[res.length - 1]).matches()) {
			throw new RuntimeException("�� ����� ����� ����� ��������� ����� ���������");
		}
		int indexOfAdd = -1; // ������ res ������ ��������� ������ (0 - index) ������� ops
		boolean prevElementIsNotOperator = false; // �������� ����������� ���������� � ���������
		for (String i : res) {
			if (token_pattern.matcher(i).matches()) {
				if (prevElementIsNotOperator) {
					line.add(++indexOfAdd, new Operation(i.trim()));
					prevElementIsNotOperator = false;
				} else {
					throw new RuntimeException("� ����� ����� ��������� ����� 2 ��������� ������.");
				}
			} else {
				if (!prevElementIsNotOperator) {
					if (indexOfAdd != -1) {
						line.add(++indexOfAdd, new Fact(i.trim(), knownFacts));
					} else {
						line.set(0, new Fact(i.trim(), knownFacts));
						++indexOfAdd;
					}
					prevElementIsNotOperator = true;
				}
			}
		}

		
		// ���� �� ���� ���������� ��������� � ������� ���������� 
		for (Integer i : OperationEnum.OPERATION_PRIORITY.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // ������� ��������, ��������������, ����� while
				for (int j = 0; j < line.size() - 2; j++) { // ops.size()-2 - ������ ����� ���� -> � ���������
					if ((line.get(j) instanceof Operation) && OperationEnum.OPERATION_PRIORITY.get(((Operation) line.get(j)).getOperation()) == i) {
						Expression expression = new Expression();
						fnd = true;
						if (j > 0 && (line.get(j - 1) instanceof FactOrExpression) && j < (line.size() - 1)
								&& (line.get(j + 1) instanceof FactOrExpression)) {
							expression.setOperand1((FactOrExpression) line.get(j - 1));
							expression.setOperand2((FactOrExpression) line.get(j + 1));
							expression.setToken((Operation) line.get(j));
							line.set(j, expression);
							Set<FactOrOperationOrExpression> remove = new HashSet<FactOrOperationOrExpression>();
							remove.add(line.get(j - 1));
							remove.add(line.get(j + 1));
							line.removeAll(remove);
						}
						break;
					} else {
						fnd = false;
					}
				}
			}
		}
	}

	/* 
	 * ������� ���������� true, ���� �� ���� ���������� � ������
	 * ���� �� ���� ���� �� ���� ���������� � ������, �� ����������� false  
	 */
	public boolean checkAllLineExpressionsDeduced() {
		boolean result = true; 
		for (FactOrOperationOrExpression i : line.stream().collect(Collectors.toList())) {
			if (!i.getIsDefined()) {
				if (i.deduceAndGetIsDefined()) { 
					//���� ����� ������� ���� isDefined ���� false, � ����� true, �� ���� ��������� ����������    
					knownFacts.add(((Fact) line.get(line.size() - 1)).getFact());
					unknownFacts.remove(((Fact) line.get(line.size() - 1)).getFact());
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
			if (last_match < m.start()) {
				splitted.add(data.substring(last_match, m.start()));
			}
			splitted.add(m.group());
			last_match = m.end();
		}
		if (last_match < data.length()) {
			splitted.add(data.substring(last_match));
		}
		return splitted.toArray(new String[splitted.size()]);
	}

}
