package be.neostoffyn.campus.service;

import be.neostoffyn.campus.exception.User.UserAlreadyExistsException;
import be.neostoffyn.campus.exception.User.UserInvalidDataException;
import be.neostoffyn.campus.exception.User.UserNotFoundException;
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(String nameMatches) {
        if (nameMatches != null && !nameMatches.isEmpty()) {
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(nameMatches, nameMatches);
        }
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User createUser(User user) {
        validateUser(user);
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updated) {
        User existing = getUserById(id);
        validateUser(updated);

        boolean emailChanged = updated.getEmail() != null
                && !updated.getEmail().equalsIgnoreCase(existing.getEmail());

        if (emailChanged && userRepository.existsByEmailIgnoreCaseAndIdNot(updated.getEmail(), id)) {
            throw new UserAlreadyExistsException("Email already in use: " + updated.getEmail());
        }

        existing.setEmail(updated.getEmail());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        return userRepository.save(existing);
    }

   
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserInvalidDataException("Email is required");
        }
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new UserInvalidDataException("Email is invalid");
        }
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new UserInvalidDataException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new UserInvalidDataException("Last name is required");
        }
    }
}
