package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

public class IngredientHelper {
    public static List<String> getFirstIngredients(int count) {
        Response response = RestAssured
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<String> ingredients = response.path("data._id");
        return ingredients.subList(0, Math.min(count, ingredients.size()));
    }
}

