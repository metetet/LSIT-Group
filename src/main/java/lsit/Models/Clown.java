package lsit.Models;

import java.util.UUID;

public class Clown {
    public UUID id;
    public String name;
    public Kind kind;

    public enum Kind {
        Funny, Killer, Investor, Sad, Buff, Hospital, TheActualJokerFromTheJokerMovieThatCameOutIn2019
    }
}
