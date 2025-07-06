package api;

import io.restassured.http.ContentType;
import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserUpdateTest extends BaseApiTest {

    // Обновление данных с авторизацией (валидный accessToken)
    @Test
    public void updateUserDataWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();

        // Регистрация и получение accessToken
        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/auth/register")
                .then()
                .extract()
                .path("accessToken");

        // Обновляем имя и email
        User updatedUser = new User("updated" + System.currentTimeMillis() + "@yandex.ru", "password", "UpdatedName");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body(updatedUser)
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(updatedUser.email))
                .body("user.name", equalTo(updatedUser.name));
    }

    // Обновление данных без авторизации (нет accessToken)
    @Test
    public void updateUserDataWithoutAuthShouldFail() {
        User updatedUser = new User("test" + System.currentTimeMillis() + "@yandex.ru", "password", "UpdatedName");

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .patch("/api/auth/user")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", containsString("You should be authorised"));
    }
}
