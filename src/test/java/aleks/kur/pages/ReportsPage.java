package aleks.kur.pages;

import io.restassured.response.Response;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class ReportsPage {

    public ReportsPage shouldBeReportsPage() {
//        $(".work-space-content-header h3").shouldHave(exactText("Отчеты")); app2
        $(".MuiTypography-root.MuiTypography-medium").shouldHave(exactText("Отчеты")); // app4
        return this;
    }

    // список отложенных отчетов пользователя
    public static Response reportsTaskList(String authCookie) {
        Response taskList =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("currentUser", true)
                        .when()
                        .get(BASE_URL + "/reports/taskList");
        return taskList;
    }


    // получить отчет по id
    public static Response reportById(String authCookie, Integer reportId) {
        Response report =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/reports/" + reportId);
        return report;
    }


    // получение файла последнего отложенного отчета пользователя
    public static Response dounloadLastDelayedReportFile(String authCookie) {
        // id последнего из списка отложенных отчетов
        int lastReportId = reportsTaskList(authCookie).then().log().ifValidationFails().statusCode(200)
                .extract().path("items.id[0]");
        try {
            // Приостанавливаем выполнение
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // Обработка исключения, если поток был прерван во время сна
            e.printStackTrace();
        }
//        ApiUtils.repeatRequestToGetStatus200(authCookie, 3, 7000, reportById(authCookie, lastReportId));
        Response lastReportFileDownload = reportById(authCookie, lastReportId);
        return lastReportFileDownload;
    }

}
