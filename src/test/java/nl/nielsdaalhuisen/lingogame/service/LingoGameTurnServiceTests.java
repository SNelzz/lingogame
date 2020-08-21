package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.FeedbackService;
import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.application.RoundService;
import nl.nielsdaalhuisen.lingogame.application.TurnService;
import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.TurnRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.ElementNotFoundException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.InvalidGuessException;
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
public class LingoGameTurnServiceTests {

    @MockBean
    TurnRepository turnRepository;

    @MockBean
    RoundService roundService;

    @MockBean
    GameService gameService;

    @MockBean
    FeedbackService feedbackService;

    @Autowired
    TurnService turnService;

    @Test
    @DisplayName("Start and save a new turn - happy")
    void StartAndSaveNewTurn() throws GameEndedException, ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        List<Turn> turns = new ArrayList<>();
        turns.add(turn);
        round.setTurns(turns);

        Mockito.when(roundService.getRoundById(any(Long.class))).thenReturn(round);
        Mockito.when(turnRepository.save(any(Turn.class))).thenReturn(turn);
        Mockito.when(roundService.addTurn(any(Long.class), any(Turn.class))).thenReturn(round);

        Round savedRound = turnService.startNewTurn(game.getId(), round.getId(), "new");
        Turn savedTurn = savedRound.getTurns().get(0);

