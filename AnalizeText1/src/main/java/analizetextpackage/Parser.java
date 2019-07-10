package analizetextpackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

public class Parser {

	public Model parseFile(String filePathName, boolean xmlFile) throws FileNotFoundException, IOException, JAXBException, SAXException {
		
		if (xmlFile) {	
			return (new Parsexmlfile()).parseFile(filePathName);
		}
		
		return (new Parsetextfile()).parseFile(filePathName);
		
	}
}

