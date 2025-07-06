package api;

import model.User;
import model.Order;
import org.junit.Test;
import utils.UserGenerator;
import utils.OrderGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderGetTest extends BaseApiTest {

    // Получение заказов авторизованного пользователя
    @Test
    public void getOrdersWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();

        // Регистрация и получение accessToken
        String accessToken = given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .extract()
                .path("accessToken");

        // Создать заказ (чтобы точно был хотя бы 1 заказ у пользователя)
        Order order = OrderGenerator.getValidOrder();
        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true));

        // Получить заказы пользователя
        given()
                .header("Authorization", accessToken)
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", not(empty()));
    }

    // Получение заказов неавторизованного пользователя (без accessToken)
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

