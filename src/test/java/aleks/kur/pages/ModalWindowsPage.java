package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ModalWindowsPage {
    public SelenideElement

            chooseOnModalBtn = $(".modal-footer .btn-success"), // кнопка Выбрать/ Продолжить (подтвердить выбор) на футере модалки

    firstRadioElementOnModal = $(".modal-content .pi-radio-btn"), // первая радио-кнопка в списке
            firstCheckboxElementOnModal = $("tbody .pi-btn-checkbox"), // первый чекбокс в списке
            firstItemsShortNameOnModal = $("tbody .k-cell-vertical_middle").sibling(0), // краткое имя первого элемента в списке
            secondItemsShortNameOnModal = $("tbody .k-cell-vertical_middle", 1).sibling(0), // краткое имя первого элемента в списке


    // Окно выбора "Кол-во ФЗ" для строки Спецификации Оборудования/ СПО
    specificationsFistRequestedQuantityField = $(".k-numerictextbox").parent().parent(), // Запрошенное количество первого подразделения
//            specificationsFirstRequestedQuantityInputField = $(".k-numerictextbox.k-fke-input"), // Input Запрошенное количество первого подразделения
            specificationsFirstRequestedQuantityInputField = $("[name='requestedQuantity']"), // Input Запрошенное количество первого подразделения


    // Окно Сообщение о запуске процесса
    processStartedCloseWindowBtn = $("button.close"), // кнопка закрыть окно - крестик

    // модальное окно подтверждений апп4
    headerNameOnModalApp4 = $(".MuiDialog-container h2>div"), // заголовок модального окна окна
            descriptionOnModalApp4 = $("div.MuiDialogContent-root p"), // заголовок модального окна окна
            cancelBtnOnModalApp4 = $("button[data-testid='@app4/document-card/summary/main-info-grid/actions/cancel']"), // Отмена
            confirmBtnOnModalApp4 = $("button[data-testid='@app4/document-card/summary/main-info-grid/actions/confirm']"), // кнопка Продолжить
            stopProcessModalApp4 = $("div[data-testid='@app4/document-card/summary/main-info-grid/stop-approving-process-dialog']"), // модальное окно 'Прекратить согласование'


    // Окно выбора чек-боксов в таблице апп4
    checkBoxTableModal = $(".MuiDialog-container>.MuiPaper-root"), // окно чек-боксов в таблице
            checkBoxTableModalHeader = checkBoxTableModal.$("h2")// заголовок окно чек-боксов в таблице


                    ;

    final public String stopProcessModalHeaderNameApp4 = "Прекратить согласование"; // заголовок модалки 'Прекратить согласование'
    final public String stopProcessModalDiscriptionApp4 = "После прекращения согласования потребуется загрузить новую версию документа и начать согласование заново. Продолжить?"; // описание модалки 'Прекратить согласование'

    // Окно Добавление документов
    // добавить док Заявка
    public void addRequestDocumentOnModal(String attachFile) {
        $(".modal-content .form-control").shouldBe(enabled, Duration.ofSeconds(3)).setValue("autoTest document Заявка");
        $(".modal-body .k-input").click();
        $x("//li/span[text() = 'Заявка']").shouldBe(enabled, Duration.ofSeconds(3)).click();
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").shouldBe(enabled, Duration.ofSeconds(3)).click();
    }

    // добавить док Функциональные требования
    public void addFunctionalRequirmentsDocumentOnModal(String attachFile) {
        $(".modal-content .form-control").setValue("autoTest document Функциональные требования");
        $(".modal-body .k-input").click();
        $x("//li/span[text() = 'Функциональные требования']").click();
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").click();
    }

    public boolean processStartedModal() {
        processStartedCloseWindowBtn.should(exist);
//       processStartedCloseWindowBtn.shouldBe(visible);
        return true;
    }


}
