package lsit.Repositories;

import java.util.List;
import java.util.UUID;

import lsit.Models.Clown;

public interface IntClownRepository {

    void add(Clown p);

    Clown get(UUID id);

    void remove(UUID id);

    void update(Clown p);

    List<Clown> list();

}
