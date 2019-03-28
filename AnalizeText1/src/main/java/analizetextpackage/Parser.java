package analizetextpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {

	private String filePathName;
	private ArrayList<Rule> allRules;
	private Set<String> deducedFacts;
	private final String allRegExps = String.join("|",
			EnumSet.allOf(OperationToken.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())); // �������

	public Parser(String[] arg) {
		filePathName = arg[0];
		allRules = new ArrayList<>();
		deducedFacts = new HashSet<String>(); 
	}

	public Model parseFile() throws FileNotFoundException, IOException {
		String[] strArr;
		final String FILE_END_DELIMITER = String.join("",
				IntStream.range(0, 64).mapToObj(i -> "-").collect(Collectors.toList()));
		FilePositionState parsingState = FilePositionState.RULE;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePathName)))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				// ���������� ���������
				switch (parsingState) {
				case RULE:
					if (strLine.equals(FILE_END_DELIMITER)) {
						parsingState = FilePositionState.DELIMITER;
					} else {
						parsingState = FilePositionState.RULE;
						strArr = strLine.split(OperationToken.EQ.getVal());
						validateRuleLine(strArr, allRegExps);
						allRules.add(build(parse(strArr))); // ������� � ���������� ���������
					}
					break;
				case KNOWN_FACTS:
					throw new RuntimeException("������ ��������� �������� ����� ������.");
				case DELIMITER:
					validateKnownFactsLine(strLine);
					strArr = strLine.split(",", -1);
					for (String i : strArr) {
						deducedFacts.add(i.trim());
					}
					parsingState = FilePositionState.KNOWN_FACTS;
					break;
				default:
					throw new RuntimeException("������ ��������� �������� ����� ������.");
				}
			}
			br.close();
			return new Model(allRules, deducedFacts);
		} catch (IOException e) {
			throw new RuntimeException("���� �� ������");
		}
	}
	
	
	public Rule parse(String[] strArr) {
		Rule rule = new Rule(); 
		rule.addLexema(new FactExpression(strArr[0].trim()));
		rule.addLexema(new OperationLexema(OperationToken.EQ.getVal()));
		rule.addLexema(new FactExpression(strArr[1].trim()));

		final String allOperationsRegularExpression = "(" + String.join("|",
				EnumSet.allOf(OperationToken.class).stream().map(i -> i.getRegExp()).collect(Collectors.toList())) + ")";
		String[] leftFacts = splitPreserveDelimiter(strArr[0].trim(), allOperationsRegularExpression);
		int indexOfAdd = -1; // ����� ������� ������� ���� ��������� ����� ����� ��� ��������
		AnalisysRuleState analisysRuleState = AnalisysRuleState.BEFORE_ANALISYS;
		boolean isOperation; 
		for (String i : leftFacts) {
			isOperation = Pattern.compile(allOperationsRegularExpression).matcher(i).matches();
			switch (analisysRuleState) {
			case BEFORE_ANALISYS:
				if (isOperation) {
					throw new RuntimeException("� ������ ���� � ����� ����� ��������� ����� ���������.");
				} else {
					analisysRuleState = AnalisysRuleState.FIRST_OPERAND;
					checkToErrorsFact(i.trim());
					rule.setIndexLexema(++indexOfAdd, new FactExpression(i.trim()));
				}
				break;
			case FIRST_OPERAND:
				if (!isOperation) {
					throw new RuntimeException("����������� ��������� - ��� �������� ������ ����� split.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERATION;
					rule.addIndexLexema(++indexOfAdd, new OperationLexema(i.trim()));
				}
				break;
			case OPERATION:
				if (isOperation) {
					throw new RuntimeException("� ����� ����� ��������� ����� 2 ��������� ������.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERAND;
					checkToErrorsFact(i.trim());
					rule.addIndexLexema(++indexOfAdd, new FactExpression(i.trim()));
				}
				break;
			case OPERAND:
				if (isOperation) {
					throw new RuntimeException("����������� ��������� - ��� �������� ������ ����� split.");
				} else {
					analisysRuleState = AnalisysRuleState.OPERATION;
					rule.addIndexLexema(++indexOfAdd, new OperationLexema(i.trim()));
				}
				break;
			default:
				throw new RuntimeException("����������� ���������");
			}
		}
		if (analisysRuleState == AnalisysRuleState.OPERATION)
			throw new RuntimeException("� ����� ����� ������ ��������.");
		
		return rule;

	}

	public Rule build(Rule rule) {
		
		final Map<OperationToken, Integer> OperationEnumPriority = 
				EnumSet.allOf(OperationToken.class).stream()
				.collect(Collectors.toMap(enumVal -> enumVal, enumVal -> enumVal.getPriority()));
		for (Integer i : OperationEnumPriority.values().stream().sorted(Comparator.reverseOrder()).distinct()
				.collect(Collectors.toList())) {
			boolean foundOperation = true;
			while (foundOperation) { 
				for (int j = 0; j < rule.getAllLexems().size() - 2; j++) {  
					if ((rule.getIndexLexema(j) instanceof OperationLexema) 
							&& OperationEnumPriority.get(((OperationLexema) rule.getIndexLexema(j)).getOperation()) == i) {  
						foundOperation = true;
						if (j > 0 && (rule.getIndexLexema(j - 1) instanceof Expression)  
								&& j < (rule.getAllLexems().size() - 1) && (rule.getIndexLexema(j + 1) instanceof Expression)) { 
							TwoOperandsExpression expression = new TwoOperandsExpression(
									(Expression) rule.getIndexLexema(j - 1),
									(Expression) rule.getIndexLexema(j + 1),
									(OperationLexema) rule.getIndexLexema(j)
									);
							
							rule.setIndexLexema(j, expression);
							Set<Lexema> remove = new HashSet<Lexema>();
							remove.add(rule.getIndexLexema(j - 1));
							remove.add(rule.getIndexLexema(j + 1));
							rule.removeAll(remove);  
						}
						break;
					} else {
						foundOperation = false;
					}
				}
			}
		}
		
		return rule;
	}

	
	/*
	 * ������� split � �������������
	 */

	public String[] splitPreserveDelimiter(String data, String regexp) {
		LinkedList<String> splitted = new LinkedList<String>();
		int last_match = 0;
		Matcher m = Pattern.compile(regexp).matcher(data);
		while (m.find()) {
			if (last_match < m.start())
				splitted.add(data.substring(last_match, m.start()));
			splitted.add(m.group());
			last_match = m.end();
		}
		if (last_match < data.length())
			splitted.add(data.substring(last_match));
		return splitted.toArray(new String[splitted.size()]);
	}

	public void validateRuleLine(String[] strArr, String allRegExps) {
		if (strArr.length < 2) {
			throw new RuntimeException("������ ��������� ����� - �������� ���������� �������.");
		}
		if (strArr.length > 2) {
			throw new RuntimeException("������ ��������� ����� - ������� ����� ->.");
		}
		if (strArr[1].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� �����.");
		}
		if (strArr[0].trim().length() == 0) {
			throw new RuntimeException("������ ��������� ����� - � ����� ����� �����.");
		}
		if (Pattern.compile(allRegExps).matcher(strArr[1]).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����� ���������.");
		}
	}

	public void validateKnownFactsLine(String strLine) {
		if (strLine.length() < 1) {
			throw new RuntimeException("������ ��������� ����� - �������� ������ ������.");
		}
		
		for (String i : strLine.split(",", -1)) {
			if (i.trim().length() == 0) throw new RuntimeException("� ������ ������ ���� ������ ����������.");
			checkToErrorsFact(i.trim());
		}
	}

	public void checkToErrorsFact(String factToken) {
		if (Pattern.compile("(&|\\||>)").matcher(factToken).find()) {
			throw new RuntimeException("������ ��������� ����� - � ������ ����������� �����������.");
		}
		if (Pattern.compile("^_\\d").matcher(factToken).find()|Pattern.compile("^\\d").matcher(factToken).find()) {
			throw new RuntimeException("� ����� ���������� ����������� ����� �������");
		}
		if (factToken.length()==0) {
			throw new RuntimeException("������ ����� � ���������.");
		}
		if (Pattern.compile("\\s").matcher(factToken).find()) {
			throw new RuntimeException("� ����� ���������� ����������� �������");
		}
	}

}
