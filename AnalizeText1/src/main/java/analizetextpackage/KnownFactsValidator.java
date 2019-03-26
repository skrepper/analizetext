package analizetextpackage;

import java.util.Set;

public class KnownFactsValidator {
	
	private Set<String> deducedFacts;
	
	public KnownFactsValidator(Set<String> deducedFacts) {
		this.deducedFacts = deducedFacts;
	}

	public void validate(String strLine) {
		if (strLine.length() < 1) {
			throw new RuntimeException("Ошибка валидации файла - неверная строка фактов.");
		}
		
		for (String i : strLine.split(",", -1)) {
			if (i.trim().length() == 0) throw new RuntimeException("В строке фактов есть пустые переменные.");
			FactValidator factValidator = new FactValidator();
			factValidator.checkToErrorsFact(i.trim());
		}

	}

}
