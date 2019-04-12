package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {

	private String filePathName;

	public Parser() {
	}

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException {
		Collection<Rule> rules = new ArrayList<>();
		Set<String> resultingFacts = new HashSet<String>(); 
		String[] knownFacts;
		this.filePathName = filePathName;
		final String FILE_END_DELIMITER = "----------------------------------------------------------------"; //64
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
						validateFact(s);
						resultingFacts.add(s);
					}
					parsingState = FilePositionState.FINISH;
					break;
				case FINISH:
				default:
					throw new RuntimeException("Ошибка структуры входного файла данных.");
				}
			}
			return new Model(rules, resultingFacts);
		} catch (IOException e) {
			throw new RuntimeException("Ошибка чтения файла. " + e.getMessage(), e);
		}
	}

	private Rule parseRule(String strLine) {
		String[] ruleParts;
		ruleParts = strLine.split("->", -1);
		if (ruleParts.length < 2) {
			throw new RuntimeException("Неверный синтаксис правила.");
		}
		if (ruleParts.length > 2) {
			throw new RuntimeException("Слишком много '->' в правиле.");
		}
		String resultingFact = ruleParts[1].trim(); 
		validateFact(resultingFact);
		if (resultingFact.length() == 0) {
			throw new RuntimeException("Неверный синтаксис правила.");
		}
		if (ruleParts[0].trim().length() == 0) {
			throw new RuntimeException("Неверный синтаксис правила.");
		}
		if (Pattern.compile("&&|\\|\\|").matcher(resultingFact).find()) {
			throw new RuntimeException("В результатирующем факте встретился оператор.");
		}

		return new Rule(parseExpression(ruleParts[0].trim()), resultingFact); // парсинг и построение выражений
	}
	
	private Expression parseExpression(String expressionString) {
		if (Pattern.matches(".*\\|\\|.*", expressionString)) 
			return getOrExpr(expressionString);
		if (Pattern.matches(".*&&.*", expressionString))  
			return getAndExpr(expressionString);
		return getFactExpr(expressionString);
	}
	
	private Expression getOrExpr(String str) {
		List<String> arrStr = Arrays.asList(str.split("\\|\\|"));
		List<Expression> listFE = arrStr.stream().map(st -> parseExpression(st)).collect(Collectors.toList());
		ArrayList<Expression> arrExpr = new ArrayList<>();
		arrExpr.addAll(listFE);
		return new OrExpression(arrExpr);
	}

	private Expression getAndExpr(String str) {
		ArrayList<String> arrStr = new ArrayList<String>(Arrays.asList(str.split("&&")));
		List<Expression> listFE = arrStr.stream().map(st -> parseExpression(st)).collect(Collectors.toList());
		ArrayList<Expression> arrExpr = new ArrayList<>();
		arrExpr.addAll(listFE);
		return new AndExpression(arrExpr);
	}
	
	private Expression getFactExpr(String str) {
		str = str.trim();
		validateFact(str);
		return new FactExpression(str);
	}

	private void validateFact(String factToken) {
		if (!Pattern.compile("^(_+)\\D+").matcher(factToken).find()) {
			throw new RuntimeException("Неверное имя факта.");
		}
		if (Pattern.compile("(&|\\||>)").matcher(factToken).find()) {
			throw new RuntimeException("В фактах встречаются спецсимволы.");
		}
		if (Pattern.compile("^_\\d").matcher(factToken).find()|Pattern.compile("^\\d").matcher(factToken).find()) {
			throw new RuntimeException("В фактах встречаются цифры вначале.");
		}
		if (factToken.length()==0) {
			throw new RuntimeException("Неверный синтаксис правила.");
		}
		if (Pattern.compile("\\s").matcher(factToken).find()) {
			throw new RuntimeException("В фактах встречаются пробелы.");
		}
	}

	private enum FilePositionState {
		RULE,
		KNOWN_FACTS,
		FINISH
	}
	
}
