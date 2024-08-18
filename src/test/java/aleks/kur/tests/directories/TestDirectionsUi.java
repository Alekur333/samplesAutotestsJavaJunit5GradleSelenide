package aleks.kur.tests.directories;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static ru.progredis.helpers.DriverSettings.BASE_URL;

@Tags({@Tag("ui"), @Tag("directions")})
@DisplayName("Проверки справочника Направления ui")
public class TestDirectionsUi extends TestBaseUi {

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String directionShortName = directionsPage.directionShortName;
    String directionFullName = directionsPage.directionFullName;
    String directionDescription = directionsPage.directionDescription;

    @BeforeEach
    public void authorization() {
        authApi.authorizationOnUibyApiPure(authCookie);
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Проверка наличия справочника Направления в группе справочников Документы")
    void shouldBeDirectionsInDocementsGroupe() {
        step("Перейти на страницу Cправочники", () -> {
            open("/" + directoriesPage.directoriesPageLink);
        });
        step("В группе справочников Документы есть справочник Направления", () -> {
            directoriesPage.directionsDirectory.scrollIntoView(true).shouldBe(visible);
        });
        step("справочник Направления имеет ссылку на страницу Направления", () -> {
            directoriesPage.directionsDirectory
                    .shouldHave(attribute("href", BASE_URL + "/#?page=reference&subpage=directions"));
        });
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Проверка элементов справочника Направления")
    void directionsPageElementsTest() {
        step(" Открыть справочник Направления", () -> {
            open("/" + directionsPage.directionsPageLink);
        });
        step("Есть заголовок Направления", () -> {
            directionsPage.directionsPageHeader.shouldHave(exactText("Направления"));
        });
        step("На верхней панеле есть все кнопки и элементы", () -> {
            step("Есть кнопка Добавить", () -> {
                directionsPage.addDirectionBtn.$("span").shouldHave(exactText("Добавить"));
            });
            step("Есть кнопка Редактировать", () -> {
                directionsPage.editDirectionBtn.$("span").shouldHave(exactText("Редактировать"));
            });
            step("Есть кнопка Удалить", () -> {
                directionsPage.deleteDirectionBtn.$("span").shouldHave(exactText("Удалить"));
            });
            step("Есть кнопка Экспорт CSV", () -> {
                directionsPage.exportCsvDirectionsBtn.$("span").shouldHave(exactText("Экспорт CSV"));
            });
            step("Есть кнопка Экспорт XSL", () -> {
                directionsPage.exportXlsDirectionsBtn.$("span").shouldHave(exactText("Экспорт XSL"));
            });
            step("Есть кнопка Импорт", () -> {
                directionsPage.importDirectionsBtn.$("span").shouldHave(exactText("Импорт"));
            });
            step("Есть поле поиска", () -> {
                directionsPage.searchFieldOnDirectionsPage.shouldBe(visible);
            });
        });
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Проверка таблицы справочника Направления")
    void directionsPageTableTest() {
        step(" Открыть справочник Направления", () -> {
            open("/" + directionsPage.directionsPageLink);
        });
        step("Таблица справочника содержит нужные колонки", () -> {
            step("Есть колонка Краткое наименование", () -> {
                directionsPage.shortNameColomn.shouldBe(visible);
            });
            step("Есть колонка Полное наименование", () -> {
                directionsPage.fullNameColomn.shouldBe(visible);
            });
            step("Есть колонка Описание", () -> {
                directionsPage.descriptionColomn.shouldBe(visible);
            });
            step("В таблице 3 колонки", () -> {
                directionsPage.directionsTableColomns.shouldHave(size(3));
            });
        });
    }

    @Test
    @Tags({@Tag("smoke")})
    @DisplayName("Новое направление сохраняется при заполнении обязательных полей")
    void newDirectionShouldBeSavedWithRequiredAttrsTest() {
        step(" Открыть справочник Направления", () -> {
            open("/" + directionsPage.directionsPageLink);
        });
        step("Добавить новое направление", () -> {
            step("кнопка Добавить", () -> {
                directionsPage.addDirectionBtn.click();
            });
            step("Ввести Краткое наименование", () -> {
                directionsPage.directionShortNameField
                        .setValue(directionShortName);
            });
            step("Ввести Полное наименование", () -> {
                directionsPage.directionFullNameField
                        .setValue(directionFullName);
            });
            step("Ввести Описание и сохранить", () -> {
                directionsPage.directionDescriptionField
                        .setValue(directionDescription);
                directionsPage.saveDirectionBtn.scrollIntoView(true).click();
            });
        });
        step("Проверить соответствие введенных значений полей в таблице направлений", () -> {
            step("Краткое имя отображается в таблице", () -> {
                SelenideElement newDirectionsShortNameInTable = $x("//td[text()=" + "'" + directionShortName + "']");
                newDirectionsShortNameInTable.shouldBe(visible);
            });
            step("Полное имя отображается в таблице", () -> {
                SelenideElement newDirectionsFullNameInTable = $x("//td[text()=" + "'" + directionFullName + "']");
                newDirectionsFullNameInTable.shouldBe(visible);
            });
            step("Описание отображается в таблице", () -> {
                SelenideElement newDirectionsDirectionInTable = $x("//td[text()=" + "'" + directionDescription + "']");
                newDirectionsDirectionInTable.shouldBe(visible);
            });
        });
        step("Удалить созданное направление", () -> {
            SelenideElement newDirectionStringInTable = $x("//td[text()=" + "'" + directionDescription + "']").parent();
            newDirectionStringInTable.click();
            directionsPage.deleteDirectionBtn.click();
            directionsPage.deleteDirectionOnModalBtn.click();
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke")})
    @DisplayName("АСУ сохраняется в новом направлении")
    void newDirectionShouldBeSavedWithAsuTest() {

        System.out.println(authCookie);
        String asuShortName = asuPage.asuShortName;
        int asuId = asuPage.createAndGetAsuIdOfNewAsuApi(authCookie, asuShortName);

        step(" Открыть справочник Направления", () -> {
            open("/" + directionsPage.directionsPageLink);
        });
        step("Добавить новое направление с АСУ", () -> {
            step("кнопка Добавить", () -> {
                directionsPage.addDirectionBtn.click();
            });
            step("Ввести Краткое наименование", () -> {
                directionsPage.directionShortNameField.shouldBe(enabled)
                        .setValue(directionShortName);
            });
            step("Ввести Полное наименование", () -> {
                directionsPage.directionFullNameField
                        .setValue(directionFullName);
            });
            step("Добавить тестовое АСУ", () -> {
                directionsPage.addAsuInDirectionBtn.click();
                directionsPage.searchFieldInAsuModal.setValue(asuShortName);
                directionsPage.firstAsuForDirectionCheck.click();
                directionsPage.chooseAsuForDirectionBtn.click();
            });
        });
        step("Сохранить направление", () -> {
            directionsPage.saveDirectionBtn.scrollIntoView(true).click();
        });
        step("Проверить, что АСУ сохранился в направлении", () -> {
            step("Открыть направление для редактирования", () -> {
                directionsPage.searchFieldOnDirectionsPage.setValue(directionShortName);
                SelenideElement newDirectionsShortNameInTable = $x("//td[text()=" + "'" + directionShortName + "']");
                newDirectionsShortNameInTable.click();
                directionsPage.editDirectionBtn.click();
            });
            step("Проверить имя сохраненного АСУ", () -> {
                directionsPage.firstAsuInDirection.shouldHave(exactText(asuShortName), Duration.ofSeconds(30));
                directionsPage.cancelDirectionBtn.scrollIntoView(true).click();
            });
        });
        step("Удалить созданное направление и АСУ", () -> {
            SelenideElement newDirectionStringInTable = $x("//td[text()=" + "'" + directionShortName + "']").parent();
            newDirectionStringInTable.click();
            directionsPage.deleteDirectionBtn.click();
            directionsPage.deleteDirectionOnModalBtn.click();
            asuPage.deleteAsuApi(authCookie, asuId);
        });

    }


}
