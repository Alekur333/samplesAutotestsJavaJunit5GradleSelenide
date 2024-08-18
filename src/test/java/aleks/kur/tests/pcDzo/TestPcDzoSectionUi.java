package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.UsersPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.PcDzoPage.*;

@Tags({@Tag("ui"), @Tag("pcDzo"), @Tag("regress")})
@DisplayName("Проверки раздела ПЦ ДЗО")
public class TestPcDzoSectionUi extends TestBaseUi {

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);


    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователь в роли \"" + "{2}" + "\" при переходе в раздел ПЦ ДЗО попадает в карточку своего ДЗО ui")
    @CsvSource(textBlock = UsersPage.respDzoTechnoprom1)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void dzoResponsibleRoleRedirectedToHisDzoCardFromPcDzoSectionUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли 'Ответственный от ДЗО'", () -> {
            open("/v4/documents-and-templates");
            authUiPage.logOutBtnOnHeader.click();
            refresh();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Перейти в раздел 'ПЦ ДЗО' на боковом меню", () -> {
            mainPage.sideMenuPcDzo.shouldBe(visible, Duration.ofSeconds(5)).click();
        });
        step("Должна открыться карточка ДЗО организации пользователя", () -> {
            Response testUser = usersPage.getUserByLoginApi(authCookie, loginUser);
            String userOrg = testUser.then().extract().path("organization.name[0]");
            step("В заголовке имя организации пользователя", () -> {
                pcDzoPage.headerNameLocatorPcDzoCard
                        .shouldHave(text(userOrg));
            });
            step("Пользователь есть в списке 'Ответственные от ДЗО'", () -> {
                pcDzoPage.respDzoBlockLocatorPcDzoCard.sibling(0)
                        .shouldHave(text(loginUser));
            });
        });
    }

//    @Disabled
    @Test
    @DisplayName("Проверка элементов хэдера списка ПЦ ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
    void shouldBeElementsOnPcDzoSectionHeaderUiTest() {

        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            open("/v4/dzo");
        });
        step("Есть кнопка фильтров", () -> {
            pcDzoPage.filtersBtnOnPcDzoPage.shouldBe(visible);
        });
        step("Есть форма поиска", () -> {
            pcDzoPage.searchFormOnPcDzoPage.shouldBe(visible);
        });
        step("Есть фильтр по году", () -> {
            pcDzoPage.yearFilterOnPcDzoPage.shouldBe(visible);
            pcDzoPage.yearFilterOnPcDzoPage.shouldHave(text("Год"));
            pcDzoPage.yearFilterOnPcDzoPage.shouldHave(text(String.valueOf(currentYear)));
        });
        step("Есть кнопка Отчеты", () -> {
            pcDzoPage.reportsBtnOnPcDzoPage.shouldBe(visible);
        });
        step("Есть кнопка Экспорт в Excel", () -> {
            pcDzoPage.excelExportBtnOnPcDzoPage.shouldBe(visible);
        });
        step("Есть кнопка Действия", () -> {
            pcDzoPage.actionsBtnOnPcDzoPage.shouldBe(visible);
        });
    }

//    @Disabled
    @Test
    @DisplayName("Проверка элементов окна кнопки 'Отчеты' хэдера списка ПЦ ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
    void shouldBeElementsOnReportsBtnWindowOfPcDzoSectionHeaderUiTest() {

        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            open("/v4/dzo");
        });
        step("Открыть окно выбора по кнопке Отчеты", () -> {
            pcDzoPage.reportsBtnOnPcDzoPage.click();
        });
        step("Есть заголовок", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldBe(visible);
            pcDzoPage.btnMenuWindowOnPcDzoPage.$("li", 0).shouldHave(exactText("Отчеты"));
        });
        step("Есть пункт 'Отчет о ходе согласования ПЦ ДЗО'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Отчет о ходе согласования ПЦ ДЗО"));
        });
        step("Есть пункт 'Отчет о ходе согласования корректировок ПЦ ДЗО'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Отчет о ходе согласования корректировок ПЦ ДЗО"));
        });
        step("Есть пункт 'Отчет о ходе согласования отчетов о выполнении ПЦ ДЗО'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Отчет о ходе согласования отчетов о выполнении ПЦ ДЗО"));
        });
    }

//    @Disabled
    @Test
    @DisplayName("Проверка элементов окна кнопки 'Экспорт в Excel' хэдера списка ПЦ ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
    void shouldBeElementsOnExcelExportBtnWindowOfPcDzoSectionHeaderUiTest() {

        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            open("/v4/dzo");
        });
        step("Открыть окно выбора по кнопке 'Экспорт в Excel'", () -> {
            pcDzoPage.excelExportBtnOnPcDzoPage.click();
        });
        step("Есть заголовок", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldBe(visible);
            pcDzoPage.btnMenuWindowOnPcDzoPage.$("li", 0).shouldHave(exactText("Экспорт"));
        });
        step("Есть пункт 'Все'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Все"));
        });
        step("Есть пункт 'Видимое'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Видимое"));
        });
    }

