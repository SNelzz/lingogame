package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.TurnRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.ElementNotFoundException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.InvalidGuessException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TurnService {
    @Autowired
    private TurnRepository turnRepository;
    @Autowired
    public RoundService roundService;
    @Autowired
    public GameService gameService;
    @Autowired
    public FeedbackService feedbackService;

    public Round startNewTurn(UUID gameId, Long roundId, String wordRepresentation) throws GameEndedException, ElementNotFoundException {
        Round round = this.roundService.getRoundById(roundId);
        Integer index = round.getTurns().size() + 1;
        if(index > round.getMaxTurns()) {
            this.gameService.endGame(gameId);
            throw new GameEndedException("The game was lost after too many turns.");
        } else {
            Turn turn = new Turn(index, round.getWinningWordLength(), round.getWinningWord());
            if(!wordRepresentation.equals("new")) {
                turn.setWordRepresentation(wordRepresentation);
            }
            Turn savedTurn = this.turnRepository.save(turn);
            return this.roundService.addTurn(roundId, savedTurn);
        }
    }

    public Round processGuess(UUID gameId, Long roundId, Long turnId, String guess) throws GameEndedException, InvalidGuessException, ElementNotFoundException {
        Round round = this.roundService.getRoundById(roundId);
        if(guess.length() != round.getWinningWordLength()) {
            throw new InvalidGuessException("Guessed word does not have the right amount of letters.");
        }

        Boolean correctWord = this.feedbackService.evaluateGuess(turnId, round.getWinningWord().getValue(), guess);
        Turn turn = this.turnRepository.findById(turnId).orElseThrow(() -> new ElementNotFoundException("The turn was not found"));
        turn.setGuess(guess);
        if(correctWord) {
            this.roundService.setWin(gameId, roundId, true);
        } else {
            List<Feedback> feedbackList = turn.getFeedbackList();
            feedbackList.sort(Comparator.comparing(Feedback::getIdx));

            StringBuilder wordRepresentation = new StringBuilder();
            char emptyChar = '_';
            for(int i = 0; i < feedbackList.size(); i++) {
                if(feedbackList.get(i).getValue() == FeedbackValue.correct) {
                    wordRepresentation.append(feedbackList.get(i).getLetter());
                } else if(turn.getWordRepresentation().charAt(i) != emptyChar) {
                    wordRepresentation.append(turn.getWordRepresentation().charAt(i));
                } else {
                    wordRepresentation.append(emptyChar);
                }
            }
            this.turnRepository.save(turn);

            return this.startNewTurn(gameId,roundId, wordRepresentation.toString());
        }
        return this.roundService.getRoundById(roundId);
    }

    public void setFeedback(Long turnId, List<Feedback> feedbackList) throws ElementNotFoundException {
        Turn turn = this.turnRepository.findById(turnId).orElseThrow(() -> new ElementNotFoundException("The turn was not found"));
        turn.setFeedbackList(feedbackList);
        this.turnRepository.save(turn);
    }
}
