import org.jsoup.nodes.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XpathHtml {

    public static String estraiAutori(String docHtml){
        List<String> autoriLista = new ArrayList<>();
        try {
            // Converte HTML in formato XML (InputSource)
            Document doc = (Document) Jsoup.parse(docHtml);

            // Estrai gli autori con un selettore CSS (equivalente di XPath)
            for (Element author : doc.select("span.ltx_personname")) {
                autoriLista.add(author.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String autoriString = String.join("; ", autoriLista);
        System.out.println(autoriString);
        return autoriString;
    }

    public static String estraiTitolo(String docHtml){
        // Parse il contenuto HTML
        Document doc = Jsoup.parse(docHtml);

        // Seleziona il titolo con la classe specifica
        Element titleElement = doc.selectFirst("h1.ltx_title");

        if (titleElement != null) {
            System.out.println("Titolo del documento: " + titleElement.text());
            return titleElement.text();
        } else {
            System.out.println("Titolo non trovato.");
            return "";
        }

    }

}
