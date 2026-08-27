package com.example.demo;

import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;

@RestController
public class HelloController {
    private final UserService userService;
    public HelloController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/hello")
    public String hello(
    @RequestParam(defaultValue = "Guest")
        String name) {
        return "Hello " + name + "!";
    }
    @GetMapping("/user")
    public User user() {
        return userService.getUser();
    }
    /*
    @GetMapping("/users")
    public List<User> users() {
        return List.of(
        new User(null,"Yamamoto", 50),
        new User(null,"Suzuki", 40),
        new User(null,"Tanaka", 30)
        );
    }
    */

    @GetMapping("/users")
    public List<User> users() {
        return userService.findAll();
    }

    /*
    @GetMapping("/users/{id}")
    public User userById(@PathVariable int id) {
        if (id == 1) {
            return new User(null,"Yamamoto", 50);
        }
        if (id == 2) {
            return new User(null,"Suzuki", 40);
        }
        throw new UserNotFoundException(
                "User not found. id=" + id);
    }
    */

    @GetMapping("/users/{id}")
    public User userById(@PathVariable Long id) {

        return userService.findById(id);
    }
    /*
    @PostMapping("/users")
    public User createUser(
            @Valid @RequestBody User user) {
            return user;
   }
    */

    @PostMapping("/users")
    public User createUser(
        @Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(
            @PathVariable int id,
            @Valid @RequestBody User user) {
            return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(
            @PathVariable int id) {
            return "User " + id + " deleted";
        }

}