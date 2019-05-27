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
		Option fileOption   =  new Option("f", "file", true, "file");
		fileOption.setArgs(1); 
		fileOption.setArgName("file");
		options.addOption(fileOption);
	    try {
	        line = cliParser.parse( options, args );
	    }
	    catch( ParseException exp ) {
	        System.err.println( "Неверная командна.  Причина: " + exp.getMessage() );
	    }
	    
		if( line.hasOption( "file" ) ) {
	        modelFileName = line.getOptionValue( "file" );
	    }

		try {
			Parser parser = new Parser();
			Model model = parser.parseFile(modelFileName.trim());
			model.calculate();
			
			System.out.print(String.join(", ", model.getApprovedFacts()));

		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
