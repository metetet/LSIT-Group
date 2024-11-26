package lsit.Repositories;

import lsit.Models.Car;
import java.util.UUID;
import java.util.List;

public interface IntCarRepository {

    void add(Car p);

    Car get(UUID id);

    void remove(UUID id);

    void update(Car car);

    List<Car> list();

}
