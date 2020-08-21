package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import nl.nielsdaalhuisen.lingogame.domain.repository.HighscoreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class HighscoreService {
    @Autowired
    private HighscoreRepository highscoreRepository;

    public Highscore saveHighscore(Integer gameScore, String playerName) {
        Highscore highscore = new Highscore(playerName, gameScore);
        return this.highscoreRepository.save(highscore);
    }

    public List<Highscore> getHighscores() {
        List<Highscore> highscores = (List<Highscore>) this.highscoreRepository.findAll();
        highscores.sort((h1, h2) -> h2.getScore().compareTo(h1.getScore()));
        return highscores.stream().limit(10).collect(Collectors.toList());
    }
}
