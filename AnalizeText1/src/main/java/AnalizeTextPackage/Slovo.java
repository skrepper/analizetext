package AnalizeTextPackage;

import java.util.regex.Pattern;

public class Slovo implements Operand, Lexema {
	String slovo;
	Boolean dfn = false; 
	
	public Slovo(String sl) {
		this.setSlovo(sl);
	}
	
	public void setSlovo(String p_slovo) {
		slovo = p_slovo;
	}
	
	public String getSlovo() {
		return slovo; 
	}

//	@Override
	public Boolean getDefined() {
		// TODO Auto-generated method stub
		// \.[]{}()<>*+-=!?^$|
		// "(&&|\\|\\|)"
		// "(&|\\|\\\\|.|\\[|\\]|\\{|\\}|\\(|\\)|\\<|\\>|\\*|\\+|\\-|\\=|\\!|\\?|\\^|\\$|\\))"
		String expr = "(&|\\|)";
		Pattern halftoken_pattern = Pattern.compile(expr);
		if (halftoken_pattern.matcher(slovo).find()) {
			throw new RuntimeException(Error.WRONG_SPECIAL_SYMBOL.getDescription());
		}
		this.dfn = GlobArrs.DefinedArray.contains(slovo);
		return this.dfn;
	}

//	@Override
	public void setDefined(Boolean p_dfn) {
		// TODO Auto-generated method stub
		dfn = p_dfn;
	}

//	@Override
	public Boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}

}
