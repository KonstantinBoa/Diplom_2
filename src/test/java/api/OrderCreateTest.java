package api;

import model.User;
import model.Order;
import org.junit.Test;
import utils.OrderGenerator;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreateTest extends BaseApiTest {
    @Test
    public void createOrderWithAuthShouldBeSuccessful() {
        User user = UserGenerator.getRandomUser();
        // Регистрируем и логинимся, получаем accessToken
        String accessToken = given().header("Content-type", "application/json").body(user)
                .post("/api/auth/register").then().extract().path("accessToken");
        Order order = OrderGenerator.getValidOrder();
        given().header("Content-type", "application/json").header("Authorization", accessToken)
                .body(order).post("/api/orders")
                .then().statusCode(200)
                .body("success", is(true));
    }
    // Аналогично — тесты без авторизации, без ингредиентов, с невалидным ингредиентом
}
