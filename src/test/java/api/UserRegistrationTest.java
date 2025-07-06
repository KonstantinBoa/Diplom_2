package api;

import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends BaseApiTest {

    @Test
    public void registrationWithValidDataShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();
        given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void registrationWithAlreadyRegisteredUserShouldFail() {
        User user = UserGenerator.getRandomUser();
        // Первый раз регистрация
        given().header("Content-type", "application/json").body(user).post("/api/auth/register");
        // Второй раз тот же пользователь
        given().header("Content-type", "application/json").body(user).post("/api/auth/register")
                .then().statusCode(403)
                .body("success", is(false))
                .body("message", containsString("User already exists"));
    }

    @Test
    public void registrationWithoutEmailShouldFail() {
        User user = UserGenerator.getUserWithoutEmail();
        given().header("Content-type", "application/json").body(user).post("/api/auth/register")
                .then().statusCode(403)
                .body("success", is(false));
    }
    // Аналогично — тесты без пароля, без имени
}
