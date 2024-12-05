package aleks.kur.tests.contract;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.helpers.Screenshots;
import ru.progredis.pages.*;
import ru.progredis.tests.TestBaseUi;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.DirectoriesPage.getAgreementSubTypesDirectoryApi;

@DisplayName("Проверки вкладки 'Ввод в ОЭ/ПЭ' карточки договора")
@Tags({@Tag("ui"), @Tag("api"), @Tag("contract"), @Tag("regress"), @Tag("putInOePeTab")})
public class TestContractComissioningOePeTab extends TestBaseUi {

    static String authCookieSa;
    static int
            contractPpoWithNoPlanPiWorkId,
            contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId;
    static List<Integer> contractSubtypesId;

    @BeforeAll
    public static void dataPreparation() throws InterruptedException {
        authCookieSa = AuthApiPage.getAuthCookie(login, passwd);
        contractPpoWithNoPlanPiWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsName, 3, 1, 0, PlanPiWorksPage.asuForPlanPiWorksId);
        contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId = contractsPage.idOfCreatedContractPpoWithPlanPiWorksAndCalendarPlanWorkWithCommissionApi(authCookieSa);
        contractSubtypesId = getAgreementSubTypesDirectoryApi(authCookieSa, new HashMap<>()).then().extract().path("id");
    }

    @AfterAll
    public static void tearDown() {
        // Удалить тестовый договор
        contractsPage.deleteContractApi(authCookieSa, contractPpoWithNoPlanPiWorkId);
        contractsPage.deleteContractApi(authCookieSa, contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
    }

    static ContractsPage contractsPage = new ContractsPage();
    static String contractsName = contractsPage.contractsName;

    static Stream<Arguments> usersForTestsOnCommissioningOePeTabTest(int contractId) {
        // собрать данные для проверок
        return Stream.of(
                Arguments.of("", UsersPage.executorProjectManager1_Progredis.getMainRole(), UsersPage.executorProjectManager1_Progredis.getLogin(), UsersPage.executorProjectManager1_Progredis.getPassword(), contractId),
                Arguments.of("", UsersPage.contractManager1.getMainRole(), UsersPage.contractManager1.getLogin(), UsersPage.contractManager1.getPassword(), contractId),
                Arguments.of("", UsersPage.akurochkinBA.getMainRole(), UsersPage.akurochkinBA.getLogin(), UsersPage.akurochkinBA.getPassword(), contractId),
                Arguments.of("не", UsersPage.executor1_Progredis.getMainRole(), UsersPage.executor1_Progredis.getLogin(), UsersPage.executor1_Progredis.getPassword(), contractId)
        );
    }


    @Tags({@Tag("ui")})
    @Test
    @DisplayName("В карточке договора ППО есть вкладка 'Ввод в ОЭ/ПЭ' ")
    void contractPpoShouldHavePutInOePeTab() {
        // Вкладка “Ввод в ОЭ/ПЭ” есть только у договоров вида ППО типов “Договор”, “Ордер-заказ” и “Заказ-наряд”.
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Открыть карточку тестового договора", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardMainInfoTab(contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
        });

        step("Проверить наличие вкладки 'Ввод в ОЭ/ПЭ'", () -> {
            contractsPage.putInOePeTab.shouldBe(visible).shouldHave(exactText("Ввод в ОЭ/ПЭ"));
        });
    }

    @Tags({@Tag("ui")})
    @Test
    @DisplayName("В карточке договора СПО нет вкладки 'Ввод в ОЭ/ПЭ' ")
    void contractSpoShouldNotHavePutInOePeTab() {
        // Вкладка “Ввод в ОЭ/ПЭ” есть только у договоров вида ППО типов “Договор”, “Ордер-заказ” и “Заказ-наряд”.
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Создать новый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsName, 2, 1, 0, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Открыть карточку тестового договора", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить наличие вкладки 'Ввод в ОЭ/ПЭ'", () -> {
            contractsPage.putInOePeTab.shouldNot(exist);
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractsId);
        });
    }

    @Tags({@Tag("ui")})
    @Test
    @DisplayName("В карточке договора Оборудование нет вкладки 'Ввод в ОЭ/ПЭ' ")
    void contractEquipmentShouldNotHaveCommissioningOePeTab() {
        // Вкладка “Ввод в ОЭ/ПЭ” есть только у договоров вида ППО типов “Договор”, “Ордер-заказ” и “Заказ-наряд”.
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Создать новый договор Оборудование через api", () -> {
        });
        int contractsId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsName, 1, 1, 0, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Открыть карточку тестового договора", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить наличие вкладки 'Ввод в ОЭ/ПЭ'", () -> {
            contractsPage.putInOePeTab.shouldNot(exist);
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractsId);
        });
    }

    @Tags({@Tag("ui")})
    @Test
    @DisplayName("Есть кнопка 'Собрать предложения в комиссию по приемке ПО' для СА в меню 'Действия' карточки договора")
    void shouldBeFormSoftwareAcceptanceCommitteeBtnForSaOnActionsMenu() {
        step("Открыть карточку тестового договора от СА", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardMainInfoTab(contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
        });
        step("Проверить наличие кнопки 'Собрать предложения в комиссию по приемке ПО' ", () -> {
            contractsPage.actionsBtn.click();
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.should(exist);
        });
    }

    @Tags({@Tag("ui")})
    @Test
    @DisplayName("Не активна кнопка 'Собрать предложения в комиссию по приемке ПО', если в договоре нет процедуры приемки, для СА в меню 'Действия' карточки договора")
    void shouldBeDesabledFormSoftwareAcceptanceCommitteeBtnForSaOnActionsMenuWhenNoAcceptanceProcedure() {
        int contractsId = contractPpoWithNoPlanPiWorkId;
        step("Открыть карточку тестового договора от СА", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить не активность кнопки 'Собрать предложения в комиссию по приемке ПО' ", () -> {
            contractsPage.actionsBtn.click();
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.parent().shouldHave(attribute("disabled"));
        });
        step("У кнопки должа быть подсказка 'Для формирования комиссии по приемке требуется загрузка ТЗ, ЧТЗ или ПР'", () -> {
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.parent().parent().
                    shouldHave(attribute("title", "Для формирования комиссии по приемке требуется загрузка ТЗ, ЧТЗ или ПР"));
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени получения элементов вкладки Ввод в ОЭ/ПЭ договора api")
    void getContractScheduleElementsDataBaseChecksApiTest() {
        open("");
        step("Проверить ответ на запрос", () -> {
            ContractsPage.getContractScheduleElementsData(authCookieSa, contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .header("Content-Type", equalTo("application/json"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/contractScheduleElementsDataSchema.json"));
        });
    }

    static Stream<Arguments> shouldHaveRequiredElementsContractComissioningOePeTabTest() {
        // собрать данные для проверок
        return Stream.of(
                Arguments.of("заголовок Календарный план договора", contractsPage.calendarPlanHeading, visible, contractPpoWithNoPlanPiWorkId),
                Arguments.of("кнопка 'Редактировать' Календарный план договора", contractsPage.calendarPlanEditBtn, enabled, contractPpoWithNoPlanPiWorkId),
                Arguments.of("кнопка Пересчитать этапы ввода", contractsPage.calendarPlanRecountStagesBtn, disabled, contractPpoWithNoPlanPiWorkId),
                Arguments.of("заголовок Ввод в эксплуатацию", contractsPage.commissioningHeading, visible, contractPpoWithNoPlanPiWorkId)
        );
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест #{index} -> Есть требуемый элемент \"{0}\" в состоянии \"{2}\" на незаполненной вкладке 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    @MethodSource
    void shouldHaveRequiredElementsContractComissioningOePeTabTest(String elementName, SelenideElement element, Condition condVal, int contractsId) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Перейти на вкладку 'Ввод в ОЭ/ПЭ' тестдоговора во фрейм с элементами", () -> {
            authApiPage.authorizationApiOnUi();
            contractsPage.openContractsCardCommissioningTab(contractsId);
            switchTo().frame(contractsPage.calendarPlanIframe);
        });
        step("Проверить, что элемент видим для пользователя и в состоянии " + condVal, () -> {
            element.shouldBe(condVal);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> id={0} сохранятеся запросом")
    @FieldSource("contractSubtypesId")
    @Tags({@Tag("api")})
    @DisplayName("Должно сохраняться поле id запросом на редактирование для всех подтипов договора из справочника api")
    void shouldBeSavedContractSubtypeApiTest(int id) {
        step("Проверить, что в теле ответа id как в запросе на редактирование подтипа", () -> {
            int actualId = ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoWithNoPlanPiWorkId, id)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().path("agreementSubType.id");
            Assertions.assertEquals(id, actualId);
        });
    }

    static Stream<Arguments> shouldHaveRequiredElementsCalendarPlanInEditModeOnContractComissioningOePeTabTest() {
        int contractsId = contractPpoWithNoPlanPiWorkId;
        // Перейти на вкладку 'Ввод в ОЭ/ПЭ' тестдоговора в режиме редактирования во фрейм с элементами
        authApiPage.authorizationOnUiWithExistingCookieApp4(authCookieSa);
        contractsPage.openContractsCardCommissioningTab(contractsId);
        switchTo().frame(contractsPage.calendarPlanIframe);
        contractsPage.calendarPlanEditBtn.click();
        contractsPage.dropdownContractSubtypeTabCommissioningBtn.click();
        // собрать данные для проверок
        String
                dropdownContractSubtypeValue1Exp = "Выберите подтип договора",
                dropdownContractSubtypeValue1Act = contractsPage.dropdownContractSubtypeListTabCommissioning.$("li", 0).getText(),
                dropdownContractSubtypeValue2Exp = "Адаптация и внедрение",
                dropdownContractSubtypeValue2Act = contractsPage.dropdownContractSubtypeListTabCommissioning.$("li", 1).getText(),
                dropdownContractSubtypeValue3Exp = "Развитие",
                dropdownContractSubtypeValue3Act = contractsPage.dropdownContractSubtypeListTabCommissioning.$("li", 2).getText(),
                dropdownContractSubtypeValue4Exp = "Разработка",
                dropdownContractSubtypeValue4Act = contractsPage.dropdownContractSubtypeListTabCommissioning.$("li", 3).getText();
        return Stream.of(
                Arguments.of("1я строка дропдауна подтипа договора", dropdownContractSubtypeValue1Exp, dropdownContractSubtypeValue1Act, contractsId),
                Arguments.of("2я строка дропдауна подтипа договора", dropdownContractSubtypeValue2Exp, dropdownContractSubtypeValue2Act, contractsId),
                Arguments.of("3я строка дропдауна подтипа договора", dropdownContractSubtypeValue3Exp, dropdownContractSubtypeValue3Act, contractsId),
                Arguments.of("4я строка дропдауна подтипа договора", dropdownContractSubtypeValue4Exp, dropdownContractSubtypeValue4Act, contractsId),
                Arguments.of("предупреждение", contractsPage.warningNoDetailedPlanOnTabCommissioningText, contractsPage.warningNoDetailedPlanOnTabCommissioning.getText()),
                Arguments.of("заголовок 1й колонки", "№ п/п", contractsPage.tableHeadOnTabCommissioning.$("th", 0).getText()),
                Arguments.of("заголовок 2й колонки", "Наименование работ", contractsPage.tableHeadOnTabCommissioning.$("th", 1).getText()),
                Arguments.of("заголовок 3й колонки", "АСУ", contractsPage.tableHeadOnTabCommissioning.$("th", 2).getText()),
                Arguments.of("заголовок 4й колонки", "Начало", contractsPage.tableHeadOnTabCommissioning.$("th", 3).getText()),
                Arguments.of("заголовок 5й колонки", "Окончание", contractsPage.tableHeadOnTabCommissioning.$("th", 4).getText()),
                Arguments.of("заголовок 6й колонки", "Требуется ОЭ", contractsPage.tableHeadOnTabCommissioning.$("th", 5).getText()),
                Arguments.of("заголовок 7й колонки", "ID процесса", contractsPage.tableHeadOnTabCommissioning.$("th", 6).getText()),
                Arguments.of("кнопка Добавить", "Добавить", contractsPage.addBtnOnTabCommissioning.getText()),
                Arguments.of("кнопка Отмена", "Отмена", contractsPage.cancelBtnOnTabCommissioning.getText()),
                Arguments.of("кнопка Сохранить", "Сохранить", contractsPage.saveBtnOnTabCommissioning.getText())
        );
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест #{index} -> Есть требуемый элемент \"{0}\" со значением \"{2}\" при редактировании календарного плана на вкладке 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    @MethodSource
    void shouldHaveRequiredElementsCalendarPlanInEditModeOnContractComissioningOePeTabTest(String elementName, String expectedValue, String actualValue) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Проверить, что элемент видим для пользователя и имеет правильное имя", () -> {
            Assertions.assertEquals(expectedValue, actualValue);
        });
    }

    static Stream<Arguments> shouldHaveRedFrameRequiredElementsOfCalendarPlanInEditModeContractComissioningOePeTabTest() {
        // "Создать новый договор через api"
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsName, 3, 1, 0, PlanPiWorksPage.asuForPlanPiWorksId);

        // Перейти на вкладку 'Ввод в ОЭ/ПЭ' тестдоговора в режиме редактирования во фрейм с элементами
        authApiPage.authorizationOnUiWithExistingCookieApp4(authCookieSa);
        contractsPage.openContractsCardCommissioningTab(contractId);
        switchTo().frame(contractsPage.calendarPlanIframe);
        contractsPage.calendarPlanEditBtn.click();
        contractsPage.addBtnOnTabCommissioning.click();

        // собрать данные для проверок
        String
                borderColorContractSubtype = contractsPage.dropdownContractSubtypeTabCommissioningBtn.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color"),
                borderColorOrderedNumberCell = contractsPage.orderedNumberCellOnTabCommissioningTable.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color"),
                borderColorNameCell = contractsPage.nameCellOnTabCommissioningTable.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color"),
                borderColorAsuCell = contractsPage.asuCellOnTabCommissioningTable.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color"),
                borderColorStartDateCell = contractsPage.startDateCellOnTabCommissioningTable.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color"),
                borderColorEndDateCell = contractsPage.endDateCellOnTabCommissioningTable.$("fieldset.MuiOutlinedInput-notchedOutline").getCssValue("border-color");

        // удалить договор
        contractsPage.deleteContractApi(authCookieSa, contractId);

        return Stream.of(
                Arguments.of("Подтип договора", borderColorContractSubtype),
                Arguments.of("№ п/п", borderColorOrderedNumberCell),
                Arguments.of("Наименование работ", borderColorNameCell),
                Arguments.of("АСУ", borderColorAsuCell),
                Arguments.of("Начало", borderColorStartDateCell),
                Arguments.of("Окончание", borderColorEndDateCell)
        );
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест #{index} -> Есть рамка обязательности у поля \"{0}\" при заполнении календарного плана вкладки 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    @MethodSource
    void shouldHaveRedFrameRequiredElementsOfCalendarPlanInEditModeContractComissioningOePeTabTest(String fieldName, String borderColor) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Проверить, что у поля рамка имеет цвет обязательного элемента", () -> {
            Assertions.assertEquals("rgb(255, 153, 153)", borderColor, "У поля " + fieldName + " цвет рамки не rgb(255, 153, 153)!!!");
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени запроса на редактирование подтипа договора вкладки Ввод в ОЭ/ПЭ api")
    void putContractSubtypeBaseChecksApiTest() {
        step("Проверить ответ на запрос", () -> {
            ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoWithNoPlanPiWorkId, contractSubtypesId.get(0))
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200).statusLine(containsString("OK"))
                    .header("Content-Type", equalTo("application/json; charset=utf-8"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/contractSubtypePutSchema.json"));
        });
    }

    static Stream<Arguments> shouldBeBtnScreenshotEqualToTemplateOnContractComissioningOePeTabTest() throws InterruptedException {
        // собрать данные для проверок
        authApiPage.authorizationApiOnUiApp4();
        contractsPage.openContractsCardCommissioningTab(contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
        switchTo().frame(contractsPage.calendarPlanIframe);
        sleep(2000);

        String editBtnCalendarPlanBlockTemplateFilePath = "src/test/resources/screenshots/editCalendarPlanBtn.png";
        SelenideElement elementLocator1 = $("button[title='Редактировать']>svg");
        File editBtnCalendarPlanBlockScreenShot = elementLocator1.screenshot();

        String editBtnComissioningBlockTemplateFilePath = "src/test/resources/screenshots/editTermOrdBtn.png";
        SelenideElement elementLocator2 = contractsPage.comissioningBlockAllOnTabCommissioning.get(0).$("span.MuiButton-startIcon>svg");
        File editBtnComissioningBlockScreenShot = elementLocator2.screenshot();

        String exportExcelBtnCalendarPlanBlockTemplateFilePath = "src/test/resources/screenshots/exportExcelComissioningBtn.png";
        SelenideElement elementLocator3 = contractsPage.comissioningBlockExportExcelBtnOnTabCommissioning.get(0).$("svg");
        File exportExcelBtnComissioningBlockScreenShot = elementLocator3.screenshot();

        return Stream.of(
                Arguments.of("Редактировать календарный план", editBtnCalendarPlanBlockTemplateFilePath, editBtnCalendarPlanBlockScreenShot),
                Arguments.of("Редактировать срок по ОРД", editBtnComissioningBlockTemplateFilePath, editBtnComissioningBlockScreenShot),
                Arguments.of("Экспорт в Excel хода ввода в эксплуатацию", exportExcelBtnCalendarPlanBlockTemplateFilePath, exportExcelBtnComissioningBlockScreenShot)
        );
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест №{index} -> Совпадает с шаблонной картинкой скриншот иконки кнопки \"{0}\" на вкладке 'Ввод в ОЭ/ПЭ' договора ППО")
    @MethodSource
    void shouldBeBtnScreenshotEqualToTemplateOnContractComissioningOePeTabTest(String elementName, String expectedImagePath, File actualScreenshotFile) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Проверить равенство размеров и пикселей шаблона и скриншота кнопки", () -> {
            Screenshots.compareImgWithFileScreenshot(expectedImagePath, actualScreenshotFile);
        });
    }

    static Stream<Arguments> onlyForValidRoleAvailableEditBtnOnCommissioningOePeTabTest() {
        // собрать данные для проверок
        return usersForTestsOnCommissioningOePeTabTest(contractPpoWithNoPlanPiWorkId);
    }

    @Tags({@Tag("api")})
    @ParameterizedTest(name = "Тест #{index} -> Для пользователя в {0}валидной роли \"{1}\" {0}доступна кнопка 'Редактировать' КП на вкладке 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    @MethodSource
    void onlyForValidRoleAvailableEditBtnOnCommissioningOePeTabTest(String isValid, String mainRole, String login, String password, int contractId) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Получить от тестперсоны значение ключа buttonsConfig.editAgreementSchedule.show из запроса", () -> {
        });
        open("");
        String userAuthCookie = AuthApiPage.getAuthCookie(login, password);
        boolean show = ContractsPage.getContractScheduleElementsData(userAuthCookie, contractId)
                .then().extract().path("buttonsConfig.editAgreementSchedule.show");
        step("Проверить, что значение show:true для валидных и false для невалидных пользователей", () -> {
            switch (isValid) {
                case "" ->
                        Assertions.assertTrue(show, "Не показывается кнопка Редактировать валидной роли " + mainRole);
                case "не" ->
                        Assertions.assertFalse(show, "Показывается кнопка Редактировать невалидной роли " + mainRole);
            }
        });
    }

    static Stream<Arguments> onlyForValidRoleAvailableEditTermOrdBtnOnComissioningOePeTabTest() throws InterruptedException {
        // собрать данные для проверок
        return usersForTestsOnCommissioningOePeTabTest(contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест #{index} -> Для пользователя в {0}валидной роли \"{1}\" {0}доступна кнопка \"Редактировать Срок по ОРД\" на вкладке 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    @MethodSource
    void onlyForValidRoleAvailableEditTermOrdBtnOnComissioningOePeTabTest(String isValid, String mainRole, String login, String password, int contractId) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Открыть от тестперсоны вкладку договора", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            sleep(1000);
            authUiPage.loginUi(login, password);
            switchTo().frame(contractsPage.calendarPlanIframe);
        });

        step("Проверить, что кнопка отображается и доступна валидной роли и не отображается для невалидной", () -> {
            switch (isValid) {
                case "": {
                    contractsPage.comissioningBlockTermOrdBtnOnTabCommissioning.get(0).shouldBe(visible, Duration.ofSeconds(5)).shouldNotHave(attribute("disabled"));
                    switchTo().defaultContent();
                    authUiPage.logOutBtnOnHeader.click();
                    break;
                }
                case "не": {
                    contractsPage.comissioningBlockOnTabCommissioning.shouldBe(visible, Duration.ofSeconds(5));
                    contractsPage.comissioningBlockTermOrdBtnOnTabCommissioning.get(0).shouldNot(exist);
                    switchTo().defaultContent();
                    authUiPage.logOutBtnOnHeader.click();
                    break;
                }
            }
        });
    }

    static Stream<Arguments> onlyForValidRoleAvailableEditCommissionMembersBtnOnComissioningOePeTabTest() throws InterruptedException {
        // собрать данные для проверок
        return usersForTestsOnCommissioningOePeTabTest(contractPpoWith2PlanPiWorksAnd2CalendarPlanWorksAndCommissionId);
    }

    @Tags({@Tag("ui")})
    @ParameterizedTest(name = "Тест #{index} -> Для пользователя в {0}валидной роли \"{1}\" {0}доступны кнопки \"Редактировать\" для Председатель, Зам. председателя, Участники на вкладке 'Ввод в ОЭ/ПЭ' договора ППО")
    @MethodSource
    void onlyForValidRoleAvailableEditCommissionMembersBtnOnComissioningOePeTabTest(String isValid, String mainRole, String login, String password, int contractId) {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Открыть от тестперсоны вкладку договора", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            sleep(1000);
            authUiPage.loginUi(login, password);
            switchTo().frame(contractsPage.calendarPlanIframe);
        });

        step("Проверить, что кнопка отображается и доступна валидной роли и не отображается для невалидной", () -> {
            switch (isValid) {
                case "": {
                    step("Кнопка Председатель - Редактировать доступна", () -> {
                        contractsPage.comissioningBlockEditChairmanBtnOnTabCommissioning.get(0).shouldBe(visible, Duration.ofSeconds(5)).shouldNotHave(attribute("disabled"));
                    });

                    step("Кнопка Зам. председателя - Редактировать доступна", () -> {
                        contractsPage.comissioningBlockEditDeputyChairmanBtnOnTabCommissioning.get(0).shouldBe(visible, Duration.ofSeconds(5)).shouldNotHave(attribute("disabled"));
                    });

                    step("Кнопка Участники - Редактировать доступна", () -> {
                        contractsPage.comissioningBlockEditMembersBtnOnTabCommissioning.get(0).shouldBe(visible, Duration.ofSeconds(5)).shouldNotHave(attribute("disabled"));
                    });

                    switchTo().defaultContent();
                    authUiPage.logOutBtnOnHeader.click();
                    break;
                }
                case "не": {
                    contractsPage.comissioningBlockOnTabCommissioning.shouldBe(visible, Duration.ofSeconds(5));
                    step("Кнопка Председатель - Редактировать недоступна", () -> {
                        contractsPage.comissioningBlockEditChairmanBtnOnTabCommissioning.get(0).shouldNot(exist);
                    });

                    step("Кнопка Зам. председателя - Редактировать недоступна", () -> {
                        contractsPage.comissioningBlockEditDeputyChairmanBtnOnTabCommissioning.get(0).shouldNot(exist);
                    });

                    step("Кнопка Участники - Редактировать недоступна", () -> {
                        contractsPage.comissioningBlockEditMembersBtnOnTabCommissioning.get(0).shouldNot(exist);
                    });

                    switchTo().defaultContent();
                    authUiPage.logOutBtnOnHeader.click();
                    break;
                }
            }
        });
    }

}
