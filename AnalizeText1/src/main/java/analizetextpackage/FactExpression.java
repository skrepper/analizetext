package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "factExpression")
public class FactExpression implements Expression {

	@XmlValue
	private String fact;
	
	public FactExpression() {
	}
	public FactExpression(String fact) {
		this.fact = fact;
	}
	
	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		return approvedFacts.contains(fact);
	}

}
