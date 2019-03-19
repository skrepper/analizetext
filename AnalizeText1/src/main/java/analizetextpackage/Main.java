package analizetextpackage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.RuntimeErrorException;

public class Main {

	private Set<String> DefinedArray = new HashSet<String>();  
	private Set<String> NonDefinedArray = new HashSet<String>();
	//������� ����� ��������� - ������������ � ����������� �����
	private static boolean tempChangedArrs = false; 
	
	
	public static void main(String[] arg) throws IOException {
		ParsingStroki parser = new ParsingStroki();

		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));

		String filePathName;

		if (arg.length == 0) { 
			System.err.print("������� ��� �����.");
			return;
		}
		filePathName = arg[0];
		GlobArrs.DefinedArray.clear();
		GlobArrs.NonDefinedArray.clear();
		boolean beforeDelim = true;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {

			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (beforeDelim) {
					if (strLine.equals(FILE_END_DELIMITER)) {
						beforeDelim = false;
					} else {
						parser.parseStroka(strLine);
					}
				} else {
						parser.parseLastStroka(strLine);
				}
			}
			br.close(); // �������������, ��� ��� ������������ ����������� try ()
			
			// � ����������� �������� GetDefined ��� ��������� ������ ������ ������
			GlobArrs.tempChangedArrs = true;
			while (GlobArrs.tempChangedArrs) {
				GlobArrs.tempChangedArrs = false;
				// ���� �� �������
				for (SomeExpressionArray str : AllExpressionArrays.allStrArrays) {
					//try {
						str.getDefined();
					/*} catch (RuntimeException e) {
						throw new RuntimeException(e.getMessage());
					}*/
				}
			}
			System.out.print(String.join(", ", GlobArrs.DefinedArray));
		} catch (IOException e) {
			System.err.print("���� �� ������");
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