//    @Disabled
    @Test
    @DisplayName("Проверка элементов окна кнопки 'Действия' хэдера списка ПЦ ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
    void shouldBeElementsOnActionsBtnWindowOfPcDzoSectionHeaderUiTest() {

        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            open("/v4/dzo");
        });
        step("Открыть окно выбора по кнопке 'Действия'", () -> {
            pcDzoPage.actionsBtnOnPcDzoPage.click();
        });
        step("Есть пункт 'Запросить загрузку ПЦ'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Запросить загрузку ПЦ"));
        });
        step("Есть пункт 'Запросить загрузку отчетов за 1 полугодие'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Запросить загрузку отчетов за 1 полугодие"));
        });
        step("Есть пункт 'Запросить загрузку отчетов за год'", () -> {
            pcDzoPage.btnMenuWindowOnPcDzoPage.shouldHave(text("Запросить загрузку отчетов за год"));
        });
    }

    @Test
    @DisplayName("Должно быть точное количество колонок в таблице 'ПЦ дочерних и зависимых обществ' ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void pcDzoListTableShouldHaveExactNumberOfColumnsTest() {
        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            open("/v4/dzo");
        });
        step("В таблице должно быть 13 колонок", () -> {
            pcDzoPage.columnsOnDzoTables.shouldHave(size(13), Duration.ofSeconds(5));
        });
    }

    // MethodSource
    static Stream<Arguments> pcDzoListTableShouldHaveExactNamesOfColumnsTest() {
        step("Перейти в раздел 'ПЦ ДЗО'", () -> {
            authApi.authorizationApiOnUiApp4();
            open("/v4/dzo");
        });
        step("Собрать данные для проверки", () -> {
        });
        return Stream.of(
                Arguments.of("1", pcDzoPage.column1NameOnPcDzoListTable, pcDzoPage.column1HeaderOnPcDzoListTable.getText()),
                Arguments.of("2", pcDzoPage.column2NameOnPcDzoListTable, pcDzoPage.column2HeaderOnPcDzoListTable.getText()),
                Arguments.of("3", pcDzoPage.column3NameOnPcDzoListTable, pcDzoPage.column3HeaderOnPcDzoListTable.getText()),
                Arguments.of("4", pcDzoPage.column4NameOnPcDzoListTable, pcDzoPage.column4HeaderOnPcDzoListTable.getText()),
                Arguments.of("5_6", pcDzoPage.column5_6NameOnPcDzoListTable, pcDzoPage.column5_6HeaderOnPcDzoListTable.getText()),
                Arguments.of("5", pcDzoPage.column5NameOnPcDzoListTable, pcDzoPage.column5HeaderOnPcDzoListTable.getText()),
                Arguments.of("6", pcDzoPage.column6NameOnPcDzoListTable, pcDzoPage.column6HeaderOnPcDzoListTable.getText()),
                Arguments.of("7_8", pcDzoPage.column7_8NameOnPcDzoListTable, pcDzoPage.column7_8HeaderOnPcDzoListTable.getText()),
                Arguments.of("7", pcDzoPage.column7NameOnPcDzoListTable, pcDzoPage.column7HeaderOnPcDzoListTable.getText()),
                Arguments.of("8", pcDzoPage.column8NameOnPcDzoListTable, pcDzoPage.column8HeaderOnPcDzoListTable.getText()),
                Arguments.of("9_10", pcDzoPage.column9_10NameOnPcDzoListTable, pcDzoPage.column9_10HeaderOnPcDzoListTable.getText()),
                Arguments.of("9", pcDzoPage.column9NameOnPcDzoListTable, pcDzoPage.column9HeaderOnPcDzoListTable.getText()),
                Arguments.of("10", pcDzoPage.column10NameOnPcDzoListTable, pcDzoPage.column10HeaderOnPcDzoListTable.getText()),
                Arguments.of("11_12", pcDzoPage.column11_12NameOnPcDzoListTable, pcDzoPage.column11_12HeaderOnPcDzoListTable.getText()),
                Arguments.of("11", pcDzoPage.column11NameOnPcDzoListTable, pcDzoPage.column11HeaderOnPcDzoListTable.getText()),
                Arguments.of("12", pcDzoPage.column12NameOnPcDzoListTable, pcDzoPage.column12HeaderOnPcDzoListTable.getText()),
                Arguments.of("13", pcDzoPage.column13NameOnPcDzoListTable, pcDzoPage.column13HeaderOnPcDzoListTable.getText())
        );
    }
    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> Колонка № {0} имеет требуемое имя \"{1}\" в таблице 'ПЦ дочерних и зависимых обществ' ui")
//    @DisplayName("Проверка имен колонок в таблице 'ПЦ дочерних и зависимых обществ' ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void pcDzoListTableShouldHaveExactNamesOfColumnsTest(String columnNumber, String expectedName, String actualName) {

        step("Ожидаемое имя колонки № " + columnNumber + " равняется фактическому из таблицы", () -> {
            assertEquals(expectedName, actualName);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователю в валидной роли \"{2}\" доступна кнопка \"Действия\" в разделe \"ПЦ дочерних и зависимых обществ\" ui")
    @CsvSource(textBlock = validRolesCanUseActionsBtnInPcDzoInSection)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void actionsBtnIsActiveForValidRolesInPcDzoSectionUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли", () -> {
//            open("/v4/documents-and-templates");
            open("/v4/dzo");

            authUiPage.logOutBtnOnHeader.click();
            refresh();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Должна быть активна кнопка 'Действия'", () -> {
            pcDzoPage.actionsBtnOnPcDzoPage.shouldBe(enabled);
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователю в невалидной роли \"{2}\" не доступна кнопка \"Действия\" в разделe \"ПЦ дочерних и зависимых обществ\" ui")
    @CsvSource(textBlock = invalidRolesCantUseActionsBtnInPcDzoInSection)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void invalidRolesCantUseActionsBtnInPcDzoSectionUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли и зайти в раздел ПЦ ДЗО", () -> {
//            open("/v4/documents-and-templates");
            open("/v4/dzo");
            authUiPage.logOutBtnOnHeader.click();
            refresh();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Должна быть неактивна кнопка 'Действия'", () -> {
            pcDzoPage.actionsBtnOnPcDzoPage.shouldBe(disabled);
        });
    }


}
