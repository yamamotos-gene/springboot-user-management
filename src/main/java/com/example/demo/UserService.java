package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        return userRepository.findById(1L)
                .orElseThrow(() ->
        new UserNotFoundException("User not found"));
    }

    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found. id=" + id));
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public User createUser(User user) {
        if (userRepository.existsByNameAndAddress(
                user.getName(), user.getAddress())) {
            throw new DuplicateUserException(
                    "同じ名前と住所のユーザーは既に登録されています");
        }

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> searchUsers(String name) {
        return userRepository.findByNameContaining(
                name, Sort.by(Sort.Direction.ASC, "id"));
    }
}
