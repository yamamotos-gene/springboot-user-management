package com.example.demo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotBlank(message = "名前は必須です")
    private String name;

    @Min(value = 0, message = "年齢は0以上で入力してください")
    private int age;

    @NotBlank(message = "住所は必須です")
    @Size(max = 255, message = "住所は255文字以内で入力してください")
    @Column(nullable = false, length = 255)
    private String address;

    public User(Long id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public User(
            Long id,
            Long version,
            String name,
            int age,
            String address) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    /*
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    */
}