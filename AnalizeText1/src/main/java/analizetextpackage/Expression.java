package analizetextpackage;

public class Expression implements FactOrExpression, FactOrOperationOrExpression { 
	private FactOrExpression operand1;
	private FactOrExpression operand2;
	private Operation operation;
	private boolean dfn = false;
	
	public FactOrExpression getOperand1() {
		return operand1;
	}
	public void setOperand1(FactOrExpression operand1) {
		this.operand1 = operand1;
	}
	public FactOrExpression getOperand2() {
		return operand2;
	}
	public void setOperand2(FactOrExpression operand2) {
		this.operand2 = operand2;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setToken(Operation operation) {
		this.operation = operation;
	}
	@Override
	public boolean deduceAndGetIsDefined() {
		// TODO Auto-generated method stub
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
		this.dfn = res; 
		return res;
	}

	@Override
	public boolean getIsDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}
	@Override
	public boolean getDefinedFactOrExpression() {
		// TODO Auto-generated method stub
		return deduceAndGetIsDefined();
	}

}
