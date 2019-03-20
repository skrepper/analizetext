package analizetextpackage;

public class Operation implements FactOrOperationOrExpression { 
	private OperationEnum operation;
	
	public Operation(String operation) { 
		setOperation(operation);
		if (this.operation==null) throw new RuntimeException("�������� ����� ������������ ��������");
	}

	public OperationEnum getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
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

}
