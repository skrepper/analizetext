package analizetextpackage;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.RuntimeErrorException;


public class Main {
	public static void main(String[] arg) throws IOException {
		Pattern pattern;
		Matcher matcher;
		AllExpressionArrays allExprArrays = new AllExpressionArrays();
		
		final String FILE_END_DELIMITER = String.join("", IntStream.range(0, 64).mapToObj(i->"-").collect(Collectors.toList()));

		String filePathName;
		try {
			
		if (arg.length == 0 || arg[0].length() == 0) {
			throw new RuntimeException("������� ��� �����.");
		} else {
			filePathName = arg[0];
		};


		Boolean beforeDelim = true;
		try (
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)
						))) {

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
						if (strArr[1].trim().length()==0) {
							throw new RuntimeException("������ ��������� ����� - � ������ ����� �����.");
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

			System.out.print(String.join(", ", GlobArrs.DefinedArray));
			
		
		} catch (IOException e) {
			throw new IOException("���� �� ������");
		} catch (RuntimeException e) {
			throw e;
		}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		
		
	}
}


