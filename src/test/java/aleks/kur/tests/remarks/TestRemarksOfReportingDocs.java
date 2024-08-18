package aleks.kur.tests.remarks;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверка действия 'Добавить замечание'")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("remarks")})
//@Disabled
public class TestRemarksOfReportingDocs extends TestBaseUi {

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
    @DisplayName("Данные нового замечания сохраняются в таблице замечаний")
    void shouldBeSavedDataOfNewRemarkOnRemarksTableUiTest() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
            mainPage.closeSidebarBtnApp4.click();
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку Замечания, обновить страницу, открыть окно добавления замечания", () -> {
            documentsPage.remarksTabApp4.click();
            refresh();
            documentsPage.addRemarkBtnRemarksTabApp4.click();
        });
        step("Заполнить № раздела", () -> {
            remarksPage.addRemarkModalChapterNumberTextField.setValue(remarksPage.remarksChapterNumber);
        });
        step("Заполнить Название раздела документа", () -> {
            remarksPage.addRemarkModalChapterNameTextField.setValue(remarksPage.remarksChapterName);
        });
        step("Заполнить Замечание", () -> {
            remarksPage.addRemarkModalRemarkTextField.setValue(remarksPage.remarksText);
        });
        step("Заполнить Оригинальная формулировка", () -> {
            remarksPage.addRemarkModalOriginalFormulationBtn.click();
            remarksPage.addRemarkModalOriginalFormulationTextField.setValue(remarksPage.originalFormulationText);
        });
        step("Заполнить Предлагаемая формулировка", () -> {
            remarksPage.addRemarkModalProposedFormulationBtn.click();
            remarksPage.addRemarkModalProposedFormulationTextField.setValue(remarksPage.proposedFormulationText);
        });
        step("Добавить файл", () -> {
            remarksPage.uploadFileToRemarkOnModalApp4(attachFile);
            sleep(1500);
        });
        step("Сохранить", () -> {
            remarksPage.addRemarkModalSaveBtn.click();
        });
        step("Проверить отображение данных в строке таблицы", () -> {
            documentsPage.allStringsBtnRemarksTabApp4.click();
            step("замечание имеет №1", () -> {
                documentsPage.remarkNumberFirstOnTableOnRemarksTabApp4.shouldHave(exactText("1"));
            });
            step("замечание имеет № и название раздела документа", () -> {
                String numberAndNameOfDocumentsChapter = remarksPage.remarksChapterNumber + " " + remarksPage.remarksChapterName;
                documentsPage.remarksChapterFirstOnTableOnRemarksTabApp4.shouldHave(exactText(numberAndNameOfDocumentsChapter));
            });
            step("замечание имеет текст, ориг и предл формулировку", () -> {
                documentsPage.remarkStringFirstOnTableOnRemarksTabApp4
                        .shouldHave(text(remarksPage.remarksText))
                        .shouldHave(text(remarksPage.originalFormulationText))
                        .shouldHave(text(remarksPage.proposedFormulationText));
            });

        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


}
