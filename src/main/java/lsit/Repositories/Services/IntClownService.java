package lsit.Repositories.Services;

import java.util.List;
import java.util.UUID;

import lsit.Models.Clown;

public interface IntClownService {
    Clown getClownById(UUID id);
    List<Clown> list();
    Clown get(UUID id);
    void add(Clown p);
    void update(Clown p);
    void remove(UUID id);
    
}
