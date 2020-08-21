package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.domain.model.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LingogameApplicationTests {

    @Test
    @DisplayName("calculate score of a round - happy")
    void CalculateScore() {
        Word word = new Word("tester");
        Turn turn1 = new Turn(1,6, word);
        Turn turn2 = new Turn(2,6, word);
        turn1.setGuess("tisler");
        turn2.setGuess("tester");
        List<Turn> turns = new ArrayList<>();
        turns.add(turn1);
        turns.add(turn2);

        Round round = new Round(word,6);
        round.setTurns(turns);

        assertEquals(round.calculateScore(), 30);
    }
}
