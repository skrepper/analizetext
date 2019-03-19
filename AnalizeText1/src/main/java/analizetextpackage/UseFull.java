package analizetextpackage;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseFull {

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
		if (last_match < data.length()) {
			splitted.add(data.substring(last_match));
		}
		return splitted.toArray(new String[splitted.size()]);
	}


	
}
