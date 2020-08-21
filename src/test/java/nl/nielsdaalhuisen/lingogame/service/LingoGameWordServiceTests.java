package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.WordService;
import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.domain.repository.WordRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LingoGameWordServiceTests {

    @MockBean
    WordRepository wordRepository;

    @MockBean
    SourceDeserializer sourceDeserializer;

    @Autowired
    WordService wordService;

    @Test
    @DisplayName("Import words into Datbase - happy")
    void ImportWords() {
        Word word1 = new Word("tester");
        Word word2 = new Word("testje");
        Word word3 = new Word("testjes");
        Word word4 = new Word("getest");

        List<Word> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);
        words.add(word3);
        words.add(word4);

        Mockito.when(sourceDeserializer.importWords()).thenReturn(words);

        List<Word> savedWords = wordService.importWords();

        assertEquals(savedWords.size(), 4);
        assertEquals(savedWords.get(0).getValue(), word1.getValue());
        assertEquals(savedWords.get(1).getValue(), word2.getValue());
        assertEquals(savedWords.get(2).getValue(), word3.getValue());
        assertEquals(savedWords.get(3).getValue(), word4.getValue());
    }

    @Test
    @DisplayName("Get random word - happy")
    void GetRandomWord() {
        Word word1 = new Word("tester");
        Word word2 = new Word("testje");
        Word word3 = new Word("testjes");
        Word word4 = new Word("getest");

        List<Word> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);
        words.add(word3);
        words.add(word4);

        Mockito.when(wordRepository.findAll()).thenReturn(words);

        Word randomWord = wordService.getRandomWord(6);

        assertNotNull(randomWord);
        assertEquals(randomWord.getValue().length(), 6);
    }

    @Test
    @DisplayName("Get random word - unhappy")
    void GetRandomLongWord() {
        Word word1 = new Word("tester");
        Word word2 = new Word("testje");
        Word word3 = new Word("testjes");
        Word word4 = new Word("getest");

        List<Word> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);
        words.add(word3);
        words.add(word4);

        Mockito.when(wordRepository.findAll()).thenReturn(words);

        Word randomWord = wordService.getRandomWord(8);

        assertNull(randomWord);
    }
}
