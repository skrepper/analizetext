package analizetextpackage;

import java.util.Set;

public class FactExpression implements Expression, Lexema {
	private String factToken;
	
	public FactExpression(String fact) {
		this.setFact(fact);
	}


	public void setFact(String fact) {
		this.factToken = fact;
	}
	
	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		return approvedFacts.contains(factToken);
	}

}
