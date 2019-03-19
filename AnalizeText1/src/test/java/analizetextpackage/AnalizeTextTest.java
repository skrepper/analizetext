package analizetextpackage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnalizeTextTest {

	private String textURL;
	private String[] actualArray;

    private final PrintStream systemOut = System.out, systemErr = System.err;

    private ByteArrayOutputStream testOut, errOut;
	
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
//        errOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setErr(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
        System.setErr(systemErr);
    }
    
	@Test
	public void testContent1() throws IOException {
		// проверка слов внизу и 1 простое определение
		Main.main(new String[] { "target/test-classes/func_text_1.txt" });
		actualArray = testOut.toString().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(
								equalTo("autumn"), 
						        equalTo("winter"), 
						        equalTo("rain"),
				                equalTo("summer"))));
		assertThat(actualArray.length, equalTo(4)); 
	}

	@Test
	public void testContent2() throws IOException {
		// проверка на ошибку - оператор справа
		Main.main(new String[] { "target/test-classes/func_text_2.txt" });
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - в правой части операторы."));
	}

	@Test
	public void testContent3() throws IOException {
		// проверка на ошибку - оператор EQ справа
		Main.main(new String[] { "target/test-classes/func_text_3.txt" });
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - в правой части операторы."));
	}

	@Test
	public void testContent4() throws IOException {

		// проверка на ошибку - оператор EQ справа
		Main.main(new String[] { "target/test-classes/func_text_4.txt" });
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - неверная строка в конце файла."));
	}

	@Test
	public void testContent5() throws IOException {

		// проверка слов на спецсимволы
		Main.main(new String[] { "target/test-classes/func_text_5.txt" });
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - в словах встречаются спецсимволы."));
	}

	@Test
	public void testContent6() throws IOException {

		// проверка слов на пустоту
		Main.main(new String[] { "target/test-classes/func_text_6.txt" }); 
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - в правой части пусто."));
	}

	@Test
	public void testContent7() throws IOException {

		// проверка на правильную длину разделителя в 64 черточки
		Main.main(new String[] { "target/test-classes/func_text_7.txt" });
		assertThat(testOut.toString(), equalTo("Ошибка валидации файла - неверное построение функции."));
	}

	@Test
	public void testContent8() throws IOException {

		// проверка на пробелы вверху
		Main.main(new String[] { "target/test-classes/func_text_8.txt" });
		assertThat(testOut.toString(), equalTo("В имени переменных встречаются пробелы"));
	}
	

	@Test
	public void testContent9() throws IOException {

		// проверка на пробелы внизу
		Main.main(new String[] { "target/test-classes/func_text_9.txt" });
		assertThat(testOut.toString(), equalTo("В имени переменных встречаются пробелы"));
	}
	

	@Test
	public void testContent10() throws IOException {

		// проверка на пробелы внизу
		Main.main(new String[] { "target/test-classes/func_text_10.txt" });
		assertThat(testOut.toString(), equalTo("В левой части выражения стоят 2 оператора подряд."));
	}
	
/*	@Test
	public void testContent11() throws IOException {

		// проверка на пробелы внизу
		Main.main(new String[] { "target/test-classes/func_text_11.txt" });
		assertThat(testOut.toString(), equalTo("В последней строке файла есть пустые переменные."));
	}*/
	
	
	@Test
	public void testArgs1() throws IOException {
		// на отсутствие имени файла
		Main.main(new String[] {  });
		assertThat(testOut.toString(), equalTo("Введите имя файла."));
	}
	
	@Test
	public void testArgs2() throws IOException {
		// проверка на ошибку в имени файла
		Main.main(new String[] { "wrong name" });
		assertThat(testOut.toString(), equalTo("Файл не найден"));
	}
	
}
