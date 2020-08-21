package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String value;

    public Word() {}

    public Word(String word) {
        this.value = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
