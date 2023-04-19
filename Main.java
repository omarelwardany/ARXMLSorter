import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    // Loads ARXML file
    private static Document loadAutosar(String fileName) throws NotValidAutosarFileException {
        if (!fileName.endsWith(".arxml")) {
            throw new NotValidAutosarFileException("The file extension is not \"arxml\"");
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            return docBuilder.parse(fileName);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    // checks if file is empty
    private static void fileIsEmpty(File file) throws EmptyAutosarFileException {
        if (file.length() == 0) {
            throw new EmptyAutosarFileException("Empty file");
        }
    }

    private static void sortAndSave(Document docIn, String filename) {

        try {
            docIn.getDocumentElement().normalize();
            NodeList nodeList = docIn.getElementsByTagName("*");

            // Create a list of elements to be sorted
            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (element.getNodeName().equals("SHORT-NAME")) {
                    elements.add((Element)element.getParentNode());
                }
            }

            // Sort the elements by the "SHORT-NAME" text field
            Collections.sort(elements, new Comparator<Element>() {
                public int compare(Element e1, Element e2) {
                    String s1 = e1.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
                    String s2 = e2.getElementsByTagName("SHORT-NAME").item(0).getTextContent();
                    return s1.compareToIgnoreCase(s2);
                }
            });

            // Write the sorted elements to a new ARXML file
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document docOut = dBuilder.newDocument();

            Element rootElement = docOut.createElement("AUTOSAR");
            docOut.appendChild(rootElement);

            for (Element element : elements) {
                Node importedNode = docOut.importNode(element, true);
                rootElement.appendChild(importedNode);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(docOut);
            StreamResult result = new StreamResult(new File(filename.replace(".arxml", "_mod.arxml")));
            transformer.transform(source, result);


            System.out.println("ARXML file sorted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        if (args.length == 1) {
            Document doc = null;
            File file = new File(args[0]);

            // checks if autosar file is empty
//            try {
                fileIsEmpty(file);
//            } catch (EmptyAutosarFileException e) {
//                System.out.println(e.getMessage());
//                System.exit(1);
//            }
            // loading file
            try {
                doc = loadAutosar(args[0]);
            } catch (NotValidAutosarFileException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            // sorts elements and saves Document
            sortAndSave(doc, args[0]);
        } else System.out.println("Invalid Args");
    }
}

class NotValidAutosarFileException extends Exception {
    NotValidAutosarFileException() {}
    NotValidAutosarFileException(String s) {
        super(s);
    }
}

class EmptyAutosarFileException extends RuntimeException {
    EmptyAutosarFileException() {}

    EmptyAutosarFileException(String s) {
        super(s);
    }
}
