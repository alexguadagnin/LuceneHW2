import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class LuceneQuery {

    // in input riceve una serie di termini separati da uno spazio
    public static void termQuery(String campo, String termini) throws IOException {
        Path indexPath = Paths.get(LuceneConstants.INDEX_PATH);
        Directory directory = FSDirectory.open(indexPath);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Estrazione dei termini usando split()
        String[] termArray = termini.split("\\s+"); // "\\s+" separa per uno o più spazi
        // Conversione in lista
        List<String> termList = Arrays.asList(termArray);

        // Costruzione dinamica della BooleanQuery
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

        // Aggiungi ogni termine alla BooleanQuery
        for (String term : termList) {
            TermQuery termQuery = new TermQuery(new Term(campo, term));
            // Utilizzo di MUST per cercare tutti i termini (AND)
            //booleanQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(termQuery, BooleanClause.Occur.MUST);
        }

        // Costruzione della query finale
        Query query = booleanQueryBuilder.build();

        stampaRisultati(query, searcher);
    }

    public static void phraseQuery(String campo, String termini) throws IOException {
        Path indexPath = Paths.get(LuceneConstants.INDEX_PATH);
        Directory directory = FSDirectory.open(indexPath);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Rimuovi punteggiatura dalla stringa
        termini = termini.replaceAll("[\\p{Punct}]", " ");
        // Estrazione dei termini usando split()
        String[] termArray = termini.split("\\s+"); // "\\s+" separa per uno o più spazi
        // Conversione in lista
        List<String> termList = Arrays.asList(termArray);

        // Costruzione della PhraseQuery
        PhraseQuery.Builder phraseQueryBuilder = new PhraseQuery.Builder();

        int position = 0; // La posizione iniziale
        for (String term : termList) {
            phraseQueryBuilder.add(new Term(campo, term), position);
            position++; // Incremento della posizione per mantenere l'ordine dei termini
        }

        // Creazione della PhraseQuery
        Query phraseQuery = phraseQueryBuilder.build();

        stampaRisultati(phraseQuery, searcher);
    }

    public static void stampaRisultati(Query query, IndexSearcher searcher) throws IOException {
        // Cerca i 10 top documenti che matchano con la query
        TopDocs results = searcher.search(query, 10);
        System.out.println("Numero di documenti trovati: " + results.totalHits);

        // Iteriamo sui risultati
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            int docId = scoreDoc.doc; // Otteniamo l'ID del documento
            Document document = searcher.doc(docId); // Recuperiamo il documento

            // Supponiamo che il nome del documento sia memorizzato in un campo "filename"
            String filename = document.get("titolo");
            System.out.println("Nome documento: " + filename);
        }
    }

}
