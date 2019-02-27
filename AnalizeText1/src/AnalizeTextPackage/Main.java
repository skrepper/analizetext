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
			System.out.println("Введите полное имя файла");
			return;
		} else {
			if (arg[0]==null || arg[0].length()==0) {
				System.out.println("Введите полное имя файла");
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
						// ->
						String[] strArr = strLine.split(CONSTANT.TokenEnum.EQ.getVal());
						if (strArr.length !=2) {throw new RuntimeException("Ошибка валидации файла - неверное построение функции");}
						pattern = Pattern.compile(CONSTANT.GetAllTokensRegExpr()); 
						matcher = pattern.matcher(strArr[1]);
						if(matcher.find()){
							throw new RuntimeException("Ошибка валидации файла - в правой части операторы");
						}
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(CONSTANT.GetAllTokensRegExpr());
						for (String i:strArrLeft) GlobArrs.NonDefinedArray.add(i.trim());
						for (String i:GlobArrs.DefinedArray) GlobArrs.NonDefinedArray.remove(i);

						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(CONSTANT.TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.MakeAnaliz();
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					//последняя строка файла
					String[] strArr = strLine.split(",");
					if (strArr.length<1) {throw new RuntimeException("Ошибка валидации файла");}
					for (String i:strArr) GlobArrs.DefinedArray.add(i.trim());
					break; // только одна строка
				}
			}
			br.close(); //необязательно, так как используется новая конструкция try () {}
		}catch (IOException e){
			System.out.println("Ошибка чтения файла");
			System.exit(0);;
		}
		
		
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			for (SomeExpressionArray str: allExprArrays.AllStrArrays) {
				str.GetDefined();
			}
		}
		
		System.out.println(String.join(", ", GlobArrs.DefinedArray));

	}
}


