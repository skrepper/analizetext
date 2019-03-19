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

public class SomeExpressionArray {

	// это строка
	public ArrayList<Lexema> ops = new ArrayList<>();

	// программа не учитывает круглые скобки!
	public void makeAnaliz() {

		String sl = null;
		if (ops.get(0) instanceof Slovo) {
			sl = ((Slovo) ops.get(0)).getSlovo();
		} else {
// обработать это исключение
			throw new RuntimeException("ќшибка массива");
		}

		String expr = "(" + Token.GetAllTokensRegExpr() + ")"; // "(&&|\\|\\|)";
		String[] res = UseFull.splitPreserveDelimiter(sl, expr);
		Pattern token_pattern = Pattern.compile(expr);
		if (token_pattern.matcher(res[0]).matches() || token_pattern.matcher(res[res.length - 1]).matches()) {
			throw new RuntimeException("ѕо кра€м левой части выражени€ сто€т операторы");
		}
		int indexOfAdd = -1; // массив res должен заместить первый (0 - index) элемент ops
		boolean prevElementIsSlovo = false; // проверка чередований операторов и операндов
		for (String i : res) {
			if (token_pattern.matcher(i).matches()) {
				if (prevElementIsSlovo) {
					ops.add(++indexOfAdd, new Token(i.trim()));
					prevElementIsSlovo = false;
				} else {
					throw new RuntimeException("¬ левой части выражени€ сто€т 2 оператора подр€д.");
				}
			} else {
				if (!prevElementIsSlovo) {
					if (indexOfAdd != -1) {
						ops.add(++indexOfAdd, new Slovo(i.trim()));
					} else {
						ops.set(0, new Slovo(i.trim()));
						++indexOfAdd;
					}
					prevElementIsSlovo = true;
				}
			}
		}

		// цикл по всем операторам в порядке приоритета
		for (Integer i : TokenEnum.TokenToPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // масссив меняется, соответственно, нужен while
				for (int j = 0; j < ops.size() - 2; j++) { // ops.size()-2 - справа сто€т знак -> и результат
					if ((ops.get(j) instanceof Token) && TokenEnum.TokenToPriority.get(((Token) ops.get(j)).token) == i) {
						Expression expression = new Expression();
						fnd = true;
						if (j > 0 && (ops.get(j - 1) instanceof Operand) && j < (ops.size() - 1)
								&& (ops.get(j + 1) instanceof Operand)) {
							expression.setOperand1((Operand) ops.get(j - 1));
							expression.setOperand2((Operand) ops.get(j + 1));
							expression.setToken((Token) ops.get(j));
							ops.set(j, expression);
							Set<Lexema> remove = new HashSet<Lexema>();
							remove.add(ops.get(j - 1));
							remove.add(ops.get(j + 1));
							ops.removeAll(remove);
						}
						break;
					} else {
						fnd = false;
					}
				}
			}
		}
	}

	public boolean getDefined() {
		boolean res = false;
		for (Lexema i : ops.stream().limit(ops.size()).collect(Collectors.toList())) {
			if (!i.seeDefined()) {
				if (i.getDefined()) {
					GlobArrs.DefinedArray.add(((Slovo) ops.get(ops.size() - 1)).getSlovo());
					GlobArrs.NonDefinedArray.remove(((Slovo) ops.get(ops.size() - 1)).getSlovo());
					GlobArrs.tempChangedArrs = true;
				}
			}
		}
		return res;
	}

}
