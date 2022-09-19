package api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

class ReqresTest {
    private static final String url = "https://reqres.in";

    @Test
    void checkAvatarAndIdTest() {
        Specification.installSpecification(Specification.requestSpec(url), Specification.responseOk200());
        List<UserData> users = given()
                .when()
                .get("/api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        assertTrue(users.stream().allMatch(x -> x.getAvatar().contains(String.valueOf(x.getId()))));
        assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    void successRegistrationTest() {
        Specification.installSpecification(Specification.requestSpec(url), Specification.responseOk200());
        int id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Registration userRegistration = new Registration("eve.holt@reqres.in", "pistol");
        SuccessRegistration registration = given()
                .body(userRegistration)
                .when()
                .post("/api/register")
                .then().log().all()
                .extract().as(SuccessRegistration.class);

        assertEquals(id, registration.getId());
        assertEquals(token, registration.getToken());
    }

}