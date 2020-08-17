package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.domain.repository.TurnRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TurnService {
    @Autowired
    private TurnRepository turnRepository;

    public Turn startNewTurn()
}
