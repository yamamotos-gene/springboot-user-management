package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class TestDataCreateTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void テストデータ1000件作成() {

        System.out.println(
                "現在件数=" + userRepository.count());

        if (userRepository.count() > 100) {
            System.out.println("既にテストデータがあります");
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

        System.out.println(
                "登録後のユーザー件数: " + userRepository.count());
    }
}
