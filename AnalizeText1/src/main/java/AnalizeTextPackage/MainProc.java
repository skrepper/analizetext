package AnalizeTextPackage; 

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainProc {

	public String startmainproc(String[] arg) {

		String result; 
		Pattern pattern;
		Matcher matcher;
		AllExpressionArrays allExprArrays = new AllExpressionArrays(); 

		String filePathName;
		if ((arg == null) || (arg.length == 0)) {
			result = Error.ENTER_FILE_NAME.getDescription();
			return result;
		} else {
			if (arg[0]==null || arg[0].length()==0) {
				result = Error.ENTER_FILE_NAME.getDescription();
				return result;
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
						String[] strArr = strLine.split(TokenEnum.EQ.getVal());
						if (strArr.length !=2) {throw new RuntimeException(Error.WRONG_FILE_VALIDATION1.getDescription());}
						pattern = Pattern.compile(CONSTANT.GetAllTokensRegExpr()); 
						matcher = pattern.matcher(strArr[1]);
						if(matcher.find()){
							result = Error.WRONG_RIGHT_OPERATOR.getDescription();
							return result;
						}
						//��� ��� ������ �� EQ - � ������ �������������� ����
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(CONSTANT.GetAllTokensRegExpr());
						//��� ��� ����� �� EQ - � ������ �������������� ����
						for (String i:strArrLeft) GlobArrs.NonDefinedArray.add(i.trim());

						//������ ������
						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.MakeAnaliz(); //���������� ������ � ������� ���������
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					//��������� ������ �����
					String[] strArr = strLine.split(",");
					if (strLine.length()<1) {throw new RuntimeException(Error.WRONG_FILE_VALIDATION2.getDescription());}
					for (String i:strArr) GlobArrs.DefinedArray.add(i.trim());
					break; // ������ ���� ������ ����� �����������!
				}
			}
			br.close(); //�������������, ��� ��� ������������ ����� ����������� try () {}
		}catch (IOException e){
			result = Error.WRONG_READ_FILE.getDescription();
			return result;
		}catch (RuntimeException e){
			result = e.getMessage();
			return result;
		}

		//� ����������� �������� GetDefined ��� ��������� ������ ������ ������
		//����������� �������
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			//���� �� �������
			for (SomeExpressionArray str: allExprArrays.AllStrArrays) {
				try {
					str.GetDefined();
				} catch (RuntimeException e){
					result = e.getMessage();
					return result;
				}
			}
		}

		result = String.join(", ", GlobArrs.DefinedArray);
		return result;

	}	

}
