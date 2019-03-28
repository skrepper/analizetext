package analizetextpackage;

import java.util.ArrayList;
import java.util.Set;

public class Rule {

	private ArrayList<Lexema> lexems;
	
	public ArrayList<Lexema> getAllLexems() {
		return lexems;
	}

	public void setAllLexems(ArrayList<Lexema> allLexems) {
		this.lexems = allLexems;
	}

	
	public Rule() {
		this.lexems = new ArrayList<Lexema>();
	}

	/*
	 * ������� ���������� true, ���� �� ���� ���������� � ������.
	 * ���� �� ���� ���� �� ���� ���������� � ������, �� ����������� false
	 */
	public boolean calculate(Set<String> deducedFacts) {
		boolean result = true;
		for (Lexema i : lexems) {
			if (!i.getDefined()) {
				if (i.calculateLexema(deducedFacts)) {
					// ���� ����� ������� ���� defined ���� false, � ����� true, �� ���� ��������� ����������
					deducedFacts.add(((FactExpression) lexems.get(lexems.size() - 1)).getFact());
					result = false;
				}
			}
		}
		return result;
	}
	
	public void addLexema(Lexema lexema) {
		lexems.add(lexema);
	}

	public void addIndexLexema(int i, Lexema lexema) {
		lexems.add(i, lexema);
	}

	public Lexema getIndexLexema(int i) {
		return lexems.get(i);
	}

	public Lexema setIndexLexema(int i, Lexema lexema) {
		return lexems.set(i, lexema);
	}
	
	public void removeAll(Set<Lexema> remove) {
		lexems.removeAll(remove);
	}

}
