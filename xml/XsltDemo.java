import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * XsltDemo.java — XSLT Transformation Example
 *
 * This program applies the events.xsl stylesheet to events.xml
 * to produce an HTML file (events-output.html) that you can open
 * in a browser to see the formatted event list.
 *
 * Key classes:
 *  - TransformerFactory : creates Transformer objects
 *  - Transformer        : performs the actual transformation
 *  - StreamSource       : wraps an XML/XSL file as input
 *  - StreamResult       : wraps the output file
 *
 * ---- How to compile and run (from the xml/ folder) ----
 *
 * Compile:
 *   javac XsltDemo.java
 *
 * Run:
 *   java XsltDemo
 *
 * Then open events-output.html in your browser to see the result.
 */
public class XsltDemo {

    public static void main(String[] args) {

        System.out.println("=== XSLT Demo — Transforming events.xml with events.xsl ===\n");

        try {
            // Input XML file
            StreamSource xmlSource = new StreamSource(new File("events.xml"));

            // XSLT stylesheet
            StreamSource xslSource = new StreamSource(new File("events.xsl"));

            // Output HTML file
            StreamResult htmlOutput = new StreamResult(new File("events-output.html"));

            // Create a TransformerFactory and then a Transformer using our XSL
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslSource);

            // Perform the transformation
            transformer.transform(xmlSource, htmlOutput);

            System.out.println("Transformation successful!");
            System.out.println("Output written to: events-output.html");
            System.out.println("Open events-output.html in a browser to view the result.");

        } catch (Exception e) {
            System.err.println("XSLT transformation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
