package nl.nielsdaalhuisen.lingogame.infrastructure.web.client;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WordClient implements SourceDeserializer {
    @Autowired
    RestTemplate restTemplate;

    public List<Word> importWords() {
        String lingowordsApi = System.getenv("LINGOWORDS_API");

        ResponseEntity<Word[]> response = restTemplate.getForEntity(lingowordsApi + "/words", Word[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
