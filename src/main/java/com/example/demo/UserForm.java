package com.example.demo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserForm {

    private Long version;

    @NotBlank(message = "名前は必須です")
    private String name;

    @NotNull(message = "年齢は必須です")
    @Min(value = 0, message = "年齢は0以上です")
    private Integer age;

    @NotBlank(message = "住所は必須です")
    @Size(max = 255, message = "住所は255文字以内で入力してください")
    private String address;

    private LocalDate birthday;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}