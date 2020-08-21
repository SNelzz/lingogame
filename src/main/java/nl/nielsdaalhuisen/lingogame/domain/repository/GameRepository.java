package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GameRepository extends CrudRepository<Game, UUID> {
}
