package analizetextpackage;

public enum ParseFileStateEnum {
	FACTS,
	DELIMITER,
	KNOWN_FACTS;
	
	public ParseFileStateEnum nextState(boolean delimiter) {
		switch (this) {
		case FACTS:
			return delimiter?DELIMITER:FACTS;
		case DELIMITER:
			return KNOWN_FACTS; 
		case KNOWN_FACTS:
			throw new RuntimeException("� ����� ����� ����� ����������� ����� �����.");
		default:
			throw new RuntimeException("����������� ���������");
		}
	}
}