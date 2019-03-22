package analizetextpackage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RuleParsing {

	private Set<String> deducedFacts;
	private Set<String> unknownFacts = new HashSet<String>();
	private ArrayList<RuleAnalysis> allRules;

	public RuleParsing(Set<String> deducedFacts, Set<String> unknownFacts, ArrayList<RuleAnalysis> allRules) {
		this.deducedFacts = deducedFacts;
		this.unknownFacts = unknownFacts;
		this.allRules = allRules; 
	}

	public void parseRule(String strLine) {
		// -> - символ EQ
		String[] strArr = strLine.split(OperationEnum.EQ.getVal());
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
		String allOperationsRegularExpressions = String.join("|", EnumSet.allOf(OperationEnum.class).stream().map(i->i.getRegExp()).collect(Collectors.toList())); 
		if (Pattern.compile(allOperationsRegularExpressions).matcher(strArr[1]).find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
		}
		// все слова в массив неопределенных слов
		unknownFacts.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(allOperationsRegularExpressions);
		for (String i : strArrLeft) unknownFacts.add(i.trim());

		// анализ массива слов
		RuleAnalysis rule = new RuleAnalysis(deducedFacts, unknownFacts);
		rule.makeRuleAnalisys(strArr); // превращаем массив слов в объекты
		allRules.add(rule);

	}

	public void parseKnownFacts(String strLine) {
		// последняя строка файла
		String[] strArr = strLine.split(",", -1);
		if (strLine.length() < 1) {
			throw new RuntimeException("Ошибка валидации файла - неверная строка в конце файла.");
		}
		for (String i : strArr) {
			if (i.trim().length() == 0)
				throw new RuntimeException("В последней строке файла есть пустые переменные.");
			(new Fact(i.trim(), deducedFacts)).checkToErrorsFact(i.trim()); 
			deducedFacts.add(i.trim());
		}
	}

}
