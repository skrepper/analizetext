package analizetextpackage;

import java.util.Set;

public class FactExpression implements Expression {
	private String fact;
	
	public FactExpression(String fact) {
		this.fact = fact;
	}


	
	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		return approvedFacts.contains(fact);
	}

}
