package analizetextpackage;

import java.util.Set;
import java.util.regex.Pattern;

public class Fact implements FactOrExpression, FactOrOperationOrExpression {
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
	
	public void checkToErrorsFact(String fact) {
		if (Pattern.compile("(&|\\||>)").matcher(fact).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����������� �����������.");
		}
		if (Pattern.compile("^_\\d").matcher(fact).find()|Pattern.compile("^\\d").matcher(fact).find()) {
			throw new RuntimeException("� ����� ���������� ����������� ����� �������");
		}
		if (fact.length()==0) {
			throw new RuntimeException("������ ����� � ���������.");
		}
		if (Pattern.compile("\\s").matcher(fact).find()) {
			throw new RuntimeException("� ����� ���������� ����������� �������");
		}
	}

	@Override
	public boolean getDefinedFactOrExpression() {
		// TODO Auto-generated method stub
		return deduceAndGetIsDefined();
	}

}
