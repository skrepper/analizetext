package AnalizeTextPackage;

/* 1. �������������� ������������ ���� ���������. ���� ������������ ��������� 
 * 2. ���������� ��� ����������
 * 3. ��������� ������� ��� ������� ���������� ������ & � | ��� �����
 * 4. ��������� && �� ��������� ||
 * 5. ����������� ��������� �� ��������� ���������
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


