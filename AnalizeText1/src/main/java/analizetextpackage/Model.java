package analizetextpackage;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "model")
public class Model {
	
	@XmlElement(name = "rules")
	private Collection<Rule> rules;
	@XmlElement(name = "approvedFacts")
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
