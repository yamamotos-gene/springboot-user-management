package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

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
        logger.info("ユーザー一覧取得開始");

        List<User> users =
                userRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id"));

        logger.info(
                "ユーザー一覧取得完了 件数={}",
                users.size());

        return users;
    }

    public User createUser(User user) {
        logger.info(
                "ユーザー登録開始 name={}",
                user.getName());

        if (userRepository.existsByNameAndAddress(
                user.getName(), user.getAddress())) {
            logger.warn(
                    "重複ユーザー登録検出 name={} address={}",
                    user.getName(),
                    user.getAddress());

            throw new DuplicateUserException(
                    "同じ名前と住所のユーザーは既に登録されています");
        }

        User savedUser = userRepository.save(user);

        logger.info(
                "ユーザー登録完了 id={}",
                savedUser.getId());

        return savedUser;
    }

    public void deleteById(Long id) {
        logger.info(
                "ユーザー削除開始 id={}",
                id);

        userRepository.deleteById(id);

        logger.info(
                "ユーザー削除完了 id={}",
                id);
    }

    @Transactional
    public User updateUser(User user) {
        logger.info(
                "ユーザー更新開始 id={}",
                user.getId());

        try {
            User updatedUser =
                    userRepository.saveAndFlush(user);

            logger.info(
                    "ユーザー更新完了 id={}",
                    updatedUser.getId());

            return updatedUser;
        } catch (ObjectOptimisticLockingFailureException e) {
            logger.warn(
                    "楽観ロック検出 id={}",
                    user.getId());
            throw e;
        }
    }

    public List<User> searchUsers(String keyword) {
        logger.info(
                "ユーザー検索 keyword={}",
                keyword);

        List<User> users =
                userRepository.findByNameContaining(
                        keyword,
                        Sort.by(Sort.Direction.ASC, "id"));

        logger.info(
                "ユーザー検索結果 件数={}",
                users.size());

        return users;
    }
}
