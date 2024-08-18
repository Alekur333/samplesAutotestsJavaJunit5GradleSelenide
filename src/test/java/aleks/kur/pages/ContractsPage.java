package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class ContractsPage {

    public Map<String, String> requestData = new HashMap<>();

    public SelenideElement
            // заголовок
            headerNameLocator = $(".work-space-content-header h3"),

    // вкладки
    putInOePeTab = $("[data-code='tabCommissioning']"),

    // меню Действия
    actionsBtn = $("#agreementnolimited").parent(),
            FormSoftwareAcceptanceCommitteeBtn = $(".pi-dropdown-extra>ul").$(byText("Собрать предложения в комиссию по приемке ПО")),
    //  таблица документов
    stateIconOnDocString = $("tr.k-master-row [role='gridCell'] svg", 0), // первая иконка состояния процесса
    documetsTableInContract = $("#documentList") // таблица доков
                    ;

    public String
            contractsPageLink = "#?page=reference&subpage=directions", // страница Направления
            contractsName = "ContractsNameAutoTest_" + faker.lorem().characters(3),
            contractsFullName = "directionFullNameAutoTest_" + faker.lorem().characters(3),
            contractsDescription = "directionDescriptionAutoTest_" + faker.lorem().characters(3)
                    ;

    public String newContractSpoBodyData =
            "{ " +
                    "\"name\":" + "\"" + contractsName + "\"" + ", " +
                    "\"agreementType\": " +
                    "{ " +
                    "        \"id\": \"1\"," +
                    "        \"shortName\": \"Договор\" " +
                    "    }," +
                    "\"agreementKind\": " +
                    "{ " +
                    "        \"id\": \"2\", " +
                    "        \"shortName\": \"СПО\" " +
                    "    }," +
                    "\"agreementStatus\": " +
                    "{ " +
                    "        \"id\": \"2\", " +
                    "        \"code\": \"draft\" " +
                    " }, " +

                    "\"executors\":" +
                    "[ " +
                    "{ " +
                    "\"main\": true," +
                    "\"organization\": " +
                    "{ " +
                    "\"id\": \"340\" " +
                    " }" +
                    " }" +
                    " ]," +

                    "\"customers\":" +
                    "[ " +
                    "{ " +
                    "\"main\": true," +
                    "\"organization\": " +
                    "{ " +
                    "\"id\": \"11\" " +
                    " }" +
                    " }" +
                    " ]" +
                    " }";

    public String newContractPpoBodyData =
            "{ " +
                    "\"name\":" + "\"" + contractsName + "\"" + ", " +
                    "\"agreementType\": " +
                    "{ " +
                    "        \"id\": \"1\"," +
                    "        \"shortName\": \"Договор\" " +
                    "    }," +
                    "\"agreementKind\": " +
                    "{ " +
                    "        \"id\": \"3\", " +
                    "        \"shortName\": \"ППО\" " +
                    "    }," +
                    "\"agreementStatus\": " +
                    "{ " +
                    "        \"id\": \"2\", " +
                    "        \"code\": \"draft\" " +
                    " }, " +

                    "\"executors\":" +
                    "[ " +
                    "{ " +
                    "\"main\": true," +
                    "\"organization\": " +
                    "{ " +
                    "\"id\": \"340\" " +
                    " }" +
                    " }" +
                    " ]," +

                    "\"customers\":" +
                    "[ " +
                    "{ " +
                    "\"main\": true," +
                    "\"organization\": " +
                    "{ " +
                    "\"id\": \"11\" " +
                    " }" +
                    " }" +
                    " ]" +
                    " }";

    public int createContractSpoDraftAndGetIdApi(String authCookie, String contractsName) {
        int contractsId =
                given()
                        .spec(requestSpec)
                        .body(newContractSpoBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/agreements")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        System.out.println("contractsSpoId = " + contractsId);
        return contractsId;
    }
    public int createContractPpoDraftAndGetIdApi(String authCookie, String contractsName) {
        int contractsId =
                given()
                        .spec(requestSpec)
                        .body(newContractPpoBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/agreements")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        System.out.println("contractsPpoId = " + contractsId);
        return contractsId;
    }

    public void deleteContractApi(String authCookie, int contractsId) {
        given()
                .spec(requestSpec)
//                .body("[]")
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .delete(BASE_URL + "/agreements/" + contractsId)
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

    // по id договора взять id 1го document_kit
    public static int firstDocKitIdInContractApi(String authCookie, int contractsId) {
        int firstDocKitId =
                given()
                .spec(requestSpec)
//                .body("[]")
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/agreements/" + contractsId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().body().path("documentKits.id[0]");
        return firstDocKitId;
    }

    public void openContractsCardDocsTab(int contractsId) {
        String linkToContractsCardDocsTab = "#?jump=agreementnolimited&subpage=show&id=" + contractsId + "&tab=tabDocuments";
        open(linkToContractsCardDocsTab);
    }

    public void openContractsCardMainInfoTab(int contractsId) {
        String linkToContractsCardMainTab = "#?jump=agreementnolimited&subpage=show&id=" + contractsId;
        open(linkToContractsCardMainTab);
    }

    //Проверить наличие кнопки 'Сформировать комиссию по приемке ПО'
    public void shouldBeSoftwareAcceptanceCommitteeBtnOnActionsMenu() {
        actionsBtn.click();
        FormSoftwareAcceptanceCommitteeBtn.should(exist);
    }

    public void stateIconOnDocStringSvgAttrsApp4Checking() {
        stateIconOnDocString.$("circle")
                .shouldHave(
                        attribute("cx", "32"),
                        attribute("cy", "32"),
                        attribute("r", "25"));
        stateIconOnDocString.$("rect")
                .shouldHave(
                        attribute("x", "25"),
                        attribute("y", "25"),
                        attribute("width", "14"),
                        attribute("height", "14")
                );
    }

}
