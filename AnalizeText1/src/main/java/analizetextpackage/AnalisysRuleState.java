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
				throw new RuntimeException("— левого кра€ в левой части выражени€ сто€т операторы.");
			return AnalisysRuleState.FIRST_FACT;
		case FIRST_FACT:
			if (!isCurrentOperator) throw new RuntimeException("Ќевозможное состо€ние - два факта подр€д после split.");
			return AnalisysRuleState.OPERATION;
		case OPERATION:
			if (isCurrentOperator) throw new RuntimeException("¬ левой части выражени€ сто€т 2 оператора подр€д.");
			return AnalisysRuleState.FACT;
		case FACT:
			if (!isCurrentOperator) throw new RuntimeException("Ќевозможное состо€ние - два факта подр€д после split.");
			return AnalisysRuleState.OPERATION;
		default:
			throw new RuntimeException("Ќевозможное состо€ние");
		}		
	}
}
