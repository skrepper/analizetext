package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class RuleChecker {
	
	private Set<String> deducedFacts;
	private Set<String> unknownFacts;

	public RuleChecker(Set<String> deducedFacts, Set<String> unknownFacts) {
		this.deducedFacts = deducedFacts;
		this.unknownFacts = unknownFacts;
	}

	/*
	 * Функция возвращает true, если не было вычислений в строке.
	 * Если же было хотя бы одно вычисление в строке, то возращается false
	 */
	public boolean checkRule(ArrayList<Lexema> rule) {
		boolean result = true;
		for (Lexema i : rule) {
			if (!i.getDefined()) {
				if (i.deduceGetDefined()) {
					// сюда можно попасть если isDefined было false, а стало true, то есть произошло вычисление
					deducedFacts.add(((Fact) rule.get(rule.size() - 1)).getFact());
					unknownFacts.remove(((Fact) rule.get(rule.size() - 1)).getFact());
					result = false;
				}
			}
		}
		return result;
	}
}
