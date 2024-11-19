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

import lsit.Models.Clown;
import lsit.Services.ClownService;

@RestController
@RequestMapping("/clown")
public class ClownController {
    ClownService clownService;

    public ClownController(ClownService clownService){
        this.clownService = clownService;
    }

    @GetMapping("")
    public List<Clown> list(){
        return clownService.list();
    }

    @GetMapping("/{id}")
    public Clown get(@PathVariable("id") UUID id){
        return clownService.get(id);
    }

    @PostMapping("")
    public Clown add(@RequestBody Clown p){
        if (p.id == null)
            p.id = UUID.randomUUID();
        clownService.add(p);
        return p;
    }

    @PutMapping("/{id}")
    public Clown update(@PathVariable("id") UUID id, @RequestBody Clown p){
        p.id = id;
        clownService.update(p);
        return p;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id){
        clownService.remove(id);
    }
    
}
