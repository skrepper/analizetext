package analizetextpackage;

public class Operation implements FactOrOperationOrExpression { 
	private OperationEnum operation;
	
	public Operation(String operation) { 
		setOperation(operation);
		if (this.operation==null) throw new RuntimeException("Ќеверный вызов конструктора операции");
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
		return true; //операци€ всегда определена
	}

	@Override
	public boolean getIsDefined() {
		return true; //операци€ всегда определена
	}

}
