package analizetextpackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum TokenEnum { 
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
	
	public static final Map<TokenEnum, Integer> TokenToPriority;
	static {
		Map<TokenEnum, Integer> aMap = new HashMap<TokenEnum, Integer>();
		aMap.put(TokenEnum.AND, 45);
		aMap.put(TokenEnum.OR, 25);
		aMap.put(TokenEnum.EQ, 125);
		TokenToPriority = Collections.unmodifiableMap(aMap);
	}

}