package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import nl.nielsdaalhuisen.lingogame.domain.repository.WordRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordService {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private SourceDeserializer sourceDeserializer;
    private final Random rand = new Random();

    public List<Word> importWords() {
        this.wordRepository.deleteAll();
        List<Word> words = this.sourceDeserializer.importWords();
        this.wordRepository.saveAll(words);
        return words;
    }

    public Word getRandomWord(Integer wordLength) {
        List<Word> words = new ArrayList<>();
        for(Word w : this.wordRepository.findAll()) {
            if(w.getValue().length() == wordLength) {
                words.add(w);
            }
        }
        return words.get(rand.nextInt(words.size()));
    }
}
