package AnalizeTextPackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class CONSTANT { 
	
	//эту функцию написал в тот момент, когда еще не знал про то, что в определение enum можно вставлять функции, например getVal
	public static final Map<TokenEnum, Integer> TokenToPriority;
	static {
		Map<TokenEnum, Integer> aMap = new HashMap<TokenEnum, Integer>();
		aMap.put(TokenEnum.AND, 45);
		aMap.put(TokenEnum.OR, 25);
		aMap.put(TokenEnum.EQ, 125);
		TokenToPriority = Collections.unmodifiableMap(aMap);
	}
	
	public static final String GetAllTokensRegExpr() {
		String result = "";
		for (TokenEnum i:TokenEnum.values()) {
			result = result + ((result.length()==0)?i.getRegExp():"|"+i.getRegExp());
		}
			return result; 
	}
	
	public static final int BIGSTRLEN = 1000000; // такой длины строки не бывает??
	
	public static final String FILE_END_DELIMITER = "----------------------------";
	
	
	public static String[] splitPreserveDelimiter(String data, String regexp) {
		LinkedList<String> splitted = new LinkedList<String>();
		int last_match = 0;
		Matcher m = Pattern.compile(regexp).matcher(data);
		while (m.find()) {
			if (last_match < m.start()) {
				splitted.add(data.substring(last_match, m.start()));
			}
			splitted.add(m.group());
			last_match = m.end();
		}
		if (last_match<data.length()) {
			splitted.add(data.substring(last_match));
		}
		return  splitted.toArray(new String[splitted.size()]);
	}
}
