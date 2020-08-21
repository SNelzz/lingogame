package nl.nielsdaalhuisen.lingogame.service;

import nl.nielsdaalhuisen.lingogame.application.FeedbackService;
import nl.nielsdaalhuisen.lingogame.application.TurnService;
import nl.nielsdaalhuisen.lingogame.domain.model.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.FeedbackRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.ElementNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class LingoGameFeedbackServiceTests {

    @MockBean
    FeedbackRepository feedbackRepository;

    @MockBean
    TurnService turnService;

    @Autowired
    FeedbackService feedbackService;

    @Test
    @DisplayName("Evaluate a new incorrect guess - happy")
    void EvaluateIncorrectGuess() throws ElementNotFoundException {
        Word word = new Word("tester");
        Turn turn = new Turn(1,6, word);
        turn.setId((long) 1);
        String guess = "tseler";

        Mockito.when(feedbackRepository.save(any(Feedback.class))).thenAnswer(i -> i.getArgument(0));

        Boolean evaluation = feedbackService.evaluateGuess(turn.getId(), word.getValue(), guess);

        assertFalse(evaluation);
    }

    @Test
    @DisplayName("Evaluate a new correct guess - happy")
    void EvaluatecorrectGuess() throws ElementNotFoundException {
        Word word = new Word("tester");
        Turn turn = new Turn(1,6, word);
        turn.setId((long) 1);
        String guess = "tester";

        Mockito.when(feedbackRepository.save(any(Feedback.class))).thenAnswer(i -> i.getArgument(0));

        Boolean evaluation = feedbackService.evaluateGuess(turn.getId(), word.getValue(), guess);

        assertTrue(evaluation);
    }

    @Test
    @DisplayName("Evaluate a new invalid guess - happy")
    void EvaluateInvalidGuess() throws ElementNotFoundException {
        Word word = new Word("tester");
        Turn turn = new Turn(1,6, word);
        turn.setId((long) 1);
        String guess = "jester";

        Mockito.when(feedbackRepository.save(any(Feedback.class))).thenAnswer(i -> i.getArgument(0));

        Boolean evaluation = feedbackService.evaluateGuess(turn.getId(), word.getValue(), guess);

        assertFalse(evaluation);
    }
}
