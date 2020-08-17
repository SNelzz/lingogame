package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import nl.nielsdaalhuisen.lingogame.domain.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RoundService {
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private GameRepository gameRepository;

    /*public Round startRound(UUID gameId) {
    }*/
}
