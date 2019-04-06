package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
		if (Pattern.compile("&&|\\|\\|").matcher(ruleParts[1]).find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
		}
		
		return new Rule(parseExpression(ruleParts[0].trim()), ruleParts[1].trim()); // парсинг и построение выражений
	}
	
	public Expression parseExpression(String expressionString) {
		if (Pattern.matches(".*\\|\\|.*", expressionString)) 
			return getOrExpr(expressionString);
		if (Pattern.matches(".*&&.*", expressionString))  
			return getAndExpr(expressionString);
		return getFactExpr(expressionString);
	}
	
	public Expression getOrExpr(String str) {
		ArrayList<String> arrStr = new ArrayList<String>(Arrays.asList(str.split("\\|\\|")));
		List<Expression> listFE = arrStr.stream().map(st -> parseExpression(st)).collect(Collectors.toList());
		ArrayList<Expression> arrExpr = new ArrayList<>();
		arrExpr.addAll(listFE);
		return new OrExpression(arrExpr);
	}

	public Expression getAndExpr(String str) {
		ArrayList<String> arrStr = new ArrayList<String>(Arrays.asList(str.split("&&")));
		List<Expression> listFE = arrStr.stream().map(st -> parseExpression(st)).collect(Collectors.toList());
		ArrayList<Expression> arrExpr = new ArrayList<>();
		arrExpr.addAll(listFE);
		return new AndExpression(arrExpr);
	}
	
	public Expression getFactExpr(String str) {
		checkFact(str);
		return new FactExpression(str);
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
	
}
