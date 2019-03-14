package analizetextpackage;

import java.io.BufferedReader; 
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] arg) {
		String result;
		MainProc mainProc = new MainProc(); 
		result = mainProc.startMainpProc(arg);
		System.out.println(result);
	} 
}


