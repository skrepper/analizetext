package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Parser {

	public Model parseFile(String filePathName) throws FileNotFoundException, IOException {
		Collection<Rule> rules = new ArrayList<>();
		Set<String> resultingFacts = new HashSet<String>(); 
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
					parseKnownFacts(resultingFacts, strLine);
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

	private void parseKnownFacts(Set<String> resultingFacts, String strLine) {
		String[] knownFacts;
		knownFacts = strLine.split(",", -1);
		for (String i : knownFacts) {
			String s = i.trim();
			validateFact(s);
			resultingFacts.add(s);
		}
	}

	private Rule parseRule(String strLine) {
		String[] ruleParts;
		ruleParts = strLine.split("->", -1);
		if (ruleParts.length != 2) {
			throw new RuntimeException("Ошибка синтаксиса правила " + strLine);
		}
		String resultingFact = ruleParts[1].trim(); 
		validateFact(resultingFact);
		return new Rule(parseExpression(ruleParts[0].trim()), resultingFact); // парсинг и построение выражений
	}
	
	private Expression parseExpression(String expressionString) {
		ArrayList<Expression> arrExpr = new ArrayList<>();
		String arrStr[] = expressionString.split("\\|\\|", -1);
		if (arrStr.length==1) return parseFactExpr(arrStr[0]);
		for (int i=0; i < arrStr.length; i++) arrExpr.add(parseAndExpr(arrStr[i]));
		return new OrExpression(arrExpr);
	}

	private Expression parseAndExpr(String expressionString) {
		ArrayList<Expression> arrExpr = new ArrayList<>();
		String arrStr[] = expressionString.split("&&", -1);
		if (arrStr.length==1) return parseFactExpr(arrStr[0]);
		for (int i=0; i < arrStr.length; i++) arrExpr.add(parseFactExpr(arrStr[i]));
		return new AndExpression(arrExpr);
	}
	
	private Expression parseFactExpr(String str) {
		str = str.trim();
		validateFact(str);
		return new FactExpression(str);
	}

	private void validateFact(String factToken) {
		if (!Pattern.compile("^_*[a-zA-Z]+\\w*").matcher(factToken).matches()) {
			throw new RuntimeException("Неверное имя факта. '"+factToken+"'");
		}  
		else {
			//System.out.println("yes fact " + factToken);
		}
	}

	private enum FilePositionState {
		RULE,
		KNOWN_FACTS,
		FINISH
	}
	
}
