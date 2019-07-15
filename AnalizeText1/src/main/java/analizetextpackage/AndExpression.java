package analizetextpackage;

import java.util.Collection;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "and", namespace="http://Cyden")
public class AndExpression implements Expression {
	
	@XmlElements({
	    @XmlElement(name="fact", type = FactExpression.class, namespace="http://Cyden")
	})	
	private Collection<Expression> operands;

	public AndExpression() {
	}

	public AndExpression(Collection<Expression> operand) {
		this.operands = operand;
	}

	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		for (Expression e : operands)
		{
			if (!e.evaluate(approvedFacts)) 
				return false;  
		}
		return true;
	}
	

}
