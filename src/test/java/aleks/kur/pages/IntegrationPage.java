package aleks.kur.pages;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.helpers.GetHashUtils.*;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class IntegrationPage {


    // интеграция с АСУ КПИ
    public Response asuKpiIntegrationApi(String authCookie, String body){
        String hashedBody = getHashMD5(body);
        Response asuKpiIntegration =
            given()
                    .spec(requestSpec)
                    .body(body)
                    .cookie("PLAY_SESSION", authCookie)
                    .header("BODY-CHECKSUM", hashedBody)
                    .when()
                    .post(BASE_URL + "/integration/asukpi/organizations")
                    .then().log().ifValidationFails().assertThat().statusCode(200)
                    .extract().response();
        return asuKpiIntegration;
    }


}
