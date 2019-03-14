package analizetextpackage;

public enum Error {  
	  WRONG_RIGHT_OPERATOR(0, "������ ��������� ����� - � ������ ����� ���������."),
	  ENTER_FILE_NAME(1, "������� ��� �����."),
	  WRONG_READ_FILE(2, "������ ������ �����."),
	  WRONG_FILE_VALIDATION1(3, "������ ��������� ����� - �������� ���������� �������."),
	  WRONG_FILE_VALIDATION2(4, "������ ��������� ����� - �������� ������ � ����� �����."),
	  WRONG_SPECIAL_SYMBOL(5, "������ ��������� ����� - � ������ ����������� �����������."),
	  EMPTY_SLOVO(6, "������ ����� � ���������."),
	  WRONG_FILE_VALIDATION3(7, "������ ��������� ����� - ������� ����� ->."),
	  ;
	

	  private final int code;
	  private final String description;

	  private Error(int code, String description) {
	    this.code = code;
	    this.description = description;
	  }

	  public String getDescription() {
	     return description; 
	  }

	  public int getCode() {
	     return code;
	  }

	  @Override
	  public String toString() {
	    return code + ": " + description;
	  }
	}