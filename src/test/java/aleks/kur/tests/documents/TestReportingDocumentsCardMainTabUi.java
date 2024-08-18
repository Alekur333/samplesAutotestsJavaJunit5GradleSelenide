package aleks.kur.tests.documents;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки карточки документа вкладки 'Основная информация' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsCardMainTab")})
public class TestReportingDocumentsCardMainTabUi extends TestBaseUi {
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
    @DisplayName("Должно быть точное количество колонок в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentVersionsTableOfNewDocumentApp4ShouldHaveExactNumberOfColumnsTest() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("В таблице версий документа должно быть 9 колонок", () -> {
            documentsPage.columnsOnDocumentVersionsTableOnMainTabApp4.shouldHave(size(9), Duration.ofSeconds(5));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Статус' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentStatusColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Статус' и ее порядковый номер", () -> {
            documentsPage.documentStatusColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "3"));
            documentsPage.documentStatusColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Статус"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Автор' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentAuthorColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Автор' и ее порядковый номер", () -> {
            documentsPage.documentAuthorColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "4"));
            documentsPage.documentAuthorColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Автор"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Загружено' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentUploadedColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Загружено' и ее порядковый номер", () -> {
            documentsPage.documentUploadedColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "5"));
            documentsPage.documentUploadedColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Загружено"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'ID процесса' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentIdProcessColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'ID процесса' и ее порядковый номер", () -> {
            documentsPage.documentIdProcessColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "6"));
            documentsPage.documentIdProcessColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("ID процесса"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'ID версии' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentIdVersionColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'ID версии' и ее порядковый номер", () -> {
            documentsPage.documentIdVersionColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "7"));
            documentsPage.documentIdVersionColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("ID версии"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Направлена' в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void documentSentDateColumnShouldBeOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'ID версии' и ее порядковый номер", () -> {
            documentsPage.documentSentDateColumnOnDocumentVersionsTableOnMainTabApp4.scrollIntoView(true).shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "8"));
            documentsPage.documentSentDateColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Направлена"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должно быть 3 колонки без заголовка в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeThreeColumnsOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонок без заголовка и их порядковый номер", () -> {
            documentsPage.firstEmptyColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("aria-colindex", "1"));
            documentsPage.firstEmptyColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldBe(empty);
            documentsPage.documentVersionColumnOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("aria-colindex", "2"));
            documentsPage.documentVersionColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldBe(empty);
            documentsPage.lastEmptyColumnOnDocumentVersionsTableOnMainTabApp4.should(exist)
                    .shouldHave(attribute("aria-colindex", "9"));
            documentsPage.lastEmptyColumnsNameOnDocumentVersionsTableOnMainTabApp4.shouldBe(empty);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть Версия 1 в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeFirstVersionOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Имя версии должно быть Версия 1", () -> {
            documentsPage.firstVersionNameOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(exactText("Версия 1"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должен быть приложенный к версии файл в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeFileInVersionOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Строка вложеннного файла раскрыта", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.shouldHave(cssClass("ag-row-group-expanded"));
        });
        step("Название файла соотвествует приложенному", () -> {
            documentsPage.firstFileOfFirstVersionOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText(attachFile));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Статус Черновик в таблице версий на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeDraftStatusOfVersionOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Статус черновик в строке версии", () -> {
            documentsPage.statusOfFirstVersionOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Черновик"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть кнопка 'Добавить новую версию' на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeAddNewVersionBtnOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Должна быть кнопка 'Добавить новую версию'", () -> {
            documentsPage.AddNewVersionBtnOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(exactText("Добавить новую версию"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Новая версия добавляется по кнопке 'Добавить новую версию' на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void canBeAddedNewVersionByAddNewVersionBtnOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Добавить 2ю версию по кнопке 'Добавить новую версию'", () -> {
            documentsPage.addNewDocsVersionByAddNewVersionBtnOnMainTabApp4(attachFile);
        });
        step("Должна быть строка с именем 2й версии  документа в таблице версий", () -> {
            documentsPage.secondVersionNameOnDocumentVersionsTableOnMainTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(exactText("Версия 2"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Только актуальная версия по умолчанию раскрыта на вкладке 'Основная информация' нового документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeOpenedActualVersionOnlyOnDocumentVersionsTableOfDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Добавить 2ю версию по кнопке 'Добавить новую версию'", () -> {
            documentsPage.addNewDocsVersionByAddNewVersionBtnOnMainTabApp4(attachFile);
            refresh();
        });
        step("Должна быть раскрыта 2я версия  документа и закрыта 1я в таблице версий", () -> {
            documentsPage.secondVersionOnDocumentVersionsTableOnMainTabApp4.shouldHave(cssClass("ag-row-group-expanded"));
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.shouldNotHave(cssClass("ag-row-group-expanded"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Статус Черновик у новой версии документа в таблице версий на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
//    @Disabled
    void shouldBeDraftStatusOfNewVersionOnDocumentVersionsTableOfNewDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Добавить 2ю версию по кнопке 'Добавить новую версию'", () -> {
            documentsPage.addNewDocsVersionByAddNewVersionBtnOnMainTabApp4(attachFile);
        });
        step("Статус Черновик в строке новой версии", () -> {
            documentsPage.statusOfSecondVersionOnDocumentVersionsTableOnMainTabApp4.shouldHave(exactText("Черновик"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


}
