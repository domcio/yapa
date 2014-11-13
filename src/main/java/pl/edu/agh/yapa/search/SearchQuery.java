package pl.edu.agh.yapa.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchQuery {

    private List<String> phrasesToMatch = new ArrayList<>();
    private List<String> wordsToExclude = new ArrayList<>();

    public SearchQuery(String query) {
        String trimmed = query.trim();
        String[] byQuotes = trimmed.split("\"");
        for (int i = 0; i < byQuotes.length; i++) {
            String phrase = byQuotes[i].trim();

            if (phrase.isEmpty()){
                continue;
            }

            if (i % 2 != 0){
                phrasesToMatch.add(phrase.trim());
            } else {
                List<String> bySpace = Arrays.asList(phrase.split(" "));
                for (String s : bySpace) {
                    if (s.isEmpty()){
                        continue;
                    }

                    if (s.startsWith("-")){
                        wordsToExclude.add(s.substring(1).trim());
                    } else {
                        phrasesToMatch.add( s.trim() );
                    }
                }
            }
        }
    }

    public String asMongoQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : phrasesToMatch) {
            stringBuilder.append("\"").append(s).append("\" ");
        }
        for (String s : wordsToExclude) {
            stringBuilder.append("-").append(s).append(" ");
        }

        return stringBuilder.toString();
    }

    public List<String> getPhrasesToMatch() {
        return phrasesToMatch;
    }

    public List<String> getWordsToExclude() {
        return wordsToExclude;
    }
}
