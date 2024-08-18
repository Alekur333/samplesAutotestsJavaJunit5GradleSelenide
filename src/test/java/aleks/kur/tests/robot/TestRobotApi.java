package aleks.kur.tests.robot;

import io.restassured.response.Response;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.helpers.ApiUtils;
import ru.progredis.models.par.ParRobotRequest;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.models.robot.RobotRequest;
import ru.progredis.models.robot.RobotResponse;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.RequestRobotsPage;
import ru.progredis.pages.RobotPage;
import ru.progredis.tests.TestBaseApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.RequestRobotsPage.*;
import static ru.progredis.pages.UsersPage.*;

//@Disabled
@Tags({@Tag("api"), @Tag("requestAuto"), @Tag("robot"), @Tag("regress")})
@DisplayName("Проверки Роботов Api")
public class TestRobotApi extends TestBaseApi {

    private final static String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @AfterAll
    static void finish() {
//        String authCookie = AuthApiPage.getAuthCookie(login, passwd);
        deleteAllTestRequestRobotsByNameApi(authCookie);
    }

    RobotRequest
            robotNew = robotPage.robotNew,
            robotEditedFields = robotPage.robotEditedFields;
    ParRobotRequest newParRobotRequest = robotPage.newRobotPar();
    RequestRobotsRequest
            requestRobotsNew = requestRobotsPage.requestRobotsNew;

//    Map<String, Object> requestRobotsEditMap = requestRobotsPage.requestRobotsEditSet();

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Создания Робота api")
    void createRobotBaseChecksApiTest() {
        step("Получаем id новой заявки", () -> {
        });
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Проверка создания нового робота", () -> {
            robotPage.createRobotApi(authCookie, requestId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .header("Content-Type", equalTo("application/json"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/robotCardPostSchema.json"));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Получения карточки Робота api")
    void getRobotBaseChecksApiTest() {
        int newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        robotPage.getRobotByIdApi(authCookie, newRobotId)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCardGetSchema.json"));
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Поля при создании равны полям при получении карточки Робота api")
    void creationFieldsMatchToGetRobotApiTest() {
        step("Получаем нового робота для проверок", () -> {
            step("Получаем объект нового робота", () -> {
            });
            Response getNewRobot = robotPage.createAndGetRobotApi(authCookie);
            getNewRobot
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
            RobotResponse actualRobot = getNewRobot.as(RobotResponse.class);
//            System.out.println(actualRobot);
            step("Имя при создании = при чтении робота", () -> {
                assertEquals(robotNew.getName(), actualRobot.getName());
            });
            step("Описание при создании = при чтении робота", () -> {
                assertEquals(robotNew.getDescription(), actualRobot.getDescription());
            });
            step("Id справочника 'Количество ИС...' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getAsuNumberId(), actualRobot.getAsuNumber().getId());
            });
            step("Id справочника 'Количество файлов...' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getFilesCountId(), actualRobot.getFilesCount().getId());
            });
            step("Id справочника 'Число предполагаемых элементарных действий' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getActionsCountId(), actualRobot.getActionsCount().getId());
            });
            step("Id справочника 'Необходимость распознавания...' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getTextRecognitionId(), actualRobot.getTextRecognition().getId());
            });
            step("Id справочника 'Описание клиентских приложений' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getApplicationDescriptionId(), actualRobot.getApplicationDescription().getId());
            });
            step("Id справочника 'Вид адаптации' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getAdaptationTypeId(), actualRobot.getAdaptationType().getId());
            });
            step("Значение эффекта 'Высвобождаемые трудозатраты...ч/ч' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getFreedUpWorkManHours(), actualRobot.getFreedUpWorkManHours().intValue());
            });
            step("Значение эффекта 'Высвобождаемые трудозатраты...тыс.руб' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getReleasedCosts(), actualRobot.getReleasedCosts().intValue());
            });
            step("Значение эффекта 'Дисконтированный доход' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getDiscountedIncome(), actualRobot.getDiscountedIncome().intValue());
            });
            step("Значение эффекта 'Срок окупаемости' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getPaybackPeriod(), actualRobot.getPaybackPeriod().intValue());
            });
            step("Id справочника 'Формат сопровождения' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getSupportFormatId(), actualRobot.getSupportFormat().getId());
            });
            step("Поле 'Качественные показатели эффективности' при создании = при чтении робота", () -> {
                assertEquals(robotNew.getQualitativePerformance(), actualRobot.getQualitativePerformance());
            });
            step("Комментарии при создании = при чтении заявки", () -> {
                assertThat(robotNew.getComments(), samePropertyValuesAs(actualRobot.getComments(), "id", "updated"));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Редактирования полей Робота api")
    void patchRobotBaseChecksApiTest() {
        robotPage.createAndPatchFieldsRobotByIdApi(authCookie)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCardPostSchema.json"));
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Сохраняются отредактированные поля Робота api")
    void editedFieldsEqualsToGetRobotApiTest() {
        step("Получаем объект после редакции полей робота для проверок", () -> {
            step("Получаем тело ответа отредактированного Робота", () -> {
            });
            Response patchedRobot = robotPage.createAndPatchFieldsRobotByIdApi(authCookie);
            patchedRobot
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
            RobotResponse patchedRobotAsClass = patchedRobot.as(RobotResponse.class);
            step("Имя редактированное = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getName(), patchedRobotAsClass.getName());
            });
            step("Описание редактированное = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getDescription(), patchedRobotAsClass.getDescription());
            });
            step("Id справочника 'Количество ИС...' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getAsuNumberId(), patchedRobotAsClass.getAsuNumber().getId());
            });
            step("Id справочника 'Количество файлов...' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getFilesCountId(), patchedRobotAsClass.getFilesCount().getId());
            });
            step("Id справочника 'Число предполагаемых элементарных действий' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getActionsCountId(), patchedRobotAsClass.getActionsCount().getId());
            });
            step("Id справочника 'Необходимость распознавания...' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getTextRecognitionId(), patchedRobotAsClass.getTextRecognition().getId());
            });
            step("Id справочника 'Описание клиентских приложений' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getApplicationDescriptionId(), patchedRobotAsClass.getApplicationDescription().getId());
            });
            step("Id справочника 'Вид адаптации' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getAdaptationTypeId(), patchedRobotAsClass.getAdaptationType().getId());
            });
            step("Значение эффекта 'Высвобождаемые трудозатраты...ч/ч' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getFreedUpWorkManHours(), patchedRobotAsClass.getFreedUpWorkManHours().intValue());
            });
            step("Значение эффекта 'Высвобождаемые трудозатраты...тыс.руб' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getReleasedCosts(), patchedRobotAsClass.getReleasedCosts().intValue());
            });
            step("Значение эффекта 'Дисконтированный доход' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getDiscountedIncome(), patchedRobotAsClass.getDiscountedIncome().intValue());
            });
            step("Значение эффекта 'Срок окупаемости' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getPaybackPeriod(), patchedRobotAsClass.getPaybackPeriod().intValue());
            });
            step("Id справочника 'Формат сопровождения' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getSupportFormatId(), patchedRobotAsClass.getSupportFormat().getId());
            });
            step("Поле 'Качественные показатели эффективности' при создании = поcле редакции поля робота", () -> {
                assertEquals(robotEditedFields.getQualitativePerformance(), patchedRobotAsClass.getQualitativePerformance());
            });
            step("Комментарии при создании = поcле редакции поля заявки", () -> {
                assertThat(robotEditedFields.getComments(), samePropertyValuesAs(patchedRobotAsClass.getComments(), "id", "updated"));
            });
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Сохраняются поля ПАР Робота api")
    void parFieldsEqualsToGetRobotApiTest() {
        step("Получаем робот после заполнения полей ПАР для проверок", () -> {
        });
        Response robotWithPar = robotPage.createAndFillRobotParFieldsApi(authCookie);
        robotWithPar
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200);
        int robotId = robotWithPar.then().extract().response().body().path("id");
        RobotResponse robotWithParAsClass = robotPage.getRobotByIdApi(authCookie, robotId).as(RobotResponse.class);
        step("id 'Утверждающее лицо' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getApprover(), robotWithParAsClass.getPar().getApprover().getId());
        });
        step("Поле 'Характеристика объектов автоматизации' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getCharacteristicsOfAutomationObjects(), robotWithParAsClass.getPar().getCharacteristicsOfAutomationObjects());
        });
        step("Поле 'Заключение на программного робота' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getConclusionOnSoftwareRobot(), robotWithParAsClass.getPar().getConclusionOnSoftwareRobot());
        });
        step("Поле 'Взаимодействие программного робота со смежными системами' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getInteractionOfRobot(), robotWithParAsClass.getPar().getInteractionOfRobot());
        });
        step("Поле 'Перечень документов, в которых описаны действия в аварийных ситуациях...' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getListDocuments(), robotWithParAsClass.getPar().getListDocuments());
        });
        step("Поле 'Рекомендации по формированию требований к защите информации' при создании = поcле создания робота", () -> {
            assertEquals(newParRobotRequest.getRecommendationApplicationSoftware(), robotWithParAsClass.getPar().getRecommendationApplicationSoftware());
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, времени Удаления Робота api")
    void deleteRobotBaseChecksApiTest() {
        step("Код ответа 200, время ответа меньше 1500мс", () -> {
            robotPage.postAndDeleteRobotApi(authCookie)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L))
                    .header("Content-Length", equalTo("0"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Есть атрибут удаления после Удаления Роботa api")
    void deletedRobotHaveDeletedTrueTest() {
        step("Удалить новый робот", () -> {
        });
        int robotId = robotPage.createRobotAndGetIdApi(authCookie);
        RobotPage.deleteRobotApi(authCookie, robotId);
        Response response = robotPage.getRobotByIdApi(authCookie, robotId);
        step("Атрибут deleted: true в удаленном роботе", () -> {
            response.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("deleted", equalTo(true));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Роботы удаляются после удаления их заявки")
    void robotsAreDeletedAfterDeletingRequestApiTest() {
        step("Создать 2 новых робота в заявке", () -> {
        });
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        int robot1Id = robotPage.createRobotApi(authCookie, requestId)
                .then().statusCode(200)
                .extract().response().body().path("id");
        int robot2Id = robotPage.createRobotApi(authCookie, requestId)
                .then().statusCode(200)
                .extract().response().body().path("id");
        step("Удалить созданную заявку", () -> {
            RequestRobotsPage.deleteRequestRobotsApi(authCookie, requestId);
        });
        step("Проверить, что роботы заявки тоже удалены", () -> {
            robotPage.getRobotByIdApi(authCookie, robot1Id)
                    .then().log().ifValidationFails()
                    .assertThat().statusCode(200)
                    .body("deleted", is(true));
            robotPage.getRobotByIdApi(authCookie, robot2Id)
                    .then().log().ifValidationFails()
                    .assertThat().statusCode(200)
                    .body("deleted", is(true));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, времени Восстановления Робота api")
    void restoreRobotBaseChecksApiTest() {
        step("Удалить новый робот", () -> {
        });
        int robotId = robotPage.createRobotAndGetIdApi(authCookie);
        RobotPage.deleteRobotApi(authCookie, robotId);
        step("Код ответа 200, время ответа меньше 1500мс для запроса на восстановление", () -> {
            robotPage.restoreRobotApi(authCookie, robotId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Есть атрибут восстановления после Восстановления Робота api")
    void restoredRobotHaveDeletedFalseTest() {
        step("Удалить новый робот", () -> {
        });
        int robotId = robotPage.createRobotAndGetIdApi(authCookie);
        RobotPage.deleteRobotApi(authCookie, robotId);
        step("Восстановить робот", () -> {
            robotPage.restoreRobotApi(authCookie, robotId)
                    .then().log().ifValidationFails().assertThat().statusCode(200);
        });
        Response response = robotPage.getRobotByIdApi(authCookie, robotId);
        step("Атрибут deleted: false в удаленном роботе", () -> {
            response.then().log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("deleted", equalTo(false));
        });
    }

    static Stream<Arguments> robotNotCreatingWithoutRequiredAttrApiTest() {
//        String authCookie = AuthApiPage.getAuthCookie(login, passwd);
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsNew);
        return Stream.of(
                Arguments.of("'Название робота'", robotPage.robotNoName(requestId)),
                Arguments.of("'Описание требуемой функциональности'", robotPage.robotNoDescription(requestId)),
                Arguments.of("'Количество ИС, используемых в работе'", robotPage.robotNoAsuNumber(requestId)),
                Arguments.of("'Количество файлов, обрабатываемых роботом'", robotPage.robotNoFilesCount(requestId)),
                Arguments.of("'Число предполагаемых элементарных действий'", robotPage.robotNoActionsCount(requestId)),
                Arguments.of("'Необходимость распознавания текста в картинке'", robotPage.robotNoTextRecognition(requestId)),
                Arguments.of("'Описание клиентских приложений'", robotPage.robotNoApplicationDescription(requestId)),
                Arguments.of("'Вид адаптации'", robotPage.robotNoAdaptationType(requestId)),
                Arguments.of("'Высвобождаемые трудозатраты в год, ч/ч'", robotPage.robotNoFreedUpWorkManHours(requestId)),
                Arguments.of("'Высвобождаемые трудозатраты в год, тыс.руб'", robotPage.robotNoReleasedCosts(requestId)),
                Arguments.of("'Дисконтированный доход, тыс.руб'", robotPage.robotNoDiscountedIncome(requestId)),
                Arguments.of("'Срок окупаемости, лет'", robotPage.robotNoPaybackPeriod(requestId)),
                Arguments.of("'Формат сопровождения'", robotPage.robotNoSupportFormat(requestId))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> Не создается робот без атрибута {0} с кодом ответа 500 api")
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    void robotNotCreatingWithoutRequiredAttrApiTest(String string1, RobotRequest robot) {
        step("Создать робота без обязательного атрибута", () -> {
        });
        val respCode = 500;
        val newRobot = RobotPage.createCustomRobotApi(authCookie, robot);
        step("Код ответа на запрос " + respCode + " без атрибута " + string1, () -> {
            newRobot.then().log().ifValidationFails()
                    .assertThat().statusCode(respCode);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Создается робот при наличии всех обязательных атрибутов api")
    void robotCreatingWithAllRequiredAttrsApiTest() {
        step("Создать робота со всеми обязательными атрибутами", () -> {
        });
        val requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsNew);
        val newRobot = RobotPage.createCustomRobotApi(authCookie, robotPage.robotAllRequiredAttrs(requestId));
        step("Робот успешно создается с кодом 200, Content-Type: application/json", () -> {
            newRobot.then().log().ifValidationFails()
                    .assertThat()
                    .statusCode(200).contentType("application/json");
        });
    }


    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ в карточку робота")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessInRobotCardApiTest(String login, String passwd, String role) {
        step("Создать нового робота от СА", () -> {
        });
        val newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Получаем нового робота от пользователя с валидной ролью. Статус код - 200, тело ответа - непустой json", () -> {
            val RobotByValidRole = robotPage.getRobotByIdApi(authCookieValidRole, newRobotId);
            RobotByValidRole.then().log().ifValidationFails()
                    .assertThat().statusCode(200)
                    .contentType("application/json").body(not(empty()));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Невалидная роль \"" + "{2}" + "\" не имеет доступ в карточку робота")
    @CsvSource(textBlock = RequestRobotsPage.invalidRolesNotHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void invalidRoleNotHasAccessInRobotCardApiTest(String login, String passwd, String role) {
        step("Создать нового робота от СА", () -> {
        });
        val newRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieInvalidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Получаем нового робота от пользователя с невалидной ролью. Статус код - 403, тело ответа - Нет прав доступа", () -> {
            Response robotByInvalidRole = robotPage.getRobotByIdApi(authCookieInvalidRole, newRobotId);
            robotByInvalidRole.then().log().ifValidationFails()
                    .assertThat().statusCode(403);
//                    .contentType("application/json").body("message", equalTo("Нет прав доступа"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Пользователь в роли ФЗ имеет доступ в карточку робота заявки, у которой в поле 'ФЗ' указана его организация")
    void fzRoleHasAccessInRobotCardWithSimilarFzOrganizationApiTest() {
        step("Создать робота заявки с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val validRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieFzRole = AuthApiPage.getAuthCookie(fzLoginFromNewRequestRobotsFz, fzPassFromNewRequestRobotsFz);
        step("Запрос карточки робота валидной заявки от тестпользователя. Код ответа 200, тело ответа не пустое", () -> {
            robotPage.getRobotByIdApi(authCookieFzRole, validRobotId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200).contentType("application/json").
                    body(not(empty()));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Можно добавить в робот документ \"Паспорт робота\" от валидной роли \"" + "{2}" + "\", когда заявка находится в статусе “Черновик")
    @CsvSource(textBlock = RequestRobotsPage.validRolesToActWithRequestRobots)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleCanAddRobotPassportInRobotCardWithRequestDraftStatusApiTest(String login, String passwd, String role) {
        step("Создать робота заявки с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val validRobotId = robotPage.createRobotAndGetIdApi(authCookie);
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Добавить 'Паспорт робота' ", () -> {
        });
        val docId = documentsPage.createDocumentInRobotAndGetIdApi(authCookieValidRole, 200, validRobotId);
        step("Добавленный документ есть в списке документов тестробота", () -> {
            val docs = documentsPage.getAllObjectDocuments(authCookieValidRole, validRobotId, "ROBOT");
            docs.then().log().ifValidationFails()
                    .assertThat().statusCode(200)
                    .body("items.id", hasItem(docId));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, времени экспорта ПАР Робота api")
    void exportRobotParReportBaseChecksApiTest() {

        step("Код ответа 200, время ответа меньше 1500мс для запроса на отчет ПАР", () -> {
            robotPage.robotParFileApi(authCookie)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Файл экспорта имеет валидное имя по действию «Скачать ПАР» робота api")
    void fileShouldHaveValidNameByActionDownloadRobotParReportApiTest() throws UnsupportedEncodingException {

        step("Получаем id нового робота с ПАР", () -> {
        });
        val robotWithPar = robotPage.createAndFillRobotParFieldsApi(authCookie);
        int robotId = robotWithPar.then().extract().path("id");
        step("Получаем имя файла из экспорта ПАР", () -> {
        });
        val parReportFile = robotPage.exportRobotParReportApi(authCookie, robotId);
        parReportFile.then().log().ifValidationFails()
                .assertThat().statusCode(200);
        val decodedFileName = ApiUtils.getFileNameFromResponseDecoded(parReportFile);
        step("Имя файла должно соотвествовать шаблону", () -> {
            assertEquals("ПАР к роботу " + robotId + ".docx", decodedFileName);
        });
    }

    static Stream<Arguments> fileShouldHaveValidDataByActionDownloadRobotParReportApiTest() {

        step("Получаем id нового робота с ПАР", () -> {
        });
        val robotWithPar = robotPage.createAndFillRobotParFieldsApi(authCookie);
        int robotId = robotWithPar.then().extract().path("id");
        RobotResponse robot = robotPage.getRobotByIdApi(authCookie, robotId).as(RobotResponse.class);

        step("Получаем файл из экспорта ПАР", () -> {
        });
        val parReportFile = robotPage.exportRobotParReportApi(authCookie, robotId);
        parReportFile.then().log().ifValidationFails()
                .assertThat().statusCode(200);

        step("Получить файл экспорта и создать переменные с данными из ПАР и файла", () -> {
        });
        final InputStream fileAsInputStream = parReportFile.asInputStream();
        try {
            // Для .doc файлов можно использовать HWPFDocument вместо XWPFDocument.
            XWPFDocument document = new XWPFDocument(fileAsInputStream);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            String
                    paragraph2Exp = "УТВЕРЖДАЮ", //
                    paragraph2Act = paragraphs.get(1).getText(), //
                    paragraph3Exp = robot.getPar().getApprover().getPosition(), //
                    paragraph3Act = paragraphs.get(2).getText(), //
                    paragraph4Exp = robot.getPar().getApprover().getDepartmentName(), //
                    paragraph4Act = paragraphs.get(3).getText(), //
                    paragraph5Exp = robot.getPar().getApprover().getShortName(), //
                    paragraph5Act = paragraphs.get(4).getText(), //
                    paragraph6Exp = " «___» _________   _____ г.", //
                    paragraph6Act = paragraphs.get(5).getText(), //
                    paragraph11Exp = "ПРЕДЛОЖЕНИЕ ПО АРХИТЕКТУРНОМУ РЕШЕНИЮ \n" +
                            "на программного робота", //
                    paragraph11Act = paragraphs.get(10).getText(), //
                    paragraph12Exp = "(" + robotId + ") «" + robot.getName() + "»", //
                    paragraph12Act = paragraphs.get(11).getText(), //
                    paragraph13Exp = "к заявке на роботизацию бизнес-процессов", //
                    paragraph13Act = paragraphs.get(12).getText(), //
                    paragraph14Exp = "(" + robot.getRequest().getId() + ") «" + robot.getRequest().getName() + "»", //
                    paragraph14Act = paragraphs.get(13).getText(), //
                    paragraph19Exp = "код документа " + currentYear + "-" + robotId, //
                    paragraph19Act = paragraphs.get(18).getText(), //
                    paragraph22Exp = "\n", //
                    paragraph22Act = paragraphs.get(21).getText(), //
                    paragraph23Exp = "Характеристика объектов автоматизации", //
                    paragraph23Act = paragraphs.get(22).getText(), //
                    paragraph24Exp = robot.getPar().getCharacteristicsOfAutomationObjects(), //
                    paragraph24Act = paragraphs.get(23).getText(), //
                    paragraph25Exp = "Рекомендации по формированию требований к структуре и функционированию программного робота", //
                    paragraph25Act = paragraphs.get(24).getText(), //
                    paragraph26Exp = "Взаимодействие программного робота со смежными системами", //
                    paragraph26Act = paragraphs.get(25).getText(), //
                    paragraph27Exp = robot.getPar().getInteractionOfRobot(), //
                    paragraph27Act = paragraphs.get(26).getText(), //
                    paragraph28Exp = "Перечень документов, в которых описаны действия в аварийных ситуациях, направленные на восстановление работоспособности программного робота ", //
                    paragraph28Act = paragraphs.get(27).getText(), //
                    paragraph29Exp = robot.getPar().getListDocuments(), //
                    paragraph29Act = paragraphs.get(28).getText(), //
                    paragraph30Exp = "Рекомендации по формированию требований к защите информации ", //
                    paragraph30Act = paragraphs.get(29).getText(), //
                    paragraph31Exp = robot.getPar().getRecommendationInformationProtection(), //
                    paragraph31Act = paragraphs.get(30).getText(), //
                    paragraph32Exp = "Рекомендации по формированию требований к прикладному ПО, используемому программным роботом", //
                    paragraph32Act = paragraphs.get(31).getText(), //
                    paragraph33Exp = robot.getPar().getRecommendationApplicationSoftware(), //
                    paragraph33Act = paragraphs.get(32).getText(), //
                    paragraph34Exp = "Заключение на программного робота", //
                    paragraph34Act = paragraphs.get(33).getText(), //
                    paragraph35Exp = robot.getPar().getConclusionOnSoftwareRobot(), //
                    paragraph35Act = paragraphs.get(34).getText(); //

            step("Сформировать поток данных для тестирования", () -> {
            });
            return Stream.of(
                    Arguments.of("Шапка 1 строка", paragraph2Exp, paragraph2Act, document, fileAsInputStream),
                    Arguments.of("Шапка 2 строка", paragraph3Exp, paragraph3Act, document, fileAsInputStream),
                    Arguments.of("Шапка 3 строка", paragraph4Exp, paragraph4Act, document, fileAsInputStream),
                    Arguments.of("Шапка 4 строка", paragraph5Exp, paragraph5Act, document, fileAsInputStream),
                    Arguments.of("Шапка 5 строка", paragraph6Exp, paragraph6Act, document, fileAsInputStream),
                    Arguments.of("Оглавление 1 строка", paragraph11Exp, paragraph11Act, document, fileAsInputStream),
                    Arguments.of("Оглавление 2 строка", paragraph12Exp, paragraph12Act, document, fileAsInputStream),
                    Arguments.of("Оглавление 3 строка", paragraph13Exp, paragraph13Act, document, fileAsInputStream),
                    Arguments.of("Оглавление 4 строка", paragraph14Exp, paragraph14Act, document, fileAsInputStream),
                    Arguments.of("Код документа", paragraph19Exp, paragraph19Act, document, fileAsInputStream),
                    Arguments.of("Строка окончания 1й страницы", paragraph22Exp, paragraph22Act, document, fileAsInputStream),
                    Arguments.of("1я строка 2й страницы", paragraph23Exp, paragraph23Act, document, fileAsInputStream),
                    Arguments.of("2я строка 2й страницы", paragraph24Exp, paragraph24Act, document, fileAsInputStream),
                    Arguments.of("3я строка 2й страницы", paragraph25Exp, paragraph25Act, document, fileAsInputStream),
                    Arguments.of("4я строка 2й страницы", paragraph26Exp, paragraph26Act, document, fileAsInputStream),
                    Arguments.of("5я строка 2й страницы", paragraph27Exp, paragraph27Act, document, fileAsInputStream),
                    Arguments.of("6я строка 2й страницы", paragraph28Exp, paragraph28Act, document, fileAsInputStream),
                    Arguments.of("7я строка 2й страницы", paragraph29Exp, paragraph29Act, document, fileAsInputStream),
                    Arguments.of("8я строка 2й страницы", paragraph30Exp, paragraph30Act, document, fileAsInputStream),
                    Arguments.of("9я строка 2й страницы", paragraph31Exp, paragraph31Act, document, fileAsInputStream),
                    Arguments.of("10я строка 2й страницы", paragraph32Exp, paragraph32Act, document, fileAsInputStream),
                    Arguments.of("11я строка 2й страницы", paragraph33Exp, paragraph33Act, document, fileAsInputStream),
                    Arguments.of("13я строка 2й страницы", paragraph34Exp, paragraph34Act, document, fileAsInputStream),
                    Arguments.of("14я строка 2й страницы", paragraph35Exp, paragraph35Act, document, fileAsInputStream)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> Валидное значение в \"{0}\" в файле экспорта по действию «Скачать ПАР» робота api")
//    @Disabled
    void fileShouldHaveValidDataByActionDownloadRobotParReportApiTest(
            String attrName, String dataExpected, String dataActual, XWPFDocument document, InputStream
            fileAsInputStream) {

        System.out.println("Проверка данных в '" + attrName + "'");
        System.out.println("Значение факт = '" + dataActual + "'");
        assertEquals(dataExpected, dataActual);
        // Закрытие document и InputStream
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOUtils.closeQuietly(fileAsInputStream);
    }

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ к действию «Скачать ПАР» api")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessToDownloadRobotParReportApiTest(String login, String passwd, String role) {

        step("Получаем id нового робота с ПАР", () -> {
        });
        val robotWithPar = robotPage.createAndFillRobotParFieldsApi(authCookie);
        int robotId = robotWithPar.then().extract().path("id");

        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);

        step("Запрос по действию «Скачать ПАР» от нужного пользователя", () -> {
            val parReportFile = robotPage.exportRobotParReportApi(authCookieValidRole, robotId);
            parReportFile.then().log().ifValidationFails()
                    .assertThat().statusCode(200);
        });
    }


}
