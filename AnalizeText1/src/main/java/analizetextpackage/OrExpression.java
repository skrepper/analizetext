package analizetextpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class OrExpression implements Expression {

	private Collection<Expression> operands;

	public OrExpression(Collection<Expression> operand) {
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
