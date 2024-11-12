package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lsit.Models.Person;
import lsit.Repositories.PersonRepository;

@RestController
public class PersonController {

    PersonRepository personRepository;

    public PersonController(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @GetMapping("/person")
    public List<Person> list(){
        return personRepository.list();
    }

    @GetMapping("/person/{id}")
    public Person get(@PathVariable("id") UUID id){
        return personRepository.get(id);
    }

    @PostMapping("/person")
    public Person add(@RequestBody Person p){
        personRepository.add(p);
        return p;
    }

    @PutMapping("/person/{id}")
    public Person update(@PathVariable("id") UUID id, @RequestBody Person p){
        p.id = id;
        personRepository.update(p);
        return p;
    }

    @DeleteMapping("/person/{id}")
    public void delete(@PathVariable("id") UUID id){
        personRepository.remove(id);
    }
    
}
