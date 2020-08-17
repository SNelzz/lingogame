package nl.nielsdaalhuisen.lingogame.domain.repository;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface WordRepository extends CrudRepository<Word, UUID> {
}
