package aleks.kur.tests.requestRobots;

import io.restassured.response.Response;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.ProcessPage;
import ru.progredis.pages.RequestRobotsPage;
import ru.progredis.pages.RobotPage;
import ru.progredis.tests.TestBaseApi;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.RequestRobotsPage.*;

//@Disabled
@Tags({@Tag("api"), @Tag("requestAuto"), @Tag("requestRobots"), @Tag("regress"), @Tag("process")})
@DisplayName("Проверки действий по процессу Заявки Роботизация Api")
public class TestRequestAutoRobotsProcessActionsApi extends TestBaseApi {

    @AfterAll
    static void finish() {
        String authCookie = AuthApiPage.getAuthCookie(login, passwd);
        deleteAllTestRequestRobotsByNameApi(authCookie);
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    RequestRobotsRequest
            requestRobotsNew = requestRobotsPage.requestRobotsNew,
            requestRobotsEdited = requestRobotsPage.requestRobotsEdited;


    @ParameterizedTest(name = "Тест #{index} -> Стартует процесс от валидной роли \"" + "{2}" + "\" по заявке роботов, если она не была в процессе и не удалена")
    @CsvSource(textBlock = RequestRobotsPage.validRolesToStartProcessRequestRobots)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleCanStartProcessRequestRobotsApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Создать с обязательными полями заявку с роботом, документом 'Паспорт робота', с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке и проверить атрибут movingProcess:true", () -> {
            startProcessRequestRobots(authCookieValidRole, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(200);
            requestRobotsPage.getRequestRobotsByIdApi(authCookieValidRole, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().body("movingProcess", is(true));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени старта процесса по заявке Роботизация api")
    void startProcessRequestRobotsBaseChecksApiTest() {
        step("Создать готовую для процесса заявку", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().all().extract().body().path("id");
        step("Запустить процесс по тестзаявке и проверить ответ на код, схему, время", () -> {
            startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(200)
                    .header("Content-Type", equalTo("application/json"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/requestRobotsStartProcessSchema.json"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Не стартует процесс заявки без документа 'Паспорта робота' в ее роботах")
    void notStartProcessRequestRobotsWithoutRobotsPassportApiTest() {
        step("Создать готовую для процесса заявку с 2 роботами без паспорта", () -> {
        });
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsNew);
        // добавить заявке Роботы с нужными атрибутам
        Response robotResponse1 = RobotPage.createCustomRobotApi(authCookie, robotPage.robotAllRequiredAttrs(requestId));
        int robot1Id = robotResponse1.then().log().ifValidationFails().extract().body().path("id");
        Response robotResponse2 = RobotPage.createCustomRobotApi(authCookie, robotPage.robotAllRequiredAttrs(requestId));
        int robot2Id = robotResponse1.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке и проверить ответ на код, title, id роботов без паспорта", () -> {
            startProcessRequestRobots(authCookie, requestId)
                    .then().log().ifValidationFails()
                    .assertThat()
                    .statusCode(400)
                    .body("title", is("Не удалось запустить процесс"))
                    .body("withoutPassportRobotIds", hasItems(robot1Id, robot2Id));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Не стартует процесс от невалидной роли \"" + "{2}" + "\" по заявке роботов")
    @CsvSource(textBlock = RequestRobotsPage.invalidRolesToStartProcessRequestRobots)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void invalidRoleCanNotStartProcessRequestRobotsApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieInvalidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Создать от СА заявку, готовую для старта процесса", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().all().extract().body().path("id");
        step("Запустить процесс от невалидной роли по тестзаявке. Проверить код ответа 403, атрибут movingProcess:false", () -> {
            startProcessRequestRobots(authCookieInvalidRole, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(anyOf(is(403), is(422)));
            requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().body("movingProcess", is(false));
        });
    }

    @Test
    @DisplayName(("Не стартует процесс по удаленной заявке роботов"))
    void notStartingProcessDeletedRequestRobotsApiTest() {
        step("Создать от СА заявку, готовую для старта процесса", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().all().extract().body().path("id");
        step("Удалить заявку", () -> {
        });
        RequestRobotsPage.deleteRequestRobotsApi(authCookie, requestRobotsReadyToProcessId);
        requestRobotsPage.clearCasheRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId);
        step("Запустить процесс по тестзаявке. Проверить код ответа 422, атрибут movingProcess:false", () -> {
            startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(422);
            requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().body("movingProcess", is(false));
        });
    }

    @Test
    @DisplayName(("Не перестартует процесс по заявке роботов, если она уже в процессе"))
    void notStartingProcessRequestRobotsInProcessApiTest() {
        step("Создать от СА заявку, готовую для старта процесса", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке.", () -> {
            startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(200);
        });
        step("Запустить процесс повторно по тестзаявке. Проверить код ответа 422 и сообщение", () -> {
            startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().statusCode(422)
                    .body("message", is("По заявке " + requestRobotsReadyToProcessId + " уже запущен процесс."));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Прекращается процесс от валидной роли \"" + "{2}" + "\" по заявке роботов")
    @CsvSource(textBlock = RequestRobotsPage.validRolesToStopProcessRequestRobots)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleCanStopProcessRequestRobotsApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Создать с обязательными полями заявку с роботом, документом 'Паспорт робота', с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке", () -> {
        });
        val requestRobotsStartProcess = startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId);
        String requestProcessInstanceIdSt = requestRobotsStartProcess
                .then().statusCode(200)
                .extract().body().path("id");
        int requestProcessInstanceId = Integer.parseInt(requestProcessInstanceIdSt);
        step("Прекратить процесс по тестзаявке и проверить атрибут movingProcess:false", () -> {
            ProcessPage.stopProcessByInstanceId(authCookieValidRole, requestProcessInstanceId)
                    .then().log().ifValidationFails().statusCode(200);
            requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().body("movingProcess", is(false));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Не прекращается процесс от невалидной роли \"" + "{2}" + "\" по заявке роботов")
    @CsvSource(textBlock = RequestRobotsPage.invalidRolesToStopProcessRequestRobots)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void invalidRoleCanNotStopProcessRequestRobotsApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieInvalidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Создать от СА заявку, готовую для старта процесса", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке", () -> {
        });
        val requestRobotsStartProcess = startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId);
        String requestProcessInstanceIdSt = requestRobotsStartProcess
                .then().statusCode(200)
                .extract().body().path("id");
        int requestProcessInstanceId = Integer.parseInt(requestProcessInstanceIdSt);
        step("Прекратить процесс по тестзаявке и проверить атрибут movingProcess:false", () -> {
            ProcessPage.stopProcessByInstanceId(authCookieInvalidRole, requestProcessInstanceId)
                    .then().log().ifValidationFails().statusCode(403);
            requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().body("movingProcess", is(true));
        });
    }

    @Test
    @DisplayName("Действие 'Прекратить процесс' сбрасывает атрибуты процесса заявки роботов")
//    @Disabled
    void stopProcessClearsProcessAttrsRequestRobotsApiTest() {
        step("Создать с обязательными полями заявку с роботом, документом 'Паспорт робота', с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestRobotsReadyToProcessId = requestRobotsReadyToProcess.then().log().ifValidationFails().extract().body().path("id");
        step("Запустить процесс по тестзаявке", () -> {
        });
        val requestRobotsStartProcess = startProcessRequestRobots(authCookie, requestRobotsReadyToProcessId);
        String requestProcessInstanceIdSt = requestRobotsStartProcess
                .then().log().ifValidationFails().statusCode(200)
                .extract().body().path("id");
        int requestProcessInstanceId = Integer.parseInt(requestProcessInstanceIdSt);
        step("Прекратить процесс по тестзаявке и проверить атрибуты movingProcess, processId", () -> {
            ProcessPage.stopProcessByInstanceId(authCookie, requestProcessInstanceId)
                    .then().log().ifValidationFails().statusCode(200);
            requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestRobotsReadyToProcessId)
                    .then().log().ifValidationFails().assertThat()
                    .body("movingProcess", is(false))
                    .body("processId", is(nullValue()));
        });
    }


}
