package aleks.kur.tests.robot;

import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.models.robot.RobotRequest;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.RequestRobotsPage;
import ru.progredis.pages.RobotPage;
import ru.progredis.tests.TestBaseApi;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.pages.RequestRobotsPage.*;
import static ru.progredis.pages.SqlRequestsPage.updateDbTableV4LongFieldByObjectId;
import static ru.progredis.pages.UsersPage.*;

//@Disabled
@Tags({@Tag("regress"), @Tag("api"), @Tag("requestAuto"), @Tag("robot"), @Tag("robotsList")})
@DisplayName("Проверки списка Роботов Api")
public class TestRobotsListApi extends TestBaseApi {
    @AfterAll
    static void finish() {
        String authCookie = AuthApiPage.getAuthCookie(login, passwd);
        deleteAllTestRequestRobotsByNameApi(authCookie);
    }

    String
            authCookie = AuthApiPage.getAuthCookie(login, passwd);

    RequestRobotsRequest
            requestRobotsNew = requestRobotsPage.requestRobotsNew,
            requestRobotsEdited = requestRobotsPage.requestRobotsEdited;

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени получения списка Роботов api")
    void getRobotsListBaseChecksApiTest() {
        step("Создать новый Робот", () -> {
        });
        robotPage.createRobotAndGetIdApi(authCookie);
        step("Получить список роботов и проверить, что код ответа 200, время ответа < 1500мс, схема валидна", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, 0, 5, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/getRobotsListSchema.json"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Новый Робот есть в списке Роботов api")
    void newRobotExistInRobotListApiTest() {
        step("Создать новый Робот", () -> {
        });
        int newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Получить список роботов и проверить в нем наличие нового", () -> {
            RobotPage.getRobotsListApi(authCookie, null, null, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", hasItem(newRobotId));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Удаленного робота нет в списке заявок роботов api")
    void noDeletedRobotInRobotsListApiTest() {
        step("Создать новый Робот", () -> {
        });
        int newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Удалить созданный робот", () -> {
            RobotPage.deleteRobotApi(authCookie, newRobotId);
        });
        step("Проверить, что удаленного робота нет в списке", () -> {
            RobotPage.getRobotsListApi(authCookie, null, null, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", not(hasItem(newRobotId)));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Фильтр 'Показать удаленные' возвращает удаленный робот в списке роботов")
    void showDeletedRobotsFilterReturnDeletedItemInRobotsListApiTest() {
        step("Создать новый Робот", () -> {
        });
        int newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Удалить созданный робот", () -> {
            RobotPage.deleteRobotApi(authCookie, newRobotId);
        });
        step("Проверить, что фильтр 'Показать удаленные' возвращает удаленный робот", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, 0, 10, "id,DESC", true, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", hasItem(newRobotId));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Фильтр 'Показать удаленные' возвращает валидные роботы в списке роботов")
    void showDeletedRobotsFilterReturnValidItemsInRobotsListApiTest() {
        step("Создать 2 новых робота", () -> {
        });
        int robotToDeleteId = robotPage.createRobotAndGetIdApi(authCookie);
        robotPage.createRobotAndGetIdApi(authCookie);
        step("Удалить 1 созданный робот", () -> {
            RobotPage.deleteRobotApi(authCookie, robotToDeleteId);
        });
        step("Проверить, что фильтр 'Показать удаленные' возвращает удаленные и неудаленные роботы", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, 0, 10, "id,DESC", true, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.deleted", hasItem(false))
                    .body("items.deleted", hasItem(true));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Фильтр 'Год' возвращает валидные роботы в списке роботов")
    void yearFilterReturnValidItemsInRobotsListApiTest() {
        int yearOfNewRequest = requestRobotsPage.requestRobotsNew.getYear();
        step("Создать новый Робот", () -> {
        });
        int newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Проверить, что фильтр по году заявки нового робота возвращает роботов из заявок только этого года", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, null, null, "id,DESC", null, yearOfNewRequest, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.request.year", not(hasItem(not(yearOfNewRequest))));
        });
    }

    @ParameterizedTest(name = "Фильтр 'Количество записей на страницу' {0} возвращает установленное количество в списке роботов")
    @ValueSource(ints = {5, 10})
//    @ValueSource(ints = {5, 10, 15, 20})
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void itemsPerPageFilterReturnValidItemsInRobotsListApiTest(int itemsPerPage) {
        step("Проверить, что фильтр 'Количество записей на страницу' возвращает нужное количнство заявок", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, 0, itemsPerPage, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.size()", equalTo(itemsPerPage));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Поиск по имени возвращает валидные записи в списке роботов")
    void searchByNameReturnValidItemsInRobotsListApiTest() {
        step("Создать робота с именем для поиска", () -> {
        });
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsNew);
        String nameForSearch = "Полное имя для для поиска autoTest " + faker.lorem().characters(5);
        RobotRequest nameForSearchRobot = new RobotRequest(
                requestId + 0,
                nameForSearch + "",
                robotPage.robotNew.getDescription() + "",
                robotPage.robotNew.getAsuNumberId() + 0,
                robotPage.robotNew.getFilesCountId() + 0,
                robotPage.robotNew.getActionsCountId() + 0,
                robotPage.robotNew.getTextRecognitionId() + 0,
                robotPage.robotNew.getApplicationDescriptionId() + 0,
                robotPage.robotNew.getAdaptationTypeId() + 0,
                robotPage.robotNew.getFreedUpWorkManHours() + 0,
                robotPage.robotNew.getReleasedCosts() + 0,
                robotPage.robotNew.getDiscountedIncome() + 0,
                robotPage.robotNew.getPaybackPeriod() + 0,
                robotPage.robotNew.getSupportFormatId() + 0,
                robotPage.robotNew.getQualitativePerformance() + "",
                null,
                null
        );
        robotPage.createCustomRobotApi(authCookie, nameForSearchRobot)
                .then()
                .log().all()
                .statusCode(200);
        step("Есть в списке робот с именем для поиска", () -> {
            RobotPage.getRobotsListApi(
                            authCookie, 0, null, "id,DESC", null, null, nameForSearch)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.name", hasItem(nameForSearch));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ к списку роботов")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessToRobotsListApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        String authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос списка Роботов с куками от нужного пользователя", () -> {
            RobotPage.getRobotsListApi(
                            authCookieValidRole, 0, 5, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200).contentType("application/json");
        });
    }


