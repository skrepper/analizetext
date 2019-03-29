package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class OrExpression implements Expression, Lexema {

	private ArrayList<Expression> operand;
	private boolean defined = false;

	public ArrayList<Expression> getOperand() {
		return operand;
	}

	public void setOperand(ArrayList<Expression> operand) {
		this.operand = operand;
	}

	public OrExpression(ArrayList<Expression> operand) {
		super();
		this.operand = operand;
	}

	@Override
	public boolean getDefined() {
		return defined;
	}
	@Override
	public boolean calculateExpression(Set<String> approvedFacts) {
		boolean res = false;
		for (Expression i:operand) {res = res || i.calculateExpression(approvedFacts);}
		this.defined = res; 
		return res;
	}
	

}
