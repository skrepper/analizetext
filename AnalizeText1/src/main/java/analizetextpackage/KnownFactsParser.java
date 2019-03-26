package analizetextpackage;

import java.util.Set;

public class KnownFactsParser {

	private Set<String> deducedFacts;

	public KnownFactsParser(Set<String> deducedFacts) {
		this.deducedFacts = deducedFacts;
	}

	public void parseKnownFacts(String strLine) {

		KnownFactsValidator knownFactsValidator = new KnownFactsValidator(deducedFacts);
		knownFactsValidator.validate(strLine);

		String[] strArr = strLine.split(",", -1);

		for (String i : strArr) {
			deducedFacts.add(i.trim());
		}
	}

}
