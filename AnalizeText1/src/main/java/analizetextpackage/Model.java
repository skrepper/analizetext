package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class Model {
	
	private ArrayList<Rule> allRules;
	private Set<String> deducedFacts;

	public Model(ArrayList<Rule> allRules, Set<String> deducedFacts) {
		this.allRules = allRules;
		this.deducedFacts = deducedFacts; 
	}

	public void calculate() {
		boolean knownFactsNotChanged;
		do {
			knownFactsNotChanged = true;
			for (Rule rule : allRules) {
				knownFactsNotChanged = knownFactsNotChanged && rule.calculate(deducedFacts);
			}
		} while (!knownFactsNotChanged);
	}
	
	
	public ArrayList<Rule> getAllRules() {
		return allRules;
	}

	public Set<String> getDeducedFacts() {
		return deducedFacts;
	}

}
