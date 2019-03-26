package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class KnownFactsFactory {

	public void deduceKnownFacts(Set<String> deducedFacts, Set<String> unknownFacts, ArrayList<ArrayList<Lexema>> allRules) {
		// анализ выражений - заполнение массива известных фактов
		RuleChecker ruleChecker = new RuleChecker(deducedFacts, unknownFacts);
		boolean knownFactsNotChanged;
		do {
			knownFactsNotChanged = true;
			for (ArrayList<Lexema> rule : allRules) {
				knownFactsNotChanged = knownFactsNotChanged && ruleChecker.checkRule(rule);
			}
		} while (!knownFactsNotChanged);
	}
}
