package AnalizeTextPackage;

public enum Error {  
	  WRONG_RIGHT_OPERATOR(0, "ќшибка валидации файла - в правой части операторы."),
	  ENTER_FILE_NAME(1, "¬ведите им€ файла."),
	  WRONG_READ_FILE(2, "ќшибка чтени€ файла."),
	  WRONG_FILE_VALIDATION1(3, "ќшибка валидации файла - неверное построение функции."),
	  WRONG_FILE_VALIDATION2(4, "ќшибка валидации файла - неверная строка в конце файла."),
	  WRONG_SPECIAL_SYMBOL(5, "ќшибка валидации файла - в словах встречаютс€ спецсимволы."),
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