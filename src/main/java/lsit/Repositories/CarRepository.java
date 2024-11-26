package lsit.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import lsit.Models.Car;

@Repository
public class CarRepository implements IntCarRepository{
    static HashMap<UUID, Car> cars = new HashMap<>();

    public void add(Car p){
        cars.put(p.id, p);
    }

    public Car get(UUID id){
        return cars.get(id);
    }

    public void remove(UUID id){
        cars.remove(id);
    }

    public void update(Car p){
        Car x = cars.get(p.id);
        x.clownList = p.clownList;
    }

    public List<Car> list(){
        return new ArrayList<>(cars.values());
    }
}
