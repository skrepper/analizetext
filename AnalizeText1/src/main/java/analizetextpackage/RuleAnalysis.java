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
	public ArrayList<WordOrExpression> lineOfExpressions = new ArrayList<>();
	
	public RuleAnalysis(Set<String> knownFacts, Set<String> unknownFacts) {
		this.knownFacts = knownFacts; 
		this.unknownFacts = unknownFacts; 
	}
	
	/*
	 * ������ ������
	 */
	public void makeAnaliz(String [] strArr) {

		lineOfExpressions.add(new Fact(strArr[0].trim(), knownFacts));
		lineOfExpressions.add(new Operation(OperationEnum.EQ.getVal()));
		lineOfExpressions.add(new Fact(strArr[1].trim(), knownFacts));
		
		
		String sl = null;
		if (lineOfExpressions.get(0) instanceof Fact) {
			sl = ((Fact) lineOfExpressions.get(0)).getFact();
		} else {
			// ��� ����� �� ��� ����������, ��� ��� �� �������� ��� ������� ������
			throw new RuntimeException("������ �������");
		}

		String expr = "(" + Operation.GetAllTokensRegExpr() + ")"; // "(&&|\\|\\|)";
		String[] res = splitPreserveDelimiter(sl, expr);
		Pattern token_pattern = Pattern.compile(expr);
		if (token_pattern.matcher(res[0]).matches() || token_pattern.matcher(res[res.length - 1]).matches()) {
			throw new RuntimeException("�� ����� ����� ����� ��������� ����� ���������");
		}
		int indexOfAdd = -1; // ������ res ������ ��������� ������ (0 - index) ������� ops
		boolean prevElementIsSlovo = false; // �������� ����������� ���������� � ���������
		for (String i : res) {
			if (token_pattern.matcher(i).matches()) {
				if (prevElementIsSlovo) {
					lineOfExpressions.add(++indexOfAdd, new Operation(i.trim()));
					prevElementIsSlovo = false;
				} else {
					throw new RuntimeException("� ����� ����� ��������� ����� 2 ��������� ������.");
				}
			} else {
				if (!prevElementIsSlovo) {
					if (indexOfAdd != -1) {
						lineOfExpressions.add(++indexOfAdd, new Fact(i.trim(), knownFacts));
					} else {
						lineOfExpressions.set(0, new Fact(i.trim(), knownFacts));
						++indexOfAdd;
					}
					prevElementIsSlovo = true;
				}
			}
		}

		
		// ���� �� ���� ���������� ��������� � ������� ���������� 
		for (Integer i : OperationEnum.OperationPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // ������� ��������, ��������������, ����� while
				for (int j = 0; j < lineOfExpressions.size() - 2; j++) { // ops.size()-2 - ������ ����� ���� -> � ���������
					if ((lineOfExpressions.get(j) instanceof Operation) && OperationEnum.OperationPriority.get(((Operation) lineOfExpressions.get(j)).getToken()) == i) {
						Expression expression = new Expression();
						fnd = true;
						if (j > 0 && (lineOfExpressions.get(j - 1) instanceof Operand) && j < (lineOfExpressions.size() - 1)
								&& (lineOfExpressions.get(j + 1) instanceof Operand)) {
							expression.setOperand1((Operand) lineOfExpressions.get(j - 1));
							expression.setOperand2((Operand) lineOfExpressions.get(j + 1));
							expression.setToken((Operation) lineOfExpressions.get(j));
							lineOfExpressions.set(j, expression);
							Set<WordOrExpression> remove = new HashSet<WordOrExpression>();
							remove.add(lineOfExpressions.get(j - 1));
							remove.add(lineOfExpressions.get(j + 1));
							lineOfExpressions.removeAll(remove);
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
	public boolean checkAllExpressionsInLineDeduced() {
		boolean result = true; 
		for (WordOrExpression i : lineOfExpressions.stream().collect(Collectors.toList())) {
			if (!i.getIsDefined()) {
				if (i.deduceAndGetIsDefined()) { 
					//���� ����� ������� ���� isDefined ���� false, � ����� true, �� ���� ��������� ����������    
					knownFacts.add(((Fact) lineOfExpressions.get(lineOfExpressions.size() - 1)).getFact());
					unknownFacts.remove(((Fact) lineOfExpressions.get(lineOfExpressions.size() - 1)).getFact());
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
