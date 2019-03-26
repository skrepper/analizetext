package analizetextpackage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleFactory {

	private Set<String> deducedFacts;
	private Set<String> unknownFacts;

	private ArrayList<ArrayList<Lexema>> allRules;
	final String allRegExps = String.join("|", EnumSet.allOf(OperationEnum.class).stream().map(i->i.getRegExp()).collect(Collectors.toList())); //склеить все регулярные выражения

	public RuleFactory(Set<String> deducedFacts, Set<String> unknownFacts, ArrayList<ArrayList<Lexema>> allRules) {
		this.deducedFacts = deducedFacts;
		this.unknownFacts = unknownFacts;
		this.allRules = allRules; 
	}

	public void addRule(String strLine) {

		// разбить строку
		String[] strArr = strLine.split(OperationEnum.EQ.getVal());

		RuleValidator ruleValidator = new RuleValidator(); 
		ruleValidator.validate(strArr, allRegExps);

		// все слова в массив неопределенных слов
		for (String i : strArr[0].split(allRegExps)) unknownFacts.add(i.trim()); //левая часть
		unknownFacts.add(strArr[1].trim()); //правая часть

		ArrayList<Lexema> rule = new ArrayList<Lexema>();

		RuleParser ruleParser = new RuleParser(deducedFacts, unknownFacts);
		ruleParser.parse(strArr, rule);

		RuleBuilder ruleBuilder = new RuleBuilder();
		ruleBuilder.build(rule);

		allRules.add(rule);

	}
}
