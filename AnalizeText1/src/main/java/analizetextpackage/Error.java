package analizetextpackage;

public enum Error {  
	  WRONG_RIGHT_OPERATOR(0, "Ошибка валидации файла - в правой части операторы."),
	  ENTER_FILE_NAME(1, "Введите имя файла."),
	  WRONG_READ_FILE(2, "Ошибка чтения файла."),
	  WRONG_FILE_VALIDATION1(3, "Ошибка валидации файла - неверное построение функции."),
	  WRONG_FILE_VALIDATION2(4, "Ошибка валидации файла - неверная строка в конце файла."),
	  WRONG_SPECIAL_SYMBOL(5, "Ошибка валидации файла - в словах встречаются спецсимволы."),
	  EMPTY_SLOVO(6, "Пустое слово в выражении."),
	  WRONG_FILE_VALIDATION3(7, "Ошибка валидации файла - слишком много ->."),
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