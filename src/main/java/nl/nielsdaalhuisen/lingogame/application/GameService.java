package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private RoundService roundService;
    @Autowired
    private HighscoreService highscoreService;

    public Game startGame() throws GameEndedException {
        Game g = new Game();
        Game savedGame = this.gameRepository.save(g);
        this.roundService.startNewRound(savedGame.getId());
        return savedGame;
    }

    public Game getGameById(UUID gameId) {
        return this.gameRepository.findById(gameId).orElseThrow();
    }

    public GameStatus getGameStatusById(UUID gameId) {
        return this.gameRepository.findById(gameId).orElseThrow().getStatus();
    }

    public Game endGame(UUID gameId) {
        Game g = this.gameRepository.findById(gameId).orElseThrow();
        g.setStatus(GameStatus.Ended);
        return this.gameRepository.save(g);
    }

    public void addRound(UUID gameId, Round round) {
        Game g = this.gameRepository.findById(gameId).orElseThrow();
        g.addRound(round);
        this.gameRepository.save(g);
    }

    public Integer getNewWordLength(UUID gameId) {
        Game g = this.gameRepository.findById(gameId).orElseThrow();
        List<Round> rounds = g.getRounds();
        if(rounds.isEmpty()) {
            return 5;
        } else {
            Round lastRound = rounds.get(rounds.size() - 1);
            switch (lastRound.getWinningWordLength()) {
                case 5:
                    return 6;
                case 6:
                    return 7;
                default:
                    return 5;
            }
        }
    }

    public void addScore (UUID gameId, Integer score) {
        Game g = this.gameRepository.findById(gameId).orElseThrow();
        g.addScore(score);
        this.gameRepository.save(g);
    }

    public Highscore saveHighscore(UUID gameId, String playerName) {
        Game g = this.gameRepository.findById(gameId).orElseThrow();
        return this.highscoreService.saveHighscore(g.getScore(), playerName);
    }
}
