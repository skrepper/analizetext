package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class OrExpression implements Expression, Lexema {

	private ArrayList<Expression> operands;

	public OrExpression(ArrayList<Expression> operand) {
		this.operands = operand;
	}

	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		for (Expression i:operands) {
			if (i.evaluate(approvedFacts)) {
				return true;
			}
		}
		return false;
	}
	

}
