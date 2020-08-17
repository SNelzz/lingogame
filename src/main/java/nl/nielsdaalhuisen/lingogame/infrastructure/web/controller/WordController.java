package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.WordService;
import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {
    @Autowired
    private WordService wordService;

    @GetMapping
    public List<Word> getWords() {
        return this.wordService.getWords();
    }

    @GetMapping("import")
    public List<Word> importWords() {
        return this.wordService.importWords();
    }
}
