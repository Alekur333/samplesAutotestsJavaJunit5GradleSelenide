package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.PcDzoPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@Tags({@Tag("ui"), @Tag("pcDzo"), @Tag("pcDzoCard"), @Tag("regress")})
@DisplayName("Проверки действий в карточке ДЗО")
public class TestActionsOnDzoCardUi extends TestBaseUi {

    static String
            testDzoAsuKpiId = organizationPage.asuKpiIdZeldortrest,
            isDzoTestOrgAsuKpiJson = pcDzoPage.isDzoZeldortrestAsuKpiJson(true);

    @BeforeAll
//    @Disabled
    public static void testDataPreparation() {
        PcDzoPage.testOrgShouldBeDzoWithRespCkiAndPcRequired(authCookie, testDzoAsuKpiId, isDzoTestOrgAsuKpiJson);
    }

    static String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    static Response testDzoCard = organizationPage.getOrganizationCardByAsuKpiIdApi(authCookie, testDzoAsuKpiId);
    static int testDzoId = testDzoCard.then().extract().path("id");

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    @Test
    @DisplayName("Должно быть окно добавления документа по кнопке \"Добавить документ\" карточки ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void shouldBeWindowAddDocUsingBtnOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Открывается окно по кнопке", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.click();
            pcDzoPage.addDocWindowOnDzoCardLocator.shouldBe(visible);
        });
    }


}
