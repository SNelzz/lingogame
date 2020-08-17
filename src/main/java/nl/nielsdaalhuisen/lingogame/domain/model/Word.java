package nl.nielsdaalhuisen.lingogame.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Word implements Serializable {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;
    private String value;

    public Word() {}

    public Word(String word) {
        this.uuid = UUID.randomUUID();
        this.value = word;
    }

    public Word(UUID uuid, String word) {
        this.uuid = uuid;
        this.value = word;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
