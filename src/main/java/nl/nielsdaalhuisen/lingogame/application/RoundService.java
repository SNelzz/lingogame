package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.domain.repository.RoundRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.ElementNotFoundException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RoundService {
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private WordService wordService;
    @Autowired
    private TurnService turnService;

    public Round startNewRound(UUID gameId) throws GameEndedException, ElementNotFoundException {
        if(this.gameService.getGameStatusById(gameId) == GameStatus.Ended) {
            throw new GameEndedException("The game was lost after too many turns.");
        } else {
            Integer wordLength = this.gameService.getNewWordLength(gameId);
            Round round = new Round(this.wordService.getRandomWord(wordLength), wordLength);
            Round savedRound = this.roundRepository.save(round);
            this.turnService.startNewTurn(gameId,savedRound.getId(), "new");
            this.gameService.addRound(gameId, savedRound);
            return savedRound;
        }
    }

    public Round getRoundById(Long roundId) throws ElementNotFoundException {
        return this.roundRepository.findById(roundId).orElseThrow(() -> new ElementNotFoundException("The round was not found"));
    }

    public Round addTurn(Long roundId, Turn turn) throws ElementNotFoundException {
        Round round = this.roundRepository.findById(roundId).orElseThrow(() -> new ElementNotFoundException("The round was not found"));
        round.addTurn(turn);
        return this.roundRepository.save(round);
    }

    public void setWin(UUID gameId, Long roundId, Boolean result) throws ElementNotFoundException {
        Round round = this.roundRepository.findById(roundId).orElseThrow(() -> new ElementNotFoundException("The round was not found"));
        round.setWin(result);
        this.gameService.addScore(gameId, round.calculateScore());
        this.roundRepository.save(round);
    }
}
