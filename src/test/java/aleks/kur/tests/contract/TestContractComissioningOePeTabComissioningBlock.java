package aleks.kur.tests.contract;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.models.contract.ContractCalendarPlanWorkPost;
import ru.progredis.pages.*;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static ru.progredis.pages.DirectoriesPage.getAgreementSubTypesDirectoryApi;

@DisplayName("Проверки вкладки 'Ввод в ОЭ/ПЭ' карточки договора")
@Tags({@Tag("ui"), @Tag("api"), @Tag("contract"), @Tag("regress"), @Tag("putInOePeTab")})
public class TestContractComissioningOePeTabComissioningBlock extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApiPage.authorizationApiOnUi();
    }

    static String authCookieSa;
    static List<Integer> contractSubtypesId;

    @BeforeAll
    public static void dataPreparation() {
        authCookieSa = AuthApiPage.getAuthCookie(login, passwd);
        contractSubtypesId = getAgreementSubTypesDirectoryApi(authCookieSa, new HashMap<>()).then().extract().path("id");

    }

    static ContractsPage contractsPage = new ContractsPage();
    static String contractsName = contractsPage.contractsName;

    static Stream<Arguments> shouldBeValidElementsInComissioningBlockViewingModeOnContractComissioningOePeTabTest() throws InterruptedException {
        // Создать новый договор ППО с подтипом
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);

        // Создать работу КП договора
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 1);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);

        // собрать данные для проверок
        authApiPage.authorizationApiOnUiApp4();
        contractsPage.openContractsCardCommissioningTab(contractId);
        switchTo().frame(contractsPage.calendarPlanIframe);
        contractsPage.comissioningBlockAllOnTabCommissioning.get(0).shouldBe(visible, Duration.ofSeconds(5));
        String
                workNameExp = "1. " + contractsPage.contractCalendarPlanWorkNameOnTabCommissioning.get(0).getText(),
                comissioningBlockNameAct = contractsPage.comissioningBlockNameOnTabCommissioning.get(0).getText(),
                colorBackgroundExp = "rgba(219, 240, 255, 1)",
                colorBackgroundComissioningBlockNameAct = contractsPage.comissioningBlockNameOnTabCommissioning.get(0).getCssValue("background-color"),
                comissioningBlockCommissionHeaderExp = "Комиссия для ввода в эксплуатацию",
                comissioningBlockCommissionHeaderAct = contractsPage.comissioningBlockCommissionHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockEsppNumberHeaderExp = "Номер проекта ЕСПП:",
                comissioningBlockEsppNumberHeaderAct = contractsPage.comissioningBlockEsppNumberHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockTableHeaderExp = "Ход ввода в эксплуатацию",
                comissioningBlockTableHeaderAct = contractsPage.comissioningBlockTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockTermOrdBtnNameExp = "Срок по ОРД",
                comissioningBlockTermOrdBtnNameAct = contractsPage.comissioningBlockTermOrdBtnOnTabCommissioning.get(0).getText(),
                comissioningBlockExportExcelBtnNameExp = "Экспорт в Excel",
                comissioningBlockExportExcelBtnNameAct = contractsPage.comissioningBlockExportExcelBtnOnTabCommissioning.get(0).getAttribute("title"),
                comissioningBlockOrderedNumberCellOnTableHeaderExp = "№",
                comissioningBlockOrderedNumberCellOnTableHeaderAct = contractsPage.comissioningBlockOrderedNumberCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockNameCellOnTableHeaderExp = "Название",
                comissioningBlockNameCellOnTableHeaderAct = contractsPage.comissioningBlockNameCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockStatusCellOnTableHeaderExp = "Статус",
                comissioningBlockStatusCellOnTableHeaderAct = contractsPage.comissioningBlockStatusCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockEsppObjectCellOnTableHeaderExp = "Объект ЕСПП",
                comissioningBlockEsppObjectCellOnTableHeaderAct = contractsPage.comissioningBlockEsppObjectCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockTermOrdCellOnTableHeaderExp = "Срок по ОРД",
                comissioningBlockTermOrdCellOnTableHeaderAct = contractsPage.comissioningBlockTermOrdCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockStartCellOnTableHeaderExp = "Начало",
                comissioningBlockStartCellOnTableHeaderAct = contractsPage.comissioningBlockStartCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockStartDatePlanCellOnTableHeaderExp = "план",
                comissioningBlockStartDatePlanCellOnTableHeaderAct = contractsPage.comissioningBlockStartDatePlanCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockStartDateFactCellOnTableHeaderExp = "факт",
                comissioningBlockStartDateFactCellOnTableHeaderAct = contractsPage.comissioningBlockStartDateFactCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockEndCellOnTableHeaderExp = "Окончание",
                comissioningBlockEndCellOnTableHeaderAct = contractsPage.comissioningBlockEndCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockEndDatePlanCellOnTableHeaderExp = "план",
                comissioningBlockEndDatePlanCellOnTableHeaderAct = contractsPage.comissioningBlockEndDatePlanCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockEndDateFactCellOnTableHeaderExp = "факт",
                comissioningBlockEndDateFactCellOnTableHeaderAct = contractsPage.comissioningBlockEndDateFactCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockLagCellOnTableHeaderExp = "Отставание",
                comissioningBlockLagCellOnTableHeaderAct = contractsPage.comissioningBlockLagCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockLagStartCellOnTableHeaderExp = "начало",
                comissioningBlockLagStartCellOnTableHeaderAct = contractsPage.comissioningBlockLagStartCellOnTableHeaderOnTabCommissioning.get(0).getText(),
                comissioningBlockLagEndCellOnTableHeaderExp = "окончание",
                comissioningBlockLagEndCellOnTableHeaderAct = contractsPage.comissioningBlockLagEndCellOnTableHeaderOnTabCommissioning.get(0).getText();
        return Stream.of(
                Arguments.of("Название блока", workNameExp, comissioningBlockNameAct),
                Arguments.of("Цвет заливки названия блока", colorBackgroundExp, colorBackgroundComissioningBlockNameAct),
                Arguments.of("Заголовок блока Комиссии", comissioningBlockCommissionHeaderExp, comissioningBlockCommissionHeaderAct),
                Arguments.of("Заголовок блока номера ЕСПП", comissioningBlockEsppNumberHeaderExp, comissioningBlockEsppNumberHeaderAct),
                Arguments.of("Заголовок таблицы хода ввода", comissioningBlockTableHeaderExp, comissioningBlockTableHeaderAct),
                Arguments.of("Имя кнопки Срок по ОРД", comissioningBlockTermOrdBtnNameExp, comissioningBlockTermOrdBtnNameAct),
                Arguments.of("Подсказка кнопки Экспорт в Excel", comissioningBlockExportExcelBtnNameExp, comissioningBlockExportExcelBtnNameAct),
                Arguments.of("Название 1й колонки таблицы Ход ввода", comissioningBlockOrderedNumberCellOnTableHeaderExp, comissioningBlockOrderedNumberCellOnTableHeaderAct),
                Arguments.of("Название 2й колонки таблицы Ход ввода", comissioningBlockNameCellOnTableHeaderExp, comissioningBlockNameCellOnTableHeaderAct),
                Arguments.of("Название 3й колонки таблицы Ход ввода", comissioningBlockStatusCellOnTableHeaderExp, comissioningBlockStatusCellOnTableHeaderAct),
                Arguments.of("Название 4й колонки таблицы Ход ввода", comissioningBlockEsppObjectCellOnTableHeaderExp, comissioningBlockEsppObjectCellOnTableHeaderAct),
                Arguments.of("Название 5й колонки таблицы Ход ввода", comissioningBlockTermOrdCellOnTableHeaderExp, comissioningBlockTermOrdCellOnTableHeaderAct),
                Arguments.of("Название 6-7й колонки таблицы Ход ввода", comissioningBlockStartCellOnTableHeaderExp, comissioningBlockStartCellOnTableHeaderAct),
                Arguments.of("Название 6й колонки таблицы Ход ввода", comissioningBlockStartDatePlanCellOnTableHeaderExp, comissioningBlockStartDatePlanCellOnTableHeaderAct),
                Arguments.of("Название 7й колонки таблицы Ход ввода", comissioningBlockStartDateFactCellOnTableHeaderExp, comissioningBlockStartDateFactCellOnTableHeaderAct),
                Arguments.of("Название 8-9й колонки таблицы Ход ввода", comissioningBlockEndCellOnTableHeaderExp, comissioningBlockEndCellOnTableHeaderAct),
                Arguments.of("Название 8й колонки таблицы Ход ввода", comissioningBlockEndDatePlanCellOnTableHeaderExp, comissioningBlockEndDatePlanCellOnTableHeaderAct),
                Arguments.of("Название 9й колонки таблицы Ход ввода", comissioningBlockEndDateFactCellOnTableHeaderExp, comissioningBlockEndDateFactCellOnTableHeaderAct),
                Arguments.of("Название 10-11й колонки таблицы Ход ввода", comissioningBlockLagCellOnTableHeaderExp, comissioningBlockLagCellOnTableHeaderAct),
                Arguments.of("Название 10 колонки таблицы Ход ввода", comissioningBlockLagStartCellOnTableHeaderExp, comissioningBlockLagStartCellOnTableHeaderAct),
                Arguments.of("Название 11 колонки таблицы Ход ввода", comissioningBlockLagEndCellOnTableHeaderExp, comissioningBlockLagEndCellOnTableHeaderAct)
        );
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест №{index} -> Валидное значение у атрибута \"{0}\" в блоке 'Ввод в эксплуатацию' вкладки 'Ввод в ОЭ/ПЭ' договора ППО")
    @MethodSource
    void shouldBeValidElementsInComissioningBlockViewingModeOnContractComissioningOePeTabTest(String elementName, String expectedValue, String actualValue) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Проверить, что атрибут имеет валидное значение", () -> {
            Assertions.assertEquals(expectedValue, actualValue);
        });
    }
}
