package Lab6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArxmlFileSorter {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ArxmlSorter <input.arxml>");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = createOutputFileName(inputFileName); // Creates a new output file name based on the input file name

        try {
            isValidExtension(inputFileName); // Checks if the input file has a valid extension

            File inputFile = new File(inputFileName);
            DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder dB = dBF.newDocumentBuilder();
            Document doc = dB.parse(inputFile);
            doc.getDocumentElement().normalize(); // Normalizes the XML document, optional

            if (doc.getDocumentElement().getChildNodes().getLength() == 0) {
                throw new EmptyAutosarFileException("ARXML file is empty."); // Throws an exception if the ARXML file is empty
            }

            NodeList containerList = doc.getElementsByTagName("CONTAINER");
            ArrayList<Element> containers = new ArrayList<Element>();
            for (int i = 0; i < containerList.getLength(); i++) {
                containers.add((Element) containerList.item(i)); // Adds all the container elements to an ArrayList
            }

            Collections.sort(containers, new Comparator<Element>() { // Sorts the container elements based on their short name
                @Override
                public int compare(Element e1, Element e2) {
                    String name1 = e1.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
                    String name2 = e2.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
                    return name1.compareTo(name2);
                }
            });

            Element rootElement = doc.getDocumentElement();
            for (Element container : containers) {
                rootElement.appendChild(container); // Appends the sorted container elements to the root element
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFileName));
            transformer.transform(source, result); // Writes the modified XML document to the output file

            System.out.println("Containers sorted successfully. Output written to " + outputFileName);
        } catch (NotVaildAutosarFileException e) { // Catches the exception thrown by isValidExtension() if the input file has an invalid extension
            System.err.println("Error: " + e.getMessage());
        }  catch (SAXException | IOException e) { // Catches SAXException and IOException if the input file is empty
        	System.err.println("Error: Input file does not have any content" );
        }catch (EmptyAutosarFileException e) { // Catches the exception thrown if the ARXML file is empty
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) { // Catches any other exceptions that may occur
            e.printStackTrace();
        }
    }

    private static void isValidExtension(String fileName) throws NotVaildAutosarFileException { // Checks if the input file has a valid extension
        if (!fileName.endsWith(".arxml")) {
            throw new NotVaildAutosarFileException("Input file does not have .arxml extension.");
        }
    }

    private static String createOutputFileName(String inputFileName) { // Creates a new output file name based on the input file name
        return inputFileName.substring(0,inputFileName.indexOf("."))+"_mod.arxml";
    }

}

@SuppressWarnings("serial")
class NotVaildAutosarFileException extends Exception { // Custom exception for invalid Autosar files
    public NotVaildAutosarFileException(String message) {
        super(message);
    }
}

@SuppressWarnings("serial")
class EmptyAutosarFileException extends RuntimeException { // Custom exception for empty Autosar files
    public EmptyAutosarFileException(String message) {
        super(message);
    }
}
