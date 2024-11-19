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

import lsit.Models.Car;
import lsit.Models.CarRequest;
import lsit.Models.Clown;
import lsit.Repositories.CarRepository;
import lsit.Services.ClownService;

@RestController
@RequestMapping("/car")
public class CarController {
    private final CarRepository carRepository;
    private final ClownService clownService;

    public CarController(CarRepository carRepository, ClownService clownService){
        this.carRepository = carRepository;
        this.clownService = clownService;
    }

    @GetMapping("")
    public List<Car> list(){
        return carRepository.list();
    }

    @GetMapping("/{id}")
    public Car get(@PathVariable("id") UUID id){
        return carRepository.get(id);
    }

    @PostMapping("")
    public Car add(@RequestBody CarRequest p){
        if (p.id == null)
            p.id = UUID.randomUUID();
        List<Clown> clowns = new ArrayList<>();
        for (int i = 0; i < p.clowns.size(); i++) {
            clowns.add(clownService.get(p.clowns.get(i)));
        }
        Car car = new Car();
        car.id = p.id;
        car.clownList = clowns;

        carRepository.add(car);
        return car;
    }

    @PutMapping("/{id}")
    public Car update(@PathVariable("id") UUID id, @RequestBody Car p){
        p.id = id;
        carRepository.update(p);
        return p;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id){
        carRepository.remove(id);
    }
    
}
