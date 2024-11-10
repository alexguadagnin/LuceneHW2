import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LuceneMain {

    public static String chiediTipologiaRicerca() {
        Scanner scanner = new Scanner(System.in);
        int scelta = 0;

        // Visualizza il messaggio di scelta dell'operazione
        System.out.println("Digita il numero dell'operazione che vuoi effettuare:");
        System.out.println("1. Ricerca per Autore/i");
        System.out.println("2. Ricerca per Contenuto");
        System.out.println("3. Ricerca per Titolo");

        // Ciclo per garantire l'inserimento corretto
        while (true) {
            System.out.print("Scelta: ");
            if (scanner.hasNextInt()) {
                scelta = scanner.nextInt();
                if (scelta >= 1 && scelta <= 3) {
                    break; // Esce dal ciclo se la scelta è valida
                } else {
                    System.out.println("Errore: inserisci un numero compreso tra 1 e 3.");
                }
            } else {
                System.out.println("Errore: inserisci un numero valido.");
                scanner.next(); // Consuma l'input errato
            }
        }
        if (scelta == 1) return "autori";
        else if (scelta == 2) return "contenuto";
        else return "titolo";
    }

    // restituisce 0 per TermQuery, 1 per PhraseQuery
    public static int chiediTipologiaQuery() {
        Scanner scanner = new Scanner(System.in);
        int scelta = 0;

        // Visualizza il messaggio di scelta dell'operazione
        System.out.println("Digita il numero dell'operazione che vuoi effettuare:");
        System.out.println("1. TermQuery");
        System.out.println("2. PhraseQuery");

        // Ciclo per garantire l'inserimento corretto
        while (true) {
            System.out.print("Scelta: ");
            if (scanner.hasNextInt()) {
                scelta = scanner.nextInt();
                if (scelta >= 1 && scelta <= 2) {
                    break; // Esce dal ciclo se la scelta è valida
                } else {
                    System.out.println("Errore: inserisci un numero compreso tra 1 e 2.");
                }
            } else {
                System.out.println("Errore: inserisci un numero valido.");
                scanner.next(); // Consuma l'input errato
            }
        }
        if (scelta == 1) return 0;
        else return 1;
    }

    /*
    public static void main(String[] args) {
        LuceneIndexer indexer = new LuceneIndexer();
        try {
            indexer.creaIndice();

            IndexWriterConfig config = new IndexWriterConfig();
            config.setCodec(new SimpleTextCodec());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


    public static void main (String[]args){
        Path path = Paths.get(LuceneConstants.INDEX_PATH);
        Directory directory;
        Scanner scanner = new Scanner(System.in);
        try {
            directory = FSDirectory.open(path);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // scelta tipologia di query
            String tipoQuery = chiediTipologiaRicerca();
            int flag = chiediTipologiaQuery();

            if (flag == 0) {
                System.out.println("Inserisci i termini (separati da uno SPAZIO) da cercare:");
                String ricerca = scanner.nextLine();
                LuceneQuery.termQuery(tipoQuery, ricerca);
            } else {
                System.out.println("Inserisci la frase da cercare:");
                String ricerca = scanner.nextLine();
                LuceneQuery.phraseQuery(tipoQuery, ricerca);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
