package lsit.Models;

import java.util.UUID;

public class Person {
    public UUID id;
    public Role role;

    public enum Role{
        Customer, Seller, Manager
    }
    
}
