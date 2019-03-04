package AnalizeTextPackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CONSTANT {
	
	public static enum TokenEnum {
		AND, 
		OR,
		EQ;
		
		public String getVal() {
			if (this == AND) return "&&";
			if (this == OR) return "||";
			if (this == EQ) return "->";
			return "";
		};

		public String getRegExp() {
			if (this == AND) return "&&"; 
			if (this == OR) return "\\|\\|";
			if (this == EQ) return "->";
			return "";
		};
	};
	
	
	//эту функцию написал в тот момент, когда еще не знал про то, что в определение enum можно вставлять функции, например getVal
	public static final Map<TokenEnum, Integer> TokenToPriority;
	static {
		Map<TokenEnum, Integer> aMap = new HashMap<TokenEnum, Integer>();
		aMap.put(TokenEnum.AND, 25);
		aMap.put(TokenEnum.OR, 25);
		aMap.put(TokenEnum.EQ, 125);
		TokenToPriority = Collections.unmodifiableMap(aMap);
	}
	
	public static final String GetAllTokensRegExpr() {
		String result = "";
		for (TokenEnum i:CONSTANT.TokenEnum.values()) {
			result = result + ((result.length()==0)?i.getRegExp():"|"+i.getRegExp());
		}
			return result; 
	}
	
	public static final int BIGSTRLEN = 1000000; // такой длины строки не бывает??
	
	public static final String FILE_END_DELIMITER = "----------------------------";

}