        assertEquals(savedRound.getWinningWord(), word);
        assertEquals(savedRound.getWinningWordLength(), 6);
        assertEquals(savedTurn.getWordRepresentation(), turn.getWordRepresentation());
    }

    @Test
    @DisplayName("Start and save a new turn after too many turns - unhappy")
    void StartAndSaveNewTurnAfterTooMany() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn1 = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        Turn turn2 = new Turn(2,round.getWinningWordLength(), round.getWinningWord());
        Turn turn3 = new Turn(3,round.getWinningWordLength(), round.getWinningWord());
        Turn turn4 = new Turn(4,round.getWinningWordLength(), round.getWinningWord());
        Turn turn5 = new Turn(5,round.getWinningWordLength(), round.getWinningWord());
        List<Turn> turns = new ArrayList<>();
        turns.add(turn1);
        turns.add(turn2);
        turns.add(turn3);
        turns.add(turn4);
        turns.add(turn5);
        round.setTurns(turns);

        Mockito.when(roundService.getRoundById(any(Long.class))).thenReturn(round);

        assertThrows(GameEndedException.class, () ->  turnService.startNewTurn(game.getId(), round.getId(), "new"), "The game was lost after too many turns.");
    }

    @Test
    @DisplayName("Process new correct guess - happy")
    void ProcessNewCorrectGuess() throws GameEndedException, ElementNotFoundException, InvalidGuessException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        turn.setId((long) 2);
        List<Turn> turns = new ArrayList<>();
        turns.add(turn);
        round.setTurns(turns);

        String guess = "tester";

        Mockito.when(roundService.getRoundById(any(Long.class))).thenReturn(round);
        Mockito.when(feedbackService.evaluateGuess(any(Long.class), any(String.class), any(String.class))).thenReturn(true);
        Mockito.when(turnRepository.findById(any(Long.class))).thenReturn(Optional.of(turn));

        Round savedRound = turnService.processGuess(game.getId(), round.getId(), turn.getId(), guess);
        Turn savedTurn = savedRound.getTurns().get(0);

        verify(roundService).setWin(game.getId(), round.getId(), true);
        assertEquals(savedRound.getWinningWord(), word);
        assertEquals(savedRound.getWinningWordLength(), 6);
        assertEquals(savedTurn.getWordRepresentation(), turn.getWordRepresentation());
        assertEquals(savedTurn.getGuess(), turn.getGuess());
    }

    @Test
    @DisplayName("Process new incorrect guess - happy")
    void ProcessNewIncorrectGuess() throws GameEndedException, ElementNotFoundException, InvalidGuessException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        turn.setId((long) 2);
        List<Turn> turns = new ArrayList<>();

        String guess = "tisler";

        Feedback feedback1 = new Feedback(1, 't', FeedbackValue.correct);
        Feedback feedback2 = new Feedback(2, 'i', FeedbackValue.absent);
        Feedback feedback3 = new Feedback(3, 's', FeedbackValue.correct);
        Feedback feedback4 = new Feedback(4, 'l', FeedbackValue.absent);
        Feedback feedback5 = new Feedback(5, 'e', FeedbackValue.correct);
        Feedback feedback6 = new Feedback(6, 'r', FeedbackValue.correct);
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback1);
        feedbackList.add(feedback2);
        feedbackList.add(feedback3);
        feedbackList.add(feedback4);
        feedbackList.add(feedback5);
        feedbackList.add(feedback6);
        turn.setFeedbackList(feedbackList);

        turns.add(turn);
        round.setTurns(turns);

        Mockito.when(roundService.getRoundById(any(Long.class))).thenReturn(round);
        Mockito.when(feedbackService.evaluateGuess(any(Long.class), any(String.class), any(String.class))).thenReturn(false);
        Mockito.when(turnRepository.findById(any(Long.class))).thenReturn(Optional.of(turn));
        Mockito.when(turnRepository.save(any(Turn.class))).thenReturn(turn);
        Mockito.when(roundService.addTurn(any(Long.class), any(Turn.class))).thenReturn(round);

        Round savedRound = turnService.processGuess(game.getId(), round.getId(), turn.getId(), guess);
        Turn savedTurn = savedRound.getTurns().get(0);

        assertEquals(savedRound.getWinningWord(), word);
        assertEquals(savedRound.getWinningWordLength(), 6);
        assertEquals(savedTurn.getWordRepresentation(), turn.getWordRepresentation());
        assertEquals(savedTurn.getGuess(), turn.getGuess());
        assertEquals(savedTurn.getFeedbackList(), turn.getFeedbackList());
    }

    @Test
    @DisplayName("Process new guess - unhappy")
    void ProcessNewInvalidGuess() throws ElementNotFoundException {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Word word = new Word("tester");
        Round round = new Round();
        round.setId((long) 1);
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Turn turn = new Turn(1,round.getWinningWordLength(), round.getWinningWord());
        turn.setId((long) 2);
        List<Turn> turns = new ArrayList<>();
        turns.add(turn);
        round.setTurns(turns);

        String guess = "teveelletters";

        Mockito.when(roundService.getRoundById(any(Long.class))).thenReturn(round);

        assertThrows(InvalidGuessException.class, () ->  turnService.processGuess(game.getId(), round.getId(), turn.getId(), guess), "Guessed word does not have the right amount of letters.");
    }

    @Test
    @DisplayName("Set feedback of turn - happy")
    void SetFeedbackOfTurn() throws ElementNotFoundException {
        Word word = new Word("tester");
        Turn turn = new Turn(1,6, word);
        turn.setId((long) 1);

        String guess = "tisler";
        turn.setGuess(guess);

        Feedback feedback1 = new Feedback(1, 't', FeedbackValue.correct);
        Feedback feedback2 = new Feedback(2, 'i', FeedbackValue.absent);
        Feedback feedback3 = new Feedback(3, 's', FeedbackValue.correct);
        Feedback feedback4 = new Feedback(4, 'l', FeedbackValue.absent);
        Feedback feedback5 = new Feedback(5, 'e', FeedbackValue.correct);
        Feedback feedback6 = new Feedback(6, 'r', FeedbackValue.correct);
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback1);
        feedbackList.add(feedback2);
        feedbackList.add(feedback3);
        feedbackList.add(feedback4);
        feedbackList.add(feedback5);
        feedbackList.add(feedback6);
        turn.setFeedbackList(feedbackList);

        Mockito.when(turnRepository.findById(any(Long.class))).thenReturn(Optional.of(turn));

        turnService.setFeedback(turn.getId(),feedbackList);

        verify(turnRepository).save(turn);
    }

    @Test
    @DisplayName("Set feedback of turn - unhappy")
    void SetFeedbackOfTurnInvalidId() {
        Word word = new Word("tester");
        Turn turn = new Turn(1,6, word);
        turn.setId((long) 1);

        String guess = "tisler";
        turn.setGuess(guess);

        Feedback feedback1 = new Feedback(1, 't', FeedbackValue.correct);
        Feedback feedback2 = new Feedback(2, 'i', FeedbackValue.absent);
        Feedback feedback3 = new Feedback(3, 's', FeedbackValue.correct);
        Feedback feedback4 = new Feedback(4, 'l', FeedbackValue.absent);
        Feedback feedback5 = new Feedback(5, 'e', FeedbackValue.correct);
        Feedback feedback6 = new Feedback(6, 'r', FeedbackValue.correct);
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback1);
        feedbackList.add(feedback2);
        feedbackList.add(feedback3);
        feedbackList.add(feedback4);
        feedbackList.add(feedback5);
        feedbackList.add(feedback6);
        turn.setFeedbackList(feedbackList);

        Mockito.when(turnRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () ->  turnService.setFeedback(turn.getId(),feedbackList), "The turn was not found");
    }
}
