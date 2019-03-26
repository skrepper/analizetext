package analizetextpackage;

import java.util.Set;
import java.util.regex.Pattern;

public class Fact implements Operand, Lexema {
	private String fact;
	private boolean isDefined = false; 
	private Set<String> deducedFacts;
	
	public Fact(String fact, Set<String> deducedFacts) {
		this.setFact(fact);
		this.deducedFacts = deducedFacts;
	}


	public void setFact(String fact) {
		this.fact = fact;
	}
	
	public String getFact() {
		return fact; 
	}

	@Override
	public boolean deduceGetDefined() {
		FactValidator factValidator = new FactValidator();
		factValidator.checkToErrorsFact(fact);
		this.isDefined = deducedFacts.contains(fact);
		return this.isDefined;
	}

	@Override
	public boolean getDefined() {
		// TODO Auto-generated method stub
		return isDefined;
	}
	
	@Override
	public boolean getDefinedFactOrExpression() {
		// TODO Auto-generated method stub
		return deduceGetDefined();
	}

}
