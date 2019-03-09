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
	public void testSum() throws UnsupportedEncodingException {
		String textURL = URLDecoder.decode(this.getClass().getResource("../func_text.txt").toString(), "UTF-8").replace("file:/", "");
		String actual = mainproc.startmainproc(new String[] {textURL});
		assertThat(actual, anyOf(containsString("autumn"), containsString("winter")));
	}
}

