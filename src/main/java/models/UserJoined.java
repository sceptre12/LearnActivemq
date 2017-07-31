package models;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class UserJoined extends UserMessage{

    public UserJoined(String user) {
        super(user);
    }
}
