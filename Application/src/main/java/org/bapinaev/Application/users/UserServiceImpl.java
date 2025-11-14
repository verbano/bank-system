package org.bapinaev.Application.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bapinaev.events.Event;
import org.bapinaev.Application.mappers.UserEntityMapper;
import org.bapinaev.producers.UserEventPublisher;
import org.bapinaev.entities.users.UserEntity;
import org.bapinaev.repositories.UserRepository;
import org.bapinaev.contracts.users.UserService;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;
import org.bapinaev.models.users.User;
import org.bapinaev.models.users.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final UserEventPublisher userEventPublisher;
    private final ObjectMapper objectMapper;


    public UserServiceImpl(UserRepository userRepository, UserEntityMapper userEntityMapper, UserEventPublisher userEventPublisher, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.userEventPublisher = userEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public boolean create(User user) {
        Optional<UserEntity> existingUser = userRepository.findById(user.getLogin());

        if (existingUser.isPresent())
            return false;

        UserEntity userEntity = userEntityMapper.toEntity(user);
        userRepository.save(userEntity);

        String userJson = null;
        try {
            userJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Event event = new Event(
                user.getLogin(),
                "USER_CREATED",
                null,
                userJson,
                Instant.now()
        );

        userEventPublisher.sendMessage(user.getLogin(), event);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getInfo(String login) {
        Optional<UserEntity> user = userRepository.findById(login);
        return new UserInfo(user.map(userEntityMapper::toDomain).get());
    }

    @Override
    @Transactional
    public void addFriend(String login1, String login2) {
        UserEntity user1 = userRepository.findById(login1).get();
        UserEntity user2 = userRepository.findById(login2).get();

        List<String> oldFriends1 = user1.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        List<String> oldFriends2 = user2.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        user1.addFriend(user2);
        user2.addFriend(user1);

        List<UserEntity> users = userRepository.saveAll(List.of(user1, user2));

        UserEntity updatedUser1 = users.get(0);
        UserEntity updatedUser2 = users.get(1);

        List<String> newFriends1 = updatedUser1.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        List<String> newFriends2 = updatedUser2.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        try {
            Event event1 = new Event(
                    user1.getLogin(),
                    "FRIEND_ADDED",
                    objectMapper.writeValueAsString(oldFriends1),
                    objectMapper.writeValueAsString(newFriends1),
                    Instant.now()
            );

            Event event2 = new Event(
                    user2.getLogin(),
                    "FRIEND_ADDED",
                    objectMapper.writeValueAsString(oldFriends2),
                    objectMapper.writeValueAsString(newFriends2),
                    Instant.now()
            );

            userEventPublisher.sendMessage(user1.getLogin(), event1);
            userEventPublisher.sendMessage(user2.getLogin(), event2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void removeFriend(String login1, String login2) {
        UserEntity user1 = userRepository.findById(login1).get();
        UserEntity user2 = userRepository.findById(login2).get();

        List<String> oldFriends1 = user1.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        List<String> oldFriends2 = user2.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        user1.removeFriend(user2);
        user2.removeFriend(user1);

        List<UserEntity> users = userRepository.saveAll(List.of(user1, user2));

        UserEntity updatedUser1 = users.get(0);
        UserEntity updatedUser2 = users.get(1);

        List<String> newFriends1 = updatedUser1.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        List<String> newFriends2 = updatedUser2.getFriends().stream()
                .map(UserEntity::getLogin)
                .collect(Collectors.toList());

        try {
            Event event1 = new Event(
                    user1.getLogin(),
                    "FRIEND_DELETED",
                    objectMapper.writeValueAsString(oldFriends1),
                    objectMapper.writeValueAsString(newFriends1),
                    Instant.now()
            );

            Event event2 = new Event(
                    user2.getLogin(),
                    "FRIEND_DELETED",
                    objectMapper.writeValueAsString(oldFriends2),
                    objectMapper.writeValueAsString(newFriends2),
                    Instant.now()
            );

            userEventPublisher.sendMessage(user1.getLogin(), event1);
            userEventPublisher.sendMessage(user2.getLogin(), event2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUser(String login) {
        return userRepository.findById(login).map(userEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByFilters(HairColor hairColor, Gender gender) {
        return userRepository.findByHairColorAndGender(hairColor, gender).stream().map(userEntityMapper::toDomain).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getFriends(String login) {
        Optional<UserEntity> userEntity = userRepository.findById(login);
        return userEntity.get().getFriends().stream().map(userEntityMapper::toDomain).toList();
    }
}