import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * DomParserDemo.java — XML DOM Parser Example
 *
 * The DOM (Document Object Model) parser reads the entire XML file
 * into memory as a tree structure. You can then navigate, search, and
 * modify any part of the tree.
 *
 * Advantages:  Random access to any element, easy to navigate.
 * Disadvantages: Uses more memory (entire document loaded at once).
 *
 * This demo reads events.xml and prints each event's title, type, and date.
 *
 * ---- How to compile and run (from the xml/ folder) ----
 *
 * Compile:
 *   javac DomParserDemo.java
 *
 * Run:
 *   java DomParserDemo
 *
 * Expected output: a list of event titles with their type and date.
 */
public class DomParserDemo {

    public static void main(String[] args) {

        System.out.println("=== DOM Parser Demo — Reading events.xml ===\n");

        try {
            // Step 1: Create a DocumentBuilderFactory
            // This is the factory that creates DocumentBuilder objects.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Step 2: Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Step 3: Parse the XML file into a Document (the DOM tree)
            Document doc = builder.parse("events.xml");

            // Step 4: Normalize the tree (cleans up text node whitespace)
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            System.out.println();

            // Step 5: Get all <event> elements
            NodeList eventList = doc.getElementsByTagName("event");

            System.out.println("Total events found: " + eventList.getLength());
            System.out.println();

            // Step 6: Loop through each <event> node
            for (int i = 0; i < eventList.getLength(); i++) {

                // Cast Node to Element to access attributes
                Element event = (Element) eventList.item(i);

                // Read an attribute value
                String id = event.getAttribute("id");

                // Read child element text content using getElementsByTagName
                String title  = getTagValue(event, "title");
                String type   = getTagValue(event, "type");
                String date   = getTagValue(event, "date");
                String speaker= getTagValue(event, "speaker");
                String seats  = getTagValue(event, "seats");

                // Print the extracted data
                System.out.println("Event " + (i + 1) + ":");
                System.out.println("  ID      : " + id);
                System.out.println("  Title   : " + title);
                System.out.println("  Type    : " + type);
                System.out.println("  Date    : " + date);
                System.out.println("  Speaker : " + speaker);
                System.out.println("  Seats   : " + seats);
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("Error parsing XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method: gets the text content of the first child element
     * with the given tag name inside a parent element.
     */
    private static String getTagValue(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }
}
