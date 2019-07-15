package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "rule", namespace="http://Cyden")
public class Rule {

	@XmlElements({
    @XmlElement(name="and", type = AndExpression.class, namespace="http://Cyden"),
    @XmlElement(name="or", type = OrExpression.class, namespace="http://Cyden"),
    @XmlElement(name="fact", type = FactExpression.class, namespace="http://Cyden")
	})	
	private Expression expression;
    
    @XmlElement(name="resultingFact", namespace="http://Cyden")
	private String resultingFact;
	
	public Rule() {
	}

	public Rule(Expression expression, String resultingFact) {
		this.expression = expression;
		this.resultingFact = resultingFact;
	}

	public void calculate(Set<String> approvedFacts) {
		if (approvedFacts.contains(resultingFact))
			return;

		if (!expression.evaluate(approvedFacts))
			return;
		
		approvedFacts.add(resultingFact);
	}
}



