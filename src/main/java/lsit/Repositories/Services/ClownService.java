package lsit.Repositories.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lsit.Models.Clown;
import lsit.Repositories.IntClownRepository;

@Primary
@Service
public class ClownService implements IntClownService {
    private final IntClownRepository clownRepository;

    public ClownService(IntClownRepository clownRepository) {
        this.clownRepository = clownRepository;
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
