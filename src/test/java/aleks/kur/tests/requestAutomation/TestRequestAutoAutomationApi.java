package aleks.kur.tests.requestAutomation;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import ru.progredis.models.request.RequestAuto;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Проверки Заявок Автоматизация Api")
@Tags({@Tag("api"), @Tag("requestAuto"), @Tag("regress")})
public class TestRequestAutoAutomationApi extends TestBaseApi {
    String
            authCookie = AuthApiPage.getAuthCookie(login, passwd);

    // Заявка Автоматизация бизнес-процессов
    RequestAuto newRequestAuto = requestAutoPage.newRequestAuto;

    @Test
//    @Disabled
    @DisplayName("Проверка кода, валидация схемы ответа, времени Создания заявки автоматизация api")
    void createRequestAutoBaseChecksApiTest() {
        requestAutoPage.createRequestAutoApi(authCookie, newRequestAuto)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/postRequestAutoSchema.json"));
    }

    @Test
//    @Disabled
    @DisplayName("Проверка кода, валидация схемы ответа, времени Получения заявки автоматизация api")
    void getRequestAutoBaseChecksApiTest() {
        requestAutoPage.createAndGetRequestByIdApi(authCookie, newRequestAuto)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/getRequestAutoSchema.json"));
    }

    @Test
//    @Disabled
    @DisplayName("Поля при создании равны полям при получения новой заявки Автоматизация api")
    void createdFieldsEqualsToGetRequestAutoApiTest() {

        step("Получаем объект новой заявки для проверок", () -> {

            step("Получаем объект новой заявки", () -> {
            });
            Response getNewRequestByIdApi = requestAutoPage.createAndGetRequestByIdApi(authCookie, newRequestAuto);
            getNewRequestByIdApi
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
            RequestAuto actualRequestAuto = getNewRequestByIdApi.as(RequestAuto.class);

            step("Значения полей при создании = при чтении заявки", () -> {
                assertThat(actualRequestAuto, samePropertyValuesAs(newRequestAuto, "id"));
            });
        });

    }

}
