package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;
import net.datafaker.Faker;
import net.datafaker.service.RandomService;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class RequestModificationPage {

    static Faker faker = new Faker(new Locale("ru"), new RandomService());
//    static Faker faker = new Faker(new Locale("ru"), new RandomService());
    public static String requestsFullName = "Полное наименование заявки autoTest " + faker.lorem().characters(30);
    public static String requestsShortName = "Краткое наименование заявки autoTest " + faker.lorem().characters(10);
    public static String requestsFullNameUpdated = "Updated " + requestsFullName;
    public static String requestsShortNameUpdated = "Updated " + requestsShortName;
    public static String requestsReasonToDo = faker.lorem().characters(10);
    public static String creatorsEmail = faker.internet().emailAddress("alekur");
//    public static String requestsFullName = faker.bothify("АвтоТест-??##");
//    public static String gender = "Male";
//    public static String mobile = faker.phoneNumber().subscriberNumber(10);
//    public static String fileToAttach = "files/filePath.png";
//    public static String currentAddress = faker.address().streetAddress();

    public SelenideElement
            // Локаторы для таблицы заявок (модификация)
            pagesHeader = $(".work-space-content-header h3"),
            // Локаторы для карточки заявки (автоматизация)
            requestsShortNameLocator = $("div.js-mate-insertion-shortNameField  span span"),
            requestsFullNameLocator = $("div.b-header h5"),
            requestsIdLocator = $("[title='Внутренний идентификатор']"),
            editBtn = $(".link-edit"),
            requestsReasonToDoLocator = $(""),
            requestsTypeBtnLocator = $("[aria-owns='dropDownSelect_typeField_listbox'] .k-select"),
            requestsTypeZayavkaFzLocator = $(".k-animation-container #dropDownSelect_typeField_listbox").$(byText("Заявка ФЗ")),
            requestsTypePoruchenieLocator = $(".k-animation-container #dropDownSelect_typeField_listbox").$(byText("Поручение")),
            requestsTypePoruchenieCkiLocator = $(".k-animation-container #dropDownSelect_typeField_listbox").$(byText("Поручение ЦКИ")),
            requestsTypeDorogiLocator = $(".k-animation-container #dropDownSelect_typeField_listbox").$(byText("Дороги")),
            requestsTypeTtsLocator = $(".k-animation-container #dropDownSelect_typeField_listbox").$(byText("ТТС")),
            requestsCaregoryBtnLocator = $("[aria-owns='dropDownSelect_categoryField_listbox'] .k-select"),
            requestsCaregoryRazvitieLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox > li:nth-child(1)");

    public RequestModificationPage shouldBeRequestsModificationPage() {
        (url()).contains("deduction");
        pagesHeader.shouldHave(exactText("Заявки и заказы на модификацию и АП"), Duration.ofSeconds(15));
        return this;
    }

}