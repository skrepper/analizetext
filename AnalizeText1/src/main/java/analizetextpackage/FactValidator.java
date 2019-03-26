package analizetextpackage;

import java.util.regex.Pattern;

public class FactValidator {

	public void checkToErrorsFact(String fact) {
		if (Pattern.compile("(&|\\||>)").matcher(fact).find()) {
			throw new RuntimeException("Ошибка валидации файла - в словах встречаются спецсимволы.");
		}
		if (Pattern.compile("^_\\d").matcher(fact).find()|Pattern.compile("^\\d").matcher(fact).find()) {
			throw new RuntimeException("В имени переменных встречаются цифры вначале");
		}
		if (fact.length()==0) {
			throw new RuntimeException("Пустое слово в выражении.");
		}
		if (Pattern.compile("\\s").matcher(fact).find()) {
			throw new RuntimeException("В имени переменных встречаются пробелы");
		}
	}

}
