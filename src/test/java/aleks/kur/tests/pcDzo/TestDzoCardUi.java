package aleks.kur.tests.pcDzo;

import io.restassured.response.Response;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.PcDzoPage;
import ru.progredis.tests.TestBaseUi;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.progredis.pages.PcDzoPage.*;

@Tags({@Tag("ui"), @Tag("pcDzo"), @Tag("pcDzoCard"), @Tag("regress")})
@DisplayName("Проверки карточки ДЗО")
public class TestDzoCardUi extends TestBaseUi {

    static String
            testDzoAsuKpiId = organizationPage.asuKpiIdZeldortrest,
            isTestDzoAsuKpiJson = pcDzoPage.isDzoZeldortrestAsuKpiJson(true);

    @BeforeAll
//    @Disabled
    public static void testDataPreparation() {
        PcDzoPage.testOrgShouldBeDzoWithRespCkiAndPcRequired(authCookie, testDzoAsuKpiId, isTestDzoAsuKpiJson);
    }

    static String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    static Response testDzoCard = organizationPage.getOrganizationCardByAsuKpiIdApi(authCookie, testDzoAsuKpiId);
    static int testDzoId = testDzoCard.then().extract().path("id");
    String
            testDzoFullName = testDzoCard.then().extract().path("name"),
            testDzoShortName = testDzoCard.then().extract().path("shortName");

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователь в валидной роли \"" + "{2}" + "\" имеет доступ в карточку ДЗО ui")
    @CsvSource(textBlock = PcDzoPage.validRolesCanSeePcDzoOnSideBarMenu)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void validRolesHaveAccessToDzoCardUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли и перейти в карточку тестовой ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
            authUiPage.logOutBtnOnHeader.click();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Должна открыться карточка ДЗО, в заголовке имя тест-ДЗО", () -> {
            pcDzoPage.headerNameLocatorPcDzoCard.shouldHave(text("ДЗО " + testDzoFullName));
        });
    }

    @ParameterizedTest(name = "Тест #{index} -> Пользователь в невалидной роли \"" + "{2}" + "\" не имеет доступ в карточку ДЗО ui")
    @CsvSource(textBlock = PcDzoPage.invalidRolesCanNotSeePcDzoOnSideBarMenu)
