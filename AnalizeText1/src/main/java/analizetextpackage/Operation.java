package analizetextpackage;

public class Operation implements WordOrExpression { 
	private OperationEnum operation;
	
	public Operation(String operation) { 
		setToken(operation);
		if (this.operation==null) throw new NullPointerException("�������� ����� ������������ ��������");
	}

	public OperationEnum getToken() {
		return operation;
	}
	
	public void setToken(String operation) {
		for (OperationEnum i:OperationEnum.values()) {
			if (i.getVal().equals(operation)) this.operation = i; 
		}
	}

	@Override
	public boolean deduceAndGetIsDefined() {
		return true; //�������� ������ ����������
	}

	@Override
	public boolean getIsDefined() {
		return true; //�������� ������ ����������
	}

	
	public static final String GetAllTokensRegExpr() {
		String result = "";
		for (OperationEnum i:OperationEnum.values()) {
			result = result + ((result.length()==0)?i.getRegExp():"|"+i.getRegExp());
		}
			return result; 
	}


}
