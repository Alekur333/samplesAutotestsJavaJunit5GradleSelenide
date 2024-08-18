package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.PcDzoPage;
import ru.progredis.tests.TestBaseUi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.PcDzoPage.dzoFirstLevelDocTypes;
import static ru.progredis.pages.PcDzoPage.dzoSecondLevelDocTypeNames;

@Tags({@Tag("ui"), @Tag("pcDzo"), @Tag("pcDzoCard"), @Tag("dzoDocuments"), @Tag("regress")})
@DisplayName("Проверки документов ДЗО")
public class TestDzoDocumentsUi extends TestBaseUi {

    static String
            testDzoAsuKpiId = organizationPage.asuKpiIdZeldortrest,
            isDzoTestOrgAsuKpiJson = pcDzoPage.isDzoZeldortrestAsuKpiJson(true);

    @BeforeAll
//    @Disabled
    public static void testDataPreparation() {
        PcDzoPage.testOrgShouldBeDzoWithRespCkiAndPcRequired(authCookie, testDzoAsuKpiId, isDzoTestOrgAsuKpiJson);
    }

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    static String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    static Response testDzoOrgCard = organizationPage.getOrganizationCardByAsuKpiIdApi(authCookie, testDzoAsuKpiId);
    int
            testDzoId = testDzoOrgCard.then().extract().path("id"),
            fileId = filesPage.uploadFileAndGetIdApi(authCookie, documentsPage.testUploadFile);
    String testDzoShortName = testDzoOrgCard.then().extract().path("shortName");

    @Test
    @DisplayName("Должны быть элементы в окне добавления документа по кнопке \"Добавить документ\" карточки ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void shouldBeWindowAddDocUsingBtnOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Открыть окно по кнопке", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.click();
        });

        step("Есть заголовок с кнопкой Закрыть", () -> {
            pcDzoPage.addDocWindowHeaderOnDzoCardLocator.shouldHave(exactText("Новый документ"));
            pcDzoPage.addDocWindowCloseCrossOnDzoCardLocator.shouldBe(enabled);
        });

        step("Есть блок выбора года, год по умолчанию - текущий", () -> {
            pcDzoPage.addDocWindowYearLabelInDzoCardLocator.should(exist);
            pcDzoPage.addDocWindowYearSelectionInDzoCardLocator.should(exist).shouldHave(exactText(String.valueOf(currentYear)));
        });

        step("Есть блок выбора Типа документа, тип по умолчанию - не выбран", () -> {
            pcDzoPage.addDocWindowDocTypeLabelInDzoCardLocator.should(exist);
            String defaultValue = pcDzoPage.addDocWindowDocTypeSelectionInDzoCardLocator.should(exist).getText();
            assertEquals("", defaultValue, "Выбран тип документа по умолчанию!!!");
        });

        step("Есть кнопка загрузки файлов", () -> {
            pcDzoPage.addDocWindowUploadBtnInDzoCardLocator.should(exist);
        });

        step("Есть кнопки Отменить, Добавить", () -> {
            pcDzoPage.addDocWindowCancelBtnInDzoCardLocator.should(exist).shouldHave(text("Отменить"));
            pcDzoPage.addDocWindowConfirmBtnInDzoCardLocator.should(exist).shouldHave(text("Добавить"));
        });
    }

    @Test
    @DisplayName("Должны быть года в дропдауне окна добавления документа по кнопке \"Добавить документ\" карточки ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void shouldBeRequiredYearsDropdownInAddDocWindowUsingBtnOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Открыть окно по кнопке", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.click();
        });

        step("Открыть дропдаун выбора года", () -> {
            pcDzoPage.addDocWindowYearSelectionInDzoCardLocator.click();
        });

        // по требованиям было 6 лет вперед, но Валерий на митинге 12/08/2024 решил не фиксить
        step("Должны быть предидущий и 5 лет вперед от текущего года для выбора", () -> {
            int i;
            for (i = 0; i <= 6; i++) {
                int currentYearMinusOne = currentYear - 1;
                int expectedYear = currentYearMinusOne + i;
                String expectedYearSt = String.valueOf(expectedYear);
                pcDzoPage.addDocWindowDropdownListInDzoCardLocator.shouldHave(text(expectedYearSt));
            }
        });
    }

    @Test
    @DisplayName("Должны быть типы документов ПЦ ДЗО в дропдауне окна добавления документа по кнопке \"Добавить документ\" карточки ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void shouldBeRequiredDocTypesDropdownInAddDocWindowUsingBtnOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Открыть окно по кнопке", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.click();
        });

        step("Получить список типов документов в дропдауне выбора типа", () -> {
        });
        pcDzoPage.addDocWindowDocTypeSelectionInDzoCardLocator.click();
        String dropdownListSt = pcDzoPage.addDocWindowDropdownListInDzoCardLocator.getText();
        // Преобразуем dropdownList в набор строк
        Set<String> dropdownSet = Arrays.stream(dropdownListSt.split("\n"))
                .collect(Collectors.toSet());

        step("Получить список типов документов в группе ПЦ ДЗО из справочника", () -> {
        });
        List<List<String>> docTypesFromGroupList = pcDzoPage.getPcDzoDocumentGroup().then()
                .extract().path("documentTypes.shortName");
        List<String> docTypesFromGroupListSt = docTypesFromGroupList.get(0);
        Set<String> docTypesFromGroupSetSt = new HashSet<>(docTypesFromGroupListSt);

        step("Должны быть все типы документов группы ПЦ ДЗО для выбора в дропдауне", () -> {
            assertEquals(docTypesFromGroupSetSt, dropdownSet, "Типы документов из группы ПЦ ДЗО не совпадают с выбором типов в дропдауне");
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Нельзя загрузить документ 2го уровня \"{1}\" без наличия дока 1го уровня в карточке ДЗО ui")
    @CsvSource(textBlock = dzoSecondLevelDocTypeNames)
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void cantBeAddedSecondLevelDocWithoutFirstLevelDocOnDzoCardUiTest(String typeCode, String docTypeName) {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Выбрать тип документа 2го уровня", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.click();
            pcDzoPage.addDocWindowDocTypeSelectionInDzoCardLocator.click();
            pcDzoPage.addDocWindowDropdownListInDzoCardLocator.$(byText(docTypeName)).click();
        });

        step("Документ не добавляется, есть предупреждение, кнопки «Добавить файлы» и «Добавить» заблокированы", () -> {
            pcDzoPage.addDocWindowNotificationInDzoCardLocator.shouldHave(exactText(pcDzoPage.addDocWindowNotificationInDzoCard));
            pcDzoPage.addDocWindowUploadBtnInDzoCardLocator.shouldHave(attribute("aria-disabled", "true"));
            pcDzoPage.addDocWindowConfirmBtnInDzoCardLocator.shouldBe(disabled);
        });
    }

