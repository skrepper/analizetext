package analizetextpackage;

import java.util.Collection;
import java.util.Set;

public class AndExpression implements Expression {
	
	private Collection<Expression> operands;

	public AndExpression(Collection<Expression> operand) {
		this.operands = operand;
	}

	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		for (Expression e : operands)
		{
			if (!e.evaluate(approvedFacts)) 
				return false;  
		}
		return true;
	}
	

}
