package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import nl.nielsdaalhuisen.lingogame.domain.repository.RoundRepository;
import nl.nielsdaalhuisen.lingogame.domain.repository.WordRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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

    public Round startNewRound(UUID gameId) throws GameEndedException {
        if(this.gameService.getGameStatusById(gameId) == GameStatus.Ended) {
            throw new GameEndedException("The game was lost after too many turns.");
        } else {
            Integer wordLength = this.gameService.getNewWordLength(gameId);
            Round round = new Round(this.wordService.getRandomWord(wordLength), wordLength);
            Round savedRound = this.roundRepository.save(round);
            this.turnService.startNewTurn(gameId,savedRound.getId(), "null");
            this.gameService.addRound(gameId, savedRound);
            return savedRound;
        }
    }

    public Round getRoundById(Long roundId) {
        return this.roundRepository.findById(roundId).get();
    }

    public Round addTurn(Long roundId, Turn turn) {
        Round round = this.roundRepository.findById(roundId).get();
        round.addTurn(turn);
        return this.roundRepository.save(round);
    }

    public void setWin(Long roundId, Boolean result) {
        Round round = this.roundRepository.findById(roundId).get();
        round.setWin(result);
        this.roundRepository.save(round);
    }
}
