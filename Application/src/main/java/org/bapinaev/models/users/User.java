package org.bapinaev.models.users;


import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String login;

    private String name;

    private int age;

    private Gender gender;

    private HairColor hairColor;

    private final Set<User> friends;

    public User(String name, String login, int age, Gender gender, HairColor hairColor) {
        this.name = name;
        this.login = login;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.friends = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public Set<User> getFriends() {
        return new HashSet<>(friends);
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        friends.remove(user);
    }
}