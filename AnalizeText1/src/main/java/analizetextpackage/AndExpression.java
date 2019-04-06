package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class AndExpression implements Expression {
	
	private ArrayList<Expression> operands;

	public AndExpression(ArrayList<Expression> operand) {
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
