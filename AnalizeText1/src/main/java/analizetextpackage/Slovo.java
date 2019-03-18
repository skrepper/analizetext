package analizetextpackage;

import java.util.regex.Pattern;

public class Slovo implements Operand, Lexema {
	private String slovo;
	private boolean dfn = false; 
	
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
	public boolean getDefined() {
		checkSlovo(slovo);
		this.dfn = GlobArrs.DefinedArray.contains(slovo);
		return this.dfn;
	}

	@Override
	public boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}
	
	public static void checkSlovo(String slovo) {
		if (Pattern.compile("(&|\\||>)").matcher(slovo).find()) {
			throw new RuntimeException("Ошибка валидации файла - в словах встречаются спецсимволы.");
		}
		if (Pattern.compile("^_\\d").matcher(slovo).find()|Pattern.compile("^\\d").matcher(slovo).find()) {
			throw new RuntimeException("В имени переменных встречаются цифры вначале");
		}
		if (slovo.length()==0) {
			throw new RuntimeException("Пустое слово в выражении.");
		}
		if (Pattern.compile("\\s").matcher(slovo).find()) {
			throw new RuntimeException("В имени переменных встречаются пробелы");
		}
	}

}
