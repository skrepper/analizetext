package analizetextpackage;

import java.util.Set;

public class OperationLexema implements Lexema {
	private OperationToken operation;

	public OperationLexema(String operation) {
		setOperation(operation);
	}

	public OperationToken getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		for (OperationToken i : OperationToken.values()) {
			if (i.getVal().equals(operation))
				this.operation = i;
		}
	}

	@Override
	public boolean calculateLexema(Set<String> deducedFacts) {
		return true; // операция всегда определена
	}

	public boolean getDefined() {
		return true; // операция всегда определена
	}

}
