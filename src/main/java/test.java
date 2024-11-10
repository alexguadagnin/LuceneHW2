import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class test {
    /*
    public static void scrivi(String content, String nomeFile) {
        try (FileWriter writer = new FileWriter(nomeFile)) {
            writer.write(content);
            System.out.println("Scrittura completata.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

        File fileDirectory = new File(LuceneConstants.FILE_PATH);
        int i = 0;
        // Verifica se il percorso è una directory
        if (fileDirectory.isDirectory()) {
            // Itera su ogni file nella directory
            for (File file : fileDirectory.listFiles()) {
                // Verifica che sia un file HTML
                if (file.isFile() && file.getName().endsWith(".html")) {
                    try {
                        // Legge il contenuto del file
                        String contenuto = Files.readString(Path.of(file.getPath()), StandardCharsets.ISO_8859_1);

                        System.out.println(file.getName());

                        // Estrai il titolo
                        System.out.println(XpathHtml.estraiTitolo(contenuto));

                        // Estrai gli autori
                        String autoriString = XpathHtml.estraiAutori(contenuto);
                        //System.out.println(HtmlTagFilter.removeNumber(autoriString));

                        String contenutoClean = HtmlTagFilter.removeHtmlTags(contenuto);
                        if(i==10) break;
                        i++;
                    } catch (IOException e) {
                        System.err.println("Errore nella lettura del file: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Il percorso specificato non è una directory.");
        }

    }*/
    /*
    public static void main(String[] args) {
        try {
            FSDirectory directory = FSDirectory.open(Paths.get(LuceneConstants.INDEX_PATH));
            IndexReader reader = DirectoryReader.open(directory);

            int numDocs = reader.numDocs();
            System.out.println("Numero di documenti indicizzati: " + numDocs);

            reader.close();
            directory.close();
        } catch (IOException e) {
            System.err.println("Errore durante l'apertura dell'indice: " + e.getMessage());
        }
    }
    */
    public static void main(String[] args) {
        String indexPath = LuceneConstants.INDEX_PATH;  // Modifica con il percorso corretto dell'indice
        String outputFilePath = "outputIndex2.txt"; // Nome del file di output

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Apri l'indice
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            writer.write("Numero di documenti nell'indice: " + reader.numDocs() + "\n");

            // Scorri i segmenti dell'indice
            for (LeafReaderContext context : reader.leaves()) {
                LeafReader leafReader = context.reader();
                FieldInfos fieldInfos = leafReader.getFieldInfos();  // Otteniamo FieldInfos

                writer.write("Campi trovati:\n");
                for (FieldInfo fieldInfo : fieldInfos) {  // Iteriamo direttamente su FieldInfos
                    //writer.write("Campo: " + fieldInfo.name + "\n");
                    writer.write("Campo: " + fieldInfo.toString() + "\n");


                    // Otteniamo i termini per ogni campo
                    Terms terms = leafReader.terms(fieldInfo.name);
                    if (terms != null) {
                        TermsEnum termsEnum = terms.iterator();
                        BytesRef term;
                        //writer.write("Termini nel campo \"" + fieldInfo.name + "\":\n");
                        writer.write("Termini nel campo \"" + fieldInfo.toString() + "\":\n");
                        int termCount = 0;
                        while ((term = termsEnum.next()) != null) {
                            writer.write(" - " + term.utf8ToString() + "\n");
                            termCount++;
                        }
                        if (termCount == 0) {
                            writer.write(" (Nessun termine trovato)\n");
                        }
                    } else {
                        writer.write(" (Nessun termine nel campo)\n");
                    }
                }
            }
            reader.close();
            System.out.println("Dati scritti su " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
