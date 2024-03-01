package nz.ac.canterbury.seng302.gardenersgrove.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository UserRepository;

    public UserService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    // Gets all Users
    public List<User> getUser() {
        return UserRepository.findAll();
    }

    // Adds a User
    public User addUser(User User) {
        return UserRepository.save(User);
    }

    public Boolean checkEmail(String email) {
        Optional<User> user = UserRepository.findByEmail(email);
        return user.isPresent();
    }

}
