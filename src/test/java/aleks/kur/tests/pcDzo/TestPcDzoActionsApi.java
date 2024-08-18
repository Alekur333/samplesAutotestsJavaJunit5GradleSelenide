package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.ProcessPage.stopProcessByInstanceId;
import static ru.progredis.pages.SqlRequestsPage.updateDbTableV2LongFieldByObjectId;

@Tags({@Tag("api"), @Tag("pcDzo"), @Tag("regress")})
@DisplayName("Проверки действий раздела ПЦ ДЗО")
public class TestPcDzoActionsApi extends TestBaseApi {

    private final String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Действие \"Запросить загрузку ПЦ\" запускает процесс для ДЗО с признаком «Предоставление ПЦ-Обязательное» и без ПЦ следующего года")
    void actionRequestPcStartsProcessForDzoIfPcIsRequiredAndPcDontExistsYetInNextYearApiTest() throws InterruptedException {

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true для тесторганизации", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(true));

        step("Заполняем в организации-ДЗО по его asuKpiId поля \"Ответственный от ЦКИ за ДЗО\", \"обязательно Предоставление ПЦ\"", () -> {
        });
        Response editedDzo = pcDzoPage.fillCkiRespDzoAndPcRequiredAttrsInOrganizationCard(authCookie, organizationPage.asuKpiIdStromir);
        String dzoShortName = editedDzo.then().extract().path("shortName");

        step("Действие \"Запросить загрузку ПЦ\" должно запустить процесс для ПЦ ДЗО", () -> {
        });
        pcDzoPage.requestDzoDocApi(authCookie, "dp");
        Thread.sleep(5000);
        Response taskList = tasksPage.getNotMyTasksListApi(authCookie, 10, 0);
        taskList.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("items.taskName", hasItem("Загрузить документ: «ПЦ ДЗО», в ДЗО «" + dzoShortName + "»"));

        step("Процесс должен быть для ПЦ на следующий год", () -> {
        });
        List<Integer> taskIds =  taskList.then().extract().response().path("items.findAll{ it.taskName =~ /.*Загрузить документ*/ }.id");
        System.out.println("taskIds = " + taskIds);
        // нахождение максимального значения id в последних чужих задачах
        int maxTaskId = taskIds.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("List is empty"));
        // проверка года запрошенного документа в поле задачи taskDescription
        Response taskCard = tasksPage.getPersonTaskByIdApi(authCookie, maxTaskId);
        taskCard.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("taskDescription", containsString("Необходимо загрузить документ: «ПЦ ДЗО» за " + (currentYear + 1) + " год"));

        step("Останавливаем процесс, делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
            int processId = taskCard.then().extract().path("rootExecutionId");
            stopProcessByInstanceId(authCookie, processId).then().log().ifValidationFails()
                    .assertThat().statusCode(200);
            integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(false));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Действие \"Запросить загрузку отчетов за 1 полугодие\" запускает процесс для ДЗО с признаком «Предоставление ПЦ - Обязательное» и без Отчета за 1 полугодие текущего года")
    void actionRequestHalfYearReportStartsProcessForDzoIfPcIsRequiredAndHalfYearReportDontExistsYetInCurrentYearApiTest() throws InterruptedException {

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true для тесторганизации", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(true));

        step("Заполняем в организации-ДЗО по его asuKpiId поля \"Ответственный от ЦКИ за ДЗО\", \"обязательно Предоставление ПЦ\"", () -> {
        });
        Response editedDzo = pcDzoPage.fillCkiRespDzoAndPcRequiredAttrsInOrganizationCard(authCookie, organizationPage.asuKpiIdStromir);
        String dzoShortName = editedDzo.then().extract().path("shortName");

        step("Действие \"Запросить загрузку отчетов за 1 полугодие\" должно запустить процесс для отчета", () -> {
        });
        pcDzoPage.requestDzoDocApi(authCookie, "report_half_dp");
        Thread.sleep(5000);
        Response taskList = tasksPage.getNotMyTasksListApi(authCookie, 2, 0);
        taskList.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("items.taskName", hasItem("Загрузить документ: «Отчет 1 полугодие», в ДЗО «" + dzoShortName + "»"));

        step("Процесс должен быть для отчета на текущий год", () -> {
        });
        List<Integer> taskIds =  taskList.then().extract().response().path("items.findAll{ it.taskName =~ /.*Загрузить документ*/ }.id");
        System.out.println("taskIds = " + taskIds);
        // нахождение максимального значения id в последних чужих задачах
        int maxTaskId = taskIds.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("List is empty"));
        // проверка года запрошенного документа в поле задачи taskDescription
        Response taskCard = tasksPage.getPersonTaskByIdApi(authCookie, maxTaskId);
        taskCard.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("taskDescription", containsString("Необходимо загрузить документ: «Отчет 1 полугодие» за " + currentYear + " год"));

        step("Останавливаем процесс, делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
            int processId = taskCard.then().extract().path("rootExecutionId");
            stopProcessByInstanceId(authCookie, processId).then().log().ifValidationFails()
                    .assertThat().statusCode(200);
            integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(false));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Действие \"Запросить загрузку отчетов за год\" запускает процесс для ДЗО с признаком «Предоставление ПЦ - Обязательное» и без Отчета за год прошлого года")
    void actionRequestYearReportStartsProcessForDzoIfPcIsRequiredAndYearReportDontExistsYetInLastYearApiTest() throws InterruptedException {

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true для тесторганизации", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(true));

        step("Заполняем в организации-ДЗО по его asuKpiId поля \"Ответственный от ЦКИ за ДЗО\", \"обязательно Предоставление ПЦ\"", () -> {
        });
        Response editedDzo = pcDzoPage.fillCkiRespDzoAndPcRequiredAttrsInOrganizationCard(authCookie, organizationPage.asuKpiIdStromir);
        String dzoShortName = editedDzo.then().extract().path("shortName");
        int dzoId = editedDzo.then().extract().path("id");

        step("Редактировать поле БД counterparty_of_dpo_from на (текущая дата - 1 год) для тесторганизации", () -> {
        });
        String
                tableName = "organization",
                tableFieldName = "counterparty_of_dpo_from";
        Long tableFieldValue = anyYearsAgoFromNowMillisDate(1);  // (текущая дата - 1 год) в unix ms
        updateDbTableV2LongFieldByObjectId(tableName, tableFieldName, tableFieldValue, (long) dzoId);

        step("Действие \"Запросить загрузку отчетов за год\" должно запустить процесс для отчета", () -> {
        });
        pcDzoPage.requestDzoDocApi(authCookie, "report_dp");
        Thread.sleep(5000);
        Response taskList = tasksPage.getNotMyTasksListApi(authCookie, 2, 0);
        taskList.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("items.taskName", hasItem("Загрузить документ: «Отчет год», в ДЗО «" + dzoShortName + "»"));

        step("Процесс должен быть для отчета на прошлый год", () -> {
        });
        List<Integer> taskIds =  taskList.then().extract().response().path("items.findAll{ it.taskName =~ /.*Загрузить документ*/ }.id");
        System.out.println("taskIds = " + taskIds);
        // нахождение максимального значения id в последних чужих задачах
        int maxTaskId = taskIds.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("List is empty"));
        // проверка года запрошенного документа в поле задачи taskDescription
        Response taskCard = tasksPage.getPersonTaskByIdApi(authCookie, maxTaskId);
        taskCard.then().log().ifValidationFails()
                .assertThat().statusCode(200)
                .body("taskDescription", containsString("Необходимо загрузить документ: «Отчет год» за " + (currentYear - 1) + " год"));

        step("Останавливаем процесс, делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
            int processId = taskCard.then().extract().path("rootExecutionId");
            stopProcessByInstanceId(authCookie, processId).then().log().ifValidationFails()
                    .assertThat().statusCode(200);
            integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoStromirAsuKpiJson(false));
        });

    }

}
