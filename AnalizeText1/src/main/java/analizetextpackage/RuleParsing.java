package analizetextpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParsing {

	private Set<String> knownFacts;
	private Set<String> unknownFacts = new HashSet<String>();
	private ArrayList<RuleAnalysis> allRules;

	public RuleParsing(Set<String> knownFacts, Set<String> unknownFacts, ArrayList<RuleAnalysis> allRules) {
		this.knownFacts = knownFacts;
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
		if (Pattern.compile(OperationEnum.GET_ALL_OPERATIONS_REGULAR_EXPRESSION()).matcher(strArr[1]).find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
		}
		// все слова в массив неопределенных слов
		unknownFacts.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(OperationEnum.GET_ALL_OPERATIONS_REGULAR_EXPRESSION());
		for (String i : strArrLeft) unknownFacts.add(i.trim());

		// анализ массива слов
		RuleAnalysis rule = new RuleAnalysis(knownFacts, unknownFacts);
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
			(new Fact(i.trim(), knownFacts)).checkToErrorsFact(i.trim()); 
			knownFacts.add(i.trim());
		}
	}

}
