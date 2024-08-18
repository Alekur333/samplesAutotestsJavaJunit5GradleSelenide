package aleks.kur.tests.mainPage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.progredis.pages.PcDzoPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@Tags({@Tag("ui"), @Tag("mainPage")})
@DisplayName("Проверки элементов главной страницы (боковое меню, заголовок)")
public class TestMainPageByRolesUi extends TestBaseUi {

        @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" видит раздел 'ПЦ ДЗО' на боковом меню апп2 ui")
        @CsvSource(textBlock = PcDzoPage.validRolesCanSeePcDzoOnSideBarMenu)
        @Tags({@Tag("smoke"), @Tag("pcDzo")})
        void sideBarMenuContainsPcDzoForValidRolesV2UiTest(String loginUser, String passwdUser, String role) {

            step("Авторизоваться от валидной роли и открыть приложение апп2", () -> {
                open("");
                authUiPage.loginUi(loginUser, passwdUser);
            });

            step("Должен быть раздел 'ПЦ ДЗО' на боковом меню", () -> {
                mainPage.sideMenuBlock.shouldHave(text("ПЦ ДЗО"));
            });
        }

        @ParameterizedTest(name = "Тест #{index} -> Валидная роль \"" + "{2}" + "\" видит раздел 'ПЦ ДЗО' на боковом меню апп4 ui")
        @CsvSource(textBlock = PcDzoPage.validRolesCanSeePcDzoOnSideBarMenu)
        @Tags({@Tag("smoke"), @Tag("pcDzo")})
        void sideBarMenuContainsPcDzoForValidRolesV4UiTest(String loginUser, String passwdUser, String role) {

            step("Авторизоваться от валидной роли и открыть приложение апп4", () -> {
                open("/v4");
                authUiPage.loginUiV4(loginUser, passwdUser);
            });

            step("Должен быть раздел 'ПЦ ДЗО' на боковом меню", () -> {
                mainPage.sideMenuBlock.shouldHave(text("ПЦ ДЗО"));
            });
        }

        @ParameterizedTest(name = "Тест #{index} -> Невалидная роль \"" + "{2}" + "\" не видит раздел 'ПЦ ДЗО' на боковом меню апп2 ui")
        @CsvSource(textBlock = PcDzoPage.invalidRolesCanNotSeePcDzoOnSideBarMenu)
        @Tags({@Tag("smoke"), @Tag("pcDzo")})
        void sideBarMenuNotContainsPcDzoForInvalidRolesV2UiTest(String loginUser, String passwdUser, String role) {

            step("Авторизоваться от валидной роли и открыть приложение апп2", () -> {
                open("");
                authUiPage.loginUi(loginUser, passwdUser);
            });

            step("Не должно быть раздела 'ПЦ ДЗО' на боковом меню", () -> {
                mainPage.sideMenuBlock.shouldNotHave(text("ПЦ ДЗО"));
            });
        }

        @ParameterizedTest(name = "Тест #{index} -> Невалидная роль \"" + "{2}" + "\" не видит раздел 'ПЦ ДЗО' на боковом меню апп4 ui")
        @CsvSource(textBlock = PcDzoPage.invalidRolesCanNotSeePcDzoOnSideBarMenu)
        @Tags({@Tag("smoke"), @Tag("pcDzo")})
        void sideBarMenuNotContainsPcDzoForInvalidRolesV4UiTest(String loginUser, String passwdUser, String role) {

            step("Авторизоваться от валидной роли и открыть приложение апп4", () -> {
                open("/v4");
                authUiPage.loginUiV4(loginUser, passwdUser);
            });

            step("Не должно быть раздела 'ПЦ ДЗО' на боковом меню", () -> {
                mainPage.sideMenuBlock.shouldNotHave(text("ПЦ ДЗО"));
            });
        }

}
