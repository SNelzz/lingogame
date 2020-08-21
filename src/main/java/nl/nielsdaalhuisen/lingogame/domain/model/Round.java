package nl.nielsdaalhuisen.lingogame.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Turn> turns = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    private Word winningWord;
    private Integer winningWordLength;
    private final Integer maxTurns = 5;
    private boolean win;

    public Round() {}

    public Round(Word winningWord, Integer winningWordLength) {
        this.winningWordLength = winningWordLength;
        this.winningWord = winningWord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxTurns() {
        return maxTurns;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Word getWinningWord() {
        return winningWord;
    }

    public void setWinningWord(Word winningWord) {
        this.winningWord = winningWord;
    }

    public Integer getWinningWordLength() {
        return winningWordLength;
    }

    public void setWinningWordLength(Integer winningWordLength) {
        this.winningWordLength = winningWordLength;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void addTurn(Turn turn) {
        this.turns.add(turn);
    }

    public Integer calculateScore() {
        int score = 0;
        switch (this.winningWordLength) {
            case 5:
                score += 10;
                break;
            case 6:
                score += 15;
                break;
            case 7:
                score += 20;
                break;
        }
        score += (maxTurns - turns.size()) * 5;
        return score;
    }
}
