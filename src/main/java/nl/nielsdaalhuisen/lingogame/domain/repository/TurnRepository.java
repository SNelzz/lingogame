package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import org.springframework.data.repository.CrudRepository;

public interface TurnRepository extends CrudRepository<Turn, Long> {
}
