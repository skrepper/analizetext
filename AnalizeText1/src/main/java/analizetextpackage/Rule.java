package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rule1")
public class Rule {
	
	@XmlElements({
	    @XmlElement(type = FactExpression.class),
	    @XmlElement(type = AndExpression.class),
	    @XmlElement(type = OrExpression.class)
	})
	private Expression expression;
    
	@XmlElement(name = "resultingfact")
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
