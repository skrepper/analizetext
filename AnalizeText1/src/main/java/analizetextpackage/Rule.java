package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class Rule {

	public Rule(Expression expression, String deducedFact) {
		this.expression = expression;
		this.deducedFact = deducedFact;
	}

	public Expression expression;
	public String deducedFact;
	
	public boolean calculate(Set<String> approvedFacts) {
		if (expression.getDefined()) {
			return true;
		} else {
			if (expression.calculateExpression(approvedFacts)) {
				approvedFacts.add(deducedFact);
				return false;
			} else {
				return true;
			}
		}
	}
}
