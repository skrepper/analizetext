package AnalizeTextPackage;

public enum Error {
	  WRONG_RIGHT_OPERATOR(0, "Ошибка валидации файла - в правой части операторы."),
	  ENTER_FILE_NAME(1, "Введите имя файла."),
	  WRONG_READ_FILE(2, "Ошибка чтения файла.");
	

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