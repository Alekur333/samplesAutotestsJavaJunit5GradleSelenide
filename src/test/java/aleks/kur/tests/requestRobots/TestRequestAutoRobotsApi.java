package aleks.kur.tests.requestRobots;

import io.restassured.response.Response;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.helpers.ApiUtils;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.models.request.RequestRobotsResponse;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.RequestRobotsPage;
import ru.progredis.tests.TestBaseApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.RequestRobotsPage.*;
import static ru.progredis.pages.UsersPage.*;

//@Disabled
@Tags({@Tag("api"), @Tag("requestAuto"), @Tag("requestRobots"), @Tag("regress")})
@DisplayName("Проверки карточки Заявки Роботизация Api")
public class TestRequestAutoRobotsApi extends TestBaseApi {

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

//    Map<String, Object> requestRobotsEditMap = requestRobotsPage.requestRobotsEditSet();

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Создания заявки Роботизация api")
    void createRequestRobotsBaseChecksApiTest() {
        requestRobotsPage.createRequestRobotsApi(authCookie, requestRobotsPage.requestRobotsNew)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/requestRobotsSchema.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Получения заявки Роботизация api")
    void getRequestRobotsBaseChecksApiTest() {
        requestRobotsPage.createAndGetRequestRobotsByIdApi(authCookie, requestRobotsNew)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/requestRobotsSchema.json"));
    }

    static Stream<Arguments> requestRobotsNotCreatingWithoutRequiredAttrApiTest() {
        return Stream.of(
                Arguments.of("Не создается", "без", "'Полное наименование'", 500, requestRobotsPage.requestRobotsNoFullName),
                Arguments.of("Не создается", "без", "'Краткое наименование'", 500, requestRobotsPage.requestRobotsNoShortName),
                Arguments.of("Создается", "при наличии", "'Полное и Краткое наименование'", 200, requestRobotsPage.requestRobotsOnlyNames)
        );
    }

    @MethodSource
    @ParameterizedTest(name = "{0} заявка роботов {1} обязательного атрибута {2} api")
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
//    @DisplayName("Нельзя создать заявку роботов без обязательных атрибутов api")
    void requestRobotsNotCreatingWithoutRequiredAttrApiTest(String word1, String word2, String word3, int respCode, RequestRobotsRequest request) {
        step("Создать новую заявку", () -> {
        });
        Response newRequest = requestRobotsPage.createRequestRobotsApi(authCookie, request);
        step("Ответ на запрос " + respCode + " , заявка " + word1, () -> {
            newRequest.then().log().ifValidationFails()
                    .assertThat()
                    .statusCode(respCode);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Поля при создании равны полям при получении новой заявки Роботизация api")
    void createdFieldsEqualsToGetRequestRobotsApiTest() {
        step("Получаем объект новой заявки для проверок", () -> {
            step("Получаем объект новой заявки", () -> {
            });
            Response getNewRequestByIdApi = requestRobotsPage.createAndGetRequestRobotsByIdApi(authCookie, requestRobotsNew);
            getNewRequestByIdApi
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
            RequestRobotsResponse actualRequestRobots = getNewRequestByIdApi.as(RequestRobotsResponse.class);
            step("Полное имя при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getName(), requestRobotsNew.getName());
            });
            step("Краткое имя при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getShortName(), requestRobotsNew.getShortName());
            });
            step("Год при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getYear(), requestRobotsNew.getYear());
            });
            step("Id АСУ при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getAsu().getId(), requestRobotsNew.getAsuId());
            });
            step("Id ФЗ при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getCompanyFz().getId(), requestRobotsNew.getCompanyFzId());
            });
            step("Id Ответственный от ФЗ при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getFzResponsiblePerson().getId(), requestRobotsNew.getResponsiblePersonFzId());
            });
            step("Id Программ мероприятий при создании = при чтении заявки", () -> {
                assertEquals(actualRequestRobots.getMeasuresPrograms()[0].getMeasuresProgram().getId(), requestRobotsNew.getMeasuresProgramsIds()[0]);
            });
            step("Комментарии при создании = при чтении заявки", () -> {
                assertThat(actualRequestRobots.getComments(), samePropertyValuesAs(requestRobotsNew.getComments(), "id", "updated"));
//                assertThat(actualRequestRobots.getComments()[0], samePropertyValuesAs(requestRobotsNew.getComments()[0], "id", "updated"));
//                assertThat(actualRequestRobots.getComments()[1], samePropertyValuesAs(requestRobotsNew.getComments()[1], "id", "updated"));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Редактирования заявки Роботизация api")
    void patchRequestRobotsBaseChecksApiTest() {
        requestRobotsPage.createAndPatchFieldsRequestRobotsByIdApi(authCookie, requestRobotsNew)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/requestRobotsSchema.json"));
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Сохраняются отредактированные поля заявки Роботизация api")
    void editedFieldsEqualsToGetRequestRobotsApiTest() {
        step("Получаем объект отредактированной заявки для проверок", () -> {
            step("Получаем тело ответа отредактированной заявки", () -> {
            });
            Response getPatchedRequestRobots = requestRobotsPage.createAndPatchFieldsRequestRobotsByIdApi(authCookie, requestRobotsNew);
            getPatchedRequestRobots
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
            RequestRobotsResponse patchedRequestRobots = getPatchedRequestRobots.as(RequestRobotsResponse.class);
            RequestRobotsResponse getEditedRequestRobots = requestRobotsPage
                    .getRequestRobotsByIdApi(authCookie, patchedRequestRobots.getId())
                    .as(RequestRobotsResponse.class);
            step("Полное имя редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getName(), requestRobotsEdited.getName());
            });
            step("Краткое имя редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getShortName(), requestRobotsEdited.getShortName());
            });
            step("Год редактированный = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getYear(), requestRobotsEdited.getYear());
            });
            step("Id АСУ редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getAsu().getId(), requestRobotsEdited.getAsuId());
            });
            step("Id ФЗ редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getCompanyFz().getId(), requestRobotsEdited.getCompanyFzId());
            });
            step("Id Ответственный от ФЗ редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getFzResponsiblePerson().getId(), requestRobotsEdited.getResponsiblePersonFzId());
            });
            step("Id Программ мероприятий редактированное = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getMeasuresPrograms()[0].getMeasuresProgram().getId(), requestRobotsEdited.getMeasuresProgramsIds()[0]);
                assertEquals(getEditedRequestRobots.getMeasuresPrograms()[1].getMeasuresProgram().getId(), requestRobotsEdited.getMeasuresProgramsIds()[1]);
            });
            step("Комментарии редактированные и добавленные = поcле редакции заявки", () -> {
                assertEquals(getEditedRequestRobots.getComments()[0].getText(), "Edited " + requestRobotsPage.newComment1.getText());
                assertEquals(getEditedRequestRobots.getComments()[1].getText(), requestRobotsPage.newComment2.getText());
                assertEquals(getEditedRequestRobots.getComments()[2].getText(), requestRobotsPage.newComment3.getText());
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, времени Удаления заявки Роботизация api")
    void deleteRequestRobotsBaseChecksApiTest() {
        step("Код ответа 200, время ответа меньше 1500мс", () -> {
            requestRobotsPage.postAndDeleteRequestRobotsByIdApi(authCookie, requestRobotsNew)
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
    @DisplayName("Есть атрибут удаления после Удаления заявки Роботизация api")
    void deletedRequestRobotsHaveDeletedTrueTest() {
        step("Удалить новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.deleteRequestRobotsApi(authCookie, newRequestId);
        RequestRobotsResponse response = requestRobotsPage.getRequestRobotsByIdApi(authCookie, newRequestId).as(RequestRobotsResponse.class);
        step("Атрибут deleted: true в удаленной заявке", () -> {
            assertEquals(true, response.getDeleted());
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, времени Восстановления заявки Роботизация api")
    void restoreRequestRobotsBaseChecksApiTest() {
        step("Удалить новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.deleteRequestRobotsApi(authCookie, newRequestId);
        step("Код ответа 200, время ответа меньше 1500мс для запроса на восстановление", () -> {
            requestRobotsPage.restoreRequestRobotsApi(authCookie, newRequestId)
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
    @DisplayName("Есть атрибут восстановления после Восстановления заявки Роботизация api")
    void restoredRequestRobotsHaveDeletedFalseTest() {
        step("Удалить новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.deleteRequestRobotsApi(authCookie, newRequestId);
        step("Восстановить заявку", () -> {
            requestRobotsPage.restoreRequestRobotsApi(authCookie, newRequestId);
        });
        step("Атрибут deleted: false в воссстановленной заявке", () -> {
            RequestRobotsResponse response = requestRobotsPage
                    .getRequestRobotsByIdApi(authCookie, newRequestId).as(RequestRobotsResponse.class);
            assertEquals(false, response.getDeleted());
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Добавления комментария к заявке Роботизация api")
    void postNewCommentRequestRobotsBaseChecksApiTest() {
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Добавить комментарий и проверить, что код ответа 200, время ответа < 1500мс, схема валидна", () -> {
            requestRobotsPage.addCommentToRequestRobots(authCookie, newRequestId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/comment.json"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Добавленный комментарий сохраняется в заявке Роботизация api")
    void requestRobotsHaveAddedCommentApiTest() {
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Добавить комментарий", () -> {
            requestRobotsPage.addCommentToRequestRobots(authCookie, newRequestId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);
        });
        step("Добавленный комментарий сохранился в заявке", () -> {
            RequestRobotsResponse requestRobotsWithEddedComment = requestRobotsPage
                    .getRequestRobotsByIdApi(authCookie, newRequestId).as(RequestRobotsResponse.class);
            assertEquals(requestRobotsPage.addComment1.getText(), requestRobotsWithEddedComment.getComments()[2].getText());
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ в карточку заявки роботов")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessToRequestRobotsCardApiTest(String login, String passwd, String role) {
        // https://wk.progredis.ru/functionality/user/RoutineAutomatization/Requirements/NewChapterRequestsForRobotization/Requests
        step("Получаем id новой тестзаявки роботов", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос тестзаявки с куками от нужного пользователя", () -> {
            requestRobotsPage.getRequestRobotsByIdApi(authCookieValidRole, newRequestId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200).contentType("application/json");
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Невалидная роль \"" + "{2}" + "\" не имеет доступ в карточку заявки роботов")
    @CsvSource(textBlock = RequestRobotsPage.invalidRolesNotHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void invalidRoleNotHasAccessToRequestRobotsCardApiTest(String login, String passwd, String role) {
        step("Получаем id новой тестзаявки роботов", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieInvalidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос тестзаявки с куками от нужного пользователя. В ответе код 403 и сообщение об отсутсвии прав", () -> {
            requestRobotsPage.getRequestRobotsByIdApi(authCookieInvalidRole, newRequestId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(403).body("message", equalTo("Нет прав доступа"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Пользователь в роли ФЗ имеет доступ в карточку заявки, у которой в поле 'ФЗ' указана его организация")
    void fzRoleGetsRequestRobotsCardWithSimilarFzOrganizationApiTest() {
        step("Создать заявку с ФЗ, как организация тестпользователя с ролью 'ФЗ'", () -> {
        });
        val validRequest = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieFzRole = AuthApiPage.getAuthCookie(fzLoginFromNewRequestRobotsFz, fzPassFromNewRequestRobotsFz);
        step("Запрос карточки валидной заявки от тестпользователя. ФЗ заявки = организации тестпользователя. Код ответа 200", () -> {
            requestRobotsPage.getRequestRobotsByIdApi(authCookieFzRole, validRequest)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200).contentType("application/json").
                    body(not(empty()));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Файл экспорта имеет валидное имя по действию «Скачать заявку» заявки роботов api")
    void fileShouldHaveValidNameByActionDownloadRequestRobotsApiTest() {
        step("Получаем id новой тестзаявки роботов", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsOnlyNames);
        step("Действие «Скачать заявку». Проверяем имя файла в ответе по шаблону", () -> {
            val export = RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookie, newRequestId);
            export
                    .then().log().ifValidationFails()
                    .assertThat().statusCode(200);
            val decodedFileName = ApiUtils.getFileNameFromResponseDecoded(export);
            assertEquals("Заявка на роботизацию " + newRequestId + ".xlsx", decodedFileName);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ к действию «Скачать заявку» заявки роботов api")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessToActionDownloadRequestRobotsApiTest(String login, String passwd, String role) {
        // https://wk.progredis.ru/functionality/user/RoutineAutomatization/Requirements/ActionsWithRequests#действие-скачать-заявку
        step("Получаем id новой тестзаявки роботов", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос действия «Скачать заявку» с куками от нужного пользователя", () -> {
            RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookieValidRole, newRequestId)
                    .then().log().ifValidationFails()
                    .assertThat().statusCode(200);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Валидное имя листа и количество листов в файле экспорта по действию «Скачать заявку» заявки роботов api")
    void fileShouldHaveValidSheetNameByActionDownloadRequestRobotsApiTest() {
        step("Получаем id новой тестзаявки роботов и скачиваем заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsOnlyNames);
        // Получение ответа как InputStream
        final InputStream fileAsInputStream = RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookie, newRequestId)
                .asInputStream();
        try {
            // Преобразование InputStream в файл XLS
            Workbook workbook = WorkbookFactory.create(fileAsInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            step("Количество листов в таблице = 1", () -> {
                assertEquals(1, workbook.getNumberOfSheets());
            });
            step("Название листа таблицы по шаблону", () -> {
                assertEquals("Экспорт заявки на Роботизацию", sheet.getSheetName());
            });
            // Закрытие workbook и InputStream
            workbook.close();
            IOUtils.closeQuietly(fileAsInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
//    @Disabled
    @DisplayName("Атрибуты заявки по шаблону в элементах файла экспорта по действию «Скачать заявку» заявки роботов api")
    void fileShouldHaveValidRequestElementsByActionDownloadRequestRobotsApiTest() {
        step("Получаем id и имя новой тестзаявки роботов", () -> {
        });
        val requestRobots = requestRobotsPage.createAndGetRequestRobotsByIdApi(authCookie, requestRobotsPage.requestRobotsOnlyNames);
        int requestId = requestRobots.then().log().ifError().extract().body().path("id");
        String requestFullName = requestRobots.then().log().ifError().extract().body().path("name");
        step("Действие «Скачать заявку»", () -> {
        });
        // Получение ответа как InputStream
        final InputStream fileAsInputStream = RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookie, requestId)
                .asInputStream();
        try {
            // Преобразование InputStream в файл XLS
            Workbook workbook = WorkbookFactory.create(fileAsInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            step("Заголовок по шаблону", () -> {
                String headerNameTemplate = "Заявка на роботизацию бизнес-процессов №" + requestId,
                        headerNameActual = sheet.getRow(0).getCell(0).getStringCellValue();
                assertEquals(headerNameTemplate, headerNameActual);
            });
            step("Имя заявки по шаблону", () -> {
                String requestNameTemplate = "Наименование: " + requestFullName,
                        requestNameActual = sheet.getRow(2).getCell(0).getStringCellValue();
                assertEquals(requestNameTemplate, requestNameActual);
            });
            // Закрытие workbook и InputStream
            workbook.close();
            IOUtils.closeQuietly(fileAsInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
//    @Disabled
    @DisplayName("Имена колонок и их порядок по шаблону в файле экспорта по действию «Скачать заявку» заявки роботов api")
    void fileShouldHaveValidColumnNamesAndOrderByActionDownloadRequestRobotsApiTest() {
        step("Получаем id новой тестзаявки роботов", () -> {
        });
        val requestRobots = requestRobotsPage.createAndGetRequestRobotsByIdApi(authCookie, requestRobotsPage.requestRobotsOnlyNames);
        int requestId = requestRobots.then()
                .log().ifError()
                .log().ifValidationFails()
                .extract().body().path("id");
        step("Действие «Скачать заявку»", () -> {
        });
        // Получение ответа как InputStream
        final InputStream fileAsInputStream = RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookie, requestId)
                .asInputStream();
        try {
            // Преобразование InputStream в файл XLS
            Workbook workbook = WorkbookFactory.create(fileAsInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            step("Название 1й колонки по шаблону", () -> {
                String columnNameTemplate = "id",
                        columnNameActual = sheet.getRow(4).getCell(0).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 1-15й колонки по шаблону", () -> {
                String columnNameTemplate = "Описание",
                        columnNameActual = sheet.getRow(4).getCell(1).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 2й колонки по шаблону", () -> {
                String columnNameTemplate = "Наименование программного робота",
                        columnNameActual = sheet.getRow(5).getCell(1).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 3й колонки по шаблону", () -> {
                String columnNameTemplate = "Краткое описание требуемой функциональности программного робота",
                        columnNameActual = sheet.getRow(5).getCell(2).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 4-8й колонки по шаблону", () -> {
                String columnNameTemplate = "Параметры и характеристики программного робота (критерии, влияющие на сложность программного робота)",
                        columnNameActual = sheet.getRow(5).getCell(3).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 4й колонки по шаблону", () -> {
                String columnNameTemplate = "Количество информационных систем ОАО «РЖД», используемых в работе программного робота",
                        columnNameActual = sheet.getRow(6).getCell(3).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 5й колонки по шаблону", () -> {
                String columnNameTemplate = "Количество входных/выходных файлов, обрабатываемых роботом",
                        columnNameActual = sheet.getRow(6).getCell(4).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 6й колонки по шаблону", () -> {
                String columnNameTemplate = "Число предполагаемых элементарных действий программного робота (элементов схемы) с учетом ветвления процесса\n",
                        columnNameActual = sheet.getRow(6).getCell(5).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 7й колонки по шаблону", () -> {
                String columnNameTemplate = "Необходимость распознавания текста в картинке",
                        columnNameActual = sheet.getRow(6).getCell(6).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 8й колонки по шаблону", () -> {
                String columnNameTemplate = "Описание клиентских приложений для программного робота",
                        columnNameActual = sheet.getRow(6).getCell(7).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 9й колонки по шаблону", () -> {
                String columnNameTemplate = "Описание вида адаптации робота",
                        columnNameActual = sheet.getRow(5).getCell(8).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 10-14й колонки по шаблону", () -> {
                String columnNameTemplate = " Ожидаемый эффект от реализации предлагаемого решения",
                        columnNameActual = sheet.getRow(5).getCell(9).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 10й колонки по шаблону", () -> {
                String columnNameTemplate = "Высвобождаемые трудозатраты (FTEэф)\n",
                        columnNameActual = sheet.getRow(6).getCell(9).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 11й колонки по шаблону", () -> {
                String columnNameTemplate = "Высвобождаемые трудозатраты в денежном эквиваленте (ФВэф),\n" +
                        " тыс. руб./год\n",
                        columnNameActual = sheet.getRow(6).getCell(10).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 12й колонки по шаблону", () -> {
                String columnNameTemplate = "Чистый дисконтированный доход (NPV), тыс. руб. \n",
                        columnNameActual = sheet.getRow(6).getCell(11).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 13й колонки по шаблону", () -> {
                String columnNameTemplate = "Дисконтированный срок окупаемости инвестиций (DPP), лет\n",
                        columnNameActual = sheet.getRow(6).getCell(12).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 14й колонки по шаблону", () -> {
                String columnNameTemplate = "Качественные показатели эффективности внедрения программного робота \n",
                        columnNameActual = sheet.getRow(6).getCell(13).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 15й колонки по шаблону", () -> {
                String columnNameTemplate = "Требования к сопровождению",
                        columnNameActual = sheet.getRow(5).getCell(14).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            step("Название 16й колонки по шаблону", () -> {
                String columnNameTemplate = "Ответственный от ФЗ за подготовку заявки\n",
                        columnNameActual = sheet.getRow(4).getCell(15).getStringCellValue();
                assertEquals(columnNameTemplate, columnNameActual);
            });
            // Закрытие workbook и InputStream
            workbook.close();
            IOUtils.closeQuietly(fileAsInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
//    @Disabled
    @DisplayName("Должны быть валидные данные в файле экспорта по действию «Скачать заявку» заявки роботов api")
    void fileShouldHaveValidDataByActionDownloadRequestRobotsApiTest() {

        step("Создать готовую для процесса заявку", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestId = requestRobotsReadyToProcess.then().log().ifValidationFails().statusCode(200).extract().body().path("id");
        step("Запустить процесс по тестзаявке", () -> {
            startProcessRequestRobots(authCookie, requestId)
                    .then().log().ifError().statusCode(200);
        });
        step("Получаем id тестробота и его данные для проверок", () -> {
        });
        val robotsOfRequest = requestRobotsPage.getRobotsListOfRequestRobotsById(authCookie, requestId, 0, 2, false);
        int robotId = robotsOfRequest.then().log().ifValidationFails().statusCode(200)
                .extract().path("items.id[0]");
        val testRobot = robotPage.getRobotByIdApi(authCookie, robotId);
        String
                robotName = testRobot.then().log().ifValidationFails().statusCode(200)
                .extract().path("name"),
                robotDescription = testRobot.then().extract().path("description"),
                robotAsuNumber = testRobot.then().extract().path("asuNumber.name"),
                robotFilesCount = testRobot.then().extract().path("filesCount.name"),
                robotActionsCount = testRobot.then().extract().path("actionsCount.name"),
                robotTextRecognition = testRobot.then().extract().path("textRecognition.name"),
                robotApplicationDescription = testRobot.then().extract().path("applicationDescription.name"),
                robotAdaptationType = testRobot.then().extract().path("adaptationType.name"),
                robotQualitativePerformance = testRobot.then().extract().path("qualitativePerformance"),
                robotSupportFormat = testRobot.then().extract().path("supportFormat.name");
        Float
                freedUpWorkManHoursF = testRobot.then().extract().path("freedUpWorkManHours"),
                paybackPeriodF = testRobot.then().extract().path("paybackPeriod");
        int
                releasedCosts = testRobot.then().extract().path("releasedCosts"),
                discountedIncome = testRobot.then().extract().path("discountedIncome");
        val testRequestRobots = requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestId);
        String requestSubmitPerson = testRequestRobots.then().log().ifValidationFails().statusCode(200)
                .extract().path("requestSubmitPerson.shortName"),
                fzResponsiblePerson = testRequestRobots.then().extract().path("fzResponsiblePerson.shortName");
        step("Действие «Скачать заявку»", () -> {
        });
        // Получение ответа как InputStream
        final InputStream fileAsInputStream = RequestRobotsPage.exportRequestRobotsByIdAsResp(authCookie, requestId)
                .asInputStream();
        try {
            // Преобразование InputStream в файл XLS
            Workbook workbook = WorkbookFactory.create(fileAsInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            step("Валидное значение атрибута 'Заявку подал'", () -> {
                String cellValueExpected = "Заявку подал: " + requestSubmitPerson,
                        cellValueActual = sheet.getRow(10).getCell(0).getStringCellValue();
                assertEquals(cellValueExpected, cellValueActual);
            });
            step("Валидное значение id робота в 1й колонке", () -> {
                int cellValueActual = (int) sheet.getRow(8).getCell(0).getNumericCellValue();
                assertEquals(robotId, cellValueActual);
            });
            step("Валидное значение 'Наименование программного робота' во 2й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(1).getStringCellValue();
                assertEquals(robotName, cellValueActual);
            });
            step("Валидное значение 'Краткое описание ...' в 3й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(2).getStringCellValue();
                assertEquals(robotDescription, cellValueActual);
            });
            step("Валидное значение 'Количество информационных систем ...' в 4й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(3).getStringCellValue();
                assertEquals(robotAsuNumber, cellValueActual);
            });
            step("Валидное значение 'Количество входных/выходных файлов ...' в 5й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(4).getStringCellValue();
                assertEquals(robotFilesCount, cellValueActual);
            });
            step("Валидное значение 'Число предполагаемых элементарных действий ...' в 6й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(5).getStringCellValue();
                assertEquals(robotActionsCount, cellValueActual);
            });
            step("Валидное значение 'Необходимость распознавания текста в картинке' в 7й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(6).getStringCellValue();
                assertEquals(robotTextRecognition, cellValueActual);
            });
            step("Валидное значение 'Описание клиентских приложений ...' в 8й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(7).getStringCellValue();
                assertEquals(robotApplicationDescription, cellValueActual);
            });
            step("Валидное значение 'Описание вида адаптации робота' в 9й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(8).getStringCellValue();
                assertEquals(robotAdaptationType, cellValueActual);
            });
            step("Валидное значение 'Высвобождаемые трудозатраты (FTEэф)' в 10й колонке", () -> {
                double cellValueActual = sheet.getRow(8).getCell(9).getNumericCellValue();
                int freedUpWorkManHoursInt = freedUpWorkManHoursF.intValue();
                assertEquals(freedUpWorkManHoursInt, cellValueActual * 100);
            });
            step("Валидное значение 'Высвобождаемые трудозатраты (FTEэф)' в 11й колонке", () -> {
                double cellValueActual = sheet.getRow(8).getCell(10).getNumericCellValue();
                assertEquals(releasedCosts, cellValueActual * 1000);
            });
            step("Валидное значение 'Чистый дисконтированный доход (NPV), тыс. руб.' в 12й колонке", () -> {
                double cellValueActual = sheet.getRow(8).getCell(11).getNumericCellValue();
                assertEquals(discountedIncome, cellValueActual * 1000);
            });
            step("Валидное значение 'Дисконтированный срок окупаемости инвестиций (DPP), лет' в 13й колонке", () -> {
                double cellValueActual = sheet.getRow(8).getCell(12).getNumericCellValue();
                int paybackPeriodInt = paybackPeriodF.intValue();
                assertEquals(paybackPeriodInt, cellValueActual);
            });
            step("Валидное значение 'Качественные показатели эффективности ...' в 14й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(13).getStringCellValue();
                assertEquals(robotQualitativePerformance, cellValueActual);
            });
            step("Валидное значение 'Требования к сопровождению' в 15й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(14).getStringCellValue();
                assertEquals(robotSupportFormat, cellValueActual);
            });
            step("Валидное значение 'Ответственный от ФЗ за подготовку заявки' в 16й колонке", () -> {
                String cellValueActual = sheet.getRow(8).getCell(15).getStringCellValue();
                assertEquals(fzResponsiblePerson, cellValueActual);
            });
            // Закрытие workbook и InputStream
            workbook.close();
            IOUtils.closeQuietly(fileAsInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени получения списка роботов заявки Роботизация api")
    void getRobotsListByRequestIdBaseChecksApiTest() {
        step("Создать заявку с роботом", () -> {
        });
        val requestRobotsWithRobot = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestId = requestRobotsWithRobot.then().log().ifValidationFails().statusCode(200).extract().body().path("id");
        step("Запросить роботы заявки, проверить, что код ответа 200, время ответа < 1500мс, схема валидна", () -> {
            requestRobotsPage.getRobotsListOfRequestRobotsById(authCookie, requestId, 0, 2, false)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/getRobotsListByRequestIdSchema.json"));
        });
    }


}