    @ParameterizedTest(name = "Тест #{index} -> Невалидная роль \"" + "{2}" + "\" не имеет доступ к списку заявок роботов")
    @CsvSource(textBlock = RequestRobotsPage.invalidRolesNotHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void invalidRoleNotHasAccessToRobotsListApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        authCookie = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос списка Роботов с куками от нужного пользователя", () -> {
            RobotPage.getRobotsListApi(
                        authCookie, 0, 5, "id,DESC", null, null, null)
                .then()
                .log().all()
                .assertThat()
                .statusCode(403);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Пользователь в роли ФЗ получает только роботы, у которых в поле 'ФЗ' указана его организация")
    void fzRoleGetsRobotsWithSimilarFzOrganizationInRobotsListApiTest() {
        step("Создать одну заявку с ФЗ, как у тестпользователя с ролью 'ФЗ', и вторую c другой организацией", () -> {
        });
        int requestIdHadFzEqualTestPersonOrg = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsEdited);
        step("Создать робота в заявку с ФЗ, как у тестпользователя с ролью 'ФЗ'", () -> {
            robotPage.createRobotApi(authCookie, requestIdHadFzEqualTestPersonOrg);
        });
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        String authCookieFzRole = AuthApiPage.getAuthCookie(fzLoginFromNewRequestRobotsFz,fzPassFromNewRequestRobotsFz);
        step("Запрос списка роботов от тестпользователя. ФЗ всех робототов = организации тестпользователя", () -> {
        RequestRobotsPage.getRequestRobotsListApi(
                        authCookieFzRole, 0, null, "id,DESC", null, null, null)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("items.companyFz.id", everyItem(equalTo(requestRobotsNew.getCompanyFzId())));
        });
    }

    static Stream<Arguments> filterValuesRobotsListOfRequestBaseChecksApiTest() {

        val authCookie = AuthApiPage.getAuthCookie(login, passwd);
        // создать тестзаявку с роботом и получить их id
        final int robotId = robotPage.createRobotAndGetIdApi(authCookie);
        final int requestId = robotPage.getRobotByIdApi(authCookie, robotId)
                .then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().path("request.id");
        // записать в тестробот id работы запросом в БД
        String
                tableName = "robot",
                tableFieldName = "plan_pi_work_id";
        Long tableFieldValue = 18_907L;  // id работы
        updateDbTableV4LongFieldByObjectId(tableName, tableFieldName, tableFieldValue, (long) robotId);
        // собрать переменные для теста
        return Stream.of(
                Arguments.of("status", requestId, authCookie),
                Arguments.of("work", requestId, authCookie),
                Arguments.of("workStatus", requestId, authCookie)
        );
    }

    @MethodSource
    @Tags({@Tag("sql")})
    @ParameterizedTest(name = "Тест #{index} -> Для запроса значений фильтра по полю {0} ответ имеет валидный код, тип содержимого, схему данных в списке роботов заявки")
//    @Disabled
    void filterValuesRobotsListOfRequestBaseChecksApiTest(String columnName, int requestId, String authCookie) {

        filterValuesRobotsListOfRequest(authCookie, columnName, requestId)
                .then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/filterValuesRobotsListSchema.json"));
    }

    @ValueSource(strings = {"status", "work", "workStatus", "companyFz", "asu", "requestId",
            "gvcResponsible", "workResponsiblePerson", "year", "agreement", "agreementManager"})
    @ParameterizedTest(name = "Тест #{index} -> Для запроса значений фильтра по полю {0} ответ имеет валидный код, тип содержимого, схему данных в списке роботов")
//    @Disabled
    void filterValuesRobotsListBaseChecksApiTest(String columnName) {

        val authCookie = AuthApiPage.getAuthCookie(login, passwd);
        RobotPage.filterValuesByColumnNameOfRobotsListApi(authCookie, columnName)
                .then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/filterValuesRobotsListSchema.json"));
    }


}
