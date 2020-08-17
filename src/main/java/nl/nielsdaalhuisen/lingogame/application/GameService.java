package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;

import java.util.Optional;
import java.util.UUID;

public class GameService {
    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startGame() {
        Game g = new Game();
        this.gameRepository.save(g);
        return g;
    }

    public Game endGame(UUID gameId) {
        Game g = this.gameRepository.findById(gameId).get();
        g.setStatus(GameStatus.Ended);
        this.gameRepository.save(g);
        return g;
    }
}
