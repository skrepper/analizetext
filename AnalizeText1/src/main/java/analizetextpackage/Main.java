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
		CommandLineParser cliParser = new DefaultParser();
		Options options = new Options();
		Option fileOption = new Option("f", "file", true, "File of model");
		options.addOption(fileOption);

		try {
			try {
				line = cliParser.parse(options, args);
			} catch (ParseException exp) {
				System.err.println("Ошибочная команда.  Причина: " + exp.getMessage());
			}
			if (!line.hasOption("file")) 
				throw new RuntimeException("Ошибка чтения файла. ");
			modelFileName = line.getOptionValue("file");
			Parser parser = new Parser();
			Model model = parser.parseFile(modelFileName.trim());
			model.calculate();

			System.out.print(String.join(", ", model.getApprovedFacts()));

		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
