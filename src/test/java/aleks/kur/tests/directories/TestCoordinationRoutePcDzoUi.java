package aleks.kur.tests.directories;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static ru.progredis.helpers.DriverSettings.BASE_URL;

@Tags({@Tag("ui"), @Tag("directories"), @Tag("pcDzo"), @Tag("regress")})
@DisplayName("Проверка справочника Маршруты согласования ПЦ ДЗО ui")
public class TestCoordinationRoutePcDzoUi extends TestBaseUi {
    String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @BeforeEach
    public void authorization() {
        authApi.authorizationOnUibyApiPure(authCookie);
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Есть справочник Маршруты согласования ПЦ ДЗО в группе справочников Документы")
    void shouldBeCoordinationRoutePcDzoInDocementsGroupeUiTest() {
        step("Перейти на страницу Cправочники", () -> {
            open("/" + directoriesPage.directoriesPageLink);
        });
        step("В группе справочников Документы есть справочник Маршруты согласования ПЦ ДЗО", () -> {
            directoriesPage.coordinationRoutePcDzoDirectory.scrollIntoView(true).shouldBe(visible);
        });
        step("справочник Маршруты согласования ПЦ ДЗО имеет ссылку на страницу Маршруты согласования ПЦ ДЗО", () -> {
            directoriesPage.coordinationRoutePcDzoDirectory
                    .shouldHave(attribute("href", BASE_URL + "/#?page=reference&subpage=dzo-directions"));
        });
    }

}
