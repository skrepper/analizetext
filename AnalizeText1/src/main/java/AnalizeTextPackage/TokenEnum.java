package AnalizeTextPackage;

public enum TokenEnum {
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