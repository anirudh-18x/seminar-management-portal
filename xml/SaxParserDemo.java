import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * SaxParserDemo.java — XML SAX Parser Example
 *
 * The SAX (Simple API for XML) parser reads XML sequentially,
 * firing events (method calls) as it encounters each element.
 * It does NOT build a tree in memory.
 *
 * Advantages:  Fast, uses very little memory — good for large files.
 * Disadvantages: Read-only, forward-only — cannot go back to earlier elements.
 *
 * Key SAX events (methods your handler overrides):
 *  - startElement()  : called when an opening tag is found, e.g. <event>
 *  - endElement()    : called when a closing tag is found, e.g. </event>
 *  - characters()    : called with the text content between tags
 *
 * This demo reads events.xml and prints a summary of each event.
 *
 * ---- How to compile and run (from the xml/ folder) ----
 *
 * Compile:
 *   javac SaxParserDemo.java
 *
 * Run:
 *   java SaxParserDemo
 */
public class SaxParserDemo {

    public static void main(String[] args) {

        System.out.println("=== SAX Parser Demo — Reading events.xml ===\n");

        try {
            // Step 1: Create a SAXParserFactory
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // Step 2: Create a SAXParser
            SAXParser saxParser = factory.newSAXParser();

            // Step 3: Create our custom handler (defined as inner class below)
            EventHandler handler = new EventHandler();

            // Step 4: Parse the file — the parser calls handler methods as it reads
            saxParser.parse(new File("events.xml"), handler);

            System.out.println("\nParsing complete. Total events found: " + handler.getEventCount());

        } catch (Exception e) {
            System.err.println("Error during SAX parsing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * EventHandler — our custom SAX event handler.
     *
     * We extend DefaultHandler which provides empty implementations of all
     * SAX callback methods. We only override the ones we need.
     */
    static class EventHandler extends DefaultHandler {

        // Track which element we are currently inside
        private String currentElement = "";

        // Accumulated text content (characters() may be called multiple times)
        private StringBuilder currentValue = new StringBuilder();

        // Store current event's values
        private String currentId    = "";
        private String currentTitle = "";
        private String currentType  = "";
        private String currentDate  = "";
        private String currentDept  = "";

        private int eventCount = 0;

        // Called at the start of each element tag
        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) throws SAXException {

            currentElement = qName;         // remember which tag we're in
            currentValue.setLength(0);       // reset the text buffer

            if ("event".equals(qName)) {
                // Read the 'id' attribute from <event id="E001">
                currentId = attributes.getValue("id");
            }
        }

        // Called when text content is found between tags
        // Note: this can be called multiple times for the same element
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentValue.append(ch, start, length);
        }

        // Called at the end of each element tag
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            String value = currentValue.toString().trim();

            // Store values based on which element just ended
            switch (qName) {
                case "title":      currentTitle = value; break;
                case "type":       currentType  = value; break;
                case "date":       currentDate  = value; break;
                case "department": currentDept  = value; break;
            }

            // When </event> is reached, print the complete event data
            if ("event".equals(qName)) {
                eventCount++;
                System.out.println("Event #" + eventCount + " [" + currentId + "]");
                System.out.println("  Title      : " + currentTitle);
                System.out.println("  Type       : " + currentType);
                System.out.println("  Department : " + currentDept);
                System.out.println("  Date       : " + currentDate);
                System.out.println();

                // Reset for the next event
                currentId = currentTitle = currentType = currentDate = currentDept = "";
            }

            currentElement = "";
        }

        // Called when parsing starts (beginning of document)
        @Override
        public void startDocument() throws SAXException {
            System.out.println("SAX: Document parsing started.\n");
        }

        // Called when parsing ends (end of document)
        @Override
        public void endDocument() throws SAXException {
            System.out.println("SAX: Document parsing finished.");
        }

        public int getEventCount() { return eventCount; }
    }
}
