package de.thowl.notetilus.storage.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name= "Sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private Date createdAt;
    private String authToken;
    private int userId;

    public Session(String authToken, User u) {
        this.authToken = authToken;
        this.userId = u.getId();
        this.createdAt = new Date();
    }

}
