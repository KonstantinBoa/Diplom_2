package api;

import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseApiTest {
    @Test
    public void loginWithValidCredentialsShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();
        // Регистрация пользователя
        given().header("Content-type", "application/json").body(user).post("/api/auth/register");

        // Попытка входа
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", user.email, user.password);
        given().header("Content-type", "application/json").body(json).post("/api/auth/login")
                .then().statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void loginWithInvalidCredentialsShouldFail() {
        String json = "{\"email\": \"fake@mail.ru\", \"password\": \"wrongpassword\"}";
        given().header("Content-type", "application/json").body(json).post("/api/auth/login")
                .then().statusCode(401)
                .body("success", is(false));
    }
}

