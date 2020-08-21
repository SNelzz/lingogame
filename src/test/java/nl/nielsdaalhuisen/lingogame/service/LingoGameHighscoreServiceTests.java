package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.HighscoreService;
import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import nl.nielsdaalhuisen.lingogame.domain.repository.HighscoreRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.ElementNotFoundException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class LingoGameHighscoreServiceTests {

    @MockBean
    HighscoreRepository highscoreRepository;

    @Autowired
    HighscoreService highscoreService;

    @Test
    @DisplayName("Save a new highscore - happy")
    void SaveHighscore() {
        Highscore highscore = new Highscore("testPlayer", 150);

        Mockito.when(highscoreRepository.save(any(Highscore.class))).thenReturn(highscore);

        Highscore savedHighscore = highscoreService.saveHighscore(highscore.getScore(),highscore.getPlayerName());

        assertEquals(savedHighscore.getPlayerName(), highscore.getPlayerName());
        assertEquals(savedHighscore.getScore(), highscore.getScore());
    }

    @Test
    @DisplayName("Get list of highscores - happy")
    void GetListHighscore() {
        Highscore highscore1 = new Highscore("testPlayer1", 150);
        Highscore highscore2 = new Highscore("testPlayer2", 100);
        Highscore highscore3 = new Highscore("testPlayer3", 110);
        Highscore highscore4 = new Highscore("testPlayer4", 120);
        Highscore highscore5 = new Highscore("testPlayer5", 160);
        Highscore highscore6 = new Highscore("testPlayer6", 130);
        Highscore highscore7 = new Highscore("testPlayer7", 90);
        Highscore highscore8 = new Highscore("testPlayer8", 70);
        Highscore highscore9 = new Highscore("testPlayer9", 80);
        Highscore highscore10 = new Highscore("testPlayer10", 200);
        Highscore highscore11 = new Highscore("testPlayer11", 190);
        Highscore highscore12 = new Highscore("testPlayer12", 210);

        List<Highscore> highscores = new ArrayList<>();
        highscores.add(highscore1);
        highscores.add(highscore2);
        highscores.add(highscore3);
        highscores.add(highscore4);
        highscores.add(highscore5);
        highscores.add(highscore6);
        highscores.add(highscore7);
        highscores.add(highscore8);
        highscores.add(highscore9);
        highscores.add(highscore10);
        highscores.add(highscore11);
        highscores.add(highscore12);


        Mockito.when(highscoreRepository.findAll()).thenReturn(highscores);

        List<Highscore> topHighscores = highscoreService.getHighscores();

        assertEquals(topHighscores.size(), 10);
    }
}
