package analizetextpackage;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "model")
public class Model {
	
	@XmlElementWrapper(name="rules")
	@XmlElement(name="rule")
    private Collection<Rule> rules;
    
	@XmlElementWrapper(name="approvedFacts")
	@XmlElement(name="approvedFact")
    private Set<String> approvedFacts;

	public Model() {
	}
	
 	public Model(Collection<Rule> rules, Set<String> approvedFacts) {
		this.rules = rules;
		this.approvedFacts = approvedFacts; 
	}

	public void calculate() {
		int approvedFactsSize;
		do {
			approvedFactsSize = approvedFacts.size();
			for (Rule rule : rules) {
				rule.calculate(approvedFacts);
			}
		} while (approvedFactsSize != approvedFacts.size());
	}
	
	public Set<String> getApprovedFacts() {
		return approvedFacts;
	}
	
}


