package AnalizeTextPackage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SomeExpressionArray { 
	
	//��� ������
	public ArrayList<Lexema> ops = new ArrayList<>(); 

	// ��������� �� ��������� ������� ������!
	public void MakeAnaliz() {

		String sl = null;
		if (ops.get(0) instanceof Slovo) {
			sl = ((Slovo) ops.get(0)).getSlovo();
		} else {
// ���������� ��� ����������
			throw new RuntimeException("������ �������"); 
		}

		String expr = "(" + CONSTANT.GetAllTokensRegExpr() + ")"; //"(&&|\\|\\|)";
		String[] res = CONSTANT.splitPreserveDelimiter(sl, expr);
		Pattern token_pattern = Pattern.compile(expr);
		int indexOfAdd = 0; // ������ res ������ ��������� ������ ������� ops
		for (String i:res){
			if (token_pattern.matcher(i).matches()) {
				ops.add(++indexOfAdd, new Token(i.trim()));				
			} else {
				if (indexOfAdd!=0) {
					ops.add(++indexOfAdd, new Slovo(i.trim()));
				} else {
					ops.set(0, new Slovo(i.trim()));
				}
			}
		}

		//System.out.println("------"); // ������� ��� ����� ��������
		
		//���� �� ���� ���������� � ������� ����������
		for (Integer i:CONSTANT.TokenToPriority.values().stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // ������� ��������, ��������������, ����� while
				for (int j=0; j<ops.size()-2; j++) { // ops.size()-2 - ������ ����� ���� -> � ���������
					if ((ops.get(j) instanceof Token) && CONSTANT.TokenToPriority.get(((Token) ops.get(j) ).token)==i) {
						Expression expression = new Expression();
						fnd = true;
						if (j>0 && (ops.get(j-1) instanceof Operand) && j<(ops.size()-1) && (ops.get(j+1) instanceof Operand)) {
							expression.setOperand1((Operand) ops.get(j-1));
							expression.setOperand2((Operand) ops.get(j+1));
							expression.setToken((Token) ops.get(j));
							ops.set(j, expression);
							Set<Lexema> remove = new HashSet<Lexema>();
							remove.add(ops.get(j-1));
							remove.add(ops.get(j+1));
							ops.removeAll(remove);
						}
						break;
					} else {
						fnd = false;
					}
				}
			}
		}
	};

	public Boolean GetDefined() {
		Boolean res = false;
		//���� �� ���� ���������� ������ - ���� �� ����� ���� ��� ���� �� ������ ��������� 
		// - ��� ��� � MakeAnaliz ��� ����� ����� ������������ � ���� ���������
		for (Lexema i:ops.stream().limit(ops.size() - 2).collect(Collectors.toList())) {
			if (!i.seeDefined()) {
				//System.out.println("-------------"); // ������� ��� ����� ��������
				if (i.getDefined()) {
					GlobArrs.DefinedArray.add(((Slovo) ops.get(ops.size()-1)).slovo);
					GlobArrs.NonDefinedArray.remove(((Slovo) ops.get(ops.size()-1)).slovo);
					GlobArrs.tempChangedArrs = true;
				}
			}
		}
		return res;
	}

}
