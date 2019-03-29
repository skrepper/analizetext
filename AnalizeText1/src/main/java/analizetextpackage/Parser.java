package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.stream.IntStream;

public class Parser {

	private String filePathName;
	private ArrayList<Rule> allRules;
	private Set<String> deducedFacts;
	private final String allRegExps = String.join("|",
			EnumSet.allOf(OperationToken.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())); // склеить

	public Parser(String[] arg) {
		filePathName = arg[0];
		allRules = new ArrayList<>();
		deducedFacts = new HashSet<String>(); 
	}

	public Model parseFile() throws FileNotFoundException, IOException {
		String[] strArr;
		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));
		FilePositionState parsingState = FilePositionState.RULE;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				switch (parsingState) {
				case RULE:
					if (strLine.equals(FILE_END_DELIMITER)) {
						parsingState = FilePositionState.DELIMITER;
					} else {
						parsingState = FilePositionState.RULE;
						strArr = strLine.split(OperationToken.EQ.getVal());
						validateRuleLine(strArr, allRegExps);
						allRules.add(parseRule(strArr)); // парсинг и построение выражений
					}
					break;
				case KNOWN_FACTS:
					throw new RuntimeException("Ошибка структуры входного файла данных.");
				case DELIMITER:
					validateKnownFactsLine(strLine);
					strArr = strLine.split(",", -1);
					for (String i : strArr) {
						deducedFacts.add(i.trim());
					}
					parsingState = FilePositionState.KNOWN_FACTS;
					break;
				default:
					throw new RuntimeException("Ошибка структуры входного файла данных.");
				}
			}
			br.close();
			return new Model(allRules, deducedFacts);
		} catch (IOException e) {
			throw new RuntimeException("Файл не найден");
		}
	}
	
	
	public Rule parseRule(String[] strArr) {
		
		ArrayList<Lexema> lexems = new ArrayList<>();
		String deducedFact;
		lexems = new ArrayList<>();
		lexems.add(new FactExpression(strArr[0].trim()));
		deducedFact = strArr[1].trim();
		
		final String allOperationsRegularExpression = "(" + String.join("|",
				EnumSet.allOf(OperationToken.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())) + ")";
		String[] leftFacts = splitPreserveDelimiter(strArr[0].trim(), allOperationsRegularExpression);
		int indexOfAdd = -1; // номер массива правила куда вставлять новые факты или операции
		AnalisysRuleState analisysRuleState = AnalisysRuleState.BEFORE_ANALISYS;
		boolean isOperation; 
		for (String i : leftFacts) {
			isOperation = Pattern.compile(allOperationsRegularExpression).matcher(i).matches();
			switch (analisysRuleState) {
			case BEFORE_ANALISYS:
				if (isOperation) {
					throw new RuntimeException("С левого края в левой части выражения стоят операторы.");
				} else {
					analisysRuleState = AnalisysRuleState.FIRST_OPERAND;
					checkToErrorsFact(i.trim());
					lexems.set(++indexOfAdd, new FactExpression(i.trim()));
				}
				break;
			case FIRST_OPERAND:
				if (!isOperation) {
					throw new RuntimeException("Невозможное состояние 1 - два операнда подряд после split.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERATION;
					lexems.add(++indexOfAdd, new OperationLexema(i.trim()));
				}
				break;
			case OPERATION:
				if (isOperation) {
					throw new RuntimeException("В левой части выражения стоят 2 оператора подряд.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERAND;
					checkToErrorsFact(i.trim());
					lexems.add(++indexOfAdd, new FactExpression(i.trim()));
				}
				break;
			case OPERAND:
				if (!isOperation) {
					throw new RuntimeException("Невозможное состояние 2 - два операнда подряд после split.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERATION;
					lexems.add(++indexOfAdd, new OperationLexema(i.trim()));
				}
				break;
			default:
				throw new RuntimeException("Невозможное состояние");
			}
		}
		if (analisysRuleState == AnalisysRuleState.OPERATION)
			throw new RuntimeException("В левой части справа оператор.");
		
		return makeRuleExpr(lexems, deducedFact);

	}

	public Rule makeRuleExpr(ArrayList<Lexema> lexems, String deducedFact) {

		final Map<OperationToken, Integer> OperationEnumPriority = 
				EnumSet.allOf(OperationToken.class).stream()
				.collect(Collectors.toMap(enumVal -> enumVal, enumVal -> enumVal.getPriority()));
		try {
		for (Integer i : OperationEnumPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { 
				int startIndex = -1; 
				ArrayList<Expression> newExprArray = new ArrayList<>();
				Set<Lexema> remove = new HashSet<Lexema>();
				for (int j = 0; j < lexems.size(); j++) {  
					if ((lexems.get(j) instanceof OperationLexema) 
							&& OperationEnumPriority.get(((OperationLexema) lexems.get(j)).getOperation()) == i) {  
						foundOperation = true;
						if (startIndex==-1) {
							newExprArray.add((Expression) lexems.get(j - 1));
							remove.add(lexems.get(j - 1));
							startIndex = j-1;
						}
						newExprArray.add((Expression) lexems.get(j + 1));
						remove.add(lexems.get(j));
						remove.add(lexems.get(j + 1));
						if (((lexems.size()<=j+2) && ((OperationLexema) lexems.get(j)).getOperation().equals(OperationToken.AND)) ||
								((lexems.size()>j+2) && (lexems.get(j + 2) instanceof OperationLexema) && 
								((OperationLexema) lexems.get(j)).getOperation().equals(OperationToken.AND) &&
								!((OperationLexema) lexems.get(j + 2)).getOperation().equals(OperationToken.AND))
								) {
							AndExpression newExpr = new AndExpression(newExprArray);
							lexems.removeAll(remove);
							lexems.add(startIndex, newExpr);
							break;
						}
						if (((lexems.size()<=j+2)  && ((OperationLexema) lexems.get(j)).getOperation().equals(OperationToken.OR)) ||
								((lexems.size()>j+2) && (lexems.get(j + 2) instanceof OperationLexema) && 
								((OperationLexema) lexems.get(j)).getOperation().equals(OperationToken.OR) &&
								!((OperationLexema) lexems.get(j + 2)).getOperation().equals(OperationToken.OR))
								) {
							OrExpression newExpr = new OrExpression(newExprArray);
							lexems.removeAll(remove);
							lexems.add(startIndex, newExpr);
							break;
						}

					} else {
						foundOperation = false;
					}
				}
			}
		}
		} catch (Exception e) {
			throw e;
		}
		
		return new Rule((Expression) lexems.get(0), deducedFact);
	}
	
	/*
	 * Функция split с разделителями
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

	public void validateRuleLine(String[] strArr, String allRegExps) {
		if (strArr.length < 2) {
			throw new RuntimeException("Ошибка валидации файла - неверное построение функции.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("Ошибка валидации файла - слишком много ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("Ошибка валидации файла - в правой части пусто.");
		}
		if (strArr[0].trim().length() == 0) {
			throw new RuntimeException("Ошибка валидации файла - в левой части пусто.");
		}
		if (Pattern.compile(allRegExps).matcher(strArr[1]).find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
		}
	}

	public void validateKnownFactsLine(String strLine) {
		if (strLine.length() < 1) {
			throw new RuntimeException("Ошибка валидации файла - неверная строка фактов.");
		}
		
		for (String i : strLine.split(",", -1)) {
			if (i.trim().length() == 0) throw new RuntimeException("В строке фактов есть пустые переменные.");
			checkToErrorsFact(i.trim());
		}
	}

	public void checkToErrorsFact(String factToken) {
		if (Pattern.compile("(&|\\||>)").matcher(factToken).find()) {
			throw new RuntimeException("Ошибка валидации файла - в словах встречаются спецсимволы.");
		}
		if (Pattern.compile("^_\\d").matcher(factToken).find()|Pattern.compile("^\\d").matcher(factToken).find()) {
			throw new RuntimeException("В имени переменных встречаются цифры вначале");
		}
		if (factToken.length()==0) {
			throw new RuntimeException("Пустое слово в выражении.");
		}
		if (Pattern.compile("\\s").matcher(factToken).find()) {
			throw new RuntimeException("В имени переменных встречаются пробелы");
		}
	}

}
