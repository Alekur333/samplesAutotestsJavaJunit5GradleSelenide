package aleks.kur.tests.auth;

import org.junit.jupiter.api.*;
import ru.progredis.tests.TestBaseApi;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static ru.progredis.filters.CustomLogFilter.customLogFilter;

@Tags({@Tag("api"), @Tag("auth")})
@DisplayName("Проверки аутентификации через Api ")
public class TestAuthApi extends TestBaseApi {

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа/выхода пользователя с валидными данными через api")
    void authValidDataApiTest() {

        requestData.put("email", login);
        requestData.put("password", passwd);

        given()
                .filter(customLogFilter().withCustomTemplates())
                .log().body()
                .accept("application/json, text/javascript, */*;")
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("/login")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/auth.json"))
                .body("identifier", is(login))
                .body("person.shortName", is(userShortName));

    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа пользователя с невалидными данными логина через api")
    void authInvalidLoginDataApiTest() {

        requestData.put("email", login + "Invalid");
        requestData.put("password", passwd);

        given()
                .filter(customLogFilter().withCustomTemplates())
                .log().body()
                .accept("application/json, text/javascript, */*;")
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("/login")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body(equalTo("Неверная пара логин/пароль"));
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа пользователя с невалидными данными пароля через api")
    void authInvalidPassDataApiTest() {

        requestData.put("email", login);
        requestData.put("password", passwd + "Invalid");

        given()
                .filter(customLogFilter().withCustomTemplates())
                .log().body()
                .accept("application/json, text/javascript, */*;")
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("/login")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body(equalTo("Неверная пара логин/пароль"));
    }

}
