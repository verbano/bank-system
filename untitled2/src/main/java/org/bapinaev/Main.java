package org.bapinaev;

public class Main {
    public static void main(String[] args) {
        User user = new User(null);
    }
}

class User {
    private Department department;

    public User(Department department) {
        this.department = department;
    }
}

class Department {
    private User manager;

    public Department(User manager) {
        this.manager = manager;
    }
}