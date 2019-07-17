package analizetextpackage;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

public interface FileParser {
	public Model parseFile(String filePathName) throws FileNotFoundException, IOException, JAXBException, SAXException, XMLStreamException;
}
