package analizetextpackage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleParser {

	private Set<String> deducedFacts;
	private Set<String> unknownFacts;

	public RuleParser(Set<String> deducedFacts, Set<String> unknownFacts) {
		this.deducedFacts = deducedFacts;
		this.unknownFacts = unknownFacts;
	}
	
	
	public void parse(String[] strArr, ArrayList<Lexema> rule) {
		rule.add(new Fact(strArr[0].trim(), deducedFacts));
		rule.add(new Operation(OperationEnum.EQ.getVal()));
		rule.add(new Fact(strArr[1].trim(), deducedFacts));

		final String allOperationsRegularExpression = "(" + String.join("|",
				EnumSet.allOf(OperationEnum.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())) + ")";
		String[] leftFacts = splitPreserveDelimiter(strArr[0].trim(), allOperationsRegularExpression);
		int indexOfAdd = -1; // номер массива правила куда вставл€ть новые факты или операции
		AnalisysRuleStateEnum analisysRuleState = AnalisysRuleStateEnum.BEFORE_ANALISYS;
		for (String i : leftFacts) {
			analisysRuleState = analisysRuleState
					.nextState(Pattern.compile(allOperationsRegularExpression).matcher(i).matches());
			switch (analisysRuleState) {
			case FIRST_FACT:
				rule.set(++indexOfAdd, new Fact(i.trim(), deducedFacts));
				break;
			case FACT:
				rule.add(++indexOfAdd, new Fact(i.trim(), deducedFacts));
				break;
			case OPERATION:
				rule.add(++indexOfAdd, new Operation(i.trim()));
				break;
			case BEFORE_ANALISYS:
				break;
			default:
				break;
			}
		}
		if (analisysRuleState == AnalisysRuleStateEnum.OPERATION)
			throw new RuntimeException("¬ левой части справа оператор.");

	}
	
	/*
	 * ‘ункци€ split с разделител€ми
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
}
