package analizetextpackage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnalizeTextTest {

	private String[] actualArray;

    private final PrintStream systemOut = System.out, systemErr = System.err;

    private ByteArrayOutputStream testOut, errOut;
	
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        errOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setErr(new PrintStream(errOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
        System.setErr(systemErr);
    }
    
	@Test
	public void testContent1() throws IOException {
		//простая проверка
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
		Main.main(new String[] { "target/test-classes/func_text_2.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent3() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_3.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent4() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_4.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent5() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_5.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent6() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_6.txt" }); 
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent7() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_7.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent8() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_8.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	

	@Test
	public void testContent9() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_9.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	

	@Test
	public void testContent10() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_10.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	
	@Test
	public void testContent11() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_11.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	
	@Test
	public void testContent12() throws IOException {
		//проверка на повторное построение выражений
		Main.main(new String[] { "target/test-classes/func_text_12.txt" });
		actualArray = testOut.toString().split(", ");
		assertThat(Arrays.asList(actualArray), 
				containsInAnyOrder(
						Arrays.asList(
								equalTo("autumn"), 
						        equalTo("winter"), 
						        equalTo("tornado"), 
						        equalTo("water"), 
						        equalTo("rain"),
				                equalTo("summer"))));
		assertThat(actualArray.length, equalTo(6)); 
	}
	
	@Test
	public void testContent13() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_13.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	
	@Test
	public void testContent14() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_14.txt" });
		assertThat(errOut.toString(), equalTo("Ошибка структуры входного файла данных."));
	}
	
	@Test
	public void testContent15() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_15.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent16() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_16.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent17() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_17.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent18() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_18.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testContent19() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_19.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}
	@Test
	public void testContent20() throws IOException {
		Main.main(new String[] { "target/test-classes/func_text_20.txt" });
		assertThat(errOut.toString(), startsWith("Неверное имя факта."));
	}

	@Test
	public void testArgs1() throws IOException {
		Main.main(new String[] {  });
		assertThat(errOut.toString(), equalTo("Введите имя файла."));
	}
	
	@Test
	public void testArgs2() throws IOException {
		Main.main(new String[] { "wrong name" });
		assertThat(errOut.toString(), startsWith("Ошибка чтения файла."));
	}
	
}
