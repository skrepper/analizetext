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
	
	//это строка
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

		String expr = "(" + Token.GetAllTokensRegExpr() + ")"; //"(&&|\\|\\|)";
		String[] res = splitPreserveDelimiter(sl, expr);
		Pattern token_pattern = Pattern.compile(expr);
		int indexOfAdd = 0; // массив res должен заместить первый элемент ops
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
		
		//цикл по всем операторам в порядке приоритета
		for (Integer i:TokenToPriority.values().stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList())) {
			boolean fnd = true;
			while (fnd) { // масссив меняется, соответственно, нужен while
				for (int j=0; j<ops.size()-2; j++) { // ops.size()-2 - справа сто€т знак -> и результат
					if ((ops.get(j) instanceof Token) && TokenToPriority.get(((Token) ops.get(j) ).token)==i) {
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

	public Boolean getDefined() {
		Boolean res = false;
		for (Lexema i:ops.stream().limit(ops.size()).collect(Collectors.toList())) {
			if (!i.seeDefined()) {
				if (i.getDefined()) {
					GlobArrs.DefinedArray.add(((Slovo) ops.get(ops.size()-1)).getSlovo());
					GlobArrs.NonDefinedArray.remove(((Slovo) ops.get(ops.size()-1)).getSlovo());
					GlobArrs.tempChangedArrs = true;
				}
			}
		}
		return res;
	}
	
	private static final Map<TokenEnum, Integer> TokenToPriority;
	static {
		Map<TokenEnum, Integer> aMap = new HashMap<TokenEnum, Integer>();
		aMap.put(TokenEnum.AND, 45);
		aMap.put(TokenEnum.OR, 25);
		aMap.put(TokenEnum.EQ, 125);
		TokenToPriority = Collections.unmodifiableMap(aMap);
	}

	private static String[] splitPreserveDelimiter(String data, String regexp) {
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
		if (last_match<data.length()) {
			splitted.add(data.substring(last_match));
		}
		return  splitted.toArray(new String[splitted.size()]);
	}
	

}
