package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            List<User> users = IntStream.rangeClosed(1, 1000)
                    .mapToObj(i -> new User(
                            null,
                            null,
                            "テストユーザー" + String.format("%04d", i),
                            "東京都テスト区" + i + "丁目",
                            LocalDate.of(
                                    1970 + (i % 40),
                                    (i % 12) + 1,
                                    (i % 28) + 1)))
                    .toList();

            userRepository.saveAll(users);
        };
    }

}
