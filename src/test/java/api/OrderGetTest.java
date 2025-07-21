package api;

import io.qameta.allure.*;
import model.User;
import model.Order;
import org.junit.Test;
import utils.UserGenerator;
import utils.OrderGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Получение заказов")
@Owner("ТвоёИмя")
public class OrderGetTest extends BaseApiTest {

    @Test
    @Story("Получение заказов с авторизацией")
    @Description("Пользователь с accessToken может получить список своих заказов")
    public void getOrdersWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();
        String accessToken = given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .extract()
                .path("accessToken");

        Order order = OrderGenerator.getValidOrder();
        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true));

        given()
                .header("Authorization", accessToken)
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", not(empty()));
    }

    @Test
    @Story("Получение заказов без авторизации")
    @Description("Проверка ошибки при попытке получить заказы без accessToken")
    public void getOrdersWithoutAuthShouldFail() {
        given()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", containsString("You should be authorised"));
    }
}
