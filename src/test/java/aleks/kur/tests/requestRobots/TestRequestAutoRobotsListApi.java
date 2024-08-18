package aleks.kur.tests.requestRobots;

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
import org.junit.jupiter.params.provider.ValueSource;
import ru.progredis.helpers.ApiUtils;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.RequestRobotsPage;
import ru.progredis.tests.TestBaseApi;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.pages.RequestRobotsPage.*;
import static ru.progredis.pages.UsersPage.*;

//@Disabled
@Tags({@Tag("regress"), @Tag("api"), @Tag("requestAuto"), @Tag("requestRobots"), @Tag("requestRobotsList")})
@DisplayName("Проверки списка Заявок Роботизация Api")
public class TestRequestAutoRobotsListApi extends TestBaseApi {
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
    @DisplayName("Проверка кода, валидация схемы ответа, времени получения списка заявок Роботизация api")
    void getRequestRobotsListBaseChecksApiTest() {
        step("Создать новую заявку", () -> {
        });
        requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Получить список заявок и проверить, что код ответа 200, время ответа < 1500мс, схема валидна", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookie, 0, 5, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/requestRobotsListSchema.json"));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Новая заявка есть в списке заявок Роботизация api")
    void newRequestRobotsExistInRequestsListApiTest() {
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Получить список заявок и проверить в нем наличие новой заявки", () -> {
            RequestRobotsPage.getRequestRobotsListApi(authCookie, null, null, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", hasItem(newRequestId));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Удаленной заявки нет в списке заявок роботов api")
    void noDeletedRequestInRequestsListApiTest() {
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Удалить созданную заявку", () -> {
            RequestRobotsPage.deleteRequestRobotsApi(authCookie, newRequestId);
        });
        step("Проверить, что удаленной заявки нет в списке заявок", () -> {
            RequestRobotsPage.getRequestRobotsListApi(authCookie, null, null, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", not(hasItem(newRequestId)));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Фильтр 'Показать удаленные' возвращает удаленную заявку в списке заявок роботов")
    void showDeletedRequestsFilterReturnDeletedItemInRequestsListApiTest() {
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Удалить созданную заявку", () -> {
            RequestRobotsPage.deleteRequestRobotsApi(authCookie, newRequestId);
        });
        step("Проверить, что фильтр 'Показать удаленные' возвращает удаленную заявку", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookie, 0, 10, "id,DESC", true, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.id", hasItem(newRequestId));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Фильтр 'Показать удаленные' возвращает валидные заявки в списке заявок роботов")
    void showDeletedRequestsFilterReturnValidItemsInRequestsListApiTest() {
        step("Создать 2 новые заявки", () -> {
        });
        int requestToDeleteId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Удалить 1 созданную заявку", () -> {
            RequestRobotsPage.deleteRequestRobotsApi(authCookie, requestToDeleteId);
        });
        step("Проверить, что фильтр 'Показать удаленные' возвращает удаленные и неудаленные заявки", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
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
    @DisplayName("Фильтр 'Год' возвращает валидные заявки в списке заявок роботов")
    void yearFilterReturnValidItemsInRequestsListApiTest() {
        int yearOfNewRequestInt = requestRobotsPage.requestRobotsNew.getYear();
        step("Создать новую заявку", () -> {
        });
        int newRequestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        step("Проверить, что фильтр по году новой заявки возвращает заявки только этого года", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookie, null, null, "id,DESC", null, yearOfNewRequestInt, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.year", not(hasItem(not(yearOfNewRequestInt))));
        });
    }

    @ParameterizedTest(name = "Фильтр 'Количество записей на страницу' {0} возвращает установленное количество в списке заявок роботов")
    @ValueSource(ints = {5, 10})
