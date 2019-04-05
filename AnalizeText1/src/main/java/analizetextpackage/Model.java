package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class Model {
	
	private ArrayList<Rule> rules;
	private Set<String> approvedFacts;

	public Model(ArrayList<Rule> rules, Set<String> approvedFacts) {
		this.rules = rules;
		this.approvedFacts = approvedFacts; 
	}

	public void calculate() {
		boolean knownFactsAppended;
		do {
			knownFactsAppended = false;
			for (Rule rule : rules) {
				knownFactsAppended = knownFactsAppended || rule.calculate(approvedFacts);
			}
		} while (knownFactsAppended);
	}
	
	public Set<String> getApprovedFacts() {
		return approvedFacts;
	}

}
