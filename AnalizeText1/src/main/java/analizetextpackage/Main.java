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

	//признак конца программы - используется в бесконечном цикле
	private static boolean tempChangedArrs = false; 
	
	
	public static void main(String[] arg) throws IOException {
		DefinedArrayClass definedArrayObject = new DefinedArrayClass(); 
		NonDefinedArrayClass nonDefinedArrayObject = new NonDefinedArrayClass(); 
		ParsingStroki parser = new ParsingStroki(definedArrayObject, nonDefinedArrayObject);

		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));

		String filePathName;

		if (arg.length == 0) { 
			System.err.print("Введите имя файла.");
			return;
		}
		filePathName = arg[0];
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
			br.close(); // необязательно, так как используется конструкция try ()
			
			// в нижележащих функциях GetDefined для выражения зашита булева логика
			tempChangedArrs = true;
			while (tempChangedArrs) {
				tempChangedArrs = false;
				// идем по строкам
				for (SomeExpressionArray str : AllExpressionArrays.allStrArrays) {
						str.getDefined();
				}
			}
			System.out.print(String.join(", ", definedArrayObject.definedArray));
		} catch (IOException e) {
			System.err.print("Файл не найден");
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
