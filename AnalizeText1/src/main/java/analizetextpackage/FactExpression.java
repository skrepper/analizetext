package analizetextpackage;

import java.util.Set;

public class FactExpression implements Expression, Lexema {
	private String factToken;
	private boolean defined = false; 
	
	public FactExpression(String fact) {
		this.setFact(fact);
	}


	public void setFact(String fact) {
		this.factToken = fact;
	}
	
	public String getFact() {
		return factToken; 
	}


	public boolean getDefined() {
		return defined;
	}
	
	@Override
	public boolean calculateExpression(Set<String> approvedFacts) {
		this.defined = approvedFacts.contains(factToken);
		return this.defined;
	}

}
