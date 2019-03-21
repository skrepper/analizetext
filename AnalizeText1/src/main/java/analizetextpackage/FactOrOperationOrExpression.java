package analizetextpackage;

import java.util.Set;

public interface FactOrOperationOrExpression {   
	public boolean deduceAndGetIsDefined(); // set property and see descendants
	public boolean getIsDefined(); // see property

}
