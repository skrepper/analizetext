package AnalizeTextPackage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AnalizeTextTest {
	
	private static MainProc mainproc;
	
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
	public void testContent() throws UnsupportedEncodingException {
		
		String textURL;
		String actual;
		
		
		//проверка слов внизу и 1 простое определение
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_1.txt").toString(), "UTF-8").replace("file:/", "");
		actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, allOf(containsString("autumn"), containsString("winter"),
				containsString("rain"), containsString("summer"), containsString("not_in_upper_text")));

		//проверка на ошибку - оператор справа
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_2.txt").toString(), "UTF-8").replace("file:/", "");
		actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, allOf(containsString(Error.WRONG_RIGHT_OPERATOR.getDescription())));

		//проверка на ошибку - оператор EQ справа 
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_3.txt").toString(), "UTF-8").replace("file:/", "");
		actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, allOf(containsString(Error.WRONG_FILE_VALIDATION1.getDescription())));

		//проверка на ошибку - оператор EQ справа
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_4.txt").toString(), "UTF-8").replace("file:/", "");
		actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, allOf(containsString(Error.WRONG_FILE_VALIDATION2.getDescription())));

		//проверка слов на спецсимволы
		textURL = URLDecoder.decode(this.getClass().getResource("../func_text_5.txt").toString(), "UTF-8").replace("file:/", "");
		actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, allOf(containsString(Error.WRONG_SPECIAL_SYMBOL.getDescription())));
		
		
	}
	
	@Test
	public void testArgs() {
		//проверка на отсутствие имени файла
		String actual = mainproc.startmainproc(new String[] {""});
		assertThat(actual, anyOf(containsString(Error.ENTER_FILE_NAME.getDescription())));
		//проверка на ошибку в имени файла
		actual = mainproc.startmainproc(new String[] {"wrong name"});
		assertThat(actual, anyOf(containsString(Error.WRONG_READ_FILE.getDescription())));
	}
}

