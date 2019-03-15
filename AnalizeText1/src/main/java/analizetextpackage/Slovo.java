package analizetextpackage;

import java.util.regex.Pattern;

public class Slovo implements Operand, Lexema {
	private String slovo;
	private Boolean dfn = false; 
	
	public Slovo(String sl) {
		this.setSlovo(sl);
	}
	
	public void setSlovo(String p_slovo) {
		slovo = p_slovo;
	}
	
	public String getSlovo() {
		return slovo; 
	}

	@Override
	public Boolean getDefined() {
		if (Pattern.compile("(&|\\||>)").matcher(slovo).find()) {
			throw new RuntimeException(Error.WRONG_SPECIAL_SYMBOL.getDescription());
		}
		if (Pattern.compile("^_\\d").matcher(slovo).find()) {
			throw new RuntimeException("В имени переменных встречаются цифры вначале");
		}
		if (slovo.length()==0) {
			throw new RuntimeException(Error.EMPTY_SLOVO.getDescription());
		}
		if (Pattern.compile("\\s").matcher(slovo).find()) {
			throw new RuntimeException("В имени переменных встречаются пробелы");
		}
		this.dfn = GlobArrs.DefinedArray.contains(slovo);
		return this.dfn;
	}

	@Override
	public Boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}

}
