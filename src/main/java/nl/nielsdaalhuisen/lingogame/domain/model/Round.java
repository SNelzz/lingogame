package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Turn> turns;
    @ManyToOne
    private Word winningWord;
    private Integer winningWordLength;
    private final Integer maxTurns = 5;
    private boolean win;

    public Round() {}

    public Round(Word winningWord) {
        turns = new ArrayList<>();
        this.winningWord = winningWord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
