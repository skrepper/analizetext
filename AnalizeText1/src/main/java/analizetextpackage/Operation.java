package analizetextpackage;

public class Operation implements Lexema {
	private OperationEnum operation;

	public Operation(String operation) {
		setOperation(operation);
	}

	public OperationEnum getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		for (OperationEnum i : OperationEnum.values()) {
			if (i.getVal().equals(operation))
				this.operation = i;
		}
	}

	@Override
	public boolean deduceGetDefined() {
		return true; // �������� ������ ����������
	}

	@Override
	public boolean getDefined() {
		return true; // �������� ������ ����������
	}

}
