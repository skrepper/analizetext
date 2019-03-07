package AnalizeTextPackage;

public class Token implements Lexema {
	CONSTANT.TokenEnum token;
	
	public Token(String token) {
		setToken(token);
		if (this.token==null) throw new NullPointerException("token is null");
	}

	public CONSTANT.TokenEnum getToken() {
		return token;
	}
	
	public void setToken(String token) {
		for (CONSTANT.TokenEnum i:CONSTANT.TokenEnum.values()) {
			if (i.getVal().equals(token)) this.token = i;
		};
	}

	@Override
	public Boolean getDefined() {
		// TODO Auto-generated method stub
		return true; //токен всегда определен
	}

	@Override
	public Boolean seeDefined() {
		// TODO Auto-generated method stub
		return true; //токен всегда определен
	}


}
