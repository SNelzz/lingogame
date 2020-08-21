package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.application.HighscoreService;
import nl.nielsdaalhuisen.lingogame.application.RoundService;
import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LingogameGameServiceTests {

    @MockBean
    GameRepository gameRepository;

    @MockBean
    HighscoreService highscoreService;

    @MockBean
    RoundService roundService;

    @Autowired
    GameService gameService;

    @Test
    @DisplayName("Start and save a new game - happy")
    void StartAndSaveGame() throws GameEndedException, ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game savedGame = gameService.startGame();

        assertEquals(savedGame.getId(), game.getId());
        assertEquals(savedGame.getStatus(), game.getStatus());
    }

    @Test
    @DisplayName("Get game by Id - happy")
    void GetGameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        Game resultGame = gameService.getGameById(game.getId());

        assertEquals(resultGame.getId(), game.getId());
        assertEquals(resultGame.getStatus(), game.getStatus());
    }

    @Test
    @DisplayName("Get game by Id - unhappy")
    void GetGameByInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> gameService.getGameById(game.getId()), "The game was not found");
    }

    @Test
    @DisplayName("Get game status by Id - happy")
    void GetGameStatusById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        GameStatus gameStatus = gameService.getGameStatusById(game.getId());

        assertEquals(gameStatus, GameStatus.Started);
    }

    @Test
    @DisplayName("Get game status by Id - unhappy")
    void GetGameStatusByInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> gameService.getGameStatusById(game.getId()), "The game was not found");
    }

    @Test
    @DisplayName("End game by Id - happy")
    void EndGameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));
        Mockito.when(gameRepository.save(any(Game.class))).thenReturn(game);

        Game resultGame = gameService.endGame(game.getId());

        assertEquals(resultGame.getId(), game.getId());
        assertEquals(resultGame.getStatus(), GameStatus.Ended);
    }

    @Test
    @DisplayName("End game by Id - unhappy")
    void EndGameByInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> gameService.endGame(game.getId()), "The game was not found");
    }

    @Test
    @DisplayName("Add round to game by Id - happy")
    void AddRoundToGameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        gameService.addRound(game.getId(), round);

        verify(gameRepository).save(game);
    }

    @Test
    @DisplayName("Add round to game by Id - unhappy")
    void AddRoundToGameInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () ->  gameService.addRound(game.getId(), round), "The game was not found");
    }

    @Test
    @DisplayName("Get new word length is 5 for game by Id - happy")
    void GetNewWordlength5GameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        Integer wordLength = gameService.getNewWordLength(game.getId());

        assertEquals(wordLength, 5);
    }

    @Test
    @DisplayName("Get new word length is 6 for game by Id - happy")
    void GetNewWordlength6GameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(5);

        List<Round> rounds = new ArrayList<>();
        rounds.add(round);
        game.setRounds(rounds);

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        Integer wordLength = gameService.getNewWordLength(game.getId());

        assertEquals(wordLength, 6);
    }

    @Test
    @DisplayName("Get new word length is 7 for game by Id - happy")
    void GetNewWordlength7GameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        List<Round> rounds = new ArrayList<>();
        rounds.add(round);
        game.setRounds(rounds);

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        Integer wordLength = gameService.getNewWordLength(game.getId());

        assertEquals(wordLength, 7);
    }

    @Test
    @DisplayName("Get new word length for game by Id - unhappy")
    void GetNewWordlengthGameByInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> gameService.getNewWordLength(game.getId()), "The game was not found");
    }

    @Test
    @DisplayName("Add score to game by Id - happy")
    void AddScoreToGameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());
        Integer score = 15;

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));

        gameService.addScore(game.getId(), score);

        verify(gameRepository).save(game);
    }

    @Test
    @DisplayName("Add score to game by Id - unhappy")
    void AddScoreToGameInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());
        Integer score = 15;

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () ->  gameService.addScore(game.getId(), score), "The game was not found");
    }

    @Test
    @DisplayName("Save highscore of game by Id - happy")
    void SaveHighscoreGameById() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        game.setScore(35);
        String playerName = "testPlayer";
        Highscore highscore = new Highscore(playerName, game.getScore());

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.of(game));
        Mockito.when(highscoreService.saveHighscore(any(Integer.class), any(String.class))).thenReturn(highscore);

        Highscore savedHighscore = gameService.saveHighscore(game.getId(), playerName);

        assertEquals(savedHighscore.getScore(), game.getScore());
        assertEquals(savedHighscore.getPlayerName(), playerName);
    }

    @Test
    @DisplayName("Save highscore of game by Id - unhappy")
    void SaveHighscoreGameByInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());
        game.setScore(35);
        String playerName = "testPlayer";

        Mockito.when(gameRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> gameService.saveHighscore(game.getId(), playerName), "The game was not found");
    }
}
