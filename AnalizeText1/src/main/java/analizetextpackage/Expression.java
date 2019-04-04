package analizetextpackage;

import java.util.Set;

public interface Expression {  
	public boolean evaluate(Set<String> approvedFacts); 
}
