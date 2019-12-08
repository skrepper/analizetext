package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rule")
public class Rule {

	@XmlElements({
    @XmlElement(name="andExpression", type = AndExpression.class),
    @XmlElement(name="orExpression", type = OrExpression.class),
    @XmlElement(name="factExpression", type = FactExpression.class)
	})	
	private Expression expression;
    
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



