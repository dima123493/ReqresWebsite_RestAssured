package api;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
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

    @Test //improved variant
    void whenRequestGet_thenOK(){
        when().request("GET", "/api/users?page=2").then().statusCode(200);
    }

    @Test //improved variant
    void whenRequestedPost_thenCreated() {
        with().body(new Registration("eve.holt@reqres.in", "pistol"))
                .when()
                .request("POST", "/odds/new")
                .then()
                .statusCode(201);
    }

    @Test
    void unsuccessfulRegistrationTest() {
        Specification.installSpecification(Specification.requestSpec(url), Specification.responseError400());
        Registration userRegistration = new Registration("sydney@fife", "");
        UnsuccessfulRegistration unsuccessfulRegistraton = given()
                .body(userRegistration)
                .post("/api/register")
                .then().log().all()
                .extract().as(UnsuccessfulRegistration.class);
        assertEquals("Missing password", unsuccessfulRegistraton.getError());
    }

    @Test
    void sortedYearsTest() {
        Specification.installSpecification(Specification.requestSpec(url), Specification.responseOk200());
        List<ColorData> colour = given()
                .when()
                .get("/api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorData.class);
        List<Integer> years =  colour.stream().map(ColorData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears =  years.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedYears, years);
    }

    @Test
    void deleteUserTest() {
        Specification.installSpecification(Specification.requestSpec(url), Specification.responseUnique(204));
                 given()
                .when()
                .delete("/api/users/2")
                .then().log().all();
    }

}