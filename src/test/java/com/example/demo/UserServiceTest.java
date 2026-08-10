package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void findByIdTest() {

        User expectedUser =
                new User(1L, "Sato", 35);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        User actualUser =
                userService.findById(1L);

        assertEquals("Sato",
                actualUser.getName());

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


}