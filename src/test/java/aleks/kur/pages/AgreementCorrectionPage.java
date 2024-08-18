package aleks.kur.pages;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class AgreementCorrectionPage {
    public String proposalToCommissionKindCode = "COMMISSION_OFFER";
    public String correctionProposalToCommissionBody = """
                {
                    "year": "2024",
                    "typeCode": "INCLUSION_OF_THE_EVENT",
                    "agreementId": 5291,
                    "changeReason": "Обоснование",
                    "changeText": "Описание",
                    "planPiWorks": [],
                    "criteria": null,
                    "effects": [],
                    "finalApprover": {
                        "id": 30
                    }
                }
                """;

    // создать корректировку договора
    public Response createAgreementCorrectionApi(String authCookie, String correctionBody, String correctionTypeCode) {
        Response correction =
                given()
                        .spec(requestSpec)
                        .body(correctionBody)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/changeRequest/" + correctionTypeCode);
        return correction;
    }



}
