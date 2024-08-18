package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RemarksPage {

    public String
            remarksChapterNumber = "999",
            remarksChapterName = "Название раздела документа в замечании test",
            remarksText = "Текст замечания к документу test",
            originalFormulationText = "Текст Оригинальная формулировка в замечании test",
            proposedFormulationText = "Текст Предлагаемая формулировка в замечании test"
                    ;

    public SelenideElement

            // Окно добавления замечания апп4
            addRemarkModal = $("[data-testid='@document-card/tab-remarks/add-remark-dialog'] .MuiDialog-paper"), // Окно добавления замечания
            addRemarkModalHeader = addRemarkModal.$("h2"), // заголовок Окно добавления замечания
            addRemarkModalInfoPanel = $("[data-testid='@document-card/tab-remarks/remark-dialog/header']"), // информационная панель Окно добавления замечания
            addRemarkModalChapterNumber = $(byText("№ раздела док-та:")).parent(), // № раздела Окно добавления замечания
            addRemarkModalChapterNumberTextField = $("input#documentSectionId"), // № раздела тескт Окно добавления замечания
            addRemarkModalChapterName = $(byText("Название раздела документа:")).parent(), // Название раздела документа: Окно добавления замечания
            addRemarkModalChapterNameTextField = $("input#documentSectionName"), // Название раздела документа текст Окно добавления замечания
            addRemarkModalRemarkText = $(byText("Замечание:")).parent(), // Замечание Окно добавления замечания
            addRemarkModalRemarkTextField = $("textarea#remarkText"), // Замечание Окно добавления замечания
            addRemarkModalOriginalFormulationBtn = $x("//button[text() = 'Оригинальная формулировка:']"), // кнопка поля Оригинальная формулировка Окно добавления замечания
            addRemarkModalOriginalFormulation = addRemarkModalOriginalFormulationBtn.parent().parent().parent(), // Оригинальная формулировка Окно добавления замечания
            addRemarkModalOriginalFormulationTextField = $("textarea#originalWording"), // поле Оригинальная формулировка Окно добавления замечания
            addRemarkModalProposedFormulationBtn = $x("//button[text() = 'Предлагаемая формулировка:']"), // кнопка Предлагаемая формулировка Окно добавления замечания
            addRemarkModalProposedFormulation = addRemarkModalProposedFormulationBtn.parent().parent().parent(), // Предлагаемая формулировка Окно добавления замечания
            addRemarkModalProposedFormulationTextField = $("textarea#proposedWording"), // поле Предлагаемая формулировка Окно добавления замечания
            addRemarkModalUploadFilesBtn = $("[data-testid='@app4/document-card/remarks/remark-list-with-handling/actions/upload-button']"), // кнопка выбора файлов Окно добавления замечания
            addRemarkModalCancelBtn = $("[data-testid='@document-card/tab-remarks/add-remark-dialog/actions/cancel']"), // кнопка Отмена Окно добавления замечания
            addRemarkModalSaveAndAddNextBtn = $("[data-testid='@document-card/tab-remarks/add-remark-dialog/actions/submit-next']"), // кнопка Сохранить и добавить следующее замечание Окно добавления замечания
            addRemarkModalSaveBtn = $("[data-testid='@document-card/tab-remarks/add-remark-dialog/actions/submit']") // кнопка Сохранить Окно добавления замечания
                    ;

        // Добавить файл в замечание в модальном окне app4
    public void uploadFileToRemarkOnModalApp4(String attachFile) {
        addRemarkModalUploadFilesBtn.$("input").uploadFromClasspath("files/" + attachFile);
    }


}
