package analizetextpackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingStroki {

	private DefinedArrayClass definedArrayObject; 
	private NonDefinedArrayClass nonDefinedArrayObject; 
	
	public ParsingStroki(DefinedArrayClass p_definedArrayObject, NonDefinedArrayClass p_nonDefinedArrayObject) {
		definedArrayObject = p_definedArrayObject; 
		nonDefinedArrayObject = p_nonDefinedArrayObject; 
	}
	

	public void parseStroka(String strLine) {
		Pattern pattern;
		Matcher matcher;
		// -> - ������ EQ
		String[] strArr = strLine.split(TokenEnum.EQ.getVal());
		if (strArr.length < 2) {
			throw new RuntimeException("������ ��������� ����� - �������� ���������� �������.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("������ ��������� ����� - ������� ����� ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� �����.");
		}
		pattern = Pattern.compile(Token.GetAllTokensRegExpr());
		matcher = pattern.matcher(strArr[1]);
		if (matcher.find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������.");
		}
		// ��� ��� ������ �� EQ - � ������ �������������� ����
		nonDefinedArrayObject.nonDefinedArray.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(Token.GetAllTokensRegExpr());
		// ��� ��� ����� �� EQ - � ������ �������������� ����
		for (String i : strArrLeft)
			nonDefinedArrayObject.nonDefinedArray.add(i.trim());

		// ������ ������
		SomeExpressionArray stroka = new SomeExpressionArray(definedArrayObject, nonDefinedArrayObject); 
		stroka.makeAnaliz(strArr); // ���������� ������ � ������� ���������
		AllExpressionArrays.allStrArrays.add(stroka);

	}
	
	
	public void parseLastStroka(String strLine) {
		// ��������� ������ �����
		if (definedArrayObject.definedArray.size()>0) throw new RuntimeException("� ����� ����� ����� ����������� ����� �����.");
		String[] strArr = strLine.split(",", -1);
		if (strLine.length() < 1) {
			throw new RuntimeException("������ ��������� ����� - �������� ������ � ����� �����.");
		}
		for (String i : strArr) {
			if (i.trim().length() == 0) throw new RuntimeException("� ��������� ������ ����� ���� ������ ����������.") ;
			Slovo.checkSlovo(i.trim());
			definedArrayObject.definedArray.add(i.trim());
		}
	}

}
