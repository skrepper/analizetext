package AnalizeTextPackage;

public class Expression implements Operand, Lexema { 
	Operand operand1;
	Operand operand2;
	Token token;
	Boolean dfn = false;
	
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
	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}
	@Override
	public Boolean getDefined() {
		// TODO Auto-generated method stub
		Boolean res = false;
		Boolean res1;
		Boolean res2;
		switch (token.getToken()) 
		{
		case AND:
			res1 = this.operand1.getDefined();
			res2 = this.operand2.getDefined();
			res = res1 && res2;
			break;
		case OR:
			res1 = this.operand1.getDefined();
			res2 = this.operand2.getDefined();
			res = res1 || res2;
			break;
	    default: 
	    	res = false;
        	break;		}
		this.dfn = res; //запомнить и не идти сюда при последующем анализе. ћожет стать не нужным, если рассматривать конфликты определений
		return res;
	}

	@Override
	public void setDefined(Boolean p_dfn) {
		// TODO Auto-generated method stub
		dfn = p_dfn;
	}
	@Override
	public Boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}

}
