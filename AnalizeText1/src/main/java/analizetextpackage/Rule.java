package analizetextpackage;

import java.util.Set;

public class Rule {
	public Expression expression;
	public String resultingFact;
	
	public Rule(Expression expression, String resultingFact) {
		this.expression = expression;
		this.resultingFact = resultingFact;
	}

	public void calculate(Set<String> approvedFacts) {
		if (approvedFacts.contains(resultingFact))
			return;

		if (!expression.evaluate(approvedFacts))
			return;
		
		approvedFacts.add(resultingFact);
	}
}
