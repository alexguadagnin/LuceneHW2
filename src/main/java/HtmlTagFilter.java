import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlTagFilter {
    public static String removeHtmlTags(String htmlContent) {
        // Usa una regex per rimuovere i tag HTML
        String regex = "<[^>]+>";
        return htmlContent.replaceAll(regex, "");
    }

    public static String removeNumber(String htmlContent) {
        // Usa una regex per rimuovere i numeri da stringhe
        String regex = "\\d+";
        return htmlContent.replaceAll(regex, "");
    }

    public static String cleanText(String original) throws IOException {
        // Carica le stopwords in un HashSet per ricerche più veloci
        Set<String> stopwords = new HashSet<>(Files.readAllLines(Paths.get("C:\\Users\\alexg\\IdeaProjects\\HW2\\englishStopWords.txt")));

        // Divide il testo in parole, gestendo anche spazi multipli e punteggiatura
        List<String> allWords = Arrays.stream(original.toLowerCase().split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", "")) // Rimuove la punteggiatura
                .collect(Collectors.toList());

        // Rimuove le stopwords
        allWords.removeAll(stopwords);

        // Unisce le parole rimanenti in una stringa separata da spazi
        return allWords.stream().collect(Collectors.joining(" "));
    }
}
