package api;

import model.User;
import model.Order;
import org.junit.Test;
import utils.UserGenerator;
import utils.OrderGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderGetTest extends BaseApiTest {

    @Test
    public void getOrdersWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();

        // Регистрируем пользователя и получаем accessToken
        String accessToken = given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .extract()
                .path("accessToken");

        // Создаём заказ
        Order order = OrderGenerator.getValidOrder();
        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true));

        // Получаем заказы пользователя
        given()
                .header("Authorization", accessToken)
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", not(empty()));
    }

    @Test
    public void getOrdersWithoutAuthShouldFail() {
        given()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", containsString("You should be authorised"));
    }
}
