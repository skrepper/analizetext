package analizetextpackage;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) {

		CommandLine line = null;
		String modelFileName = null;
		String typeFile = null;
		CommandLineParser cliParser = new DefaultParser();
		Options options = new Options();
		Option typeOption = new Option("t", "type", true, "Type of file");
		Option fileOption = new Option("f", "file", true, "Input file");
		fileOption.setRequired(true);
		typeOption.setRequired(false);
		options.addOption(fileOption);
		options.addOption(typeOption);

		try {
			line = cliParser.parse(options, args);
			
			modelFileName = line.getOptionValue("file");
			if (line.hasOption("type")) {typeFile = line.getOptionValue("type");} else {typeFile="txt";}
			ParserFactory parseFactory = new ParserFactory();
			FileParser fileParser = parseFactory.createParser(typeFile.trim()); 
			Model model = fileParser.parseFile(modelFileName.trim());
			model.calculate();

			System.out.print(String.join(", ", model.getApprovedFacts()));

		} catch (ParseException exp) {
			System.err.println("Неверное указание файла.  Причина: " + exp.getMessage());
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
