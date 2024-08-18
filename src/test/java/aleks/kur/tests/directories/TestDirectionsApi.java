package aleks.kur.tests.directories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

@Tags({@Tag("directions"), @Tag("api")})
@DisplayName("Проверки справочника Направления через api")
public class TestDirectionsApi extends TestBaseApi {

    public Map<String, String> requestData = new HashMap<>();

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String fullName = directionsPage.directionFullName;
    String shortName = directionsPage.directionShortName;
    String description = directionsPage.directionDescription;
    String fullNameUpdated = directionsPage.directionFullNameUpdated;
    String shortNameUpdated = directionsPage.directionShortNameUpdated;
    String descriptionUpdated = directionsPage.directionDescriptionUpdated;

    public Map<String, String> directionDataSet() {
        requestData.put("shortName", shortName);
        requestData.put("fullName", fullName);
        requestData.put("description", description);
        requestData.put("approvalRoutes", null); // array of id
        requestData.put("reviewOrganizerId", "0"); // int
        requestData.put("asuIdList", null); // array
        return requestData;
    }

    Map<String, String> requestDirectionDataSet = directionDataSet();

    public static Map<String, String> requestDataUpdated = new HashMap<>();

    public Map<String, String> directionDataSetUpdated() {
        requestDataUpdated.put("shortName", shortNameUpdated);
        requestDataUpdated.put("fullName", fullNameUpdated);
        requestDataUpdated.put("description", descriptionUpdated);
        requestDataUpdated.put("approvalRoutes", null); // array of id
        requestDataUpdated.put("reviewOrganizerId", "0"); // int
        requestDataUpdated.put("responsibleForSlaFromGvcId", null); // int
//        requestData.put( "id", id); // int
        requestDataUpdated.put("asuIdList", null); // array
        return requestDataUpdated;
    }
    Map<String, String> requestDirectionDataSetUpdated = directionDataSetUpdated();

    @Test
//    @Disabled
    @Tags({@Tag("regress")})
    @DisplayName("Проверка создания направления через api")
    void createDirectionApiTest() {
        step("Создать направление, проверить статус, тело ответа, сохранение данных при создании", () -> {
            int directionId =
                    given()
                            .spec(requestSpec)
                            .body(requestDirectionDataSet)
                            .cookie("PLAY_SESSION", authCookie)
                            .when()
                            .post(BASE_URL + "/app4/directions")
                            .then()
                            .log().all()
                            .statusCode(201)
                            .body(matchesJsonSchemaInClasspath("schemas/direction.json"))
                            .body("fullName", is(fullName))
                            .body("shortName", is(shortName))
                            .body("description", is(description))
                            .extract().response().body().path("id");
            step("Удалить созданное направление", () -> {
                directionsPage.deleteDirectionApi(authCookie, directionId);
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress")})
    @DisplayName("Проверка получения Направления по id через api")
    void getDirectionByIdApiTest() {
        step("Добавить Направление через api", () -> {
        });
        int directionId = directionsPage.createDirectionAndGetIdApi(authCookie, fullName, shortName, description);
        step("Получить напраления и проверить статус, тело ответа", () -> {
            given()
                    .spec(requestSpec)
                    .cookie("PLAY_SESSION", authCookie)
                    .when()
                    .get(BASE_URL + "/app4/directions/" + directionId)
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/direction.json"))
                    .body("fullName", is(fullName))
                    .body("shortName", is(shortName))
                    .body("description", is(description));
        });
        step("Удалить созданное направление", () -> {
            directionsPage.deleteDirectionApi(authCookie, directionId);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress")})
    @DisplayName("Проверка получение всех направлений по api")
    void directionsListApiTest() {
        step("Добавить Направление через api", () -> {
        });
        int directionId = directionsPage.createDirectionAndGetIdApi(authCookie, fullName, shortName, description);
        step("Получить напраления и проверить статус, тело ответа", () -> {
            given()
                    .spec(requestSpec)
                    .cookie("PLAY_SESSION", authCookie)
                    .when()
                    .get(BASE_URL + "/app4/directions")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/directionsList.json"));
        });
        step("Удалить созданное направление", () -> {
            directionsPage.deleteDirectionApi(authCookie, directionId);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress")})
    @DisplayName("Проверка обновления текстовых полей направления через api")
    void updateDirectionsTextFieldsApiTest() {
        step("Добавить Направление через api", () -> {
        });
        int directionId = directionsPage.createDirectionAndGetIdApi(authCookie, fullName, shortName, description);
        requestDataUpdated.put("id", Integer.toString(directionId)); // int
        step("Обновить описание, краткое, полное имя напраления и проверить статус, тело ответа", () -> {
            given()
                    .spec(requestSpec)
                    .body(requestDirectionDataSetUpdated)
                    .cookie("PLAY_SESSION", authCookie)
                    .when()
                    .put(BASE_URL + "/app4/directions/")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/direction.json"))
                    .body("fullName", is(fullNameUpdated))
                    .body("shortName", is(shortNameUpdated))
                    .body("description", is(descriptionUpdated));
        });
        step("Удалить созданное направление", () -> {
            directionsPage.deleteDirectionApi(authCookie, directionId);
        });
    }



}
