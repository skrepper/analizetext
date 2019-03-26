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
		
		// ���� �� ���� ���������� ��������� � ������� ����������
		final Map<OperationEnum, Integer> OperationEnumPriority = EnumSet.allOf(OperationEnum.class).stream()
				.collect(Collectors.toMap(enumVal -> enumVal, enumVal -> enumVal.getPriority()));
		for (Integer i : OperationEnumPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { // ���������� ������ ���� �� ������ ������ �� �������� ���������
				for (int j = 0; j < rule.size() - 2; j++) { // ����� �� EQ 
					if ((rule.get(j) instanceof Operation) //���� ������� ����������� ������� �������� ���������
							&& OperationEnumPriority.get(((Operation) rule.get(j)).getOperation()) == i) { //���� �������� ����� �������� �������� �������� ����� 
						Expression expression = new Expression();
						foundOperation = true;
						if (j > 0 && (rule.get(j - 1) instanceof Operand)  //���� ����� �������
								&& j < (rule.size() - 1) && (rule.get(j + 1) instanceof Operand)) { //���� ������ �������
							expression.setOperand1((Operand) rule.get(j - 1));
							expression.setOperand2((Operand) rule.get(j + 1));
							expression.setOperation((Operation) rule.get(j));
							rule.set(j, expression);
							Set<Lexema> remove = new HashSet<Lexema>();
							remove.add(rule.get(j - 1));
							remove.add(rule.get(j + 1));
							rule.removeAll(remove); //������� ��, ��� �������� � ��������� - ������ ������� �� ���� ���� ��� "������ �� �����" 
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
