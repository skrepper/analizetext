package AnalizeTextPackage;

/* 1. функциональное тестирование всей программы. Юнит тестирование исключить 
 * 2. обработать все исключения
 * 3. учитывать меньшее или большее количество знаков & и | чем нужно
 * 4. приоритет && по сравнению ||
 * 5. проработать выражения из множества операндов
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] arg) {
		String result;
		MainProc mainproc = new MainProc(); 
		result = mainproc.startmainproc(arg);
		System.out.println(result);
	}
}


