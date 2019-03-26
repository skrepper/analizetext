package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] arg) throws IOException {
		Set<String> deducedFacts = new HashSet<String>();
		Set<String> unknownFacts = new HashSet<String>();
		ArrayList<ArrayList<Lexema>> allRules = new ArrayList();
		RuleFactory ruleFactory = new RuleFactory(deducedFacts, unknownFacts, allRules);
		KnownFactsParser knownFactsParser = new KnownFactsParser(deducedFacts);

		if (arg.length == 0) {
			System.err.print("������� ��� �����.");
			return;
		}
		String filePathName = arg[0];

		FilePositionState parsingState = FilePositionState.FACTS;
		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				//���������� ���������
				switch (parsingState) {
				case FACTS: 
					parsingState = strLine.equals(FILE_END_DELIMITER)?FilePositionState.DELIMITER:FilePositionState.FACTS;
					break;
				case KNOWN_FACTS: 
					parsingState = FilePositionState.ERROR;
					break;
				case DELIMITER:
					parsingState = FilePositionState.KNOWN_FACTS;
					break;
				case ERROR:
					System.err.print("������ ��������� �������� ����� ������.");
					return;
				default:
					System.err.print("������ ��������� �������� ����� ������.");
					return;
				}
				
				switch (parsingState) {
				case FACTS: 
					ruleFactory.addRule(strLine); //������� � ���������� ���������
					break;
				case KNOWN_FACTS: 
					knownFactsParser.parseKnownFacts(strLine); //������� � �����
					parsingState = FilePositionState.ERROR;
					break;
				default:
					break;
				}
				
			}
			br.close(); //����� �������� �����
			
			KnownFactsFactory knownFactFactory = new KnownFactsFactory();
			knownFactFactory.deduceKnownFacts(deducedFacts, unknownFacts, allRules);
			
			System.out.print(String.join(", ", deducedFacts));

		} catch (IOException e) {
			System.err.print("���� �� ������");
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
