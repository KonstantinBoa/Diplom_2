package api;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Обновление данных пользователя")
@Owner("ТвоёИмя")
public class UserUpdateTest extends BaseApiTest {

    @Test
    @Story("Обновление данных с авторизацией")
    @Description("Пользователь может обновить email/имя при наличии accessToken")
    public void updateUserDataWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/auth/register")
                .then()
                .extract()
                .path("accessToken");

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

    @Test
    @Story("Обновление данных без авторизации")
    @Description("Ошибка, если accessToken не передан")
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
