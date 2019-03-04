package AnalizeTextPackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	public static void main(String[] arg) {
		
		Pattern pattern;
		
		Matcher matcher;
		AllExpressionArrays allExprArrays = new AllExpressionArrays(); 
		
		String filePathName;
		if ((arg == null) || (arg.length == 0)) {
			System.out.println("������� ������ ��� �����");
			return;
		} else {
			if (arg[0]==null || arg[0].length()==0) {
				System.out.println("������� ������ ��� �����");
				return;
			} else {
				filePathName = arg[0];
			}
		}

		
		Boolean beforeDelim = true;
		try (	FileInputStream fstream = new FileInputStream(filePathName);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));) 
		{

			String strLine;
			while ((strLine = br.readLine()) != null){
				if (beforeDelim) {
					if (strLine.equals(CONSTANT.FILE_END_DELIMITER)) {
						beforeDelim = false;
					} else {
						// -> - ������ EQ
						String[] strArr = strLine.split(CONSTANT.TokenEnum.EQ.getVal());
						// ���� ���������
						if (strArr.length !=2) {throw new RuntimeException("������ ��������� ����� - �������� ���������� �������");}
						pattern = Pattern.compile(CONSTANT.GetAllTokensRegExpr()); 
						matcher = pattern.matcher(strArr[1]);
						if(matcher.find()){
							throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������");
						}
						//��� ��� ������ �� EQ - � ������ �������������� ����
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(CONSTANT.GetAllTokensRegExpr());
						//��� ��� ����� �� EQ - � ������ �������������� ����
						for (String i:strArrLeft) GlobArrs.NonDefinedArray.add(i.trim());
						//�����������, ��� ��� MakeAnaliz �� �������� � �������� ������������ ���� 
						//���, ��� ����������, ������� �� ������� �������������� ����
						//for (String i:GlobArrs.DefinedArray) GlobArrs.NonDefinedArray.remove(i);

						//������ ������
						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(CONSTANT.TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.MakeAnaliz(); //���������� ������ � ������� ���������
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					//��������� ������ �����
					String[] strArr = strLine.split(",");
					if (strArr.length<1) {throw new RuntimeException("������ ��������� �����");}
					for (String i:strArr) GlobArrs.DefinedArray.add(i.trim());
					break; // ������ ���� ������ ����� �����������!
				}
			}
			br.close(); //�������������, ��� ��� ������������ ����� ����������� try () {}
		}catch (IOException e){
			System.out.println("������ ������ �����");
			System.exit(0);;
		}
		
		//� ����������� �������� GetDefined ��� ��������� ������ ������ ������
		//����������� �������
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			//���� �� �������
			for (SomeExpressionArray str: allExprArrays.AllStrArrays) {
				str.GetDefined(); 
			}
		}
		
		System.out.println(String.join(", ", GlobArrs.DefinedArray));

	}
}


