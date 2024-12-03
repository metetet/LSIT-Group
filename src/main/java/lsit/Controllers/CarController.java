package lsit.Controllers;

import java.util.*;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
//import lsit.Repositories.CarRepository;
import lsit.Repositories.IntCarRepository;
import lsit.Repositories.Services.ClownService;

@RestController
@RequestMapping("/car")
public class CarController {
    private final IntCarRepository carRepository;
    private final ClownService clownService;

    public CarController(IntCarRepository carRepository, ClownService clownService){
        this.carRepository = carRepository;
        this.clownService = clownService;
    }

    @GetMapping("")
    public List<Car> list(OAuth2AuthenticationToken authentication) throws Exception{
        String[] array = {"lsit-ken3239/roles/cc-inc.manager","lsit-ken3239/roles/cc-inc.seller","lsit-ken3239/roles/cc-inc.customer"};
        authenticationMethod(authentication, array);
        return carRepository.list();
    }

    @GetMapping("/{id}")
    public Car get(OAuth2AuthenticationToken authentication,@PathVariable("id") UUID id) throws Exception{
        String[] array = {"lsit-ken3239/roles/cc-inc.manager","lsit-ken3239/roles/cc-inc.seller","lsit-ken3239/roles/cc-inc.customer"};
        authenticationMethod(authentication, array);
        return carRepository.get(id);
    }

    @PostMapping("")
    public Car add(OAuth2AuthenticationToken authentication,@RequestBody CarRequest p) throws Exception{
        String[] array = {"lsit-ken3239/roles/cc-inc.manager"};
        authenticationMethod(authentication, array);
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
    public Car update(OAuth2AuthenticationToken authentication,@PathVariable("id") UUID id, @RequestBody CarRequest p) throws Exception{
        String[] array = {"lsit-ken3239/roles/cc-inc.manager"};
        authenticationMethod(authentication, array);
        p.id = id;
        List<Clown> clowns = new ArrayList<>();
        for (int i = 0; i < p.clowns.size(); i++) {
            clowns.add(clownService.get(p.clowns.get(i)));
        }
        Car car = new Car();
        car.id = p.id;
        car.clownList = clowns;

        carRepository.update(car);
        return car;
    }

    @DeleteMapping("/{id}")
    public void delete(OAuth2AuthenticationToken authentication,@PathVariable("id") UUID id) throws Exception{
        String[] array = {"lsit-ken3239/roles/cc-inc.manager"};
        authenticationMethod(authentication, array);
        carRepository.remove(id);
    }


    public String authenticationMethod(OAuth2AuthenticationToken authentication,String[] array) throws Exception{
        var group = (List<String>)authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        // if(!group.contains(parameter) ){
        //     throw new Exception("Authentication Failure");
        // };

        var userAttributes = authentication.getPrincipal().getAttributes();

        //https://gitlab.org/claims/groups/owner

    //  StringBuilder b = new StringBuilder();
    //  for(var entry: userAttributes.entrySet()){
    //      var s = entry.getKey() + ": " + entry.getValue();
    //      b.append("\n").append(s);
    //  }
        int counter = 0;
        for (int index = 0; index < array.length; index++) {
            if(group.contains(array[index])){
                counter++;
            };
        }
        if(counter == 0){
            throw new Exception("Authentication Failure");
        }
        return "<pre> \n" +
            userAttributes.entrySet().parallelStream().collect(
                StringBuilder::new,
                (s, e) -> s.append(e.getKey()).append(": ").append(e.getValue()),
                (a, b) -> a.append("\n").append(b)
            ) +
            "</pre>";

    }
    
}

