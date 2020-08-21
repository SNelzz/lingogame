package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import org.springframework.data.repository.CrudRepository;

public interface WordRepository extends CrudRepository<Word, Long> {
}
