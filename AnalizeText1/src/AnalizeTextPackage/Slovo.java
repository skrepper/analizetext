package AnalizeTextPackage;

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

	@Override
	public Boolean getDefined() {
		// TODO Auto-generated method stub
		this.dfn = GlobArrs.DefinedArray.contains(slovo);
		return this.dfn;
	}

	@Override
	public void setDefined(Boolean p_dfn) {
		// TODO Auto-generated method stub
		dfn = p_dfn;
	}

	@Override
	public Boolean seeDefined() {
		// TODO Auto-generated method stub
		return dfn;
	}

}
