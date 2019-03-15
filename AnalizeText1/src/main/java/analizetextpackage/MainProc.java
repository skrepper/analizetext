package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class MainProc {

	public String startMainProc(String[] arg) throws IOException {

		String result;
		Pattern pattern;
		Matcher matcher;
		AllExpressionArrays allExprArrays = new AllExpressionArrays();
		
		final String FILE_END_DELIMITER = String.join("", IntStream.range(0, 64).mapToObj(i->"-").collect(Collectors.toList()));

		String filePathName;

		if (arg.length == 0 || arg[0].length() == 0) {
			throw new RuntimeException("������� ��� �����.");
		} else {
			filePathName = arg[0];
		}

		Boolean beforeDelim = true;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {

			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (beforeDelim) {
					if (strLine.equals(FILE_END_DELIMITER)) {
						beforeDelim = false;
					} else {
						// -> - ������ EQ
						String[] strArr = strLine.split(TokenEnum.EQ.getVal());
						if (strArr.length < 2) {
							throw new RuntimeException("������ ��������� ����� - �������� ���������� �������.");
						}
						if (strArr.length > 2) {
							throw new RuntimeException("������ ��������� ����� - ������� ����� ->.");
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
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					// ��������� ������ �����
					String[] strArr = strLine.split(",");
					if (strLine.length() < 1) {
						throw new RuntimeException("������ ��������� ����� - �������� ������ � ����� �����.");
					}
					for (String i : strArr)
						GlobArrs.DefinedArray.add(i.trim());
					break; // ������ ���� ������ ����� �����������!
				}
			}
			br.close(); // �������������, ��� ��� ������������ ����� ����������� try () {}
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}

		// � ����������� �������� GetDefined ��� ��������� ������ ������ ������
		// ����������� �������
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			// ���� �� �������
			for (SomeExpressionArray str : allExprArrays.AllStrArrays) {
				try {
					str.getDefined();
				} catch (RuntimeException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}

		result = String.join(", ", GlobArrs.DefinedArray);
		return result;

	}
	
}
