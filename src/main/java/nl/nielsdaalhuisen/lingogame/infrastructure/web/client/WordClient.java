package nl.nielsdaalhuisen.lingogame.infrastructure.web.client;

import nl.nielsdaalhuisen.lingogame.application.WordService;
import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class WordClient {
    public List<Word> importWords() {
        RestTemplate restTemplate = new RestTemplate();
        String lingowordsApi = System.getenv("LINGOWORDS_API");

        ResponseEntity<Word[]> response = restTemplate.getForEntity(lingowordsApi + "/words", Word[].class);
        List<Word> words = Arrays.asList(response.getBody());

        return words;
    }
}
