package analizetextpackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum OperationEnum { 
	AND, 
	OR,
	EQ;
	
	public String getVal() { 
		if (this == AND) return "&&"; 
		if (this == OR) return "||";
		if (this == EQ) return "->";
		return "";
	}

	public String getRegExp() {
		if (this == AND) return "&&"; 
		if (this == OR) return "\\|\\|"; 
		if (this == EQ) return "->";
		return "";
	}
	
	public static final Map<OperationEnum, Integer> OPERATION_PRIORITY;
	static {
		Map<OperationEnum, Integer> aMap = new HashMap<OperationEnum, Integer>();
		aMap.put(OperationEnum.AND, 45);
		aMap.put(OperationEnum.OR, 25);
		aMap.put(OperationEnum.EQ, 125);
		OPERATION_PRIORITY = Collections.unmodifiableMap(aMap);
	}
	
	public static final String GET_ALL_OPERATIONS_REGULAR_EXPRESSION() {
		String result = "";
		for (OperationEnum i:OperationEnum.values()) {
			result = result + ((result.length()==0)?i.getRegExp():"|"+i.getRegExp());
		}
			return result; 
	}

}