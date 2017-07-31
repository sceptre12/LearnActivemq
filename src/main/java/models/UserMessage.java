package models;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Immutable
public class UserMessage implements Serializable {
    private final String user;
    private final LocalDateTime creationDate;

    public UserMessage(String user) {
        this.user = user;
        this.creationDate = LocalDateTime.now();
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "user='" + user + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
