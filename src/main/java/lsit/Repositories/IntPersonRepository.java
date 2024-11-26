package lsit.Repositories;

import java.util.List;
import java.util.UUID;

import lsit.Models.Person;

public interface IntPersonRepository {
    
    void add(Person p);

    Person get(UUID id);

    void remove(UUID id);

    void update(Person p);

    List<Person> list();

}
