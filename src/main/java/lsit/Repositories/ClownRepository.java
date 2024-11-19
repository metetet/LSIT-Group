package lsit.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import lsit.Models.Clown;

@Repository
public class ClownRepository {
    static HashMap<UUID, Clown> clowns = new HashMap<>();

        public void add(Clown p){
        clowns.put(p.id, p);
    }

    public Clown get(UUID id){
        return clowns.get(id);
    }

    public void remove(UUID id){
        clowns.remove(id);
    }

    public void update(Clown p){
        Clown x = clowns.get(p.id);
        x.kind = p.kind;
        x.name = p.name;
    }

    public List<Clown> list(){
        return new ArrayList<>(clowns.values());
    }
}
