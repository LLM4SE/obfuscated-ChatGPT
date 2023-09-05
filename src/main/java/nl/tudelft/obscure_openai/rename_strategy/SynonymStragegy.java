package nl.tudelft.obscure_openai.rename_strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.tudelft.obscure_openai.Context;

public class SynonymStragegy implements RenameStrategy {

  private Context context;
  private final String USER_AGENT = "Mozilla/5.0";
  private Map<String, List<Word>> mapping = new HashMap<>();

  public SynonymStragegy(Context context) {
    super();
    this.context = context;
  }

  private List<Word> searchSynonym(String wordToSearch) throws Exception {
    if (this.mapping.containsKey(wordToSearch.toLowerCase())) {
      return this.mapping.get(wordToSearch.toLowerCase());
    }

    String url = "https://api.datamuse.com/words?rel_syn=" + wordToSearch.toLowerCase();

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", USER_AGENT);

    // ordering the response
    StringBuilder response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String inputLine;
      response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    }

    ObjectMapper mapper = new ObjectMapper();

    try {
      // converting JSON array to ArrayList of words
      ArrayList<Word> words = mapper.readValue(
          response.toString(),
          mapper.getTypeFactory().constructCollectionType(ArrayList.class, Word.class));
      this.mapping.put(wordToSearch.toLowerCase(), words);
      return words;
    } catch (IOException e) {
      e.getMessage();
    }
    return Collections.emptyList();
  }

  @Override
  public String rename(String name) {
    String output = "";
    // split name into words following camelCase
    String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
    for (String word : words) {
      // search for synonyms of each word
      try {
        List<Word> synonyms = searchSynonym(word);
        // if there are synonyms, replace the word with a random synonym
        if (!synonyms.isEmpty()) {
          String replacement = synonyms.get(0).getWord();
          if (replacement.indexOf(" ") != -1) {
            // camelCase the synonym if it contains spaces
            String[] replacementWords = replacement.split("[ -_]");
            replacement = replacementWords[0];
            for (int i = 1; i < replacementWords.length; i++) {
              String replacementWord = replacementWords[i];
              replacement += replacementWord.substring(0, 1).toUpperCase() + replacementWord.substring(1);
            }
          }
          if (output.isEmpty()) {
            output = replacement;
          } else {
            // if the word is not the first word, capitalize it
            output += replacement.substring(0, 1).toUpperCase() + replacement.substring(1);
          }
        } else {
          // if there are no synonyms, keep the word
          if (output.isEmpty()) {
            output = word;
          } else {
            output += word.substring(0, 1).toUpperCase() + word.substring(1);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (output.isEmpty()) {
          output = word;
        } else {
          output += word.substring(0, 1).toUpperCase() + word.substring(1);
        }
      }
    }
    return output;
  }

  // word and score attributes are from DataMuse API
  static class Word {
    private String word;
    private int score;

    public String getWord() {
      return this.word;
    }

    public int getScore() {
      return this.score;
    }
  }
}
