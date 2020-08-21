package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LingoGameWordClientTest {

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    SourceDeserializer sourceDeserializer;

    @Test
    @DisplayName("import words - happy")
    void ImportWords() {

        Word word1 = new Word("tester");
        Word word2 = new Word("tested");
        Word word3 = new Word("testje");
        Word[] words = {word1, word2, word3};

        Mockito.when(restTemplate.getForEntity(any(String.class), any())).thenReturn(new ResponseEntity<>(words, HttpStatus.OK));

        List<Word> wordList = sourceDeserializer.importWords();
        assertNotNull(wordList);
        assertEquals(wordList.get(0), words[0]);
    }
}
