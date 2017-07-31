package models;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class HeartBeat extends UserMessage{
    public HeartBeat(String user) {
        super(user);
    }
}
