package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseApiTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.filters(new AllureRestAssured());
    }
}
