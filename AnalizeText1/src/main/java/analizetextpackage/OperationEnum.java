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

	public int getPriority() {
		if (this == AND) return 45; 
		if (this == OR) return 25; 
		if (this == EQ) return 125;
		return 0;
	}
	
}