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

	public String startMainpProc(String[] arg) {

		String result;
		Pattern pattern;
		Matcher matcher;
		AllExpressionArrays allExprArrays = new AllExpressionArrays();
		
		final String FILE_END_DELIMITER = String.join("", IntStream.range(0, 64).mapToObj(i->"-").collect(Collectors.toList()));

		String filePathName;

		if (arg.length == 0 || arg[0].length() == 0) {
			return Error.ENTER_FILE_NAME.getDescription();
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
						// -> - символ EQ
						String[] strArr = strLine.split(TokenEnum.EQ.getVal());
						if (strArr.length < 2) {
							throw new RuntimeException(Error.WRONG_FILE_VALIDATION1.getDescription());
						}
						if (strArr.length > 2) {
							throw new RuntimeException(Error.WRONG_FILE_VALIDATION3.getDescription());
						}
						pattern = Pattern.compile(Token.GetAllTokensRegExpr());
						matcher = pattern.matcher(strArr[1]);
						if (matcher.find()) {
							result = Error.WRONG_RIGHT_OPERATOR.getDescription();
							return result;
						}
						// все что справа от EQ - в массив неопределенных слов
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(Token.GetAllTokensRegExpr());
						// все что слева от EQ - в массив неопределенных слов
						for (String i : strArrLeft)
							GlobArrs.NonDefinedArray.add(i.trim());

						// анализ заново
						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.makeAnaliz(); // превращаем строку в объекты выражений
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					// последн€€ строка файла
					String[] strArr = strLine.split(",");
					if (strLine.length() < 1) {
						throw new RuntimeException(Error.WRONG_FILE_VALIDATION2.getDescription());
					}
					for (String i : strArr)
						GlobArrs.DefinedArray.add(i.trim());
					break; // только одна строка после разделител€!
				}
			}
			br.close(); // необ€зательно, так как используетс€ нова€ конструкци€ try () {}
		} catch (IOException e) {
			result = Error.WRONG_READ_FILE.getDescription();
			return result;
		} catch (RuntimeException e) {
			result = e.getMessage();
			return result;
		}

		// в нижележащих функци€х GetDefined дл€ выражени€ зашита булева логика
		// вышележаща€ функци€
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			// идем по строкам
			for (SomeExpressionArray str : allExprArrays.AllStrArrays) {
				try {
					str.getDefined();
				} catch (RuntimeException e) {
					result = e.getMessage();
					return result;
				}
			}
		}

		result = String.join(", ", GlobArrs.DefinedArray);
		return result;

	}
	
}
