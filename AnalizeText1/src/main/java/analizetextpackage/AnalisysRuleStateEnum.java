package analizetextpackage;

public enum AnalisysRuleStateEnum {
	BEFORE_ANALISYS,
	FIRST_FACT,
	OPERATION,
	FACT;
	
	public AnalisysRuleStateEnum nextState(boolean isCurrentOperator) {
		switch (this) {
		case BEFORE_ANALISYS:
			if (isCurrentOperator) 
				throw new RuntimeException("� ������ ���� � ����� ����� ��������� ����� ���������.");
			return AnalisysRuleStateEnum.FIRST_FACT;
		case FIRST_FACT:
			if (!isCurrentOperator) throw new RuntimeException("����������� ��������� - ��� ����� ������ ����� split.");
			return AnalisysRuleStateEnum.OPERATION; 
		case OPERATION:
			if (isCurrentOperator) throw new RuntimeException("� ����� ����� ��������� ����� 2 ��������� ������.");
			return AnalisysRuleStateEnum.FACT;
		case FACT:
			if (!isCurrentOperator) throw new RuntimeException("����������� ��������� - ��� ����� ������ ����� split.");
			return AnalisysRuleStateEnum.OPERATION;
		default:
			throw new RuntimeException("����������� ���������");
		}		
	}
}
