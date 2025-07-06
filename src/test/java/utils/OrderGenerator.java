package utils;

import model.Order;
import java.util.Collections;
import java.util.List;

public class OrderGenerator {
    public static Order getValidOrder() {
        List<String> ingredientIds = IngredientHelper.getFirstIngredients(2);
        return new Order(ingredientIds);
    }

    public static Order getOrderWithoutIngredients() {
        return new Order(Collections.emptyList());
    }

    public static Order getOrderWithInvalidIngredient() {
        return new Order(Collections.singletonList("invalid_ingredient_id"));
    }
}
