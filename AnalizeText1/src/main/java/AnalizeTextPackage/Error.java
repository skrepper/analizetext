package AnalizeTextPackage;

public enum Error {
	  WRONG_RIGHT_OPERATOR(0, "������ ��������� ����� - � ������ ����� ���������."),
	  ENTER_FILE_NAME(1, "������� ��� �����."),
	  WRONG_READ_FILE(2, "������ ������ �����.");
	

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