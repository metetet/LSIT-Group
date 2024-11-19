package lsit.Controllers;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lsit.Models.Person;
import lsit.Repositories.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {
    PersonRepository personRepository;

    public PersonController(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @GetMapping("")
    public List<Person> list(){
        return personRepository.list();
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable("id") UUID id){
        return personRepository.get(id);
    }

    @PostMapping("")
    public Person add(@RequestBody Person p){
        if (p.id == null)
            p.id = UUID.randomUUID();
        personRepository.add(p);
        return p;
    }

    @PutMapping("/{id}")
    public Person update(@PathVariable("id") UUID id, @RequestBody Person p){
        p.id = id;
        personRepository.update(p);
        return p;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id){
        personRepository.remove(id);
    }
    
}
