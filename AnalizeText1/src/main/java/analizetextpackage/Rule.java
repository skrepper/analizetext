package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class Rule {
	public Expression expression;
	public String resultedFact;
	
	public Rule(Expression expression, String resultedFact) {
		this.expression = expression;
		this.resultedFact = resultedFact;
	}

	// https://rules.sonarsource.com/java/tag/convention/RSPEC-2047 
	public boolean update(Set<String> approvedFacts) {
		if (approvedFacts.contains(resultedFact)) return false;

		if (!expression.evaluate(approvedFacts))
			return false;
		
		approvedFacts.add(resultedFact);
		return true;
	}
}
