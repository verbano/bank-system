package org.bapinaev.contracts.users;

import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;
import org.bapinaev.models.users.User;
import org.bapinaev.models.users.UserInfo;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean create(User user);

    UserInfo getInfo(String login);

    void addFriend(String login1, String login2);

    void removeFriend(String login1, String login2);

    Optional<User> getUser(String login);

    List<User> getUsersByFilters(HairColor hairColor, Gender gender);

    List<User> getFriends(String login);
}