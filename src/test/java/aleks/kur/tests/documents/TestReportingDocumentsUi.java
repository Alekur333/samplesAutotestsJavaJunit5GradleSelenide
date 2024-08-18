package aleks.kur.tests.documents;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
public class TestReportingDocumentsUi extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String contractsName = contractsPage.contractsName;
    String docName = documentsPage.docName;
    String decimalNumber = documentsPage.decimalNumber;
    String docNameUpdated = documentsPage.docNameUpdated;
//    File testUploadFile = filesPage.testUploadFile;
//    File testUploadFile1 = filesPage.testUploadFile1;

    int docTypeId = 45; // Техническое задание на систему (ТЗ)
    int contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName); // Создать тестовый договор через api
    int fileId = filesPage.uploadFileAndGetIdApi(authCookie, documentsPage.testUploadFile); // загрузить файл и получить id
    int docId = documentsPage.createDocumentInContractAndGetIdApi(authCookie, docName, decimalNumber, docTypeId, contractsId, fileId); // Добавить документ ТЗ в тестовый договор через api

    @Test
//    @Disabled
    @DisplayName("Карточка документа ТЗС доступна после добавления в договор")
    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
    void tzsDocumentsCardAvailableAfterAddingToContractTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
        });
        step("Открыть карточку по имени созданного документа на вкладке Документы", () -> {
            documentsPage.openDocsCardByDocsNameOnDocsTab(docName);
        });
//        step("Название страницы - Документ", () -> {
//            documentsPage.headerOnDocsCard.shouldHave(exactText("Документ"));
//        });
//        step("Название документа на карточке соответствует созданному", () -> {
//            documentsPage.docsNameOnDocsCard.shouldHave(exactText(docName));
//        });
//        step("Децимальный номер на карточке соответствует созданному", () -> {
//            documentsPage.decimalNumberOnDocsCard.shouldHave(exactText(decimalNumber));
//        });
//        step("Тип документа на карточке соответствует созданному", () -> {
//            documentsPage.docsTypeOnDocsCard.shouldHave(exactText("ТЗ"));
//        });
//        step("Название договора на карточке соответствует созданному", () -> {
//            documentsPage.contractsNameOnDocsCard.shouldHave(exactText("Договор " + "\"" + contractsName + "\""));
//        });
//        step("Должны быть хлебные крошки для возврата в таблицу документов договора", () -> {
//            String linkToContractsDocsTab = documentsPage.makeLinkToContractsDocsTabOnDocsCard(contractsId);
//            documentsPage.contractsNameOnDocsCard.shouldHave(attribute("href", linkToContractsDocsTab));
//        });
        step("ID документа на карточке соответствует созданному", () -> {
            documentsPage.docIdOnDocsCardApp4.shouldHave(exactText("ID документа " + docId)); //апп4
//            documentsPage.docIdOnDocsCard.shouldHave(exactText("ID документа " + docId)); //апп2
        });
