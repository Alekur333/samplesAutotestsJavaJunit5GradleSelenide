package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;

import java.util.Map;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class DirectoriesPage {
    // Раздел Справочники

    public String directoriesPageLink = "#?page=reference"; // страница Справочники

    public SelenideElement
//            headerNameLocator =  $(".work-space-content-header h3"), // Заголовок app2
            headerNameLocator = $("[data-testid='@app4/title']"), // Заголовок app4
            requestAutoGroupOnDirectoriesPage = $(byText("Заявки на автоматизацию")).parent(), // группа Заявки на автоматизацию
            documentsGroupOnDirectoriesPage = $(byText("Документы")).parent(), // группа Документы
            directionsDirectory = documentsGroupOnDirectoriesPage.find(byText("Направления")),  // справочник Направления на странице справочников
            coordinationRoutePcDzoDirectory = documentsGroupOnDirectoriesPage.find(byText("Маршруты согласования ПЦ ДЗО"));  // справочник Маршруты согласования ПЦ ДЗО на странице справочников
    String sortedByAscNameRequestBody =
            "{\n" +
                    "  \"page\": {\n" +
                    "    \"page\": 0,\n" +
                    "    \"limit\": 20,\n" +
                    "    \"sort\": [\n" +
                    "      {\n" +
                    "        \"property\": \"name\",\n" +
                    "        \"direction\": \"ASC\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"search\": \"\",\n" +
                    "  \"deleted\": false,\n" +
                    "  \"blocked\": false\n" +
                    "}",
            sortedByAscCostRequestBody =
                    "{\n" +
                            "  \"page\": {\n" +
                            "    \"page\": 0,\n" +
                            "    \"limit\": 20,\n" +
                            "    \"sort\": [\n" +
                            "      {\n" +
                            "        \"property\": \"cost\",\n" +
                            "        \"direction\": \"ASC\"\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  },\n" +
                            "  \"search\": \"\",\n" +
                            "  \"deleted\": false,\n" +
                            "  \"blocked\": false\n" +
                            "}";

    // Роботы
    // справочник 'Статусы роботов'
    public Response getRobotStatusesDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/robot-status/all");
        return RobotDirectory;
    }

    // справочник 'Базовая стоимость робота'
    public Response getRobotBasicCostDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscCostRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/basic-robot-cost/all");
        return RobotDirectory;
    }

    // справочник 'Стоимость работы по разработке управляющего модуля'
    public Response getWorkRobotsControlModuleCostDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscCostRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/cost-developing-control-module/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Количество ИС, используемых в работе»'
    public Response getUsedInWorkAsuNumberCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-used-asu-number/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Количество файлов, обрабатываемых роботом»'
    public Response getFilesNumberProcessedByRobotCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-files-count/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Число предполагаемых элементарных действий»'
    public Response getElementaryActionsNumberRobotCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-actions-count/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Необходимость распознавания текста в картинке»'
    public Response getTextRecognitionRobotCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-text-recognition/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Описание клиентских приложений»'
    public Response getClientsAppsDescriptionRobotCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-application-description/all");
        return RobotDirectory;
    }

    // справочник 'Коэффициент «Вид адаптации»'
    public Response getAdaptationTypeRobotCoefficientDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/coefficient-of-adaptation-type/all");
        return RobotDirectory;
    }

    // справочник 'Форматы сопровождения'
    public Response getSupportFormatsDirectoryApi(String authCookie) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(sortedByAscNameRequestBody)
                        .when()
                        .post(BASE_URL + "/app4/dictionary/support-format/all");
        return RobotDirectory;
    }

    // справочник Группы документов
    public static Response getDocumentGroupsDirectoryApi(String authCookie, Map<String, ?> queryParams) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParams(queryParams)
                        .when()
                        .get(BASE_URL + "/documentgroups")
                        .then().log().ifValidationFails().statusCode(200)
                        .extract().response();
        return RobotDirectory;
    }

    // справочник Типы документов
    public static Response getDocumentTypesDirectoryApi(String authCookie, Map<String, ?> queryParams) {
        Response RobotDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParams(queryParams)
                        .when()
                        .get(BASE_URL + "/documenttypes")
                        .then().log().ifValidationFails().statusCode(200)
                        .extract().response();
        return RobotDirectory;
    }


}
