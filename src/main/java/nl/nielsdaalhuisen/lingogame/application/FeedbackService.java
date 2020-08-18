package nl.nielsdaalhuisen.lingogame.application;

import nl.nielsdaalhuisen.lingogame.domain.model.Feedback;
import nl.nielsdaalhuisen.lingogame.domain.model.FeedbackValue;
import nl.nielsdaalhuisen.lingogame.domain.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FeedbackService {
    @Autowired
    public FeedbackRepository feedbackRepository;
    @Autowired
    public TurnService turnService;

    public Boolean evaluateGuess(Long turnId, String winningWord, String guess) {
        char[] guessChars = guess.toCharArray();
        char[] winningChars = winningWord.toCharArray();
        List<Feedback> feedbackList = new ArrayList<>();
        Boolean result = false;
        if(guessChars[0] != winningChars[0]) {
            for (int i = 0; i < guessChars.length; i++) {
                Feedback feedback = new Feedback(i+1, guessChars[i], FeedbackValue.invalid);
                Feedback savedFeedback = this.feedbackRepository.save(feedback);
                feedbackList.add(savedFeedback);
            }
        } else if(guess.equals(winningWord)) {
            for (int i = 0; i < guessChars.length; i++) {
                Feedback feedback = new Feedback(i+1, guessChars[i], FeedbackValue.correct);
                Feedback savedFeedback = this.feedbackRepository.save(feedback);
                feedbackList.add(savedFeedback);
                result = true;
            }
        } else {
            for (int i = 0; i < guessChars.length; i++) {
                if(guessChars[i] == winningChars[i]) {
                    Feedback feedback = new Feedback(i+1, guessChars[i], FeedbackValue.correct);
                    Feedback savedFeedback = this.feedbackRepository.save(feedback);
                    feedbackList.add(savedFeedback);
                    guessChars[i] = 0;
                    winningChars[i] = 0;
                }
            }
            for (int i = 0; i < guessChars.length; i++) {
                if (guessChars[i] != 0) {
                    for (int j = 0; j < winningChars.length; j++) {
                        if (guessChars[i] == winningChars[j]) {
                            Feedback feedback = new Feedback(i + 1, guessChars[i], FeedbackValue.present);
                            Feedback savedFeedback = this.feedbackRepository.save(feedback);
                            feedbackList.add(savedFeedback);
                            guessChars[i] = 0;
                            winningChars[j] = 0;
                            break;
                        }
                    }
                    if(guessChars[i] != 0) {
                        Feedback feedback = new Feedback(i + 1, guessChars[i], FeedbackValue.absent);
                        Feedback savedFeedback = this.feedbackRepository.save(feedback);
                        feedbackList.add(savedFeedback);
                        guessChars[i] = 0;
                    }
                }
            }
        }
        this.turnService.setFeedback(turnId, feedbackList);
        return result;
    }
}
