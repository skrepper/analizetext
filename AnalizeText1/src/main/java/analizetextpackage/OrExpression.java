package analizetextpackage;

import java.util.Collection;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElements;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orExpression")
public class OrExpression implements Expression {

	@XmlElements({
	    @XmlElement(name="andExpression", type = AndExpression.class),
	    @XmlElement(name="factExpression", type = FactExpression.class)
	})	
	private Collection<Expression> operands;

	public OrExpression() {
	}

	public OrExpression(Collection<Expression> operand) {
		this.operands = operand;
	}

	@Override
	public boolean evaluate(Set<String> approvedFacts) {
		for (Expression i:operands) {
			if (i.evaluate(approvedFacts)) {
				return true;
			}
		}
		return false;
	}
	

}
