package analizetextpackage;

import java.util.Set;
import java.util.regex.Pattern;

public class Fact implements Operand, WordOrExpression {
	private String fact;
	private boolean isDefined = false; 
	private Set<String> knownFacts;
	
	public Fact(String fact, Set<String> knownFacts) {
		this.setFact(fact);
		this.knownFacts = knownFacts;
	}
	
	public void setFact(String fact) {
		this.fact = fact;
	}
	
	public String getFact() {
		return fact; 
	}

	@Override
	public boolean deduceAndGetIsDefined() {
		checkToErrorsFact(fact);
		this.isDefined = knownFacts.contains(fact);
		return this.isDefined;
	}

	@Override
	public boolean getIsDefined() {
		// TODO Auto-generated method stub
		return isDefined;
	}
	
	public static void checkToErrorsFact(String fact) {
		if (Pattern.compile("(&|\\||>)").matcher(fact).find()) {
			throw new RuntimeException("Ошибка валидации файла - в словах встречаются спецсимволы.");
		}
		if (Pattern.compile("^_\\d").matcher(fact).find()|Pattern.compile("^\\d").matcher(fact).find()) {
			throw new RuntimeException("В имени переменных встречаются цифры вначале");
		}
		if (fact.length()==0) {
			throw new RuntimeException("Пустое слово в выражении.");
		}
		if (Pattern.compile("\\s").matcher(fact).find()) {
			throw new RuntimeException("В имени переменных встречаются пробелы");
		}
	}

	@Override
	public boolean getDefinedOperand() {
		// TODO Auto-generated method stub
		return deduceAndGetIsDefined();
	}

}
