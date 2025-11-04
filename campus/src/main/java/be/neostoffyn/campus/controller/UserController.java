package be.neostoffyn.campus.controller;

import be.neostoffyn.campus.error.FieldMessage;
import be.neostoffyn.campus.exception.User.UserAlreadyExistsException;
import be.neostoffyn.campus.exception.User.UserInvalidDataException;
import be.neostoffyn.campus.exception.User.UserNotFoundException;
import be.neostoffyn.campus.model.User;
import be.neostoffyn.campus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /user
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) String nameMatches
    ) {
        return ResponseEntity.ok(userService.getAllUsers(nameMatches));
    }

    // GET /user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // POST /user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /user/{id}
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // DELETE /user/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // EXCEPTIONS
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<FieldMessage> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new FieldMessage("user", ex.getMessage()));
    }

    @ExceptionHandler({
            UserInvalidDataException.class,
            UserAlreadyExistsException.class
    })
    public ResponseEntity<FieldMessage> handleUserBadRequest(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FieldMessage("user", ex.getMessage()));
    }
}
