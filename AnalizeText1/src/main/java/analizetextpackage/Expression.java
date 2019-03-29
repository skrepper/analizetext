package analizetextpackage;

import java.util.Set;

public interface Expression {  
	public boolean calculateExpression(Set<String> approvedFacts); 
	public boolean getDefined();

}
