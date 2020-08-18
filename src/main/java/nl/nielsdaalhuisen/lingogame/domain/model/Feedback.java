package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer idx;
    private char letter;
    private FeedbackValue value;

    public Feedback() {}

    public Feedback(Integer index, char letter, FeedbackValue feedbackValue) {
        this.idx = index;
        this.letter = letter;
        this.value = feedbackValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer index) {
        this.idx = index;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public FeedbackValue getValue() {
        return value;
    }

    public void setValue(FeedbackValue value) {
        this.value = value;
    }
}
