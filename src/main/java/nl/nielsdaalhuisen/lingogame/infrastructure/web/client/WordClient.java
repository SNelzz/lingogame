package nl.nielsdaalhuisen.lingogame.infrastructure.web.client;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WordClient implements SourceDeserializer {
    public List<Word> importWords() {
        RestTemplate restTemplate = new RestTemplate();
        String lingowordsApi = System.getenv("LINGOWORDS_API");

        ResponseEntity<Word[]> response = restTemplate.getForEntity(lingowordsApi + "/words", Word[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
