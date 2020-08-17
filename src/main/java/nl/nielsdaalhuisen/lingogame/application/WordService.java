package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.domain.repository.WordRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.client.WordClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class WordService {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordClient wordClient;

    public List<Word> importWords() {
        List<Word> words = this.wordClient.importWords();
        this.wordRepository.saveAll(words);
        return words;
    }

    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();
        for(Word w : this.wordRepository.findAll()) {
            words.add(w);
        }
        return words;
    }
}
