package analizetextpackage;

public class Main {

	public static void main(String[] arg) {

		if (arg.length == 0) {
			System.err.print("ֲגוהטעו טלÿ פאיכא.");
			return;
		}

		try {
			Parser parser = new Parser();
			Model model = parser.parseFile(arg[0]);
			model.calculate();
			
			System.out.print(String.join(", ", model.getApprovedFacts()));

		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
