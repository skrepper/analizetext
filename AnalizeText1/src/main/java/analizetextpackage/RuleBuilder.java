package analizetextpackage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleBuilder {
	
	public void build(ArrayList<Lexema> rule) {
		
		// цикл по всем логическим действиям в порядке приоритета
		final Map<OperationEnum, Integer> OperationEnumPriority = EnumSet.allOf(OperationEnum.class).stream()
				.collect(Collectors.toMap(enumVal -> enumVal, enumVal -> enumVal.getPriority()));
		for (Integer i : OperationEnumPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { // наращиваем дерево пока на первом уровне не исчезнут операторы
				for (int j = 0; j < rule.size() - 2; j++) { // слева от EQ 
					if ((rule.get(j) instanceof Operation) //если текущая лексическая единица является операцией
							&& OperationEnumPriority.get(((Operation) rule.get(j)).getOperation()) == i) { //если операция равна текущему значению внешнего цикла 
						Expression expression = new Expression();
						foundOperation = true;
						if (j > 0 && (rule.get(j - 1) instanceof Operand)  //если слева операнд
								&& j < (rule.size() - 1) && (rule.get(j + 1) instanceof Operand)) { //если справа операнд
							expression.setOperand1((Operand) rule.get(j - 1));
							expression.setOperand2((Operand) rule.get(j + 1));
							expression.setOperation((Operation) rule.get(j));
							rule.set(j, expression);
							Set<Lexema> remove = new HashSet<Lexema>();
							remove.add(rule.get(j - 1));
							remove.add(rule.get(j + 1));
							rule.removeAll(remove); //удалить то, что внедрили в выражение - дерево выросло за счет того что "лежало на земле" 
						}
						break;
					} else {
						foundOperation = false;
					}
				}
			}
		}
		
		
	}

}
