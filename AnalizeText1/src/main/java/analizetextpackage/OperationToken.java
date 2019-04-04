package analizetextpackage;

public enum OperationToken { 
	AND("&&"), 
	OR("||");
	
    // constructor
    private OperationToken(final String val) {
        this.val = val;
    }
 
    // internal state
    private String val;
 
    public String getVal() {
        return val;
    }
	
}