package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.TurnRepository;
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

    public Round startNewTurn(UUID gameId, Long roundId, String wordRepresentation) throws GameEndedException {
        Round round = this.roundService.getRoundById(roundId);
        Integer index = round.getTurns().size() + 1;
        if(index > round.getMaxTurns()) {
            Game game = this.gameService.endGame(gameId);
            throw new GameEndedException("The game was lost after too many turns.");
        } else {
            Turn turn = new Turn(index, round.getWinningWordLength(), round.getWinningWord());
            if(!wordRepresentation.equals("null")) {
                turn.setWordRepresentation(wordRepresentation);
            }
            Turn savedTurn = this.turnRepository.save(turn);
            return this.roundService.addTurn(roundId, savedTurn);
        }
    }

    public Round processGuess(UUID gameId, Long roundId, Long turnId, String guess) throws GameEndedException, InvalidGuessException {
        Round round = this.roundService.getRoundById(roundId);
        if(guess.length() != round.getWinningWordLength()) {
            throw new InvalidGuessException("Guessed word does not have the right amount of letters.");
        }

        Boolean correctWord = this.feedbackService.evaluateGuess(turnId, round.getWinningWord().getValue(), guess);
        Turn turn = this.turnRepository.findById(turnId).get();
        turn.setGuess(guess);
        if(correctWord) {
            this.roundService.setWin(roundId, correctWord);
        } else {
            List<Feedback> feedbackList = turn.getFeedbackList();
            feedbackList.sort(new Comparator<Feedback>() {
                @Override
                public int compare(Feedback o1, Feedback o2) {
                    return o1.getIdx().compareTo(o2.getIdx());
                }
            });

            String wordRepresentation = "";
            char emptyChar = '_';
            for(int i = 0; i < feedbackList.size(); i++) {
                if(feedbackList.get(i).getValue() == FeedbackValue.correct) {
                    wordRepresentation += feedbackList.get(i).getLetter();
                } else if(turn.getWordRepresentation().charAt(i) != emptyChar) {
                    wordRepresentation += turn.getWordRepresentation().charAt(i);
                } else {
                    wordRepresentation += "_";
                }
            }
            Turn savedTurn = this.turnRepository.save(turn);

            return this.startNewTurn(gameId,roundId, wordRepresentation);
        }
        return this.roundService.getRoundById(roundId);
    }

    public void setFeedback(Long turnId, List<Feedback> feedbackList) {
        Turn turn = this.turnRepository.findById(turnId).get();
        turn.setFeedbackList(feedbackList);
        this.turnRepository.save(turn);
    }
}
