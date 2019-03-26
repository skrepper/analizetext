package analizetextpackage;

import java.util.regex.Pattern;

public class FactValidator {

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

}
