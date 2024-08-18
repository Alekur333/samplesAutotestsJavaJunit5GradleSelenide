package aleks.kur.tests.documents;

import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки карточки документа вкладки 'Ход согласования' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsCardAgreementProgressTab")})
public class TestReportingDocumentsCardAgreementProgressTabUi extends TestBaseUi {
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
    @DisplayName("Таблица задач доступна на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void tasksTableVisibleOnAgreementProgressTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть таблица версий документа на вкладке 'Ход согласования'", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.tasksTableOnAgreementProgressTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должно быть точное количество колонок в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void tasksTableOfNewReportingDocumentApp4ShouldHaveExactNumberOfColumnsTest() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("В таблице задач документа должно быть 9 колонок", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.columnsOnTasksTableOnAgreementProgressTabApp4.shouldHave(size(9), Duration.ofSeconds(5));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Подразделение' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void departmentColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Подразделение' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.departmentColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "1"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Согласующий' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void approverColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Согласующий' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.approverColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "3"));
            documentsPage.approverColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("Согласующий"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Результат' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void resultColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Результат' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.resultColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "4"));
            documentsPage.resultColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("Результат"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'ID задачи' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void taskIdColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'ID задачи' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.taskIdColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "5"));
            documentsPage.taskIdColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("ID задачи"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Задача' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void taskColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Задача' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.taskColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "6"));
            documentsPage.taskColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("Задача"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Поставлена' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void taskCreationDateColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Поставлена' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.taskCreationDateColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "8"));
            documentsPage.taskCreationDateColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("Поставлена"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'План' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void planFinishDateColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'План' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.planFinishDateColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "9"));
            documentsPage.planFinishDateColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("План"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть колонка 'Факт' в таблице на вкладке 'Ход согласования' нового документа апп4 группы Отчетные")
//    @Disabled
    void factFinishDateColumnShouldBeOnTasksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить наличие колонки 'Факт' и ее порядковый номер", () -> {
            documentsPage.agreementProgressTabApp4.click();
            documentsPage.factFinishDateColumnOnTasksTableOnAgreementProgressTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(attribute("aria-colindex", "10"));
            documentsPage.factFinishDateColumnsNameOnTasksTabOnAgreementProgressTabApp4.shouldHave(exactText("Факт"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть обязательные элементы в строке версии в таблице задач на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldBeElementsOnVersionStringOnTasksTableOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Ход согласования'", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection elementsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, elementsCollection, 2, 12000);
        });
        step("Есть Версия в таблице задач", () -> {
            documentsPage.versionNemberOnTasksTableOnAgreementProgressTabApp4.shouldHave(text("Версия 1"));
        });
        step("Есть Статус в таблице задач", () -> {
            documentsPage.versionStatusOnTasksTableOnAgreementProgressTabApp4.shouldNotBe(empty);
        });
        step("Есть панель кнопок по ховеру в строке версии таблицы задач", () -> {
            documentsPage.btnsPanelOnVersionStringOnTasksTableOnAgreementProgressTabApp4.hover().shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть задачи после старта процесса на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldBeTasksOnAgreementProgressTabOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Ход согласования'", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection versionsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, versionsCollection, 2, 12000);
        });
        step("Проверить наличие задач в таблице", () -> {
            documentsPage.tasksOnTasksTableOnAgreementProgressTabApp4.shouldHave(sizeGreaterThan(1));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть обязательные атрибуты у задач на вкладке 'Ход согласования' документа группы Отчетные апп4")
//    @Disabled
    void shouldHaveRequiredAttrsTasksOnAgreementProgressTabOfReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
//        step("Запустить процесс согласования по кнопке в строке версии", () -> {
//            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
//            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
//        });
        String docId = documentsPage.getDocIdFromDocsCard();
        step("Стартовать процесс по тестовому документу по api", () -> {
            documentsPage.startProcessOfReportingDocumentApi(authCookie, Integer.parseInt(docId));
//            refresh();
        });
        step("Перейти на вкладку 'Ход согласования'", () -> {
            documentsPage.agreementProgressTabApp4.click();
        });
        step("Перезагружать страницу до появления версии на таблице задач", () -> {
            int versions = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4.size();
            ElementsCollection versionsCollection = documentsPage.versionNumbersOnTasksTableOnAgreementProgressTabApp4;
            reloadPageToCatchElement(versions, versionsCollection, 2, 12000);
        });
        step("Проверить наличие атрибутов задач в таблице", () -> {
            step("Должно быть заполнено поле 'Подразделение'", () -> {
                documentsPage.departmentValuesOnTaskRowOnAgreementProgressTabApp4.get(1).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'Согласующий'", () -> {
                documentsPage.agreenentPersonValuesOnTaskRowOnAgreementProgressTabApp4.get(1).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'Результат'", () -> {
                documentsPage.resultValuesOnTaskRowOnAgreementProgressTabApp4.get(0).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'ID задачи'", () -> {
                documentsPage.taskIdValuesOnTaskRowOnAgreementProgressTabApp4.get(0).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'Задача'", () -> {
                documentsPage.taskNameValuesOnTaskRowOnAgreementProgressTabApp4.get(1).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'Поставлена'", () -> {
                documentsPage.taskCreatedDateValuesOnTaskRowOnAgreementProgressTabApp4.get(1).shouldNotBe(empty);
            });
            step("Должно быть заполнено поле 'План'", () -> {
                documentsPage.taskPlanFinishDateValuesOnTaskRowOnAgreementProgressTabApp4.get(1).shouldNotBe(empty);
            });
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }



}
