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

	@Override
	public boolean calculateLexema(Set<String> deducedFacts) {
		this.defined = deducedFacts.contains(factToken);
		return this.defined;
	}

	public boolean getDefined() {
		// TODO Auto-generated method stub
		return defined;
	}
	
	@Override
	public boolean calculateExpression(Set<String> deducedFacts) {
		// TODO Auto-generated method stub
		return calculateLexema(deducedFacts);
	}

}
