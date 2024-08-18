package aleks.kur.pages;

import io.restassured.http.ContentType;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBase.attachFile1;

public class FilesPage {

    public File testUploadFile = new File
//            ("src/test/resources/files/" + attachFile);
            ("src/test/resources/files/some_document.xlsx");

    public File testUploadFile1 = new File
            ("src/test/resources/files/" + attachFile1);


    public int uploadFileAndGetIdApi(String authCookie, File testFile) {
        int fileId =
                given()
                        .contentType(ContentType.MULTIPART)
                        .accept("application/json, text/plain, */*")
                        .multiPart(testFile)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/upload")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("[0].id");
        return fileId;
    }


}
