package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Person;

@Repository
public class PersonRepository implements IntPersonRepository{

    static HashMap<UUID, Person> person = new HashMap<>();

    public void add(Person p){
        person.put(p.id, p);
    }

    public Person get(UUID id){
        return person.get(id);
    }

    public void remove(UUID id){
        person.remove(id);
    }

    public void update(Person p){
        Person x = person.get(p.id);
        x.role = p.role;
    }

    public List<Person> list(){
        return new ArrayList<>(person.values());
    }
    
}
