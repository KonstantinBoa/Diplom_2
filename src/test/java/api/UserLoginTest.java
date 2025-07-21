package api;

import io.qameta.allure.*;
import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Логин пользователя")
@Owner("ТвоёИмя")
public class UserLoginTest extends BaseApiTest {
    @Test
    @Story("Вход с валидными данными")
    @Description("Пользователь может войти, если указал правильный email и пароль")
    public void loginWithValidCredentialsShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();
        given().header("Content-type", "application/json").body(user).post("/api/auth/register");

        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", user.email, user.password);
        given().header("Content-type", "application/json").body(json).post("/api/auth/login")
                .then().statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Story("Вход с невалидными данными")
    @Description("Пользователь не может войти с неправильным паролем")
    public void loginWithInvalidCredentialsShouldFail() {
        String json = "{\"email\": \"fake@mail.ru\", \"password\": \"wrongpassword\"}";
        given().header("Content-type", "application/json").body(json).post("/api/auth/login")
                .then().statusCode(401)
                .body("success", is(false));
    }
}
