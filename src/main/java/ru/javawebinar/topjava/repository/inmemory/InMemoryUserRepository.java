package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    public static void main(String[] args) {
        InMemoryUserRepository asd = new InMemoryUserRepository();
        asd.save(new User(2, "bvc", "qwer", "1234", Role.USER));
        asd.save(new User(1, "asd", "eqw", "1234", Role.USER));
        asd.save(new User(3, "cvb", "zxc", "1234", Role.USER));

        System.out.println(asd.getByEmail("eqw"));

        List<User> assd = asd.getAll();
        System.out.println(assd);
        System.out.println(1);
    }

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        user.setId(counter.incrementAndGet());
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream().toList().stream().sorted((x, y) -> y.getName().compareTo(x.getName())).collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Optional<User> result = repository.values().stream().filter(x -> x.getEmail().equals(email)).findFirst();
        return result.orElse(null);
    }
}
