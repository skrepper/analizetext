package analizetextpackage;

import java.util.regex.Pattern;

public class Slovo implements Operand, Lexema {
	private String slovo;
	private boolean dfn = false; 
	private DefinedArrayClass definedArrayObject;
	
	public Slovo(String sl, DefinedArrayClass p_definedArrayObject) {
		this.setSlovo(sl);
		this.definedArrayObject = p_definedArrayObject;
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
		this.dfn = definedArrayObject.definedArray.contains(slovo);
		return this.dfn;
	}

	@Override
	public boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}
	
	public static void checkSlovo(String slovo) {
		if (Pattern.compile("(&|\\||>)").matcher(slovo).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����������� �����������.");
		}
		if (Pattern.compile("^_\\d").matcher(slovo).find()|Pattern.compile("^\\d").matcher(slovo).find()) {
			throw new RuntimeException("� ����� ���������� ����������� ����� �������");
		}
		if (slovo.length()==0) {
			throw new RuntimeException("������ ����� � ���������.");
		}
		if (Pattern.compile("\\s").matcher(slovo).find()) {
			throw new RuntimeException("� ����� ���������� ����������� �������");
		}
	}

	@Override
	public boolean getDefinedOperand() {
		// TODO Auto-generated method stub
		return getDefined();
	}

}
