package aleks.kur.tests.documents;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.ContractsPage;
import ru.progredis.pages.DocumentsPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.SqlRequestsPage.updateDbTableV2LongFieldByObjectId;

@DisplayName("Проверки кнопок на строке версии в карточке документа вкладка 'Основная информация' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsCardMainTab"), @Tag("btnOnVersionString")})
public class TestBtnsOnVersionStringOfReportingDocumentsCardMainTabUi extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String contractsName = contractsPage.contractsName;
    String docName = documentsPage.docName;
    String decimalNumber = documentsPage.decimalNumber;
    String docNameUpdated = documentsPage.docNameUpdated;
    //    int docTypeId = 45; // Техническое задание на систему (ТЗ)
    int
            docTypeId = 48, // "Руководство по организации сопровождения"(code:"ROS")
            fileId = filesPage.uploadFileAndGetIdApi(authCookie, documentsPage.testUploadFile); // загрузить файл и получить id

    @Test
    @DisplayName("Должна быть кнопка 'Загрузить файлы' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeUploadFilesBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Есть кнопка 'Загрузить файлы' с подсказкой 'Загрузить файлы' на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.uploadFilesBtnOnVersionStringOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("title", "Загрузить файлы"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконка кнопки 'Загрузить файлы' должна иметь точные атрибуты svg path на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void uploadFilesBtnShouldHaveExactSvgImgOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Иконка кнопки 'Загрузить файлы' имеет точные атрибуты svg no hover", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.uploadFilesBtnSvgPathAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Открывется окно добавления файлов по кнопке 'Загрузить файлы' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void uploadFilesBtnOpensModalOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Должно открываться окно добавления файлов по кнопке 'Загрузить файлы'", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.uploadFilesBtnOnVersionStringOnMainTabApp4.click();
            modalWindowsPage.headerNameOnModalApp4.shouldHave(exactText("Добавить файлы к версии документа"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть кнопка 'Скачать файлы' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDownloadFilesBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Есть кнопка 'Скачать файлы' с подсказкой 'Загрузить Скачать' на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadFilesBtnOnVersionStringOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("title", "Скачать файлы"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконка кнопки 'Скачать файлы' должна иметь точные атрибуты svg path на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void downloadFilesBtnShouldHaveExactSvgImgOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Иконка кнопки 'Загрузить файлы' имеет точные атрибуты svg no hover", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadFilesBtnSvgPathAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Кнопка 'Скачать файлы' и файл должны иметь ссылку на файл на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void downloadFilesBtnShouldHaveLinksToFilesVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Кнопка 'Скачать файлы' и файл имеет ссылку на скачиваение файла", () -> {
            String linkToFile = documentsPage.firstFileOfFirstVersionOnDocumentVersionsTableOnMainTabApp4.getAttribute("href");
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            String linkToFileInDownloadFilesBtn = documentsPage.downloadFilesBtnOnVersionStringOnMainTabApp4.getAttribute("href");
            String linkToDownloadFileWithUrl = BASE_URL + "/download?files=" + documentsPage.getFileIdFromFilesLinkOfFirstVersionOnDocumentVersionsTableOnMainTabApp4();
            System.out.println(
                    "linkToFile = " + linkToFile
                            + ", linkToFileInDownloadFilesBtn = " + linkToFileInDownloadFilesBtn
                            + ", linkToDownloadFileWithUrl = " + linkToDownloadFileWithUrl);
            assertThat(linkToFile).isEqualTo(linkToFileInDownloadFilesBtn);
            assertThat(linkToFile).isEqualTo(linkToDownloadFileWithUrl);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть кнопка 'Скачать документ для печати' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDownloadRegisteredFilesBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Есть кнопка 'Скачать документ для печати' с подсказкой 'Документ для печати будет доступен после регистрации' на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("title", "Документ для печати будет доступен после регистрации"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть не активна кнопка 'Скачать документ для печати' для не зарегистрированных документов на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDisabledDownloadRegisteredFilesBtnForUnregisteredDocOnVersionStringApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Кнопка 'Скачать документ для печати' неактивна на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4
//                    .shouldHave(attribute("data-disabled")) // хотфикс
                    .shouldHave(attribute("title", "Документ для печати будет доступен после регистрации"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконка кнопки 'Скачать документ для печати' должна иметь точные атрибуты svg на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void downloadRegisteredFilesBtnShouldHaveExactSvgImgOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Иконка кнопки 'Загрузить файлы' имеет точные атрибуты svg no hover", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRegisteredFilesBtnSvgAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть кнопка 'Скачать перечень замечаний' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDownloadRemarksBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Есть кнопка 'Скачать перечень замечаний' с подсказкой 'Данная версия не отправлялась на согласование' на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRemarksBtnOnVersionStringOnMainTabApp4.shouldBe(visible)
                    .shouldHave(attribute("title", "Данная версия не отправлялась на согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть неактивна кнопка 'Скачать перечень замечаний', если версия документа еще не отправлялась на согласование, на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDisabledDownloadRemarksBtnForUnprocessedDocOnVersionStringApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Кнопка 'Скачать перечень замечаний' неактивна на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRemarksBtnOnVersionStringOnMainTabApp4
//                    .shouldHave(attribute("data-disabled")) // хотфикс
                    .shouldHave(attribute("title", "Данная версия не отправлялась на согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть активна кнопка 'Скачать перечень замечаний', если версия документа отправлялась на согласование, на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeEnabledDownloadRemarksBtnForProcessedDocOnVersionStringApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Проверить, что кнопка 'Скачать перечень замечаний' активна", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRemarksBtnOnVersionStringOnMainTabApp4.shouldBe(visible).shouldNotHave(attribute("disabled"), Duration.ofSeconds(10));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконка кнопки 'Скачать перечень замечаний' должна иметь точные атрибуты svg на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void downloadRemarksBtnShouldHaveExactSvgImgOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Иконка кнопки 'Загрузить файлы' имеет точные атрибуты svg no hover", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.downloadRemarksBtnSvgAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Активная кнопка 'Скачать перечень замечаний' должна иметь ссылку на скачивание замечаний на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDownloadLinkOnEnabledDownloadRemarksBtnForProcessedDocOnVersionStringApp4Test() {
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
            refresh();
        });
        step("Проверить, что кнопка 'Скачать перечень замечаний' имеет ссылку на скачивание замечаний", () -> {
            String href = BASE_URL + "/app4/remark/export?documentId=" + docId + "&documentVersion=1&exportType=EXCEL&allPermissions=true";
            documentsPage.downloadRemarksBtnOnVersionStringOnMainTabApp4.
                    shouldHave(attribute("href", href), Duration.ofSeconds(10));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @Tags({@Tag("sql")})
    @DisplayName("Должна быть кнопка 'Начать согласование' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Disabled
    void shouldBeStartProcessBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractPpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        String docId = documentsPage.getDocIdFromDocsCard();
        int contractsId = documentsPage.getContractIdByDocIdApiApp4(authCookie, Integer.parseInt(docId));
        step("Стартовать процесс по тестовому документу по api", () -> {
            documentsPage.startProcessOfReportingDocumentApi(authCookie, Integer.parseInt(docId));
        });
        refresh();
        step("Остановить процесс", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabEnabledApp4.click();
            modalWindowsPage.confirmBtnOnModalApp4.click();
        });
        // нужна комиссия по приемке, чтобы получить активную кнопку с подсказкой
        // по id договора взять id document_kit
        int firstDocKitId = ContractsPage.firstDocKitIdInContractApi(authCookie, contractsId);
        System.out.println("firstDocKitId = " + firstDocKitId);
        // добавить agreement_commission_id=11 (любая тестовая с state=FORMED) по id document_kit в таблицу БД document_kit
        String
                tableName = "document_kit",
                tableFieldName = "agreement_commission_id";
        Long tableFieldValue = 11L;  // значение нужного поля
        updateDbTableV2LongFieldByObjectId(tableName, tableFieldName, tableFieldValue, (long) firstDocKitId);
        step("Добавить новую версию тестовому документу по api", () -> {
            int newDocVersionId = DocumentsPage.createNewDocumentVersionAndGetItsIdApi(authCookie, Integer.parseInt(docId), fileId);
        });
        refresh();
        step("Есть кнопка 'Начать согласование' с подсказкой 'Начать согласование' на строке новой версии", () -> {
            documentsPage.secondVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnSecondVersionStringOnMainTabApp4.shouldBe(enabled)
                    .shouldHave(attribute("title", "Начать согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Kнопка 'Начать согласование' активна до старта процесса на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeEnabledStartProcessBtnBeforeStartOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Кнопка 'Начать согласование' активна", () -> {
//            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.shouldNotHave(attribute("disabled"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Kнопка 'Начать согласование' неактивна после старта процесса на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeDisabledStartProcessBtnAfterStartOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Кнопка 'Начать согласование' неактивна", () -> {
            documentsPage.startProcessBtnDisabledIconOnVersionStringOnMainTabApp4.should(exist);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должна быть кнопка 'Прекратить согласование' на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void shouldBeStopProcessBtnOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Кнопка 'Прекратить согласование' должна быть неактивной и с подсказкой 'Данная версия документа не отправлялась на согласование' на строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabDisabledApp4.shouldBe(visible)
                    .shouldHave(attribute("title", "Данная версия документа не отправлялась на согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Кнопка остановки процесса должна иметь подсказку 'Прекратить согласование' документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void stopProcessBtnShouldHaveTitleStopProcessOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractPpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        String docId = documentsPage.getDocIdFromDocsCard();
        step("Стартовать процесс по тестовому документу по api", () -> {
            documentsPage.startProcessOfReportingDocumentApi(authCookie, Integer.parseInt(docId));
        });
        refresh();
        step("Кнопка имеет подсказку 'Прекратить согласование'", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabEnabledApp4
                    .shouldHave(attribute("title", "Прекратить согласование")); // release
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Модальное окно 'Прекратить согласование' должно иметь необходимые элементы для документа апп4 группы Отчетные")
    @Tags({@Tag("wiremock")})
//    @Disabled
    void stopProcessModalShouldHaveValidElementsApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        String docId = documentsPage.getDocIdFromDocsCard();

        step("Стартовать процесс по тестовому документу", () -> {
            documentsPage.startProcessOfReportingDocumentApi(authCookie, Integer.parseInt(docId));
            sleep(1500);
        });

        step("Обновить страницу", Selenide::refresh);

        step("Вызвать модальное окно 'Прекратить согласование' по кнопке остановки процесса", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabEnabledApp4.click();
        });

        step("Есть кнопка Отмена и Продолжить на окне 'Прекратить согласование'", () -> {
            modalWindowsPage.cancelBtnOnModalApp4.shouldBe(visible);
            modalWindowsPage.confirmBtnOnModalApp4.shouldBe(visible);
        });

        step("Имя заголовка и описание соответствуют требованиям на окне 'Прекратить согласование'", () -> {
            modalWindowsPage.headerNameOnModalApp4.shouldHave(exactText(modalWindowsPage.stopProcessModalHeaderNameApp4));
            modalWindowsPage.descriptionOnModalApp4.shouldHave(exactText(modalWindowsPage.stopProcessModalDiscriptionApp4));
        });

        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


    @Test
    @DisplayName("Кнопка 'Прекратить согласование' должна быть неактивной и иметь подсказку 'Версия документа уже отозвана' после остановки процесса для документа апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void stopProcessBtnShouldBeDisabledWithExactTitleOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        String docId = documentsPage.getDocIdFromDocsCard();
        step("Стартовать процесс по тестовому документу по api", () -> {
            documentsPage.startProcessOfReportingDocumentApi(authCookie, Integer.parseInt(docId));
        });
        refresh();
        step("Остановить процесс", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnOnVersionStringOnMainTabEnabledApp4.click();
            modalWindowsPage.confirmBtnOnModalApp4.click();
        });
        step("Кнопка 'Прекратить согласование' должна быть неактивной с подсказкой 'В настоящее время документ не проходит согласование'", () -> {
            documentsPage.stopProcessBtnOnVersionStringOnMainTabDisabledApp4
//                    .shouldHave(attribute("data-disabled"), Duration.ofSeconds(10)) // для хотфикс
                    .shouldHave(attribute("title", "В настоящее время документ не проходит согласование"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконка кнопки 'Прекратить согласование' должна иметь точные атрибуты svg на строке версии на вкладке 'Основная информация' апп4 группы Отчетные")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("btnOnVersionString")})
//    @Disabled
    void stopProcessBtnShouldHaveExactSvgImgOnVersionStringOnDocumentVersionsTableApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Иконка кнопки 'Прекратить согласование' имеет точные атрибуты svg no hover", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.stopProcessBtnSvgAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Disabled
    @Test
    @DisplayName("Можно удалить файл документа, который не был на согласовании, на вкладке 'Основная информация' апп4 группы Отчетные")
    void canBeDeletedFileOfNotProcessedReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
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
