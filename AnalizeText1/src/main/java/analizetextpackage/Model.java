package analizetextpackage;

import java.util.Collection;
import java.util.Set;

public class Model {
	
	private Collection<Rule> rules;
	private Set<String> approvedFacts;

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
