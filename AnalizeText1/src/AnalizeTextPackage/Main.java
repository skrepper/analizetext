package AnalizeTextPackage;

/* 1. функциональное тестирование всей программы. Юнит тестирование сключить 
 * 2. обработать все исключения
 * 3. учитывать меньшее или большее количество знаков & и | чем нужно
 * 4. приоритет && по сравнению ||
 * 5. уточнять то, что не сказано вначале
 * 6. проработать с учетом приоритетов выражения из множества операндов
 */

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
			System.out.println("¬ведите полное им¤ файла");
			return;
		} else {
			if (arg[0]==null || arg[0].length()==0) {
				System.out.println("¬ведите полное им¤ файла");
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
						// -> - символ EQ
						String[] strArr = strLine.split(CONSTANT.TokenEnum.EQ.getVal());
						// пара валидаций
						if (strArr.length !=2) {throw new RuntimeException("ќшибка валидации файла - неверное построение функции");}
						pattern = Pattern.compile(CONSTANT.GetAllTokensRegExpr()); 
						matcher = pattern.matcher(strArr[1]);
						if(matcher.find()){
							throw new RuntimeException("ќшибка валидации файла - в правой части операторы");
						}
						//все что справа от EQ - в массив неопределенных слов
						GlobArrs.NonDefinedArray.add(strArr[1].trim());
						String[] strArrLeft = strArr[0].split(CONSTANT.GetAllTokensRegExpr());
						//все что слева от EQ - в массив неопределенных слов
						for (String i:strArrLeft) GlobArrs.NonDefinedArray.add(i.trim());
						//бессмыслено, так как MakeAnaliz не работает с массивом определенных слов 
						//все, что определено, удалить из массива неопределенных слов
						//for (String i:GlobArrs.DefinedArray) GlobArrs.NonDefinedArray.remove(i);

						//анализ заново
						SomeExpressionArray stroka = new SomeExpressionArray();
						stroka.ops.add(new Slovo(strArr[0].trim()));
						stroka.ops.add(new Token(CONSTANT.TokenEnum.EQ.getVal()));
						stroka.ops.add(new Slovo(strArr[1].trim()));
						stroka.MakeAnaliz(); //превращаем строку в объекты выражений
						allExprArrays.AllStrArrays.add(stroka);
					}
				} else {
					//последн¤¤ строка файла
					String[] strArr = strLine.split(",");
					if (strArr.length<1) {throw new RuntimeException("ќшибка валидации файла");}
					for (String i:strArr) GlobArrs.DefinedArray.add(i.trim());
					break; // только одна строка после разделител¤!
				}
			}
			br.close(); //необ¤зательно, так как используетс¤ нова¤ конструкци¤ try () {}
		}catch (IOException e){
			System.out.println("ќшибка чтени¤ файла");
			System.exit(0);;
		}
		
		//в нижележащих функци¤х GetDefined дл¤ выражени¤ зашита булева логика
		//вышележаща¤ функци¤
		GlobArrs.tempChangedArrs = true;
		while (GlobArrs.tempChangedArrs) {
			GlobArrs.tempChangedArrs = false;
			//идем по строкам
			for (SomeExpressionArray str: allExprArrays.AllStrArrays) {
				str.GetDefined(); 
			}
		}
		
		System.out.println(String.join(", ", GlobArrs.DefinedArray));

	}
}


