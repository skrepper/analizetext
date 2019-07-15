package analizetextpackage;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "model", namespace="http://Cyden")
public class Model {
	
	@XmlElementWrapper(name="rules", namespace="http://Cyden")
	@XmlElement(name="rule", namespace="http://Cyden")
    private Collection<Rule> rules;
    
	@XmlElementWrapper(name="approvedFacts", namespace="http://Cyden")
	@XmlElement(name="approvedFact", namespace="http://Cyden")
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


