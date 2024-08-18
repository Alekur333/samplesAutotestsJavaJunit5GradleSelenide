package aleks.kur.pages;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.progredis.filters.CustomLogFilter.customLogFilter;
import static ru.progredis.pages.RequestAutomationPage.faker;

public class AsuPage {
    public String asuShortName = "asuShortNameAutoTest_" + faker.lorem().characters(3);
    public String asuFullName = "asuFullNameAutoTest_" + faker.lorem().characters(3);
    public String asuCode = String.valueOf(faker.number().randomNumber(9, true));
    public Map<String, String> requestData = new HashMap<>();

    public int createAndGetAsuIdOfNewAsuApi(String authCookie, String asuShortName) {
        requestData.put("code", asuCode);
        requestData.put("name", asuFullName);
        requestData.put("shortName", asuShortName);
        int asuId =
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType("application/json")
                        .body(requestData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post("/asu")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract()
                        .response().body().path("id");
//        System.out.println("asuId = " + asuId + " / asuShortName = " + asuShortName);
        return asuId;
    }

    public void deleteAsuApi(String authCookie, int asuId) {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType("application/json")
                .body(requestData)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .delete("/asu/" + asuId)
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

}