//    @ValueSource(ints = {5, 10, 15, 20, 25, 50})
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void itemsPerPageFilterReturnValidItemsInRequestsListApiTest(int itemsPerPage) {
        step("Проверить, что фильтр 'Количество записей на страницу' возвращает нужное количнство заявок", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
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
    @DisplayName("Поиск по полному имени возвращает валидные записи в списке заявок роботов")
    void searchByFullNameReturnValidItemsInRequestsListApiTest() {
        step("Создать заявку с полным именем  для поиска", () -> {
        });
        String fullNameForSearch = "Полное имя для для поиска autoTest " + faker.lorem().characters(5);
        System.out.println(fullNameForSearch);
        RequestRobotsRequest requestRobotsNameForSearch = new RequestRobotsRequest(
                requestRobotsPage.requestsShortName + "",
                fullNameForSearch + "",
                requestRobotsPage.currentYear + 0,
                requestRobotsPage.asuId + 0,
                requestRobotsPage.companyFzId + 0,
                requestRobotsPage.responsiblePersonFzId + 0,
                requestRobotsPage.newMeasuresProgramsIds,
                requestRobotsPage.newRequestRobotsComments
        );
        requestRobotsPage.createRequestRobotsApi(authCookie, requestRobotsNameForSearch);
        step("Есть в списке заявка с новым названием", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookie, 0, null, "id,DESC", null, null, fullNameForSearch)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.name", hasItem(fullNameForSearch));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Поиск по краткому имени возвращает валидные записи в списке заявок роботов")
    void searchByShortNameReturnValidItemsInRequestsListApiTest() {
        step("Создать заявку с кратким именем для поиска", () -> {
        });
        String shortNameForSearch = "Краткое имя для для поиска autoTest " + faker.lorem().characters(3);
        System.out.println(shortNameForSearch);
        RequestRobotsRequest requestRobotsNameForSearch = new RequestRobotsRequest(
                shortNameForSearch + "",
                requestRobotsPage.requestsFullName + "",
                requestRobotsPage.currentYear + 0,
                requestRobotsPage.asuId + 0,
                requestRobotsPage.companyFzId + 0,
                requestRobotsPage.responsiblePersonFzId + 0,
                requestRobotsPage.newMeasuresProgramsIds,
                requestRobotsPage.newRequestRobotsComments
        );
        requestRobotsPage.createRequestRobotsApi(authCookie, requestRobotsNameForSearch);
        step("Есть в списке заявка с новым названием", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookie, 0, null, "id,DESC", null, null, shortNameForSearch)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.shortName", hasItem(shortNameForSearch));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" имеет доступ к списку заявок роботов")
    @CsvSource(textBlock = RequestRobotsPage.validRolesHaveAccessToRequestRobotsList)
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    void validRoleHasAccessToRequestRobotsListApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос списка заявок с куками от нужного пользователя", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
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
    void invalidRoleNotHasAccessToRequestsListApiTest(String login, String passwd, String role) {
        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieInvalidRole = AuthApiPage.getAuthCookie(login, passwd);
        step("Запрос списка заявок с куками от нужного пользователя", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookieInvalidRole, 0, 5, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(403);
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Пользователь в роли ФЗ получает только заявки, у которых в поле 'ФЗ' указана его организация")
    void fzRoleGetsRequestsWithSimilarFzOrganizationInRequestRobotsListApiTest() {
        step("Создать одну заявку с ФЗ, как у тестпользователя с ролью 'ФЗ', и вторую c другой организацией", () -> {
        });
        requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsEdited);
        step("Залогиниться и получить куки от тестпользователя", () -> {
        });
        val authCookieFzRole = AuthApiPage.getAuthCookie(fzLoginFromNewRequestRobotsFz, fzPassFromNewRequestRobotsFz);
        step("Запрос списка заявок от тестпользователя. ФЗ всех заявок = организации тестпользователя", () -> {
            RequestRobotsPage.getRequestRobotsListApi(
                            authCookieFzRole, 0, null, "id,DESC", null, null, null)
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .body("items.companyFz.id", everyItem(equalTo(requestRobotsNew.getCompanyFzId())));
        });
    }

    @Test
//    @Disabled
//    @Tags({@Tag("requestRobotsList")})
    @DisplayName("Файл экспорта имеет валидное имя по действию «Экспорт в Excel» списка заявок роботов api")
    void fileShouldHaveValidNameByActionExportRequestRobotsListApiTest() {

        step("Получаем имя файла из ответа на запрос", () -> {
        });
        val lastReportFile = RequestRobotsPage.dounloadFileByActionExportRequestRobotsList(authCookie);
        lastReportFile.then().log().ifValidationFails()
                .assertThat().statusCode(200);
        val decodedFileName = ApiUtils.getFileNameFromHavingUtf8AttrResponseDecoded(lastReportFile);
        step("Имя файла должно соотвествовать шаблону", () -> {
            assertEquals("Перечень заявок на роботизацию " + currentYear + " г.xlsx", decodedFileName);
        });
    }

    static Stream<Arguments> fileShouldHaveValidAttributeNamesAndOrderByActionExportRequestRobotsListApiTest() {
        val lastReportFile = RequestRobotsPage.dounloadFileByActionExportRequestRobotsList(AuthApiPage.getAuthCookie(login, passwd));
        lastReportFile.then().log().ifValidationFails()
                .assertThat().statusCode(200);
        final InputStream fileAsInputStream = lastReportFile.asInputStream();
        String currentDateDdMmYyyyDots = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDateTime.now());
        try {
            Workbook workbookValidNames = WorkbookFactory.create(fileAsInputStream);
            Sheet sheetValidNames = workbookValidNames.getSheetAt(0);
            return Stream.of(
                    Arguments.of("Название листа", "Перечень заявок на роботизацию ", sheetValidNames.getSheetName(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Заголовок таблицы", "Перечень заявок на роботизацию " + currentYear + " г (по состоянию на " + currentDateDdMmYyyyDots + ")",
                            sheetValidNames.getRow(0).getCell(0).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 1й колонки", "ID", sheetValidNames.getRow(2).getCell(0).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 2й колонки", "Краткое наименование", sheetValidNames.getRow(2).getCell(1).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 3й колонки", "Полное наименование", sheetValidNames.getRow(2).getCell(2).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 4й колонки", "Категория", sheetValidNames.getRow(2).getCell(3).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 5й колонки", "ФЗ заявки", sheetValidNames.getRow(2).getCell(4).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 6й колонки", "Ответственный от ФЗ", sheetValidNames.getRow(2).getCell(5).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 7й колонки", "АСУ", sheetValidNames.getRow(2).getCell(6).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 8й колонки", "Год", sheetValidNames.getRow(2).getCell(7).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 9й колонки", "Статус", sheetValidNames.getRow(2).getCell(8).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 10й колонки", "Время установки статуса", sheetValidNames.getRow(2).getCell(9).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 11й колонки", "Дата подачи заявки", sheetValidNames.getRow(2).getCell(10).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 12й колонки", "Время последнего изменения заявки", sheetValidNames.getRow(2).getCell(11).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 13й колонки", "Ответственный от ГВЦ", sheetValidNames.getRow(2).getCell(12).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 14й колонки", "Ответственный от ПКТБ ЦЦТ", sheetValidNames.getRow(2).getCell(13).getStringCellValue(), workbookValidNames, fileAsInputStream),
                    Arguments.of("Название 15й колонки", "Последний комментарий", sheetValidNames.getRow(2).getCell(14).getStringCellValue(), workbookValidNames, fileAsInputStream)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> {0} по шаблону в файле экспорта по действию «Экспорт в Excel» списка заявок роботов")
//    @Disabled
    void fileShouldHaveValidAttributeNamesAndOrderByActionExportRequestRobotsListApiTest(
            String attrName, String nameTemplate, String nameActual, Workbook workbook, InputStream fileAsInputStream) {
        System.out.println("Проверка атрибута '" + attrName + "' с ожидаемым значением '" + nameTemplate + "'");
        assertEquals(nameTemplate, nameActual);
        // Закрытие workbook и InputStream
        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOUtils.closeQuietly(fileAsInputStream);
    }

    static Stream<Arguments> fileShouldHaveValidDataByActionExportRequestRobotsListApiTest() {

        val authCookie = AuthApiPage.getAuthCookie(login, passwd);
        DateTimeFormatter
                // Форматтер для входной строки
                inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
                // Форматтер для желаемой строки
                outputFormatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),
                outputFormatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        step("Создать готовую для процесса заявку", () -> {
        });
        val requestRobotsReadyToProcess = requestRobotsPage.createAndGetRequestRobotsReadyToProcessApi
                (authCookie, requestRobotsPage.requestRobotsNew, requestId -> robotPage.robotAllRequiredAttrs(requestId));
        int requestId = requestRobotsReadyToProcess.then().log().ifValidationFails().statusCode(200).extract().body().path("id");

        step("Запустить процесс по тестзаявке", () -> {
            startProcessRequestRobots(authCookie, requestId)
                    .then().log().ifError().statusCode(200);
        });
        step("Получить значение атрибутов у заявки в процессе", () -> {
        });
        val requestRobotsInProcess = requestRobotsPage.getRequestRobotsByIdApi(authCookie, requestId);
        int year = requestRobotsInProcess.then().extract().path("year");
        String
                shortName = requestRobotsInProcess.then().extract().path("shortName") + "\n",
                name = requestRobotsInProcess.then().extract().path("name") + "\n",
                category = requestRobotsInProcess.then().extract().path("category"),
                categoryNullToStr = Objects.requireNonNullElse(category, ""),
                companyFz = requestRobotsInProcess.then().extract().path("companyFz.shortName"),
                fzResponsiblePerson = requestRobotsInProcess.then().extract().path("fzResponsiblePerson.shortName"),
                asu = requestRobotsInProcess.then().extract().path("asu.shortName"),
                status = requestRobotsInProcess.then().extract().path("status.shortName"),
                statusUpdateTimeFromRequest = requestRobotsInProcess.then().extract().path("statusUpdateTime"),
                requestSubmitDateFromRequest = requestRobotsInProcess.then().extract().path("submitDate"),
                requestUpdateTimeFromRequest = requestRobotsInProcess.then().extract().path("updateTime"),
                gvcResponsiblePerson = requestRobotsInProcess.then().extract().path("gvcResponsiblePerson.shortName"),
                pktbResponsiblePerson = requestRobotsInProcess.then().extract().path("pktbResponsiblePerson.shortName"),
                lastComment = requestRobotsInProcess.then().extract().path("lastComment.text");
        // Преобразование строки из заявки в LocalDateTime и модификация по формату файла экспорта
        LocalDateTime
                statusUpdateTime = LocalDateTime.parse(statusUpdateTimeFromRequest, inputFormatter),
                requestSubmitDateTime = LocalDateTime.parse(requestSubmitDateFromRequest, inputFormatter),
                requestUpdateTimeTime = LocalDateTime.parse(requestUpdateTimeFromRequest, inputFormatter);
        String
                statusUpdateTimeSt = statusUpdateTime.format(outputFormatterTime),
                requestSubmitDateSt = requestSubmitDateTime.format(outputFormatterDate),
                requestUpdateTimeSt = requestUpdateTimeTime.format(outputFormatterTime);

        step("Получить файл экспорта и создать переменные с данными из файла", () -> {
        });
        val lastReportFile = RequestRobotsPage.dounloadFileByActionExportRequestRobotsList(authCookie);
        lastReportFile.then().log().ifValidationFails()
                .assertThat().statusCode(200);
        final InputStream fileAsInputStream = lastReportFile.asInputStream();

        try {
            Workbook workbookValidData = WorkbookFactory.create(fileAsInputStream);
            Sheet sheetValidData = workbookValidData.getSheetAt(0);
            int
                    valueColumn1 = (int) sheetValidData.getRow(3).getCell(0).getNumericCellValue(), // ID
                    valueColumn8 = (int) sheetValidData.getRow(3).getCell(7).getNumericCellValue(); // Год
            String
                    valueColumn2 = sheetValidData.getRow(3).getCell(1).getStringCellValue(), // Краткое наименование
                    valueColumn3 = sheetValidData.getRow(3).getCell(2).getStringCellValue(), // Полное наименование
                    valueColumn4 = sheetValidData.getRow(3).getCell(3).getStringCellValue(), // Категория
                    valueColumn5 = sheetValidData.getRow(3).getCell(4).getStringCellValue(), // ФЗ заявки
                    valueColumn6 = sheetValidData.getRow(3).getCell(5).getStringCellValue(), // Ответственный от ФЗ
                    valueColumn7 = sheetValidData.getRow(3).getCell(6).getStringCellValue(), // АСУ
                    valueColumn9 = sheetValidData.getRow(3).getCell(8).getStringCellValue(), // Статус
                    valueColumn10 = sheetValidData.getRow(3).getCell(9).getStringCellValue(), // Время установки статуса
                    valueColumn11 = sheetValidData.getRow(3).getCell(10).getStringCellValue(), // Дата подачи заявки
                    valueColumn12 = sheetValidData.getRow(3).getCell(11).getStringCellValue(), // Время последнего изменения заявки
                    valueColumn13 = sheetValidData.getRow(3).getCell(12).getStringCellValue(), // Ответственный от ГВЦ
                    valueColumn14 = sheetValidData.getRow(3).getCell(13).getStringCellValue(), // Ответственный от ПКТБ ЦЦТ
                    valueColumn15 = sheetValidData.getRow(3).getCell(14).getStringCellValue(); // Последний комментарий

            step("Сформировать поток данных для тестирования", () -> {
            });
            return Stream.of(
                    Arguments.of("ID", String.valueOf(requestId), String.valueOf(valueColumn1), workbookValidData, fileAsInputStream),
                    Arguments.of("Краткое наименование", shortName, valueColumn2, workbookValidData, fileAsInputStream),
                    Arguments.of("Полное наименование", name, valueColumn3, workbookValidData, fileAsInputStream),
                    Arguments.of("Категория", categoryNullToStr, valueColumn4, workbookValidData, fileAsInputStream),
                    Arguments.of("ФЗ заявки", companyFz, valueColumn5, workbookValidData, fileAsInputStream),
                    Arguments.of("Ответственный от ФЗ", fzResponsiblePerson, valueColumn6, workbookValidData, fileAsInputStream),
                    Arguments.of("АСУ", asu, valueColumn7, workbookValidData, fileAsInputStream),
                    Arguments.of("Год", String.valueOf(year), String.valueOf(valueColumn8), workbookValidData, fileAsInputStream),
                    Arguments.of("Статус", status, valueColumn9, workbookValidData, fileAsInputStream),
                    Arguments.of("Время установки статуса", statusUpdateTimeSt, valueColumn10, workbookValidData, fileAsInputStream),
                    Arguments.of("Дата подачи заявки", requestSubmitDateSt, valueColumn11, workbookValidData, fileAsInputStream),
                    Arguments.of("Время последнего изменения заявки", requestUpdateTimeSt, valueColumn12, workbookValidData, fileAsInputStream),
                    Arguments.of("Ответственный от ГВЦ", gvcResponsiblePerson, valueColumn13, workbookValidData, fileAsInputStream),
                    Arguments.of("Ответственный от ПКТБ ЦЦТ", pktbResponsiblePerson, valueColumn14, workbookValidData, fileAsInputStream),
                    Arguments.of("Последний комментарий", lastComment, valueColumn15, workbookValidData, fileAsInputStream)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> Валидное значение в колонке \"{0}\" в файле экспорта по действию «Экспорт в Excel» списка заявок роботов")
//    @Disabled
    void fileShouldHaveValidDataByActionExportRequestRobotsListApiTest(
            String columnName, String dataExpected, String dataActual, Workbook workbook, InputStream
            fileAsInputStream) {

        System.out.println("Проверка данных в колонке '" + columnName + "'");
        assertEquals(dataExpected, dataActual);
        // Закрытие workbook и InputStream
        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOUtils.closeQuietly(fileAsInputStream);
    }

    @CsvSource({
            "year, requestRobotsListFilterValuesYearSchema.json",
            "status, requestRobotsListFilterValuesSchema.json",
            "kind, requestRobotsListFilterValuesSchema.json",
            "category, requestRobotsListFilterValuesSchema.json",
            "customers.organization, requestRobotsListFilterValuesSchema.json",
            "asu, requestRobotsListFilterValuesSchema.json",
            "fzResponsiblePerson, requestRobotsListFilterValuesSchema.json",
            "gvcResponsiblePerson, requestRobotsListFilterValuesSchema.json"
    })
    @ParameterizedTest(name = "Тест #{index} -> Для запроса значений фильтра по полю {0} ответ имеет валидный код, тип содержимого, схему данных в списке заявок роботов")
//    @Disabled
    void filterValuesRequestRobotsListBaseChecksApiTest(String filterName, String validSchemaName) {

        val authCookie = AuthApiPage.getAuthCookie(login, passwd);
        RequestRobotsPage.filterValuesRequestRobotsList(authCookie, filterName)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .time(lessThan(1500L))
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/" + validSchemaName));

    }

}
