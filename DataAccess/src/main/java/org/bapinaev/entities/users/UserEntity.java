package org.bapinaev.entities.users;


import jakarta.persistence.*;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private HairColor hairColor;

    @ManyToMany()
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_login"),
            inverseJoinColumns = @JoinColumn(name = "friend_login")
    )
    private Set<UserEntity> friends;

    public UserEntity(String name, String login, int age, Gender gender, HairColor hairColor) {
        this.name = name;
        this.login = login;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.friends = new HashSet<>();
    }

    public UserEntity() {
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

    public Set<UserEntity> getFriends() {
        return new HashSet<>(friends);
    }

    public void addFriend(UserEntity user) {
        friends.add(user);
    }

    public void removeFriend(UserEntity user) {
        friends.remove(user);
    }
}