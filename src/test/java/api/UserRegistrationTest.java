package api;

import io.qameta.allure.*;
import model.User;
import org.junit.Test;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Регистрация пользователя")
@Owner("ТвоёИмя")
public class UserRegistrationTest extends BaseApiTest {

    @Test
    @Story("Валидная регистрация")
    @Description("Пользователь может зарегистрироваться с валидными данными")
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
    @Story("Регистрация уже существующего пользователя")
    @Description("Ошибка, если пользователь уже существует")
    public void registrationWithAlreadyRegisteredUserShouldFail() {
        User user = UserGenerator.getRandomUser();
        given().header("Content-type", "application/json").body(user).post("/api/auth/register");
        given().header("Content-type", "application/json").body(user).post("/api/auth/register")
                .then().statusCode(403)
                .body("success", is(false))
                .body("message", containsString("User already exists"));
    }

    @Test
    @Story("Регистрация без email")
    @Description("Ошибка, если не передан email")
    public void registrationWithoutEmailShouldFail() {
        User user = UserGenerator.getUserWithoutEmail();
        given().header("Content-type", "application/json").body(user).post("/api/auth/register")
                .then().statusCode(403)
                .body("success", is(false));
    }
}
