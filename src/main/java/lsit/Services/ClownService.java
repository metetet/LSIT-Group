package lsit.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lsit.Models.Clown;
import lsit.Repositories.ClownRepository;

@Service
public class ClownService {
    private final ClownRepository clownRepository;

    public ClownService(ClownRepository clownRepository) {
        this.clownRepository = clownRepository;
    }

    public Clown getClownById(UUID id) {
        return clownRepository.get(id); 
    }

    public List<Clown> list() {
        return clownRepository.list();
    }

    public Clown get(UUID id) {
        return clownRepository.get(id);
    }

    public void add(Clown p) {
        clownRepository.add(p);
    }

    public void update(Clown p) {
        clownRepository.update(p);
    }

    public void remove(UUID id) {
        clownRepository.remove(id);
    }
}
