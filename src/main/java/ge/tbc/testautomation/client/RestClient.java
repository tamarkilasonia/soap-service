package ge.tbc.testautomation.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {
    private static final String BASE_URL = "http://localhost:8086";

    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .contentType("application/json")
                .accept("application/json");
    }

    public static RequestSpecification getAuthenticatedRequest(String token) {
        return getRequestSpecification()
                .header("Authorization", "Bearer " + token);
    }

    public static Response post(String endpoint, Object body) {
        return getRequestSpecification()
                .body(body)
                .post(endpoint);
    }

    public static Response post(String endpoint, Object body, String token) {
        return getAuthenticatedRequest(token)
                .body(body)
                .post(endpoint);
    }

    public static Response get(String endpoint, String token) {
        return getAuthenticatedRequest(token)
                .get(endpoint);
    }
}