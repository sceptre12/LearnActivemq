package models;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class UserText extends UserMessage {
    private final String message;

    public UserText(String user,String message) {
        super(user);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "UserText{" +
                "message='" + message + '\'' +
                "} " + super.toString();
    }
}
