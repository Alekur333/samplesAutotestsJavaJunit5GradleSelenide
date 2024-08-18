package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class SettingsPage {

    public SelenideElement
            headerNameLocator =  $(".work-space-content-header h3"),
            headerNameLocatorApp4 =  $("h2[data-testid='@app4/title']");

    //

    // Api методы
    public Map<String, String> requestData = new HashMap<>();

    // Получить значение атрибута active нотификации
    public boolean getNotificationsActivity(String authCookie, String notificationsId) {
        boolean checkNotificationIsActive =
                given()
//                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
//                        .contentType("application/json")
                        .spec(requestSpec)
//                        .body(TestBaseApi.requestData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/notificationTemplates/" + notificationsId)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .response().body().path("active");
        return checkNotificationIsActive;
    }

    public void switchNotificationsActivity(String authCookie, String notificationsId, String active, String showOnScreen) {
        requestData.put("id", notificationsId);
        requestData.put("active", active);
        requestData.put("showOnScreen", showOnScreen);
                given()
                        .spec(requestSpec)
                        .body(requestData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .put(BASE_URL + "/notificationTemplates/" + notificationsId)
                        .then()
                        .log().all()
                        .statusCode(200);
//                        .extract().response().body().path("id");
    }

}
