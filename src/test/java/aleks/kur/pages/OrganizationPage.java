package aleks.kur.pages;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class OrganizationPage {

    public final String
            queryStromir = "СТРОМИР", // поиск
            asuKpiIdStromir = "856", // СТРОМИР
            asuKpiIdAbi = "627", // Аби
            queryAbi = "Общество с ограниченной ответственностью \"Аби\"",
//            asuKpiIdTechnoprom = "1052", // Технопром
            asuKpiIdZeldortrest = "111" // Желдортрест
    ;

    // поиск
    // запрос организаций по параметрам
    // queryParams("param1", "value1", "param2", "value2", "param3", "value3")
    //        Map<String, Object> queryParams = new HashMap<>();
    //        queryParams.put("asuKpiId", 856);
    //        queryParams.put("q", organizationPage.queryStromir);
    //        queryParams.put("deleted", true); // and so on
    public Response getOrganizationsApi(String authCookie, Map<String, ?> queryParams) {
        Response org =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParams(queryParams)
                        .when()
                        .get(BASE_URL + "/organizations");
        return org;
    }

    // Определяем id организации по asuKpiId
    public int getOrgIdByAsuKpiIdApi(String authCookie, String asuKpiId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", asuKpiId);
        Response organisation =
                getOrganizationsApi(authCookie, queryParams);
        int orgId = organisation.then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().path("id[0]");
        return orgId;
    }

    // сокращенная карточка организации по asuKpiId
    public Response getShortOrgCardByAsuKpiIdApi(String authCookie, String asuKpiId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", asuKpiId);
        Response organisationByAsuKpiId =
                getOrganizationsApi(authCookie, queryParams)
                        .then().log().ifValidationFails().statusCode(200)
                        .extract().response();
        return organisationByAsuKpiId;
    }

    // карточка организации по asuKpiId
    public Response getOrganizationCardByAsuKpiIdApi(String authCookie, String asuKpiId) {
        int orgId = getOrgIdByAsuKpiIdApi(authCookie, asuKpiId);
        Response organisationCard = getOrganizationByIdApi(authCookie, orgId)
                .then().log().ifValidationFails().assertThat().statusCode(200)
                .extract().response();
        return organisationCard;
    }

    // организация по id
    public Response getOrganizationByIdApi(String authCookie, int id) {
        Response org =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/organizations/" + id);
        return org;
    }

    // редактирование организации
    public Response putOrganizationByIdApi(String authCookie, int id, String bodyJson) {
        Response org =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(bodyJson)
                        .when()
                        .put(BASE_URL + "/organizations/" + id);
        return org;
    }


}
