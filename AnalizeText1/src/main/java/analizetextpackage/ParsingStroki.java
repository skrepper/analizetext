package analizetextpackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingStroki {

	public void parseStroka(String strLine) {
		Pattern pattern;
		Matcher matcher;
		// -> - символ EQ
		String[] strArr = strLine.split(TokenEnum.EQ.getVal());
		if (strArr.length < 2) {
			throw new RuntimeException("Ошибка валидации файла - неверное построение функции.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("Ошибка валидации файла - слишком много ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("Ошибка валидации файла - в правой части пусто.");
		}
		pattern = Pattern.compile(Token.GetAllTokensRegExpr());
		matcher = pattern.matcher(strArr[1]);
		if (matcher.find()) {
			throw new RuntimeException("Ошибка валидации файла - в правой части операторы.");
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
		AllExpressionArrays.allStrArrays.add(stroka);

	}
	
	
	public void parseLastStroka(String strLine) {
		// последняя строка файла
		if (GlobArrs.DefinedArray.size()>0) throw new RuntimeException("В конце файла после разделителя много строк.");
		String[] strArr = strLine.split(",", -1);
		if (strLine.length() < 1) {
			throw new RuntimeException("Ошибка валидации файла - неверная строка в конце файла.");
		}
		for (String i : strArr) {
			if (i.trim().length() == 0) throw new RuntimeException("В последней строке файла есть пустые переменные.") ;
			Slovo.checkSlovo(i.trim());
			GlobArrs.DefinedArray.add(i.trim());
		}
	}

}