//    @Tags({@Tag("regress"), @Tag("pcDzo")})
    void invalidRolesNotHaveAccessToDzoCardUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли и перейти в карточку тестовой ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
            authUiPage.logOutBtnOnHeader.click();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Не должна открыться карточка ДЗО", () -> {
            pcDzoPage.headerNameLocatorPcDzoCard.shouldNot(exist);
        });
    }

    @Test
    @DisplayName("Проверка элементов карточки ДЗО ui")
    void shouldBeRequiredElementsOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });
        step("Есть кнопка «К перечню ПЦ ДЗО»", () -> {
            pcDzoPage.backToListFromDzoCardLocator.shouldBe(visible)
                    .shouldHave(exactText("К перечню ПЦ ДЗО"));
        });
        step("Есть заголовок", () -> {
            pcDzoPage.headerNameLocatorPcDzoCard
                    .shouldHave(exactText("ДЗО " + testDzoFullName + " (" + testDzoShortName + ")"));
        });
        step("Есть элемент \"Ответственные от ДЗО\"", () -> {
            pcDzoPage.respDzoBlockLocatorPcDzoCard
                    .shouldBe(visible).shouldHave(exactText("Ответственные от ДЗО"));
        });
        step("Есть элемент \"Ответственный от ЦКИ за ДЗО\"", () -> {
            pcDzoPage.respCkiDzoBlockLocatorPcDzoCard
                    .shouldBe(visible).shouldHave(exactText("Ответственный от ЦКИ за ДЗО"));
        });
        step("Есть заголовок таблицы", () -> {
            pcDzoPage.tableHeaderLocatorPcDzoCard
                    .shouldBe(visible).shouldHave(exactText("Программы цифровизации"));
        });
        step("Есть кнопка \"Добавить документ\"", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard
                    .shouldBe(visible).shouldHave(exactText("Добавить документ"));
        });
        step("Есть таблица документов", () -> {
            pcDzoPage.tableLocatorPcDzoCard.shouldBe(visible);
        });
    }

    @Test
    @DisplayName("Проверка данных в поле \"Ответственные от ДЗО\" карточки ДЗО ui")
    void shouldBeRightDataInResponsibleDzoAttrOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО и получить строку из поля", () -> {
            open("/v4/dzo/" + testDzoId);
        });
        val respsDzoOnCardSt = pcDzoPage.respDzoBlockLocatorPcDzoCard.parent().$("ul").getText() + "\n";

        step("В требуемом порядке собираем строку из полей запроса карточки ДЗО", () -> {
        });
        Response dzoCard = pcDzoPage.getDzoCardApi(authCookie, testDzoId);

        // Извлечение списка responsiblePersons из ответа
        List<Map<String, String>> responsiblePersons = dzoCard.path("responsiblePersons");

        // Формирование строки из данных responsiblePersons
        StringBuilder requiredStFromRequest = new StringBuilder();
        for (Map<String, String> person : responsiblePersons) {
            String shortName = person.get("shortName");
            String position = person.get("position");
            String phone = person.get("phone");
            String email = person.get("email");

            requiredStFromRequest.append(shortName);
            if (position != null && !position.isEmpty()) {
                requiredStFromRequest.append(", ").append(position);
            }
            requiredStFromRequest.append(", ").append(email);
            if (phone != null && !phone.isEmpty()) {
                requiredStFromRequest.append(", ").append(phone);
            }
            requiredStFromRequest.append("\n");
        }
        step("Все данные из ответа есть в поле на карточке и находятся в нужном порядке", () -> {
            assertEquals(requiredStFromRequest.toString(), respsDzoOnCardSt, "Строки не совпадают!");
        });
    }

    @Test
    @DisplayName("Проверка данных в поле \"Ответственный от ЦКИ за ДЗО\" карточки ДЗО ui")
    void shouldBeRightDataInResponsibleCkiDzoAttrOnDzoCardUiTest() {

        step("Перейти в карточку ДЗО и получить строку из поля", () -> {
            open("/v4/dzo/" + testDzoId);
        });
        val respsDzoOnCardSt = pcDzoPage.respCkiDzoBlockLocatorPcDzoCard.parent().$("li").getText();

        step("В требуемом порядке собираем строку из полей запроса карточки ДЗО", () -> {
        });
        Response dzoCard = pcDzoPage.getDzoCardApi(authCookie, testDzoId);

        // Извлечение списка responsiblePersons из ответа
        Map<String, Object> responsibleCkiPerson = dzoCard.path("responsibleFromCkiForDpo");

        // Формирование строки из данных responsiblePersons
        StringBuilder requiredStFromRequest = new StringBuilder();
        String shortName = (String) responsibleCkiPerson.get("shortName");
        String position = (String) responsibleCkiPerson.get("position");
        String phone = (String) responsibleCkiPerson.get("phone");
        String email = (String) responsibleCkiPerson.get("email");

        requiredStFromRequest.append(shortName);
        if (position != null && !position.isEmpty()) {
            requiredStFromRequest.append(", ").append(position);
        }
        requiredStFromRequest.append(", ").append(email);
        if (phone != null && !phone.isEmpty()) {
            requiredStFromRequest.append(", ").append(phone);
        }

        step("Все данные из ответа есть в поле на карточке и находятся в нужном порядке", () -> {
            assertEquals(requiredStFromRequest.toString(), respsDzoOnCardSt, "Строки не совпадают!");
        });
    }

    // https://wk.progredis.ru/functionality/user/PC_DPO/DPOCard
    @ParameterizedTest(name = "Тест #{index} -> Пользователю в валидной роли \"{2}\" доступна кнопка \"Добавить документ\" в карточке ДЗО ui")
    @CsvSource(textBlock = validRolesCanUseAddDocumentBtnOnDzoZeldortrestCard)
    void addDocumentBtnIsActiveForValidRolesOnDzoCardUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли", () -> {
            open("/v4/dzo/" + testDzoId);
            authUiPage.logOutBtnOnHeader.click();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Должна быть активна кнопка 'Добавить документ'", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.shouldBe(enabled);
        });
    }

    // https://wk.progredis.ru/functionality/user/PC_DPO/DPOCard
    @ParameterizedTest(name = "Тест #{index} -> Пользователю в невалидной роли \"{2}\" не доступна кнопка \"Действия\" в карточке ДЗО ui")
    @CsvSource(textBlock = invalidRolesCantUseActionsBtnInPcDzoInSection)
    void invalidRolesCantUseAddDocumentBtnOnDzoCardUiTest(String loginUser, String passwdUser, String role) {

        step("Авторизоваться от пользователя в роли", () -> {
            open("/v4/dzo/" + testDzoId);
            authUiPage.logOutBtnOnHeader.click();
            authUiPage.loginUiV4(loginUser, passwdUser);
        });
        step("Должна быть активна кнопка 'Добавить документ'", () -> {
            pcDzoPage.addDocBtnLocatorPcDzoCard.shouldNot(exist);
        });
    }

    //    @Disabled
    @Test
    @DisplayName("Должно быть точное количество колонок в таблице карточки ДЗО ui")
