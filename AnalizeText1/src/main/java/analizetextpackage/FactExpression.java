package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "factExpression")
public class FactExpression implements Expression, Cloneable {

	@XmlElement(name = "fact")
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

    @Override
    public FactExpression clone() throws CloneNotSupportedException {
        return (FactExpression) super.clone();
    }
	
}
