import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilterFactory;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LuceneIndexer {

    public void creaIndice() throws IOException {
        Path indexPath = Paths.get(LuceneConstants.INDEX_PATH);
        Directory directory = FSDirectory.open(indexPath);

        Map<String, Analyzer> perFieldAnalyzerMap = LuceneAnalyzer.defineMapAnalyzer();
        // l'EnglishAnalyzer è per gli eventuali campi non definiti, perFieldAnalyzerMap è per i campi specifici
        Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), perFieldAnalyzerMap);

        // Semantica di text processing per l'indicizzazione dei Fields definiti qui!
        IndexWriterConfig config = new IndexWriterConfig(perFieldAnalyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        File fileDirectory = new File(LuceneConstants.FILE_PATH);

        // Misurazione del tempo di inizio
        long startTime = System.nanoTime();

        // Verifica se il percorso è una directory
        if (fileDirectory.isDirectory()) {
            // Itera su ogni file nella directory
            for (File file : fileDirectory.listFiles()) {
                // Verifica che sia un file HTML
                if (file.isFile() && file.getName().endsWith(".html")) {
                    try {
                        // Legge il contenuto del file
                        String contenuto = Files.readString(Path.of(file.getPath()));

                        String testoClean = HtmlTagFilter.removeHtmlTags(contenuto);
                        testoClean = HtmlTagFilter.cleanText(testoClean);
                        Document doc = new Document();
                        doc.add(new TextField("titolo", XpathHtml.estraiTitolo(contenuto), Field.Store.YES));
                        doc.add(new TextField("autori", HtmlTagFilter.removeNumber(XpathHtml.estraiAutori(contenuto)), Field.Store.YES));
                        doc.add(new TextField("contenuto", testoClean, Field.Store.YES));

                        writer.addDocument(doc);
                        writer.commit();

                        // Misurazione del tempo di fine
                        long endTime = System.nanoTime();
                        long durationInMillis = (endTime - startTime) / 1_000_000;

                        // Statistiche finali
                        System.out.println("Tempo totale di indicizzazione: " + durationInMillis + " ms");
                        System.out.println("Numero totale di documenti indicizzati: " + writer.getDocStats().numDocs);


                    } catch (IOException e) {
                        System.err.println("Errore nella lettura del file: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Il percorso specificato non è una directory.");
        }

    }

}
