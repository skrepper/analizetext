package analizetextpackage;

import java.util.regex.Pattern;

public class RuleValidator {
	
	public RuleValidator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void validate(String[] strArr, String allRegExps) {
		if (strArr.length < 2) {
			throw new RuntimeException("������ ��������� ����� - �������� ���������� �������.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("������ ��������� ����� - ������� ����� ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� �����.");
		}
		if (strArr[0].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ����� ����� �����.");
		}
		if (Pattern.compile(allRegExps).matcher(strArr[1]).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������.");
		}
	}
}
