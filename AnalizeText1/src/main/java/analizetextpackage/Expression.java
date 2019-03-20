package analizetextpackage;

public class Expression implements Operand, WordOrExpression { 
	private Operand operand1;
	private Operand operand2;
	private Operation token;
	private boolean dfn = false;
	
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
	public Operation getToken() {
		return token;
	}
	public void setToken(Operation token) {
		this.token = token;
	}
	@Override
	public boolean deduceAndGetIsDefined() {
		// TODO Auto-generated method stub
		boolean res = false;
		boolean res1;
		boolean res2;
		switch (token.getToken()) 
		{
		case AND:
			res1 = this.operand1.getDefinedOperand();
			res2 = this.operand2.getDefinedOperand();
			res = res1 && res2;
			break;
		case OR:
			res1 = this.operand1.getDefinedOperand();
			res2 = this.operand2.getDefinedOperand();
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
	public boolean getDefinedOperand() {
		// TODO Auto-generated method stub
		return deduceAndGetIsDefined();
	}

}
