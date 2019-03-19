package analizetextpackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingStroki {

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
		GlobArrs.NonDefinedArray.add(strArr[1].trim());
		String[] strArrLeft = strArr[0].split(Token.GetAllTokensRegExpr());
		// ��� ��� ����� �� EQ - � ������ �������������� ����
		for (String i : strArrLeft)
			GlobArrs.NonDefinedArray.add(i.trim());

		// ������ ������
		SomeExpressionArray stroka = new SomeExpressionArray();
		stroka.ops.add(new Slovo(strArr[0].trim()));
		stroka.ops.add(new Token(TokenEnum.EQ.getVal()));
		stroka.ops.add(new Slovo(strArr[1].trim()));
		stroka.makeAnaliz(); // ���������� ������ � ������� ���������
		AllExpressionArrays.allStrArrays.add(stroka);

	}
	
	
	public void parseLastStroka(String strLine) {
		// ��������� ������ �����
		if (GlobArrs.DefinedArray.size()>0) throw new RuntimeException("� ����� ����� ����� ����������� ����� �����.");
		String[] strArr = strLine.split(",", -1);
		if (strLine.length() < 1) {
			throw new RuntimeException("������ ��������� ����� - �������� ������ � ����� �����.");
		}
		for (String i : strArr) {
			if (i.trim().length() == 0) throw new RuntimeException("� ��������� ������ ����� ���� ������ ����������.") ;
			Slovo.checkSlovo(i.trim());
			GlobArrs.DefinedArray.add(i.trim());
		}
	}

}
