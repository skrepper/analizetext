package analizetextpackage;

public class ParserFactory {

	public FileParser createParser(String typeFile) {
		if (typeFile.equals("xml")) return new XmlFileParser();
		if (typeFile.equals("txt")) return new TextFileParser();
		return null;
	}
}

