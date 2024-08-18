package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class AuthUiPage {

    public SelenideElement
            // Модальное окно ошибки авторизации
    loginField = $(byXpath("//*[contains(@id, 'inputEmail')]")),
    loginFieldV4 = $("#email"),
    passwordField = $(byXpath("//*[contains(@id, 'inputPassword')]")),
    passwordFieldV4 = $("#password"),
    authErrorModalHeader = $(".modal-header h3"),
    authErrorModalNotification = $(".modal-body p"),
    authErrorModalCrossBtn = $(".modal-header button"),
    authErrorModalOkBtn = $(".modal-footer button"),
    // Хэдер
    logOutBtnOnHeader = $(".MuiToolbar-root [title='Выход']")
            ;

    // метод для ввода логина
    public void inputLogin(String login) {
        loginField.shouldBe(enabled).setValue(login);
    }
    public void inputLoginV4(String login) {
        loginFieldV4.shouldBe(enabled).setValue(login);
    }

    // метод для ввода пароля
    public void inputPasswd(String password) {
        passwordField.setValue(password);
    }
    public void inputPasswdV4(String password) {
        passwordFieldV4.setValue(password);
    }

    //метод для осуществления нажатия кнопки входа в аккаунт
    public AuthUiPage clickLoginBtn() {
        //loginBtn.click();
        $(byCssSelector("*[type='submit']")).shouldBe(enabled).click();
        return this;
    }

    public AuthUiPage clickLoginBtnV4() {
        //loginBtn.click();
        $("[data-testid='@app4/sign-in-dialog/actions/confirm']").shouldBe(enabled).click();
        return this;
    }

    public void loginUi(String login, String password) {
//        assertThat(url()).contains("login");
        inputLogin(login);
        inputPasswd(password);
        clickLoginBtn();
    }

    public void loginUiV4(String login, String password) {
//        assertThat(url()).contains("login");
        inputLoginV4(login);
        inputPasswdV4(password);
        clickLoginBtnV4();
    }

}
