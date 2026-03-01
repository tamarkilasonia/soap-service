package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.client.RestClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class RestSteps {

    @Step("REST: Register user with email: {email}")
    public Response registerUser(String email, String password) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        return RestClient.post("/api/v2/auth/register", request);
    }

    @Step("REST: Authenticate user: {email}")
    public Response authenticateUser(String email, String password) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        return RestClient.post("/api/v2/auth/authenticate", request);
    }

    @Step("REST: Access protected resource")
    public Response accessProtectedResource(String token) {
        return RestClient.get("/api/v1/admin/resource", token);
    }

    @Step("REST: Change password")
    public Response changePassword(String oldPassword, String newPassword, String token) {
        Map<String, String> request = new HashMap<>();
        request.put("oldPassword", oldPassword);
        request.put("newPassword", newPassword);
        return RestClient.post("/api/v1/user/change-password", request, token);
    }

    @Step("REST: Logout")
    public Response logout(String token) {
        return RestClient.post("/api/v2/auth/logout", null, token);
    }

    @Step("REST: Change email to: {newEmail}")
    public Response changeEmail(String newEmail, String token) {
        Map<String, String> request = new HashMap<>();
        request.put("newEmail", newEmail);
        return RestClient.post("/api/v1/user/change-email", request, token);
    }

    @Step("Extract JWT token from response")
    public String extractToken(Response response) {
        return response.jsonPath().getString("token");
    }
}