//        step("ID версии документа соответствует созданному", () -> {
//            documentsPage.docVersionIdOnDocsCard.shouldHave(exactText("ID версии документа " + docId));
//        });
//        step("Должен быть элемент 'т/н ЕАСД'", () -> {
//            documentsPage.tnEasdElementOnDocsCard.shouldHave(exactText("т/н ЕАСД: н/д"));
//        });
//        step("Должен быть cтатус согласования документа", () -> {
//            documentsPage.docStatusOnDocsCard.shouldHave(exactText("Черновик"));
//        });
//        step("Должен быть элемент 'Дата запуска на согласование'", () -> {
//            documentsPage.approvalStartTimeStatusOnDocsCard.shouldBe(visible);
//        });
//        step("Должен быть элемент 'Пользователь, инициировавший согласование'", () -> {
//            documentsPage.approvalInitiatorOnDocsCard.shouldBe(visible);
//        });
//        step("Должен быть элемент 'Номер текущей версии документа'", () -> {
//            documentsPage.currentVersionOnDocsCard.shouldBe(visible);
//        });
//        step("Должен быть блок вкладок в карточке документа", () -> {
//            documentsPage.tabsBlockOnDocsCard.shouldBe(visible);
//        });
//        step("Должна быть вкладка «Основная информация»", () -> {
//            documentsPage.firstTabNameOnDocsCard.shouldHave(exactText("Основная информация"));
//        });
//        step("При открытии карточки открывается вкладка «Основная информация»", () -> {
//            documentsPage.firstTabOnDocsCard.shouldHave(cssClass("k-state-active"));
//        });
//        step("Должна быть вкладка «Ход согласования»", () -> {
//            documentsPage.secondTabNameOnDocsCard.shouldHave(exactText("Ход согласования"));
//        });
//        step("Должна быть вкладка «Замечания»", () -> {
//            documentsPage.thirdTabNameOnDocsCard.shouldHave(exactText("Замечания"));
//        });
        step("Удалить тестовый договор", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("Название документа ТЗС можно отредактировать")
    void canBeEditedNameOfTzsDocumentOnDocumentsTabTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
        });
        step("Открыть окно редактирования по кнопке Редактировать в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.click();
            documentsPage.editBtnOnDocStringOnDocsTab.click();
        });
        step("Изменить имя, добавить файл, сохранить", () -> {
            documentsPage.docNameFieldOnEditDocModal.setValue(docNameUpdated);
            documentsPage.saveBtnOnEditDocModal.click();
        });
        step("Проверить, что новое имя сохранилось", () -> {
            documentsPage.firstDocNameOnDocsTab.shouldHave(exactText(docNameUpdated));
        });
        step("Удалить тестовый договор", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("Документу ТЗС можно добавить новый файл")
    void canBeAddedNewFileInTzsDocumentOnDocumentsTabTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
        });
        step("Открыть окно редактирования по кнопке Редактировать в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.click();
            documentsPage.editBtnOnDocStringOnDocsTab.click();
        });
        step("Добавить файл, сохранить", () -> {
            documentsPage.uploadFileToDocOnEditModal(attachFile1);
            sleep(1500);
            documentsPage.saveBtnOnEditDocModal.click();
        });
        step("Проверить в карточке документа, что добавленный файл сохранился", () -> {
            documentsPage.openDocsCardByDocsNameOnDocsTab(docName);
            documentsPage.checkFileNameInDocsCardApp4(attachFile1);
        });
        step("Удалить тестовый договор", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("Mожно удалить файл Документа ТЗС не в процессе на вкладке Документы договора")
    void canBeDeletedFileInTzsDocumentNoProcessOnDocumentsTabOfContract() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
        });
        step("Открыть окно редактирования по кнопке Редактировать в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.click();
            documentsPage.editBtnOnDocStringOnDocsTab.click();
        });
        step("Добавить файл, сохранить", () -> {
            documentsPage.uploadFileToDocOnEditModal(attachFile1);
            sleep(1000);
            documentsPage.saveBtnOnEditDocModal.click();
            sleep(1500);
        });
        step("Удалить добавленный файл документа, сохранить", () -> {
            documentsPage.firstDocStringOnDocsTab.click();
            documentsPage.editBtnOnDocStringOnDocsTab.click();
            documentsPage.deleteFileByNameOnEditDocModal(attachFile1);
            sleep(1500);
        });
        step("Проверить в карточке документа, что файл удален", () -> {
            documentsPage.openDocsCardByDocsNameOnDocsTab(docName);
            sleep(2000);
            documentsPage.fileNotExistByNameOnDocsCard(attachFile1);
        });
        step("Удалить тестовый договор", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Можно удалить документ ТЗС, который не идет и не шел по процессу согласования, от СА")
    void saCanDeleteTzsDocumentNoProcessOnDocumentsTabOfContractTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
        });
        step("Удалить документ по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.click();
            documentsPage.deleteBtnOnDocStringOnDocsTab.click();
        });
        step("Подтвердить удаление на модальном окне", () -> {
            modalWindowsPage.chooseOnModalBtn.click();
        });
        step("Проверить, что документ удален на вкладке Документы", () -> {
            documentsPage.documentNotExistByNameOnDocsTab(docName);
        });
        step("Удалить тестовый договор", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Нельзя удалить документ ТЗС, который идет по процессу согласования, от СА")
    void saCanNotDeleteTzsDocumentInProcessOnDocumentsTabOfContractTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
            contractsPage.documetsTableInContract.shouldBe(visible, Duration.ofSeconds(5));
        });
        step("Запустить процесс согласования по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.startProcessBtnOnDocStringOnDocsTab.click();
        });
        step("Проверить, что кнопка Удалить документ доступна и не активна", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.deleteBtnOnDocStringOnDocsTab.shouldBe(visible).shouldHave(cssClass("disabled"));
        });
        step("Проверить, что кнопка Удалить документ имеет подсказку 'Нельзя удалять документы, которые отправлялись на согласование' ", () -> {
            documentsPage.deleteBtnOnDocStringOnDocsTab.parent().
                    shouldHave(attribute("title", "Нельзя удалять документы, которые отправлялись на согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Нельзя удалить документ ТЗС, который шел по процессу согласования, от СА")
    void saCanNotDeleteTzsDocumentWasInProcessOnDocumentsTabOfContractTest() {

        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
            contractsPage.documetsTableInContract.shouldBe(visible, Duration.ofSeconds(5));
        });
        step("Запустить процесс согласования по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.startProcessBtnOnDocStringOnDocsTab.click();
        });
        step("Остановить процесс согласования по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.stopProcessBtnOnDocStringOnDocsTab.click();
        });
        step("Проверить, что кнопка Удалить документ доступна и не активна", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.deleteBtnOnDocStringOnDocsTab.shouldBe(visible).shouldHave(cssClass("disabled"));
        });
        step("Проверить, что кнопка Удалить документ имеет подсказку 'Нельзя удалять документы, которые отправлялись на согласование' ", () -> {
            documentsPage.deleteBtnOnDocStringOnDocsTab.parent().
                    shouldHave(attribute("title", "Нельзя удалять документы, которые отправлялись на согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Процесс прекращается по кнопке Прекратить согласование на строке документа в таблице документов договора")
    void processStopsByBtnOnDocStringTest() {
        step("Открыть карточку созданного договора на вкладке Документы", () -> {
            contractsPage.openContractsCardDocsTab(contractsId);
            contractsPage.documetsTableInContract.shouldBe(visible, Duration.ofSeconds(5));
        });
        step("Запустить процесс согласования по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.startProcessBtnOnDocStringOnDocsTab.hover().click();
        });
        step("Остановить процесс согласования по кнопке в строке документа", () -> {
            documentsPage.firstDocStringOnDocsTab.hover();
            documentsPage.stopProcessBtnOnDocStringOnDocsTab.hover().click();
        });
        refresh();
        step("Проверить, что иконка состояния процесса изменилась", () -> {
            contractsPage.stateIconOnDocString
                    .shouldBe(visible)
                    .shouldHave(attribute("title", "Процесс подписания документа прекращен"), Duration.ofSeconds(5));
            contractsPage.stateIconOnDocStringSvgAttrsApp4Checking();
        });
        step("Проверить, что статус документа Отозван ", () -> {
            $(byText(docName)).click();
            documentsPage.statusOfFirstVersionOnDocumentVersionsTableOnMainTabApp4
                    .shouldHave(exactText("Отозван"));
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
//    @Disabled
    @DisplayName("Процесс прекращается по кнопке Прекратить согласование на строке версии документа в карточке документа")
    void processStopsByBtnOnVersionStringTest() {
        step("Открыть карточку созданного документа", () -> {
            documentsPage.openDocsCardMainTabApp4(docId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
            sleep(1000);
        });
        step("Остановить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.documentAuthorColumnOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabEnabledApp4.click();
            modalWindowsPage.confirmBtnOnModalApp4.click();
        });
        step("Проверить, что статус документа Отозван ", () -> {
            documentsPage.statusOfFirstVersionOnDocumentVersionsTableOnMainTabApp4
                    .shouldHave(exactText("Отозван"));
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }


}