//    @Test
    @ParameterizedTest(name = "Тест #{index} -> Можно загрузить документ 1го уровня \"{1}\" в карточке ДЗО ui")
    @CsvSource(textBlock = dzoFirstLevelDocTypes)
    void canBeAddedApprovedPcDzoDocOnDzoCardUiTest(String type, String shortName) throws InterruptedException {

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        List docIdsBeforeDocAddition = pcDzoPage.getDzoCardApi(authCookie, testDzoId).then().extract().path("documents.id");
        int docsBeforeDocAddition = docIdsBeforeDocAddition.size();

        step("Добавить документ «" + shortName + "»", () -> {
            pcDzoPage.addDzoDocByTypeInCardUi(type);
        });

        Thread.sleep(2000);
        List docIdsAfterDocAddition = pcDzoPage.getDzoCardApi(authCookie, testDzoId).then().log().all().extract().path("documents.id");
        int docsAfterDocAddition = docIdsAfterDocAddition.size();

        step("Проверить, что документ добавляется в таблицу карточки ДЗО", () -> {
            assertEquals(docsBeforeDocAddition + 1, docsAfterDocAddition, "Документ не добавлен в карточку ДЗО");
        });

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Можно загрузить документ 2го уровня \"{1}\" при наличии дока 1го уровня Утв. ПЦ ДЗО в карточке ДЗО ui")
    @CsvSource(textBlock = dzoSecondLevelDocTypeNames)
    void canBeAddedSecondLevelDocWithFirstLevelDocTypeApprovedDpOnDzoCardUiTest(String typeCode, String docTypeName) {

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });

         step("Добавить документ 1го уровня Утв. ПЦ ДЗО", () -> {
            pcDzoPage.createDzoDocAnyStatusApi(authCookie, "approved_dp", 4, testDzoId, fileId);
        });

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Добавить документ 2го уровня «" + docTypeName + "»", () -> {
            pcDzoPage.addDzoDocByTypeInCardUi(typeCode);
        });
        sleep(2000);

        step("Проверить, что документ есть в таблице карточки ДЗО", () -> {
             String dzoDocTypeNameInTableRow =
            pcDzoPage.docTypeNamesInTableRowOnDzoTables.filterBy(text(docTypeName)).get(0).getText();
            assertEquals(dzoDocTypeNameInTableRow, docTypeName, "документ 2го уровня «" + docTypeName + "» не добавлен");
        });

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Можно загрузить документ 2го уровня \"{1}\" при наличии дока 1го уровня ПЦ ДЗО в статусе \"Утверждён\" в карточке ДЗО ui")
    @CsvSource(textBlock = dzoSecondLevelDocTypeNames)
    void canBeAddedSecondLevelDocWithFirstLevelDocTypeDpHavingApprovedStatusOnDzoCardUiTest(String typeCode, String docTypeName) {

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });

         step("Добавить документ 1го уровня ПЦ ДЗО в статусе \"Утверждён\"", () -> {
            pcDzoPage.createDzoDocAnyStatusApi(authCookie, "dp", 4, testDzoId, fileId);
        });

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Добавить документ 2го уровня «" + docTypeName + "»", () -> {
            pcDzoPage.addDzoDocByTypeInCardUi(typeCode);
        });
        sleep(2000);

        step("Проверить, что документ есть в таблице карточки ДЗО", () -> {
             String dzoDocTypeNameInTableRow =
                     pcDzoPage.docTypeNamesInTableRowOnDzoTables.filterBy(text(docTypeName)).get(0).getText();
            assertEquals(dzoDocTypeNameInTableRow, docTypeName, "документ 2го уровня «" + docTypeName + "» не добавлен");
        });

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Название загруженного документа 1го уровня \"{1}\" формируется автоматически по шаблону ui")
    @CsvSource(textBlock = dzoFirstLevelDocTypes)
    void nameOfFirsLevelDocFormingAsTemplateOnDzoCardUiTest(String typeCode, String docTypeName) {

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Добавить документ 1го уровня «" + docTypeName + "»", () -> {
            pcDzoPage.addDzoDocByTypeInCardUi(typeCode);
        });
        sleep(2000);

        String docNameTemplate = docTypeName + " " + testDzoShortName;

        step("Проверить, что имя документа по шаблону в таблице карточки ДЗО", () -> {
             String dzoDocNameInTableRow =
                     pcDzoPage.docNamesInTableRowOnDzoTables.get(0).getText();
            assertEquals(docNameTemplate, dzoDocNameInTableRow, "Не по шаблону имя добавленного документа типа «" + docTypeName + "»");
        });

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Название загруженного документа 2го уровня \"{1}\" формируется автоматически по шаблону ui")
    @CsvSource(textBlock = dzoSecondLevelDocTypeNames)
    void nameOfSecondLevelDocFormingAsTemplateOnDzoCardUiTest(String typeCode, String docTypeName) {

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });

        step("Добавить документ 1го уровня Утв. ПЦ ДЗО", () -> {
            pcDzoPage.createDzoDocAnyStatusApi(authCookie, "approved_dp", 4, testDzoId, fileId);
        });

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });

        step("Добавить документ 2го уровня «" + docTypeName + "»", () -> {
            pcDzoPage.addDzoDocByTypeInCardUi(typeCode);
        });
        sleep(2000);

        String docNameTemplate = docTypeName + " " + testDzoShortName;

        step("Проверить, что имя документа по шаблону в таблице карточки ДЗО", () -> {
             String dzoDocNameInTableRow =
                     pcDzoPage.docNamesInTableRowOnDzoTables.filterBy(text(docNameTemplate)).get(0).getText();
            assertEquals(docNameTemplate, dzoDocNameInTableRow, "Не по шаблону имя добавленного документа типа «" + docTypeName + "»");
        });

        step("Удалить документы ДЗО", () -> {
            pcDzoPage.deleteAllDzoDocsByItsIdApi(authCookie, testDzoId);
        });
    }


}
