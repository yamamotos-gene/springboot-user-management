package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void findByIdTest() {

        User expectedUser =
                new User(1L, "Sato", 35, "Tokyo");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        User actualUser =
                userService.findById(1L);

        assertEquals("Sato",
                actualUser.getName());

        assertEquals(35,
                actualUser.getAge());

        assertEquals("Tokyo",
                actualUser.getAddress());
    }

    @Test
    void findByIdNotFoundTest() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(999L)
        );
    }

    @Test
    void addressRequiredTest() {

        UserForm userForm = new UserForm();
        userForm.setName("Sato");
        userForm.setAge(35);
        userForm.setAddress("");

        assertTrue(
                validator.validate(userForm)
                        .stream()
                        .anyMatch(error ->
                                error.getPropertyPath()
                                        .toString()
                                        .equals("address")));
    }

    @Test
    void address255CharactersIsValidTest() {

        UserForm userForm = new UserForm();
        userForm.setName("Sato");
        userForm.setAge(35);
        userForm.setAddress("a".repeat(255));

        assertFalse(
                validator.validate(userForm)
                        .stream()
                        .anyMatch(error ->
                                error.getPropertyPath()
                                        .toString()
                                        .equals("address")));
    }

    @Test
    void address256CharactersIsInvalidTest() {

        UserForm userForm = new UserForm();
        userForm.setName("Sato");
        userForm.setAge(35);
        userForm.setAddress("a".repeat(256));

        assertTrue(
                validator.validate(userForm)
                        .stream()
                        .anyMatch(error ->
                                error.getPropertyPath()
                                        .toString()
                                        .equals("address")));
    }

}