//    @Tags({@Tag("ui"), @Tag("document"), @Tag("regress")})
    void dzoCardTableShouldHaveExactNumberOfColumnsTest() {
        step("Перейти в карточку ДЗО", () -> {
            open("/v4/dzo/" + testDzoId);
        });
        step("В таблице должно быть 8 колонок", () -> {
            pcDzoPage.columnsOnDzoTables.shouldHave(size(8));
        });
    }

    static Stream<Arguments> dzoCardTableShouldHaveExactNamesOfColumnsTest() {
        step("Перейти в карточку ДЗО", () -> {
            authApi.authorizationApiOnUiApp4();
            open("/v4/dzo/" + testDzoId);
        });
        step("Собрать данные для проверки", () -> {
        });
        return Stream.of(
                Arguments.of("1", pcDzoPage.column1NameOnDzoCardTable, pcDzoPage.column1HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("2", pcDzoPage.column2NameOnDzoCardTable, pcDzoPage.column2HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("3", pcDzoPage.column3NameOnDzoCardTable, pcDzoPage.column3HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("4", pcDzoPage.column4NameOnDzoCardTable, pcDzoPage.column4HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("5", pcDzoPage.column5NameOnDzoCardTable, pcDzoPage.column5HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("6", pcDzoPage.column6NameOnDzoCardTable, pcDzoPage.column6HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("7", pcDzoPage.column7NameOnDzoCardTable, pcDzoPage.column7HeaderOnDzoCardTableLocator.getText()),
                Arguments.of("8", pcDzoPage.column8NameOnDzoCardTable, pcDzoPage.column8HeaderOnDzoCardTableLocator.getText())
        );
    }

    //   https://wk.progredis.ru/functionality/user/PC_DPO/DPOCard
    @MethodSource
    @ParameterizedTest(name = "Тест #{index} -> Колонка № {0} имеет требуемое имя \"{1}\" в таблице ДЗО ui")
    void dzoCardTableShouldHaveExactNamesOfColumnsTest(String columnNumber, String expectedName, String actualName) {

        step("Ожидаемое имя колонки № " + columnNumber + " равняется фактическому из таблицы", () -> {
            assertEquals(expectedName, actualName, "Имя колонки в таблице не по требованиям");
        });
    }


}
