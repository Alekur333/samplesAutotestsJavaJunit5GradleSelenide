package aleks.kur.tests.directories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.PcDzoCoordinationRoutePage.*;

//@Disabled
@Tags({@Tag("api"), @Tag("pcDzo"), @Tag("regress"), @Tag("coordinationRoutePcDzo")})
@DisplayName("Проверки справочника Маршруты согласования ПЦ ДЗО")
public class TestCoordinationRoutePcDzoApi extends TestBaseApi {

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String
            fullName = pcDzoCoordinationRoutePage.coordinationRoutePcDzoFullName,
            shortName = pcDzoCoordinationRoutePage.coordinationRoutePcDzoShortName,
            description = pcDzoCoordinationRoutePage.coordinationRoutePcDzoDescription,
            fullNameUpdated = pcDzoCoordinationRoutePage.coordinationRoutePcDzoFullNameUpdated,
            shortNameUpdated = pcDzoCoordinationRoutePage.coordinationRoutePcDzoShortNameUpdated,
            descriptionUpdated = pcDzoCoordinationRoutePage.coordinationRoutePcDzoDescriptionUpdated;
    int[]
            coordinatorsEmpty = pcDzoCoordinationRoutePage.coordinatorsEmpty,
            dpoIdsEmpty = pcDzoCoordinationRoutePage.dpoIdsEmpty;
    Map<String, Object>
            coordinationRoutePcDzoDataSetNew = pcDzoCoordinationRoutePage.coordinationRoutePcDzoDataSet(
            shortName, fullName, description, dpoIdsEmpty, coordinatorsEmpty);

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" может создать Маршрут согласования ПЦ ДЗО api")
    @CsvSource(textBlock = validRolesCanCreateCoordinationRoutePcDzo)
//    @Disabled
//    @Tags({@Tag("regress")})
//    @DisplayName("Проверка создания Маршрута согласования ПЦ ДЗО api")
    void createCoordinationRoutePcDzoBaseApiTest(String login, String passwd, String role) {

        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);

        step("Создать Маршрут, проверить статус, тело ответа, сохранение данных при создании", () -> {
            int coordinationRoutePcDzoId =
                    pcDzoCoordinationRoutePage.createCoordinationRoutePcDzoApi(authCookieValidRole, coordinationRoutePcDzoDataSetNew)
                            .then()
                            .log().ifValidationFails()
                            .statusCode(201)
                            .body(matchesJsonSchemaInClasspath("schemas/coordinationRoutePcDzoNew.json"))
                            .body("fullName", is(fullName))
                            .body("shortName", is(shortName))
                            .body("description", is(description))
                            .extract().response().body().path("id");

            step("Удалить созданный Маршрут", () -> {
                pcDzoCoordinationRoutePage.deleteCoordinationRoutePcDzoApi(authCookie, coordinationRoutePcDzoId);
            });
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("regress")})
    @DisplayName("Проверка получения Маршрута по id через api")
    void getCoordinationRoutePcDzoByIdBaseApiTest() {

        step("Создать Маршрут через api", () -> {
        });
        int routePcDzoId = pcDzoCoordinationRoutePage
                .createCoordinationRoutePcDzoAndGetIdApi(authCookie);

        step("Получить Маршрут и проверить статус, тело ответа", () -> {
            pcDzoCoordinationRoutePage.getCoordinationRoutePcDzoByIdApi(authCookie, routePcDzoId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/coordinationRoutePcDzoNew.json"))
                    .body("fullName", is(fullName))
                    .body("shortName", is(shortName))
                    .body("description", is(description));
        });

        step("Удалить созданный Маршрут", () -> {
            pcDzoCoordinationRoutePage.deleteCoordinationRoutePcDzoApi(authCookie, routePcDzoId);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("regress")})
    @DisplayName("Проверка получения списка 'Маршруты согласования ПЦ ДЗО' api")
    void coordinationRoutePcDzoListBaseApiTest() {

        step("Создать Маршрут через api", () -> {
        });
        int routePcDzoId = pcDzoCoordinationRoutePage
                .createCoordinationRoutePcDzoAndGetIdApi(authCookie);

        step("Получить Маршруты и проверить статус, тело ответа", () -> {
            pcDzoCoordinationRoutePage.getCoordinationRoutePcDzoListApi(authCookie)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/coordinationRoutePcDzoList.json"));
        });

        step("Удалить созданный Маршрут", () -> {
            pcDzoCoordinationRoutePage.deleteCoordinationRoutePcDzoApi(authCookie, routePcDzoId);
        });
    }
    @Test
//    @Disabled
//    @Tags({@Tag("regress")})
    @DisplayName("Проверка редакции полей Маршрута согласования ПЦ ДЗО api")
    void editCoordinationRoutePcDzoApiBaseTest() {

        step("Создать Маршрут через api", () -> {
        });
        int routePcDzoId = pcDzoCoordinationRoutePage
                .createCoordinationRoutePcDzoAndGetIdApi(authCookie);

        step("Получить json-строку тела из запроса карточки маршрута и редактировать описание, краткое, полное имя");
        String initialJsonString = pcDzoCoordinationRoutePage.getCoordinationRoutePcDzoJsonStByIdApi(authCookie, routePcDzoId);
        String editedJsonString = pcDzoCoordinationRoutePage.editCoordinationRoutePcDzo(
                initialJsonString, shortNameUpdated, fullNameUpdated, descriptionUpdated);

        step("Обновить описание, краткое, полное имя маршрута и проверить статус, тело ответа", () -> {
            pcDzoCoordinationRoutePage.editCoordinationRoutePcDzoByJsonStringApi(authCookie, editedJsonString)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(201)
                    .body(matchesJsonSchemaInClasspath("schemas/coordinationRoutePcDzoNew.json"))
                    .body("fullName", is(fullNameUpdated))
                    .body("shortName", is(shortNameUpdated))
                    .body("description", is(descriptionUpdated));
        });

        step("Удалить созданный Маршрут", () -> {
            pcDzoCoordinationRoutePage.deleteCoordinationRoutePcDzoApi(authCookie, routePcDzoId);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("regress")})
    @DisplayName("Проверка удаления Маршрута согласования ПЦ ДЗО api")
    void deleteCoordinationRoutePcDzoApiTest() {

        step("Создать Маршрут через api", () -> {
        });
        int routePcDzoId = pcDzoCoordinationRoutePage
                .createCoordinationRoutePcDzoAndGetIdApi(authCookie);

        step("Удалить созданный Маршрут", () -> {
            pcDzoCoordinationRoutePage.deleteCoordinationRoutePcDzoApi(authCookie, routePcDzoId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat().statusCode(200);
        });

        step("Проверить, что удаленный маршрут есть в БД и атрибут deleted: true", () -> {
            pcDzoCoordinationRoutePage.getCoordinationRoutePcDzoByIdApi(authCookie, routePcDzoId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .body("deleted", is(true));
        });
    }




}
