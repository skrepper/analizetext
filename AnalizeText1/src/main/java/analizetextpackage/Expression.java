package analizetextpackage;

public class Expression implements Operand, Lexema { 
	private Operand operand1;
	private Operand operand2;
	private Operation operation;
	private boolean defined = false;
	
	public Operand getOperand1() {
		return operand1;
	}
	public void setOperand1(Operand operand1) {
		this.operand1 = operand1;
	}
	public Operand getOperand2() {
		return operand2;
	}
	public void setOperand2(Operand operand2) {
		this.operand2 = operand2;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	@Override
	public boolean deduceGetDefined() {
		boolean res = false;
		boolean res1;
		boolean res2;
		switch (operation.getOperation()) 
		{
		case AND:
			res1 = this.operand1.getDefinedFactOrExpression();
			res2 = this.operand2.getDefinedFactOrExpression();
			res = res1 && res2;
			break;
		case OR:
			res1 = this.operand1.getDefinedFactOrExpression();
			res2 = this.operand2.getDefinedFactOrExpression();
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
	public boolean getDefinedFactOrExpression() {
		return deduceGetDefined();
	}

}
