package aleks.kur.pages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.pages.UsersPage.*;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class PcDzoCoordinationRoutePage {

    // справочник 'Маршруты согласования ПЦ ДЗО'
//    public SelenideElement
//            // раздел ПЦ ДЗО
//            headerNameLocatorPcDzoPage = $(".MuiTypography-root.MuiTypography-medium");

    //  поля маршрута
    public String
            coordinationRoutePcDzoShortName = "coordinationRoutePcDzoShortNameAutoTest_" + faker.lorem().characters(3),
            coordinationRoutePcDzoFullName = "coordinationRoutePcDzoFullNameAutoTest_" + faker.lorem().characters(3),
            coordinationRoutePcDzoDescription = "coordinationRoutePcDzoDescriptionAutoTest_" + faker.lorem().characters(3),
            coordinationRoutePcDzoShortNameUpdated = "updatedCoordinationRoutePcDzoShortNameAutoTest_" + faker.lorem().characters(3),
            coordinationRoutePcDzoFullNameUpdated = "updatedCoordinationRoutePcDzoFullNameAutoTest_" + faker.lorem().characters(3),
            coordinationRoutePcDzoDescriptionUpdated = "updatedCoordinationRoutePcDzoDescriptionAutoTest_" + faker.lorem().characters(3);
    // списки ролей пц дзо
    final public static String validRolesCanSeePcDzoOnSideBarMenu =
            respDzoTechnoprom1 + "\n" +
                    respToAssignCoordinatorDzoCfto1 + "\n" +
                    coordinatingDzoCfto1 + "\n" +
                    respCkiDzo1 + "\n" +
                    baDzo1 + "\n" +
                    testAuditor1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    final public static String invalidRolesCanNotSeePcDzoOnSideBarMenu =
            FCI_1 + "\n" +
                    executor_1;

    public int[]
            coordinatorsEmpty = {},
            dpoIdsEmpty = {};


    // json маршрута
    public Map<String, Object> coordinationRoutePcDzoDataSet(
            String shortName, String fullName, String description, int[] dpoIds, int[] coordinators) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("shortName", shortName);
        requestData.put("fullName", fullName);
        requestData.put("description", description);
        requestData.put("dpoIds", dpoIds); // array of id
        requestData.put("coordinators", coordinators); // array of objects
        return requestData;
    }

    // список маршрутов
    public Response getCoordinationRoutePcDzoListApi(String authCookie) {
        Response coordinationRoutePcDzoDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/approvalRoute");
        return coordinationRoutePcDzoDirectory;
    }

    // маршрут по id
    public Response getCoordinationRoutePcDzoByIdApi(String authCookie, int routePcDzoId) {
        Response routePcDzo =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/approvalRoute/" + routePcDzoId);
        return routePcDzo;
    }

    // получить json-строку тела из запроса карточки маршрута
    public String getCoordinationRoutePcDzoJsonStByIdApi(String authCookie, int routePcDzoId) {
        String jsonResponseSt =
                given()
                        .spec(requestSpec)
//                        .body(newContractSpoBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/approvalRoute/" + routePcDzoId)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().asString();
        return jsonResponseSt;
    }

    // создать маршрут
    public Response createCoordinationRoutePcDzoApi(String authCookie, Map<String, Object> coordinationRoutePcDzoBody) {
        Response coordinationRoutePcDzo =
                given()
                        .spec(requestSpec)
                        .body(coordinationRoutePcDzoBody)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/approvalRoute/");
        return coordinationRoutePcDzo;
    }

    // создать маршрут и получить его id
    public int createCoordinationRoutePcDzoAndGetIdApi(String authCookie) {
        Map<String, Object> coordinationRoutePcDzoDataSetNew = coordinationRoutePcDzoDataSet(
                coordinationRoutePcDzoShortName, coordinationRoutePcDzoFullName, coordinationRoutePcDzoDescription, dpoIdsEmpty, coordinatorsEmpty);
        int routePcDzoId = createCoordinationRoutePcDzoApi(authCookie, coordinationRoutePcDzoDataSetNew)
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract().response().body().path("id");
        return routePcDzoId;
    }

    final public static String validRolesCanCreateCoordinationRoutePcDzo =
            akurochkin_BA + "\n" +
                    akurochkin_SA;

    // редактировать маршрут по описание, краткое, полное имя
    public Response editCoordinationRoutePcDzoByJsonStringApi(String authCookie, String coordinationRoutePcDzoBodyJsonString) {
        Response coordinationRoutePcDzo =
                given()
                        .spec(requestSpec)
                        .body(coordinationRoutePcDzoBodyJsonString)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .put(BASE_URL + "/app4/approvalRoute/");
        return coordinationRoutePcDzo;
    }

    // редактировать json маршрута
    public String editCoordinationRoutePcDzo(String initialJsonString, String shortName, String fullName, String description) {
        // заменить значение нужного ключа в json и преобразовать обратно в строку JSON
        String updatedJsonString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(initialJsonString);

            if (rootNode.isObject()) {
                // Приводим узел к ObjectNode для возможности модификации
                ObjectNode rootNodeObject = (ObjectNode) rootNode;
                // Меняем значение "disabled" на false
                rootNodeObject.put("shortName", shortName);
                rootNodeObject.put("fullName", fullName);
                rootNodeObject.put("description", description);
            }

            updatedJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Преобразуем обратно в строку JSON
        return updatedJsonString;
    }

    // удалить маршрутов
    public Response deleteCoordinationRoutePcDzoApi(String authCookie, int id) {
        Response deleteRoutePcDzoDirectory =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .delete(BASE_URL + "/app4/approvalRoute/" + id);
        return deleteRoutePcDzoDirectory;
    }


}
