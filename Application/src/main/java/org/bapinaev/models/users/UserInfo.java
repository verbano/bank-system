package org.bapinaev.models.users;

import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

import java.util.HashSet;
import java.util.Set;

public class UserInfo {
    private final String name;
    private final int age;
    private final Gender gender;
    private final HairColor hairColor;
    private final Set<String> friends;

    public UserInfo(User user) {
        name = user.getName();
        age = user.getAge();
        gender = user.getGender();
        hairColor = user.getHairColor();
        friends = new HashSet<>();

        for (var friend : user.getFriends()) {
            friends.add(friend.getName());
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public Set<String> getFriends() {
        return friends;
    }
}