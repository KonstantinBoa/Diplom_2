package utils;

import model.Order;
import java.util.Arrays;
import java.util.Collections;

public class OrderGenerator {
    // Генератор заказа с валидными ингредиентами
    public static Order getValidOrder() {
        // Здесь можно будет динамически получать ингредиенты, пока просто пример
        return new Order(Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870"));
    }
    // Заказ без ингредиентов
    public static Order getOrderWithoutIngredients() {
        return new Order(Collections.emptyList());
    }
    // Заказ с невалидным id ингредиента
    public static Order getOrderWithInvalidIngredient() {
        return new Order(Arrays.asList("invalid_ingredient_id"));
    }
}
