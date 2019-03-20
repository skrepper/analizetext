package analizetextpackage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.RuntimeErrorException;

public class Main {

	public static void main(String[] arg) throws IOException {
		Set<String> knownFacts = new HashSet<String>();
		Set<String> unknownFacts = new HashSet<String>();
		ArrayList<RuleAnalysis> allRules = new ArrayList<RuleAnalysis>();
		RuleParsing parser = new RuleParsing(knownFacts, unknownFacts, allRules);

		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));

		String filePathName;

		if (arg.length == 0) {
			System.err.print("Введите имя файла.");
			return;
		}
		filePathName = arg[0];
		boolean beforeDelimiter = true;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {

			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (beforeDelimiter) {
					if (strLine.equals(FILE_END_DELIMITER)) {
						beforeDelimiter = false;
					} else {
						parser.parseRule(strLine);
					}
				} else {
					if (knownFacts.size() > 0)
						throw new RuntimeException("В конце файла после разделителя много строк.");
					parser.parseKnownFacts(strLine);
				}
			}
			br.close(); // необязательно

			boolean knownFactsNotChanged; 
			do  {
				knownFactsNotChanged = true;
				for (RuleAnalysis str : allRules) { 
					knownFactsNotChanged = knownFactsNotChanged && str.checkAllExpressionsInLineDeduced();
				}
			} while (!knownFactsNotChanged);
			System.out.print(String.join(", ", knownFacts));
		} catch (IOException e) {
			System.err.print("Файл не найден");
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
