package aleks.kur.pages;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class ProcessPage {

    // Остановка процесса по id процесса
    public static Response stopProcessByInstanceId(String authCookie, int processId) {
        Response stopProcessResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("instanceId", processId)
                        .when()
                        .delete(BASE_URL + "/app4/processes/stop");
        return stopProcessResponse;
    }


}
