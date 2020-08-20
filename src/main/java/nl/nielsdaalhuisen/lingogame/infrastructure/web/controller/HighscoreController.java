package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.HighscoreService;
import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("highscore")
public class HighscoreController {
    @Autowired
    private HighscoreService highscoreService;

    @GetMapping
    public List<Highscore> getHighscores() {
        return highscoreService.getHighscores();
    }

}
