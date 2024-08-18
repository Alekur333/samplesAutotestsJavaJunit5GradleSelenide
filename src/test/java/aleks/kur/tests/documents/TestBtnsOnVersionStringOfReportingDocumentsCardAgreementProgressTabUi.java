package aleks.kur.tests.documents;

import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Проверки кнопок на строке версии в карточке документа вкладка 'Ход согласования' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsAgreementProgressMainTab"), @Tag("btnOnVersionString")})
public class TestBtnsOnVersionStringOfReportingDocumentsCardAgreementProgressTabUi extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String contractsName = contractsPage.contractsName;
    String docName = documentsPage.docName;
    String decimalNumber = documentsPage.decimalNumber;
    String docNameUpdated = documentsPage.docNameUpdated;
    int docTypeId = 45; // Техническое задание на систему (ТЗ)

    @Test
    @DisplayName("Проверка количества и имени кнопок на строке версии в таблице на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldBeBtnsOnVersionStringOnTasksTableOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Ход согласования' и дождаться появления задач в таблице", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection versionsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, versionsCollection, 2, 12000);
        });
        step("Вызвать панель кнопок по ховеру в строке версии таблицы задач", () -> {
            documentsPage.btnsPanelOnVersionStringOnTasksTableOnAgreementProgressTabApp4.hover();
        });
        step("Есть кнопка 'Экспортировать отчет по процессу согласования' в таблице задач", () -> {
            documentsPage.btnExportReportAgreementProcessOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldBe(visible).
                    shouldHave(attribute("title", "Экспортировать отчет по процессу согласования"));
        });
        step("Есть кнопка 'Экспортировать отчет о результатах согласования' в таблице задач", () -> {
            documentsPage.btnExportReportAgreementResultOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldBe(visible).
                    shouldHave(attribute("title", "Экспортировать отчет о результатах согласования"));
        });
        step("Есть кнопка 'Экспортировать замечания к версии' в таблице задач", () -> {
            documentsPage.btnExportVersionsRemarksOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldBe(visible).
                    shouldHave(attribute("title", "Экспортировать замечания к версии"));
        });
        step("Есть кнопка 'Экспортировать лист согласования' в таблице задач", () -> {
            documentsPage.btnExportAgreementListOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldBe(visible).
                    shouldHave(attribute("title", "Экспортировать лист согласования"));
        });
        step("Есть кнопка 'Скорректировать даты' в таблице задач", () -> {
            documentsPage.btnEditDatesOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldBe(visible).
                    shouldHave(attribute("title", "Корректировка дат для незарегистрированного документа является преждевременной"));
        });
        step("Должно быть точное количество кнопок в строке версии таблицы задач", () -> {
            int btnsNumberOnPanel = documentsPage.btnsEnabledOnVersionSrtingOnAgreementProgressTabApp4.size() +
                    documentsPage.btnsDisabledOnVersionSrtingOnAgreementProgressTabApp4.size();
            assertThat(btnsNumberOnPanel).isEqualTo(5);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Проверка иконок кнопок на строке версии в таблице на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldHaveExactSvgBtnsOnVersionStringOnTasksTableOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Ход согласования' и дождаться появления задач в таблице", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection versionsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, versionsCollection, 2, 12000);
        });
        step("Вызвать панель кнопок по ховеру в строке версии таблицы задач", () -> {
            documentsPage.btnsPanelOnVersionStringOnTasksTableOnAgreementProgressTabApp4.hover();
        });
        step("Иконка кнопки 'Экспортировать отчет по процессу согласования' имеет точные атрибуты svg", () -> {
            documentsPage.btnExportReportAgreementProcessEnabledSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Экспортировать отчет о результатах согласования' имеет точные атрибуты svg", () -> {
            documentsPage.btnExportReportAgreementResultEnabledSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Экспортировать замечания к версии' имеет точные атрибуты svg", () -> {
            documentsPage.btnExportVersionsRemarksEnabledSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Экспортировать лист согласования' имеет точные атрибуты svg", () -> {
            documentsPage.btnExportAgreementListEnabledSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Скорректировать даты' имеет точные атрибуты svg", () -> {
            documentsPage.btnEditDatesRemarksDisabledSvgAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Проверка действия кнопок на строке версии в таблице на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldHaveActionsBtnsOnVersionStringOnTasksTableOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Ход согласования' и дождаться появления задач в таблице", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection versionsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, versionsCollection, 2, 12000);
        });
        step("Вызвать панель кнопок по ховеру в строке версии таблицы задач", () -> {
            documentsPage.btnsPanelOnVersionStringOnTasksTableOnAgreementProgressTabApp4.hover();
        });
        step("Кнопка 'Экспортировать отчет по процессу согласования' имеет точную ссылку на формирование отчета", () -> {
            String expectedLink = documentsPage.exportReportAgreementProcessForDocumentVersionLink();
            documentsPage.btnExportReportAgreementProcessOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldHave(attribute("href", expectedLink));
        });
        step("Кнопка 'Экспортировать отчет о результатах согласования' имеет точную ссылку на формирование отчета", () -> {
            String expectedLink = documentsPage.exportReportAgreementResultForDocumentLink();
            documentsPage.btnExportReportAgreementResultOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldHave(attribute("href", expectedLink));
        });
        step("Кнопка 'Экспортировать замечания к версии' имеет точную ссылку на формирование отчета", () -> {
            String expectedLink = documentsPage.exportVersionsRemarksForDocumentLink();
            documentsPage.btnExportVersionsRemarksOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldHave(attribute("href", expectedLink));
        });
        step("Кнопка 'Экспортировать лист согласования' имеет точную ссылку на формирование отчета", () -> {
            String expectedLink = documentsPage.exportgreementListForDocumentLink();
            documentsPage.btnExportAgreementListOnVersionStringOnTasksTableOnAgreementProgressTabApp4.
                    shouldHave(attribute("href", expectedLink));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


}
