package analizetextpackage;

public enum AnalisysRuleState {
	BEFORE_ANALISYS,
	FIRST_FACT,
	OPERATION,
	FACT;
	
	public AnalisysRuleState nextState(boolean isCurrentOperator) {
		switch (this) {
		case BEFORE_ANALISYS:
			if (isCurrentOperator) 
				throw new RuntimeException("� ������ ���� � ����� ����� ��������� ����� ���������.");
			return AnalisysRuleState.FIRST_FACT;
		case FIRST_FACT:
			if (!isCurrentOperator) throw new RuntimeException("����������� ��������� - ��� ����� ������ ����� split.");
			return AnalisysRuleState.OPERATION;
		case OPERATION:
			if (isCurrentOperator) throw new RuntimeException("� ����� ����� ��������� ����� 2 ��������� ������.");
			return AnalisysRuleState.FACT;
		case FACT:
			if (!isCurrentOperator) throw new RuntimeException("����������� ��������� - ��� ����� ������ ����� split.");
			return AnalisysRuleState.OPERATION;
		default:
			throw new RuntimeException("����������� ���������");
		}		
	}
}
