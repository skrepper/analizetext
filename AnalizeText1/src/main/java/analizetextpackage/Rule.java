package analizetextpackage;

import java.util.Set;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rule", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
public class Rule {

	@XmlElements({
    @XmlElement(name="andExpression", type = AndExpression.class, namespace="xsi:http://www.w3.org/2001/XMLSchema-instance"),
    @XmlElement(name="orExpression", type = OrExpression.class, namespace="xsi:http://www.w3.org/2001/XMLSchema-instance"),
    @XmlElement(name="factExpression", type = FactExpression.class, namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
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



