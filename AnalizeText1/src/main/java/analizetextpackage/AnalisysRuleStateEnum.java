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
				throw new RuntimeException("— левого кра€ в левой части выражени€ сто€т операторы.");
			return AnalisysRuleStateEnum.FIRST_FACT;
		case FIRST_FACT:
			if (!isCurrentOperator) throw new RuntimeException("Ќевозможное состо€ние - два факта подр€д после split.");
			return AnalisysRuleStateEnum.OPERATION; 
		case OPERATION:
			if (isCurrentOperator) throw new RuntimeException("¬ левой части выражени€ сто€т 2 оператора подр€д.");
			return AnalisysRuleStateEnum.FACT;
		case FACT:
			if (!isCurrentOperator) throw new RuntimeException("Ќевозможное состо€ние - два факта подр€д после split.");
			return AnalisysRuleStateEnum.OPERATION;
		default:
			throw new RuntimeException("Ќевозможное состо€ние");
		}		
	}
}
