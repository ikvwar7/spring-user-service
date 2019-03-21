package spring.user.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spring.user.service.domain.User;
import spring.user.service.exceptions.UserNotFoundException;
import spring.user.service.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<User> save(@RequestBody @Valid User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping(value = "/find")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}