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
		boolean xmlFile = false;
		CommandLineParser cliParser = new DefaultParser();
		Options options = new Options();
		Option xmlOption = new Option("xml", "xml", false, "Input file");
		Option fileOption = new Option("f", "file", true, "Input file");
		fileOption.setRequired(true);
		xmlOption.setRequired(false);
		options.addOption(fileOption);
		options.addOption(xmlOption);

		try {
			line = cliParser.parse(options, args);
			
			modelFileName = line.getOptionValue("file");
			xmlFile = line.hasOption("xml");
			Parser parser = new Parser();
			Model model = parser.parseFile(modelFileName.trim(), xmlFile);
			model.calculate();

			System.out.print(String.join(", ", model.getApprovedFacts()));

		} catch (ParseException exp) {
			System.err.println("�������� �������� �����.  �������: " + exp.getMessage());
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
