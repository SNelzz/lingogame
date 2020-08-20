package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import org.springframework.data.repository.CrudRepository;

public interface HighscoreRepository extends CrudRepository<Highscore, Long> {
}
