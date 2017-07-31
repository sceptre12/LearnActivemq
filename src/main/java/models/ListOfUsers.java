package models;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Set;

@Immutable
public class ListOfUsers extends UserMessage {
    private Set<UserJoined> listOfUsers;

    public ListOfUsers(String user,Set<UserJoined> listOfUsers) {
        super(user);
        this.listOfUsers = listOfUsers;
    }

    public Set<UserJoined> getListOfUsers() {
        return listOfUsers;
    }
}
