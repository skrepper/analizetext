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
	// это строка
	public ArrayList<FactOrOperationOrExpression> line = new ArrayList<>();
	
	public RuleAnalysis(Set<String> knownFacts, Set<String> unknownFacts) {
		this.knownFacts = knownFacts; 
		this.unknownFacts = unknownFacts; 
	}
	
	/*
	 * Анализ строки
	 */
	public void makeRuleAnalisys(String [] strArr) {

		line.add(new Fact(strArr[0].trim(), knownFacts));
		line.add(new Operation(OperationEnum.EQ.getVal()));
		line.add(new Fact(strArr[1].trim(), knownFacts));
		
		
		String sl = null;
		if (line.get(0) instanceof Fact) {
			sl = ((Fact) line.get(0)).getFact();
		} else {
			// нет теста на это исключение, так как не придумал как вызвать ошибку
			throw new RuntimeException("Ошибка массива");
		}

		String allOperationsRegularExpression = "(" + OperationEnum.GET_ALL_OPERATIONS_REGULAR_EXPRESSION() + ")"; // "(&&|\\|\\|)";
		String[] res = splitPreserveDelimiter(sl, allOperationsRegularExpression);
		Pattern token_pattern = Pattern.compile(allOperationsRegularExpression);
		if (token_pattern.matcher(res[0]).matches() || token_pattern.matcher(res[res.length - 1]).matches()) {
			throw new RuntimeException("По краям левой части выражения стоят операторы");
		}
		int indexOfAdd = -1; // массив res должен заместить первый (0 - index) элемент ops
		boolean prevElementIsNotOperator = false; // проверка чередований операторов и операндов
		for (String i : res) {
			if (token_pattern.matcher(i).matches()) {
				if (prevElementIsNotOperator) {
					line.add(++indexOfAdd, new Operation(i.trim()));
					prevElementIsNotOperator = false;
				} else {
					throw new RuntimeException("В левой части выражения стоят 2 оператора подряд.");
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

		
		// цикл по всем логическим действиям в порядке приоритета 
		for (Integer i : OperationEnum.OPERATION_PRIORITY.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // масссив менЯетсЯ, соответственно, нужен while
				for (int j = 0; j < line.size() - 2; j++) { // ops.size()-2 - справа стоят знак -> и результат
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
	 * Функция возвращает true, если не было вычислений в строке
	 * Если же было хотя бы одно вычисление в строке, то возращается false  
	 */
	public boolean checkAllLineExpressionsDeduced() {
		boolean result = true; 
		for (FactOrOperationOrExpression i : line.stream().collect(Collectors.toList())) {
			if (!i.getIsDefined()) {
				if (i.deduceAndGetIsDefined()) { 
					//сюда можно попасть если isDefined было false, а стало true, то есть произошло вычисление    
					knownFacts.add(((Fact) line.get(line.size() - 1)).getFact());
					unknownFacts.remove(((Fact) line.get(line.size() - 1)).getFact());
					result = false;
				}
			}
		}
		return result; 
	}

	
	/*
	 * Функция split с разделителями
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
