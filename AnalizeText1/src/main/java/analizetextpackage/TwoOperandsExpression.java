package analizetextpackage;

import java.util.Set;

public class TwoOperandsExpression implements Expression, Lexema { 

	private Expression operand1;
	private Expression operand2;
	private OperationLexema operation;
	private boolean defined = false;
	
	public TwoOperandsExpression(Expression operand1, Expression operand2, OperationLexema operation) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operation = operation;
	}
	
	@Override
	public boolean calculateLexema(Set<String> deducedFacts) {
		boolean res = false;
		boolean res1;
		boolean res2;
		switch (operation.getOperation()) 
		{
		case AND:
			res1 = this.operand1.calculateExpression(deducedFacts);
			res2 = this.operand2.calculateExpression(deducedFacts);
			res = res1 && res2;
			break;
		case OR:
			res1 = this.operand1.calculateExpression(deducedFacts);
			res2 = this.operand2.calculateExpression(deducedFacts);
			res = res1 || res2;
			break;
	    default: 
	    	res = false;
        	break;		}
		this.defined = res; 
		return res;
	}

	@Override
	public boolean getDefined() {
		return defined;
	}
	@Override
	public boolean calculateExpression(Set<String> deducedFacts) {
		return calculateLexema(deducedFacts);
	}

}
