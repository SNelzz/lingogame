package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Feedback> feedbackList = new ArrayList<>();
    private String guess;
    private Integer idx;
    private String wordRepresentation;

    public Turn() {}

    public Turn(Integer index, Integer winningWordLength, Word winningWord) {
        this.idx = index;
        this.wordRepresentation = String.format("%1$-" + winningWordLength + "s", winningWord.getValue().charAt(0)).replace(' ', '_');
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer index) {
        this.idx = index;
    }

    public String getWordRepresentation() {
        return wordRepresentation;
    }

    public void setWordRepresentation(String wordRepresentation) {
        this.wordRepresentation = wordRepresentation;
    }
}
