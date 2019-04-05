package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
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
	private ArrayList<Rule> rules = new ArrayList<>();
	private Set<String> resultedFacts;
	private final String operationsRegExp = "&&|\\|\\|"; 

	public Parser(String[] arg) {
		filePathName = arg[0];
		resultedFacts = new HashSet<String>(); 
	}

	public Model parseFile() throws FileNotFoundException, IOException {
		String[] ruleParts, knownFacts;
		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));
		FilePositionState parsingState = FilePositionState.RULE;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				switch (parsingState) {
				case RULE:
					if (strLine.equals(FILE_END_DELIMITER)) {
						parsingState = FilePositionState.KNOWN_FACTS;
						break;
					}
					
					rules.add(parseRule(strLine));
					break;
				case KNOWN_FACTS:
					knownFacts = strLine.split(",", -1);
					for (String i : knownFacts) {
						String s = i.trim();
						checkFact(s);
						resultedFacts.add(s);
					}
					parsingState = FilePositionState.FINISH;
					break;
				case FINISH:
				default:
					throw new RuntimeException("Ошибка структуры входного файла данных.");
				}
			}
			br.close();
			return new Model(rules, resultedFacts);
		} catch (IOException e) {
			throw new RuntimeException("Файл не найден");
		}
	}

	private Rule parseRule(String strLine) {
		String[] ruleParts;
		ruleParts = strLine.split("->");
		if (ruleParts.length < 2) {
			throw new RuntimeException("Ошибка валидации файла - неверное построение функции.");
		}
		if (ruleParts.length > 2) {
			throw new RuntimeException("Ошибка валидации файла - слишком много ->.");
		}
		if (ruleParts[1].trim().length() == 0) {
			throw new RuntimeException("Ошибка валидации файла - в правой части пусто.");
		}
		if (ruleParts[0].trim().length() == 0) {
			throw new RuntimeException("Ошибка валидации файла - в левой части пусто.");
		}
		if (Pattern.compile(operationsRegExp).matcher(ruleParts[1]).find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
		}
		
		return new Rule(parseExpression(ruleParts[0].trim()), ruleParts[1].trim()); // парсинг и построение выражений
	}
	
	public Expression parseExpression(String expressionString) {
		ArrayList<Expression> lexems = new ArrayList<Expression>();
		String[] tokens = splitPreserveDelimiter(expressionString, "("+operationsRegExp+")");

		int nextElementIndex = -1; // номер массива правила куда вставлять новые факты или операции
		AnalisysRuleState analisysRuleState = AnalisysRuleState.OPERAND;
		for (String token : tokens) {
			boolean isOperation = Pattern.compile("("+operationsRegExp+")").matcher(token).matches();
			switch (analisysRuleState) {
			case OPERAND:
				if (isOperation) 
					throw new RuntimeException("В левой части выражения стоят 2 оператора подряд.");
				analisysRuleState = AnalisysRuleState.OPERATION;
				checkFact(token.trim());
				lexems.add(++nextElementIndex, new FactExpression(token.trim()));
				break;
			case OPERATION:
				if (!isOperation) 
					throw new RuntimeException("Невозможное состояние 2 - два операнда подряд после split.");
				analisysRuleState = AnalisysRuleState.OPERAND;
				lexems.add(++nextElementIndex, new OperationLexema(token.trim()));
				break;
			default:
				throw new RuntimeException("Невозможное состояние");
			}
		}

		if (analisysRuleState == AnalisysRuleState.OPERAND)
			throw new RuntimeException("В левой части справа оператор.");

		final Map<OperationToken, Integer> operationPriority = 
				new HashMap<OperationToken, Integer>(){{
					put(OperationToken.AND, 45); 
					put(OperationToken.OR, 25); 
				}};
		for (Integer i : operationPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { 
				int startIndex = -1; 
				ArrayList<Expression> newExprArray = new ArrayList<>();
				Set<Lexema> remove = new HashSet<Lexema>();
				for (int j = 0; j < lexems.size(); j++) {  
					if ((lexems.get(j) instanceof OperationLexema) 
							&& operationPriority.get(((OperationLexema) lexems.get(j)).getOperation()) == i) {  
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
		return (Expression) lexems.get(0);
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

	public void checkFact(String factToken) {
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

	public enum FilePositionState {
		RULE,
		KNOWN_FACTS,
		FINISH
	}

	public enum AnalisysRuleState {
		OPERAND,
		OPERATION
	}
	
}
