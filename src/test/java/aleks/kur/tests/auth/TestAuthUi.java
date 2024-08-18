package aleks.kur.tests.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tags({@Tag("ui"), @Tag("auth")})
@DisplayName("Проверки аутентификации через Ui")
public class TestAuthUi extends TestBaseUi {

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа/выхода пользователя с валидными данными через UI")
    void loginOutValidDataUITest() {

//        System.setProperty("userSet", "userTechP");

        step("При входе есть форма авторизации, пользователь может авторизоваться по Логин (e-mail) и паролю", () -> {
            open("");
            assertThat(url()).contains("login");
            authUiPage.loginUi(login, passwd);
        });
        step("Имя пользователя в заголовке соответствует залогированной персоне", () -> {
            mainPage.userNameIsEqualToLoginPerson(userShortName);
        });
        step("Есть страница login после выхода из учетной записи пользователя", () -> {
            mainPage.userLogout();
            authUiPage.loginField.shouldBe(visible, Duration.ofSeconds(5));
            assertThat(url()).contains("login");
        });

    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа пользователя с невавалидным логином через UI")
    void authInvalidLoginDataUiTest() {
        step("Ввести в форму авторизации невалидный Логин (e-mail)", () -> {
            open("/");
            authUiPage.inputLogin(login + "Invalid");
            authUiPage.inputPasswd(passwd);
            authUiPage.clickLoginBtn();
        });
        step("Проверить, что есть модальное окно с ошибкой авторизации", () -> {
            authUiPage.authErrorModalHeader.shouldHave(exactText("Ошибка"));
            authUiPage.authErrorModalNotification.shouldHave(exactText("Неверная пара логин/пароль"));
        });
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Тест входа пользователя с невавалидным паролем через UI")
    void authInvalidPassDataUiTest() {
        step("Ввести в форму авторизации невалидный пароль", () -> {
            open("/");
            authUiPage.inputLogin(login);
            authUiPage.inputPasswd(passwd + "Invalid");
            authUiPage.clickLoginBtn();
        });
        step("Проверить, что есть модальное окно с ошибкой авторизации", () -> {
            authUiPage.authErrorModalHeader.shouldHave(exactText("Ошибка"));
            authUiPage.authErrorModalNotification.shouldHave(exactText("Неверная пара логин/пароль"));
        });
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Модальное окно ошибки авторизации закрывается после подтверждения")
    void authErrorModalShouldCloseUiTest() {
        step("Ввести в форму авторизации невалидные данные", () -> {
            open("");
            authUiPage.inputLogin(login + "Invalid");
            authUiPage.inputPasswd(passwd + "Invalid");
            authUiPage.clickLoginBtn();
        });
        step("Закрыть модальное окно с ошибкой кнопкой Крестик", () -> {
            authUiPage.authErrorModalCrossBtn.click();
        });
    }

}
