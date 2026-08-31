package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.Period;

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

    @NotBlank(message = "住所は必須です")
    @Size(max = 255, message = "住所は255文字以内で入力してください")
    @Column(nullable = false, length = 255)
    private String address;

    private LocalDate birthday;

    /*
     * These constructors keep existing callers source-compatible during the
     * migration. The age argument is no longer stored.
     */
    @Deprecated
    public User(Long id, String name, int age, String address) {
        this(id, null, name, address, null);
    }

    @Deprecated
    public User(
            Long id,
            String name,
            int age,
            String address,
            LocalDate birthday) {
        this(id, null, name, address, birthday);
    }

    @Deprecated
    public User(
            Long id,
            Long version,
            String name,
            int age,
            String address,
            LocalDate birthday) {
        this(id, version, name, address, birthday);
    }

    public User(
            Long id,
            Long version,
            String name,
            String address,
            LocalDate birthday) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
    }

    public int getAge() {
        if (birthday == null) {
            return 0;
        }

        return Period.between(birthday, LocalDate.now()).getYears();
    }
}