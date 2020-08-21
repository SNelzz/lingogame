package nl.nielsdaalhuisen.lingogame.infrastructure.data;

import nl.nielsdaalhuisen.lingogame.domain.model.Word;

import java.util.List;

public interface SourceDeserializer {
    List<Word> importWords();
}
