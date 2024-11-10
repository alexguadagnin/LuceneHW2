import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilterFactory;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LuceneAnalyzer {

    // Analyzer per testi
    public static Analyzer getTextAnalyzer() throws IOException {
        return CustomAnalyzer.builder().withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class).addTokenFilter(WordDelimiterGraphFilterFactory.class)
                .addTokenFilter(ASCIIFoldingFilterFactory.class).build();

        /*
        * addTokenFilter(KStemFilterFactory.class)
        * */
    }

    // Analyzer per autori
    public static Analyzer getAuthorAnalyzer() throws IOException {
        return CustomAnalyzer.builder().withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                .addTokenFilter(ASCIIFoldingFilterFactory.class).build();
    }

    // Mappa di Analyzer per ciascun campo
    public static Map<String, Analyzer> defineMapAnalyzer() throws IOException {
        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();

        perFieldAnalyzers.put("titolo", LuceneAnalyzer.getTextAnalyzer());
        perFieldAnalyzers.put("contenuto", LuceneAnalyzer.getTextAnalyzer());
        perFieldAnalyzers.put("autori", LuceneAnalyzer.getAuthorAnalyzer());

        Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), perFieldAnalyzers);
        return perFieldAnalyzers;
    }
}
