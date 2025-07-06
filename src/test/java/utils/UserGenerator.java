package utils;

import model.User;

public class UserGenerator {
    public static User getRandomUser() {
        String email = "test" + System.currentTimeMillis() + "@yandex.ru";
        String password = "password";
        String name = "TestUser";
        return new User(email, password, name);
    }

    // Например, пользователь без одного из обязательных полей
    public static User getUserWithoutEmail() {
        return new User(null, "password", "TestUser");
    }
    public static User getUserWithoutPassword() {
        return new User("test" + System.currentTimeMillis() + "@yandex.ru", null, "TestUser");
    }
    public static User getUserWithoutName() {
        return new User("test" + System.currentTimeMillis() + "@yandex.ru", "password", null);
    }
}

