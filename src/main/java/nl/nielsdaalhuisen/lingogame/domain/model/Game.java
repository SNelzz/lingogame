package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Game {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @OneToMany
    private List<Round> rounds = new ArrayList<>();
    private Integer score;
    private GameStatus status;

    public Game() {
        this.status = GameStatus.Started;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void addScore(Integer roundScore) {
        this.score += roundScore;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round round) {
        this.rounds.add(round);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
