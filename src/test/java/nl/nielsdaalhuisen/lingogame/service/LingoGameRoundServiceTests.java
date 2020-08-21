package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.application.RoundService;
import nl.nielsdaalhuisen.lingogame.application.TurnService;
import nl.nielsdaalhuisen.lingogame.application.WordService;
import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.RoundRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LingoGameRoundServiceTests {

    @MockBean
    RoundRepository roundRepository;

    @MockBean
    GameService gameService;

    @MockBean
    WordService wordService;

    @MockBean
    TurnService turnService;

    @Autowired
    RoundService roundService;

    @Test
    @DisplayName("Start and save a new round - happy")
    void StartAndSaveNewRound() throws GameEndedException, ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(gameService.getNewWordLength(any(UUID.class))).thenReturn(6);
        Mockito.when(wordService.getRandomWord(any(Integer.class))).thenReturn(word);
        Mockito.when(roundRepository.save(any(Round.class))).thenReturn(round);

        Round savedRound = roundService.startNewRound(game.getId());

        assertEquals(savedRound.getWinningWord(), word);
        assertEquals(savedRound.getWinningWordLength(), 6);
    }

    @Test
    @DisplayName("Add round to game by Id after round ended - unhappy")
    void StartAndSaveNewRoundEndedGame() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Ended);
        game.setId(UUID.randomUUID());

        Mockito.when(gameService.getGameStatusById(any(UUID.class))).thenReturn(game.getStatus());


        assertThrows(GameEndedException.class, () ->  roundService.startNewRound(game.getId()), "The game was lost after too many turns.");
    }

    @Test
    @DisplayName("Get round by Id - happy")
    void GetRoundById() throws ElementNotFoundException {
        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.of(round));

        Round resultRound = roundService.getRoundById(round.getId());

        assertEquals(resultRound.getId(), round.getId());
        assertEquals(resultRound.getWinningWordLength(), round.getWinningWordLength());
        assertEquals(resultRound.getWinningWord(), round.getWinningWord());
    }

    @Test
    @DisplayName("Get round by Id - unhappy")
    void GetRoundByInvalidId() {
        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> roundService.getRoundById(round.getId()), "The round was not found");
    }

    @Test
    @DisplayName("Add turn to round by Id - happy")
    void AddTurnToRoundById() throws ElementNotFoundException {
        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        List<Turn> turns = new ArrayList<>();
        turns.add(turn);
        round.setTurns(turns);

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.of(round));
        Mockito.when(roundRepository.save(any(Round.class))).thenReturn(round);

        Round savedRound = roundService.addTurn(round.getId(), turn);

        assertEquals(savedRound.getId(), round.getId());
        assertEquals(savedRound.getWinningWord(), round.getWinningWord());
        assertEquals(savedRound.getWinningWordLength(), round.getWinningWordLength());
        assertEquals(savedRound.getTurns(), round.getTurns());
    }

    @Test
    @DisplayName("Add turn to round by Id - unhappy")
    void AddTurnToRoundInvalidId() {
        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> roundService.addTurn(round.getId(), turn), "The round was not found");
    }

    @Test
    @DisplayName("Set Round win - happy")
    void SetWinToRound() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.of(round));

        roundService.setWin(game.getId(), round.getId(), true);

        verify(roundRepository).save(round);
    }

    @Test
    @DisplayName("Set round win - unhappy")
    void SetWinToRoundInvalidId() {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(roundRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () ->  roundService.setWin(game.getId(), round.getId(), true), "The game was not found");
    }
}
