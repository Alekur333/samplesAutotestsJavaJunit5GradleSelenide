package aleks.kur.tests.documents;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки кнопок на строке версии в карточке документа вкладка 'Основная информация' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsCardMainTab"), @Tag("btnOnFileString")})
public class TestBtnsOnFileStringOfReportingDocumentsCardMainTabUi extends TestBaseUi {
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

//    @Disabled
    @Test
    @DisplayName("Можно удалить файл документа, который не был на согласовании, на вкладке 'Основная информация' апп4 группы Отчетные")
    void canBeDeletedFileOfNotProcessedReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Удалить файл по кнопке Удалить файл в строке файла", () -> {
            documentsPage.firstFileOfFirstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.deleteFileBtnOnFileStringOnMainTabApp4.get(0).click();
            modalWindowsPage.confirmBtnOnModalApp4.click();
        });
        step("Проверить отсутствие удаленного файла в документе", () -> {
            documentsPage.filesOfDocVersionOnMainTabApp4.get(0).shouldNotHave(text(attachFile), Duration.ofSeconds(5));
        });

        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Disabled
    @Test
    @DisplayName("Нельзя удалить файлы версии документа, по которому идет согласование, на вкладке 'Основная информация' апп4 группы Отчетные")
    void cantBeDeletedFileOfReportingDocumentInProcessApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });

        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Disabled
    @Test
    @DisplayName("Можно удалить файлы версии документа, который был на согласовании, от Администратора на вкладке 'Основная информация' апп4 группы Отчетные")
    void cantBeDeletedFilesOfProcessedReportingDocumentByAdminApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });

        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Disabled
    @Test
    @DisplayName("Нельзя удалить файлы версии документа, который был на согласовании, от 'Руководитель проекта от исполнителя' на вкладке 'Основная информация' апп4 группы Отчетные")
    void cantBeDeletedFileOfProcessedReportingDocumentByExecutorProjectDirectorApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });

        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


}
