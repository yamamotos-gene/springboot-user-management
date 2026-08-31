package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
                new User(
                        1L,
                        null,
                        "Sato",
                        "Tokyo",
                        LocalDate.now().minusYears(35));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        User actualUser =
                userService.findById(1L);

        assertEquals("Sato",
                actualUser.getName());

        assertEquals("Tokyo",
                actualUser.getAddress());

        assertEquals(35,
                actualUser.getAge());
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
        userForm.setAddress("");
        userForm.setBirthday(LocalDate.of(1990, 1, 1));

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
        userForm.setAddress("a".repeat(255));
        userForm.setBirthday(LocalDate.of(1990, 1, 1));

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
        userForm.setAddress("a".repeat(256));
        userForm.setBirthday(LocalDate.of(1990, 1, 1));

        assertTrue(
                validator.validate(userForm)
                        .stream()
                        .anyMatch(error ->
                                error.getPropertyPath()
                                        .toString()
                                        .equals("address")));
    }

    @Test
    void birthdayRequiredTest() {

        UserForm userForm = new UserForm();
        userForm.setName("Sato");
        userForm.setAddress("Tokyo");

        assertTrue(
                validator.validate(userForm)
                        .stream()
                        .anyMatch(error ->
                                error.getPropertyPath()
                                        .toString()
                                        .equals("birthday")));
    }



}