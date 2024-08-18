package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.PcDzoPage;
import ru.progredis.tests.TestBaseApi;

import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.progredis.pages.SqlRequestsPage.*;

@Tags({@Tag("api"), @Tag("pcDzo"), @Tag("regress")})
@DisplayName("Проверки раздела ПЦ ДЗО")
public class TestPcDzoApi extends TestBaseApi {

    private final String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @ParameterizedTest(name = "Тест #{index} -> Пользователь в роли \"" + "{2}" + "\" при переходе в раздел 'ПЦ ДЗО' видит список всех ДЗО api")
    @CsvSource(textBlock = PcDzoPage.validRolesCanSeeAllPcDzoInSection)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void allDzosAvailableForSomeRolesInPcDzoSectionApiTest(String loginUser, String passwdUser, String role) {

        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(loginUser, passwdUser);

        step("Берем id действующих дзо из БД по условию", () -> {
        });
        String
                conditions = "counterparty_dpo=true AND counterparty_of_dpo_to IS NULL AND counterparty_of_dpo_from IS NOT NULL",
//                conditions = "counterparty_dpo=true AND counterparty_of_dpo_to IS NULL AND deleted=false AND blocked=false",
                tableName = "organization";
        Set<Integer> dzoIdsFromDbSet = selectIdsByConditionsDbTableV2(tableName, "id", "", conditions);
        System.out.println("ids from db = " + dzoIdsFromDbSet);

        step("Берем id дзо от пользователя из запроса по текущему году", () -> {
        });
        List<Integer> dzoIdsFromApiRequestList = pcDzoPage.getDzoListApi(authCookieValidRole, String.valueOf(currentYear))
                .then()
                .extract().response().body().path("items.id");
        Set<Integer> dzoIdsFromApiRequestSet = new HashSet<>();
        for (int id : dzoIdsFromApiRequestList) {
            dzoIdsFromApiRequestSet.add(id);
        }
        System.out.println("ids from request = " + dzoIdsFromApiRequestSet);

        step("Сравниваем наборы id из БД и запроса", () -> {
            assertEquals(dzoIdsFromDbSet, dzoIdsFromApiRequestSet);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователь в роли \"" + "{2}" + "\" видит перечень только тех ДЗО, в согласовании ПЦ которых участвует его организация api")
    @CsvSource(textBlock = PcDzoPage.validRolesCanSeeOnlyCoordinatedByThemDzosInSection)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void onlyCoordinatingByThemDzosAvailableForSomeRolesInPcDzoSectionApiTest(String loginUser, String passwdUser, String role) {

        step("Берем id организации пользователя по его логину", () -> {
        });
        int userOrganizationId = usersPage.getUserByLoginApi(authCookie, loginUser)
                .then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().body().path("organization.id[0]");
        System.out.println("userOrganizationId = " + userOrganizationId);

        step("Берем id действующих дзо из БД, в согласовании которых принимает участие организация пользователя с ролью, по условию", () -> {
        });
        Set<Integer> dzoIdsOnlyCoordinatingByUserFromDb =
                pcDzoPage.dzoIdsOnlyCoordinatingByUserFromDbSet(userOrganizationId);

        step("Залогиниться и получить куки от нужного пользователя", () -> {
        });
        val authCookieValidRole = AuthApiPage.getAuthCookie(loginUser, passwdUser);

        step("Берем id дзо от пользователя из запроса по текущему году", () -> {
        });
        List<Integer> dzoIdsFromApiRequestList = pcDzoPage.getDzoListApi(authCookieValidRole, String.valueOf(currentYear))
                .then()
                .extract().response().body().path("items.id");
        Set<Integer> dzoIdsFromApiRequestSet = new HashSet<>();
        for (int id : dzoIdsFromApiRequestList) {
            dzoIdsFromApiRequestSet.add(id);
        }
        System.out.println("ids from request = " + dzoIdsFromApiRequestSet);

        step("Сравниваем наборы id из БД и запроса", () -> {
            assertEquals(dzoIdsOnlyCoordinatingByUserFromDb, dzoIdsFromApiRequestSet);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Получения списка ПЦ ДЗО api")
    void getPcDzoListBaseChecksApiTest() {
        pcDzoPage.getDzoListApi(authCookie, String.valueOf(currentYear))
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/pcDzoList.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени Получения карточки ПЦ ДЗО api")
    void getDzoCardBaseChecksApiTest() {
        step("Берем id первой дзо из запроса по текущему году", () -> {
        });
        int dzoFirstIdFromApiRequestList = pcDzoPage.getDzoListApi(authCookie, String.valueOf(currentYear))
                .then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().body().path("items.id[0]");

        step("Проверяем ответ запроса на карточку ДЗО", () -> {
        });
        pcDzoPage.getDzoCardApi(authCookie, dzoFirstIdFromApiRequestList)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/dzoCard.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Интеграция с АСУ КПИ редактирует признак ДЗО isCounterpartyDpo у организации")
    void asuKpiIntegrationMakesOrganizationDzoApiTest() {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", organizationPage.asuKpiIdAbi);

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoAbiAsuKpiJson(true));

        step("Проверяем, что организация имеет признак ДЗО isCounterpartyDpo:true", () -> {
            Response organisation =
                    organizationPage.getOrganizationsApi(authCookie, queryParams);
            Boolean isDzoTrue =
                    organisation.then()
                            .log().ifValidationFails()
                            .assertThat().statusCode(200)
                            .extract().response().path("isCounterpartyDpo[0]");
            assertTrue(isDzoTrue);
        });

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
            integrationPage.asuKpiIntegrationApi(authCookie, pcDzoPage.isDzoAbiAsuKpiJson(false));
        });

        step("Проверяем, что организация не имеет признак ДЗО isCounterpartyDpo:false", () -> {
            Response organisation =
                    organizationPage.getOrganizationsApi(authCookie, queryParams);
            Boolean isDzoFalse =
                    organisation.then()
                            .log().ifValidationFails()
                            .assertThat().statusCode(200)
                            .extract().response().path("isCounterpartyDpo[0]");
            assertFalse(isDzoFalse);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Заполняется дата установки признака 'ДЗО' counterpartyOfDpoFrom у организации после интеграции с АСУ КПИ")
    void asuKpiIntegrationFillsOrganizationCounterpartyOfDpoFromAttrApiTest() {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", organizationPage.asuKpiIdAbi);
        val orgIsDzoJsonBody = pcDzoPage.isDzoAbiAsuKpiJson(true);

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, orgIsDzoJsonBody);

        step("Определяем id тесторганизации", () -> {
        });
        Response organisation =
                organizationPage.getOrganizationsApi(authCookie, queryParams);
        int orgId = organisation.then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().path("id[0]");
        Response orgById = organizationPage.getOrganizationByIdApi(authCookie, orgId);

        step("Проверяем, что заполнена дата в поле counterpartyOfDpoFrom у организации", () -> {
            orgById.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("counterpartyOfDpoFrom", notNullValue());
        });

        step("Проверяем, что не заполнена дата в поле counterpartyOfDpoTo у организации", () -> {
            orgById.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("counterpartyOfDpoTo", nullValue());
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Заполняется дата снятия признака 'ДЗО' counterpartyOfDpoTo у организации после интеграции с АСУ КПИ")
    void asuKpiIntegrationFillsOrganizationCounterpartyOfDpoToAttrApiTest() {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", organizationPage.asuKpiIdAbi);
        val orgIsDzoJsonBody = pcDzoPage.isDzoAbiAsuKpiJson(true);
        val orgIsNotDzoJsonBody = pcDzoPage.isDzoAbiAsuKpiJson(false);

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, orgIsDzoJsonBody);

        step("Определяем id тесторганизации", () -> {
        });
        Response organisation =
                organizationPage.getOrganizationsApi(authCookie, queryParams);
        int orgId = organisation.then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().path("id[0]");

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, orgIsNotDzoJsonBody);
        Response orgById = organizationPage.getOrganizationByIdApi(authCookie, orgId);

        step("Проверяем, что заполнена дата в поле counterpartyOfDpoFrom у организации", () -> {
            orgById.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("counterpartyOfDpoFrom", notNullValue());
        });

        step("Проверяем, что заполнена дата в поле counterpartyOfDpoTo у организации", () -> {
            orgById.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("counterpartyOfDpoTo", notNullValue());
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("inegration"), @Tag("asuKpi")})
    @DisplayName("Очищается дата counterpartyOfDpoTo у организации после активации признака 'ДЗО' интеграции с АСУ КПИ")
    void asuKpiIntegrationClearsCounterpartyOfDpoToAttrIfisDpoTrueApiTest() {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("asuKpiId", organizationPage.asuKpiIdAbi);
        val orgIsDzoJsonBody = pcDzoPage.isDzoAbiAsuKpiJson(true);
        val orgIsNotDzoJsonBody = pcDzoPage.isDzoAbiAsuKpiJson(false);

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true", () -> {
        });
        integrationPage.asuKpiIntegrationApi(authCookie, orgIsDzoJsonBody);

        step("Определяем id тесторганизации на стенде", () -> {
        });
        Response organisation =
                organizationPage.getOrganizationsApi(authCookie, queryParams);
        int orgId = organisation.then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().path("id[0]");
        Response orgById = organizationPage.getOrganizationByIdApi(authCookie, orgId);

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:false", () -> {
            integrationPage.asuKpiIntegrationApi(authCookie, orgIsNotDzoJsonBody);
        });

        step("Делаем запрос из АСУ КПИ с атрибутом isDPO:true", () -> {
            integrationPage.asuKpiIntegrationApi(authCookie, orgIsDzoJsonBody);
        });

        step("Проверяем, что не заполнена дата в поле counterpartyOfDpoTo у организации", () -> {
            orgById.then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("counterpartyOfDpoTo", nullValue());
        });
    }


}
