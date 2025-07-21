package api;

import io.qameta.allure.*;
import model.User;
import model.Order;
import org.junit.Test;
import utils.OrderGenerator;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Stellar Burgers API")
@Feature("Создание заказов")
@Owner("ТвоёИмя")
public class OrderCreateTest extends BaseApiTest {

    @Test
    @Story("Создание заказа с авторизацией")
    @Description("Пользователь с accessToken может создать заказ")
    public void createOrderWithAuthShouldBeSuccessful() {
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
    }

    @Test
    @Story("Создание заказа без авторизации")
    @Description("Пользователь без accessToken тоже может создать заказ")
    public void createOrderWithoutAuthShouldBeSuccessful() {
        Order order = OrderGenerator.getValidOrder();
        given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Story("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при попытке создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsShouldFail() {
        given()
                .header("Content-type", "application/json")
                .body(OrderGenerator.getOrderWithoutIngredients())
                .post("/api/orders")
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", containsString("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа с невалидным ингредиентом")
    @Description("Проверка 500 ошибки при несуществующем ингредиенте")
    public void createOrderWithInvalidIngredientShouldFail() {
        given()
                .header("Content-type", "application/json")
                .body(OrderGenerator.getOrderWithInvalidIngredient())
                .post("/api/orders")
                .then()
                .statusCode(500);
    }
}
