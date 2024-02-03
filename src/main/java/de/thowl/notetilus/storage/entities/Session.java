package de.thowl.notetilus.storage.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Sessions")
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String authToken;
    private Date createdAt;
    private int userId;

    public Session(String USID, User usr) {
        this.authToken = USID;
        this.userId = usr.getId();
        this.createdAt = new Date();
    }
}
