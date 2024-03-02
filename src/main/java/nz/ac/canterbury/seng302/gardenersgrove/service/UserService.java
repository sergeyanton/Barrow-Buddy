package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.getUserByEmailAndPassword(email, password);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public Boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

//    public User findEmail(String email) {
//        Optional<User> user = UserRepository.findByEmail(email);
//        return user.orElse(null);
//    }
}
