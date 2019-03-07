package AnalizeTextPackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
	public void testSum() {
		String textURL = getClass().getResource("func_text.txt").toString();
		String result = mainproc.startmainproc(new String[] {textURL}) ;
		System.out.println("sssss");
		assertEquals("sss", result);
	}
}

