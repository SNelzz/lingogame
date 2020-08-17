package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import nl.nielsdaalhuisen.lingogame.domain.repository.RoundRepository;
import nl.nielsdaalhuisen.lingogame.domain.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RoundService {
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private WordService wordService;

    public Round startRound(UUID gameId) {
        Integer wordLength = this.gameService.getNewWordLength(gameId);
        Round round = new Round(this.wordService.getRandomWord(wordLength), wordLength);
        Round savedRound = this.roundRepository.save(round);
        this.gameService.addRound(gameId,savedRound);
        return savedRound;
    }
}
