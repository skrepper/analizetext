package analizetextpackage;

import static org.hamcrest.Matchers. *;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import analizetextpackage.Error;
import analizetextpackage.MainProc;

public class AnalizeTextTest {

	private static MainProc mainproc;
	private String textURL;
	private String actual;
	private String[] actualArray;

	@BeforeClass
	public static void initAnalize() {
		mainproc = new MainProc();
	}

	@Before
	public void beforeEachTest() {
		System.out.println("This is executed before each Test");
	}

	@After
	public void afterEachTest() {
		System.out.println("This is excecuted after each Test");
	}

	@Test
	public void testContent1() throws UnsupportedEncodingException {
		// проверка слов внизу и 1 простое определение
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_1.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actualArray = mainproc.startMainpProc(new String[] { textURL }).split(", ");
		assertThat(Arrays.asList(actualArray), containsInAnyOrder(Arrays.asList(equalTo("autumn"), equalTo("winter"), equalTo("rain"),
				equalTo("summer"), equalTo("not_in_upper_text"))));
		assertThat(actualArray.length, equalTo(5));
	}

	@Test
	public void testContent2() throws UnsupportedEncodingException {
		// проверка на ошибку - оператор справа
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_2.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(equalTo(Error.WRONG_RIGHT_OPERATOR.getDescription())));
	}

	@Test
	public void testContent3() throws UnsupportedEncodingException {
		// проверка на ошибку - оператор EQ справа
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_3.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(equalTo(Error.WRONG_FILE_VALIDATION3.getDescription())));
	}

	@Test
	public void testContent4() throws UnsupportedEncodingException {

		// проверка на ошибку - оператор EQ справа
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_4.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(equalTo(Error.WRONG_FILE_VALIDATION2.getDescription())));
	}

	@Test
	public void testContent5() throws UnsupportedEncodingException {

		// проверка слов на спецсимволы
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_5.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(equalTo(Error.WRONG_SPECIAL_SYMBOL.getDescription())));
	}

	@Test
	public void testContent6() throws UnsupportedEncodingException {

		// проверка слов на пустоту
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_6.txt").toString(), "UTF-8")
				.replace("file:/", "");
		actual = mainproc.startMainpProc(new String[] { textURL }); 
		assertThat(actual, allOf(equalTo(Error.EMPTY_SLOVO.getDescription())));
	}

	@Test
	public void testContent7() throws UnsupportedEncodingException {

		// проверка на правильную длину разделителя в 64 черточки
		textURL = getUrl("../func_text_7.txt");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(containsString(Error.WRONG_FILE_VALIDATION1.getDescription())));
	}

	@Test
	public void testContent8() throws UnsupportedEncodingException {

		// проверка на пробелы
		textURL = getUrl("../func_text_8.txt");
		actual = mainproc.startMainpProc(new String[] { textURL });
		assertThat(actual, allOf(containsString("В имени переменных встречаются пробелы")));
	}
	
	
	@Test
	public void testArgs() {
		// на отсутствие имени файла
		String actual = mainproc.startMainpProc(new String[] { "" });
		assertThat(actual, anyOf(containsString(Error.ENTER_FILE_NAME.getDescription())));
		// проверка на ошибку в имени файла
		actual = mainproc.startMainpProc(new String[] { "wrong name" });
		assertThat(actual, anyOf(containsString(Error.WRONG_READ_FILE.getDescription())));
	}
	
	private String getUrl(String relativeNameOfFile) throws UnsupportedEncodingException {
		return URLDecoder.decode(this.getClass().getResource(relativeNameOfFile).toString(), "UTF-8").replace("file:/", "");
//		return this.getClass().getResource(relativeNameOfFile).toString().replace("file:/", ""); непонятно почему так не работает
	}
}
