package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.domain.repository.TurnRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class TurnService {
    @Autowired
    private TurnRepository turnRepository;
    @Autowired
    public RoundService roundService;
    @Autowired
    public GameService gameService;

    public Turn startNewTurn(UUID gameId, Long roundId) throws GameEndedException {
        Round round = this.roundService.getRoundById(roundId);
        Integer index = round.getTurns().size() + 1;
        if(index > round.getMaxTurns()) {
            Game game = this.gameService.endGame(gameId);
            throw new GameEndedException("The game was lost after too many turns.");
        } else {
            Turn turn = new Turn(index, round.getWinningWordLength(), round.getWinningWord());
            Turn savedTurn = this.turnRepository.save(turn);
            this.roundService.addTurn(roundId, savedTurn);
            return turn;
        }
    }
}
