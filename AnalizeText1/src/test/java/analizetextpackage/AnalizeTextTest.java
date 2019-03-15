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

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
	
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    
	@Test
	public void testContent1() throws IOException {
		// проверка слов внизу и 1 простое определение
		textURL = getUrl("../func_text_1.txt");
		
		Main.main(new String[] { textURL });
		actualArray = getOutput().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(
								equalTo("autumn"), 
						        equalTo("winter"), 
						        equalTo("rain"),
				                equalTo("summer"))));
		assertThat(actualArray.length, equalTo(4)); 
	}

	@Test(expected = RuntimeException.class)
	public void testContent2() throws IOException {
		// проверка на ошибку - оператор справа
		textURL = getUrl("../func_text_2.txt");
		Main.main(new String[] { textURL });
	}

	@Test(expected = RuntimeException.class)
	public void testContent3() throws IOException {
		// проверка на ошибку - оператор EQ справа
		textURL = getUrl("../func_text_3.txt");
		Main.main(new String[] { textURL });
	}

	@Test(expected = RuntimeException.class)
	public void testContent4() throws IOException {

		// проверка на ошибку - оператор EQ справа
		textURL = getUrl("../func_text_4.txt");
		Main.main(new String[] { textURL });
	}

	@Test(expected = RuntimeException.class)
	public void testContent5() throws IOException {

		// проверка слов на спецсимволы
		textURL = getUrl("../func_text_5.txt");
		Main.main(new String[] { textURL });
	}

	@Test(expected = RuntimeException.class)
	public void testContent6() throws IOException {

		// проверка слов на пустоту
		textURL = getUrl("../func_text_6.txt");
		Main.main(new String[] { textURL }); 
	}

	@Test(expected = RuntimeException.class)
	public void testContent7() throws IOException {

		// проверка на правильную длину разделителя в 64 черточки
		textURL = getUrl("../func_text_7.txt");
		Main.main(new String[] { textURL });
	}

	@Test(expected = RuntimeException.class)
	public void testContent8() throws IOException {

		// проверка на пробелы
		textURL = getUrl("../func_text_8.txt");
		Main.main(new String[] { textURL });
	}
	

	@Test(expected = RuntimeException.class)
	public void testContent9() throws IOException {

		// проверка на пробелы
		textURL = getUrl("../func_text_9.txt");
		Main.main(new String[] { textURL });
	}
	
	
	@Test(expected = RuntimeException.class)
	public void testArgs1() throws IOException {
		// на отсутствие имени файла
		Main.main(new String[] { "" });
	}
	
	@Test(expected = IOException.class)
	public void testArgs2() throws IOException {
		// проверка на ошибку в имени файла
		Main.main(new String[] { "wrong name" });
	}
	
	private String getUrl(String relativeNameOfFile) throws UnsupportedEncodingException {
		return URLDecoder.decode(this.getClass().getResource(relativeNameOfFile).toString(), "UTF-8").replace("file:/", "");
	}
	
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }
    
}
