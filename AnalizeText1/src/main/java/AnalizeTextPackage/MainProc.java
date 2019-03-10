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
						// -> - символ EQ
						String[] strArr = strLine.split(TokenEnum.EQ.getVal());
// Нельзя оставлять throw необработанным 
						if (strArr.length !=2) {throw new RuntimeException("Ошибка валидации файла - неверное построение функции");}
						pattern = Pattern.compile(CONSTANT.GetAllTokensRegExpr()); 
						matcher = pattern.matcher(strArr[1]);
						if(matcher.find()){
							result = Error.WRONG_RIGHT_OPERATOR.getDescription();
							return result;
						}
						//все что справа от EQ - в массив неопределенных слов
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(CONSTANT.GetAllTokensRegExpr());
						//все что слева от EQ - в массив неопределенных слов
						for (String i:strArrLeft) GlobArrs.NonDefinedArray.add(i.trim());

						//анализ заново
						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.MakeAnaliz(); //превращаем строку в объекты выражений
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					//последняя строка файла
					String[] strArr = strLine.split(",");
// Нельзя оставлять throw необработанным 
					if (strArr.length<1) {throw new RuntimeException("Ошибка валидации файла");}
					for (String i:strArr) GlobArrs.DefinedArray.add(i.trim());
					break; // только одна строка после разделителя!
				}
			}
			br.close(); //необязательно, так как используется новая конструкция try () {}
		}catch (IOException e){
			result = Error.WRONG_READ_FILE.getDescription();
			return result;
		}
		
		//в нижележащих функциях GetDefined для выражения зашита булева логика
		//вышележащая функция
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			//идем по строкам
			for (SomeExpressionArray str: allExprArrays.AllStrArrays) {
				str.GetDefined(); 
			}
		}
		
		result = String.join(", ", GlobArrs.DefinedArray);
		return result;

	}	
	
}
