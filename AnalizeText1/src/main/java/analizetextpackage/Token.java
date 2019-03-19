package analizetextpackage;

public class Token implements Lexema { 
	private TokenEnum token;
	
	public Token(String token) { 
		setToken(token);
		if (this.token==null) throw new NullPointerException("token is null");
	}

	public TokenEnum getToken() {
		return token;
	}
	
	public void setToken(String token) {
		for (TokenEnum i:TokenEnum.values()) {
			if (i.getVal().equals(token)) this.token = i; 
		}
	}

	@Override
	public boolean getDefined() {
		// TODO Auto-generated method stub
		return true; //токен всегда определен
	}

	@Override
	public boolean seeDefined() {
		// TODO Auto-generated method stub
		return true; //токен всегда определен
	}

	
	public static final String GetAllTokensRegExpr() {
		String result = "";
		for (TokenEnum i:TokenEnum.values()) {
			result = result + ((result.length()==0)?i.getRegExp():"|"+i.getRegExp());
		}
			return result; 
	}


}
