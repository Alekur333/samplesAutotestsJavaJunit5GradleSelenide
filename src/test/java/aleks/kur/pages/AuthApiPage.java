package aleks.kur.pages;

import io.restassured.http.Cookies;
import org.openqa.selenium.Cookie;
import ru.progredis.tests.TestBaseApi;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static ru.progredis.filters.CustomLogFilter.customLogFilter;
import static ru.progredis.tests.TestBaseUi.*;

public class AuthApiPage extends TestBaseApi {

    public static Map<String, String> requestData = new HashMap<>();

    public static String getAuthCookie(String login, String passwd) {
        requestData.put("email", login);
        requestData.put("password", passwd);
        String authCookie =
                given()
                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
                        .accept("application/json, text/javascript, */*;")
                        .contentType("application/json")
                        .body(requestData)
                        .when()
                        .post("/login")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().cookie("PLAY_SESSION");
        return authCookie;
    }

    public static String getDetailedAuthCookie(String login, String passwd) {
        requestData.put("email", login);
        requestData.put("password", passwd);
        Cookies detailedAuthCookies =
                given()
                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
                        .accept("application/json, text/javascript, */*;")
                        .contentType("application/json")
                        .body(requestData)
                        .when()
                        .post("/login")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .detailedCookies();
        String cook = detailedAuthCookies.toString();
        return cook;
    }

    public static void logoutApi(String authCookie) {

                given()
                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
                        .accept("application/json, text/javascript, */*;")
                        .contentType("application/json")
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post("/logout")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200);
    }
// от любого юзера
    public void authorizationOnUibyApiPure(String authCookie) {
        open("/assets/img/icons/pending-reports-header.png");
        getWebDriver().manage().addCookie(
                new Cookie("PLAY_SESSION", authCookie));
        localStorage().setItem("session", session);
    }
// От СА
    public void authorizationApiOnUi() {
        //"Get cookie by API and set it with session to browser"
        requestData.put("email", login);
        requestData.put("password", passwd);
        String authorizationCookie =
                given()
                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
                        .contentType("application/json")
                        .body(requestData)
                        .when()
                        .post("/login")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .cookie("PLAY_SESSION");
        open("/assets/img/icons/pending-reports-header.png");
        getWebDriver().manage().addCookie(
                new Cookie("PLAY_SESSION", authorizationCookie));
        localStorage().setItem("session", session);
    }

    public void authorizationApiOnUiApp4() {
        //"Get cookie by API and set it with session to browser"
        requestData.put("email", login);
        requestData.put("password", passwd);
        String authorizationCookie =
                given()
                        .filter(customLogFilter().withCustomTemplates())
//                        .log().all()
                        .contentType("application/json")
                        .body(requestData)
                        .when()
                        .post("/login")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .cookie("PLAY_SESSION");
        open("/v4/favicon.ico");
//        open("/assets/img/icons/i-help.png");
        getWebDriver().manage().addCookie(
                new Cookie("PLAY_SESSION", authorizationCookie));
        localStorage().setItem("session", session);
    }

}


