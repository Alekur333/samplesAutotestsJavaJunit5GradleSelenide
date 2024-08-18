package aleks.kur.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.response.Response;
import net.datafaker.Faker;
import net.datafaker.service.RandomService;
import ru.progredis.models.request.RequestAuto;
import ru.progredis.models.request.RequestKind;
import ru.progredis.models.request.RequestStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.models.request.RequestKind.PPOBPM;
import static ru.progredis.models.request.RequestStatus.DRAFT;
import static ru.progredis.tests.TestBase.attachFile;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class RequestAutomationPage {

    public Map<String, String> requestData = new HashMap<>();

    ModalWindowsPage modalWindowsPage = new ModalWindowsPage();
    FilesPage filesPage = new FilesPage();


    public static Faker faker = new Faker(new Locale("ru"), new RandomService());
//    public static Faker faker = new Faker(new Locale("ru"), new RandomService());
    public String requestsFullName = "Полное наименование заявки autoTest " + faker.lorem().characters(30);
    public String requestsShortName = "Краткое наименование заявки autoTest " + faker.lorem().characters(10);
    public String requestsFullNameUpdated = "Updated " + requestsFullName;
    public String requestsShortNameUpdated = "Updated " + requestsShortName;
    public String requestsReasonToDo = faker.lorem().characters(100);
    public String systemPurposeField = faker.lorem().characters(10);
    public String creatorsEmail = faker.internet().emailAddress("alekur");
    //    public static String requestsFullName = faker.bothify("АвтоТест-??##");
//    public static String gender = "Male";
//    public static String mobile = faker.phoneNumber().subscriberNumber(10);
//    public static String fileToAttach = "files/filePath.png";
//    public static String currentAddress = faker.address().streetAddress();
    // Предупреждение по заполнению обязательных ролей на футере
    public static String fillRequiredFieldsNotificationOnFooter = "Для перевода заявки в этот статус обязательно заполнение отмеченных полей";
    public LocalDate today = LocalDate.now();
    public String currentYear = String.valueOf(today.getYear());
    public String currentYearPlus1 = String.valueOf(today.getYear() + 1);
    public String currentYearPlus2 = String.valueOf(today.getYear() + 2);
    public String position = "Генеральный AutoTest";
    public String departmentsName = "Краткое название подразделения AutoTest";
    public String iof = "А.О.Кур";
    public String fio = "Кур А.О.";
    public String phone = faker.phoneNumber().phoneNumber();
    public String email = faker.internet().emailAddress();

    public SelenideElement

            // Локаторы для таблицы заявок (автоматизация)
            pagesHeader = $(".work-space-content-header h3"),
    //            requestsFooterRedactMode = $(".pi-form__footer .pi-table"), // футер
    requestsFooterRedactMode = $(".pi-form__footer"), // футер с кнопками

    // Элементы заголовка заявки в режиме просмотра
    requestsHeaderBlock = $(".js-mate-insertion-header"),
            requestsStatusOnHeaderLocator = $(".b-info-left .b-info-item_indicator span:nth-child(1)"),
            requestsProcessGoOnHeaderLocator = $(".process-label span"),
            requestsProcessIdOnHeaderLocator = $(".js-show-process-info span"),

    // Элементы заголовка заявки в режиме редактирования
    requestsHeaderBlockEditMode = $(".js-mate-insertion-headerContent"),

    // Задачи процесса в меню 'ID процесса' заголовка заявки
    firsLinkToProcessTaskOnHeaderLocator = $(".pi-popup__body").$(byText("Задачи")).parent().$("a"),

    // Текстовые поля
    // Локаторы для карточки заявки (автоматизация)
    requestsIdLocator = $("[title='Внутренний идентификатор']"),
            requestsFullNameReadModeLocator = $("div.b-header h5"), // Полное наименование
            requestsShortNameReadModeLocator = $("div.js-mate-insertion-shortNameField  span span"), // Краткое наименование
            requestsShortNameLocator = $("#name-field-entityShortName"), // Краткое наименование
            requestsFullNameLocator = $("#name-field-entityName"), // Полное наименование
            requestsTypeFieldLocator = $(".js-mate-insertion-typeField .js-mate-mainitem-value span"), // Тип заявки в режиме просмотра
            requestsCategoryFieldLocator = $(".js-mate-insertion-categoryField .js-mate-mainitem-value span"), // Категория заявки в режиме просмотра
            requestsPriorityFieldLocator = $(".js-mate-insertion-priorityField .js-mate-mainitem-value span"), // Приоритет заявки в режиме просмотра
            requestsYearFieldLocator = $("[title='Год планирования'] span"), // Год планирования заявки в режиме просмотра
            requestsMainCustomerLocator = $(".js-mate-insertion-mainCustomerField .js-mate-mainitem-value span"), // Основной ФЗ
            requestsExtraCustomerLocator = $(".js-mate-insertion-additionalCustomersField .js-mate-mainitem-value span"), // Основной ФЗ

    // Руководитель основного ФЗ заявки (автоматизация) - Должность, ИОФ
    requestsFzChefHeader = $(byText("Руководитель основного ФЗ")),
            requestsFzHeadsPositionFieldLocator = $(".js-mate-insertion-customerManagerField [name='position']"),
            requestsFzHeadsPositionLocator = $(".js-mate-insertion-customerManagerField .pi-line"),
            requestsFzHeadsNameFieldLocator = $(".js-mate-insertion-customerManagerField [name='name']"),
            requestsFzHeadsNameLocator = $(".js-mate-insertion-customerManagerField .js-mate-mainitem-value span"),

    // Куратор проекта заявки (автоматизация) - Должность, ИОФ
    requestsProjectsCuratorPositionFieldLocator = $(".js-mate-insertion-approvedPersonField [name='position']"),
            requestsProjectsCuratorPositionLocator = $(".js-mate-insertion-approvedPersonField .pi-line", 1),
            requestsProjectsCuratorNameFieldLocator = $(".js-mate-insertion-approvedPersonField [name='name']"),
            requestsProjectsCuratorNameLocator = $(".js-mate-insertion-approvedPersonField .js-mate-mainitem-value span"),

    // Согласующие лица - кнопка Добавить, Краткое название подразделения, ИОФ
    addApprovingPersonBtnLocator = $(".js-mate-insertion-confirmedPersonsField .link-add"), // 'Добавить согласующего'
            requestsApprovingPersonsDepartmentsNameFieldLocator = $(".js-mate-insertion-confirmedPersonsField [name='organizationName']"),
            requestsApprovingPersonsDepartmentsNameLocator = $(".js-mate-insertion-confirmedPersonsField .pi-line"),
            requestsApprovingPersonsNameFieldLocator = $(".js-mate-insertion-confirmedPersonsField [name='personName']"),
            requestsApprovingPersonsNameLocator = $(".js-mate-insertion-confirmedPersonsField .js-mate-mainitem-value span"),

    // Ответственные от ФЗ за подготовку заявки - кнопка Добавить, ФИО, Должность, телефон, емейл
    requestsFzResponsiblesBtnLocator = $(".js-mate-insertion-preparationResponsibleField a"),
            requestsPreparationResponsibleNameFieldLocator = $(".js-mate-insertion-preparationResponsibleField [name='name']"),
            requestsPreparationResponsibleNameLocator = $(".js-mate-insertion-preparationResponsibleField .js-mate-mainitem-value p"),
            requestsPreparationResponsiblePositionFieldLocator = $(".js-mate-insertion-preparationResponsibleField [name='position']"),
            requestsPreparationResponsiblePositionLocator = $(".js-mate-insertion-preparationResponsibleField .js-mate-mainitem-value p", 1),
            requestsPreparationResponsiblePhoneFieldLocator = $(".js-mate-insertion-preparationResponsibleField [name='phone']"),
            requestsPreparationResponsibleEmailFieldLocator = $(".js-mate-insertion-preparationResponsibleField [name='email']"),
            requestsPreparationResponsiblePhoneAndEmailLocator = $(".js-mate-insertion-preparationResponsibleField .js-mate-mainitem-value p", 2),

    // Ожидаемые сроки реализации заявки (автоматизация)
    requestsExpectedImplementationTimelineBtnLocator = $(".js-mate-insertion-plannedPeriodsGrid .btn-link"),
    // Термины
    termsLocator = $("div.js-mate-insertion-termsField"),
    // Полигон внедрения заявки (автоматизация)
    addPolygonBtnLocator = $(".js-mate-insertion-polygonsGrid .btn-link"),
    // Основания для выполнения работ
    requestsReasonToDoFieldLocator = $(".js-mate-insertion-reasonField textarea"),
            requestsAutoReasonToDoLocator = $(".js-mate-insertion-reasonField .js-mate-mainitem-value span"),
    // Назначение системы
    requestsSystemPurposeFieldLocator = $(".js-mate-insertion-systemPurposeField textarea"),
    // Цели работ
    requestsWorksPurposeFieldLocator = $(".js-mate-insertion-createPurposeField textarea"),
    // Перспективы развития
    requestsDevelopmentProspectsFieldLocator = $("#name-field-entityEvolutionPerspective"),
    // Характеристика объектов автоматизации
    requestsAutomationObjectFeaturesFieldLocator = $("#entityComponentAutomationFeature"),
    // Описание существующих бизнес процессов
    requestsCurrentBPDiscriptionFieldLocator = $("#entityExistingBPDescription"),
    // Ссылка на существующие процессы в АСУ БМ
    requestsCurrentProcessLinkFieldLocator = $("#name-field-entityAsuBmCurrentProcessLink"),
    // Ссылка на планируемые процессы в АСУ БМ
    requestsAsuBmPlannedProcessLinkFieldLocator = $("#name-field-entityAsuBmPlannedProcessLink"),
    // Описание текущего состояния
    requestsCurrentConditionFieldLocator = $("#entityStateDescription"),
    // Предпосылки возникновения потребности в автоматизации
    requestsPreconditionToAutomateFieldLocator = $("#entityAutomationPrecondition"),
    // Предполагаемые изменения в бизнес-процессах и организационной структуре
    requestsExpectedChangesOfBpAndStructureFieldLocator = $("#entitySupposeChanges"),
    // Предложения по автоматизации бизнес-процессов
    requestsProposalsToAutomateBpFieldLocator = $("#entityAutomationOffer"),
    // Ожидаемый эффект от реализации предлагаемого решения - Выполнение стратегических целей компании
    requestsImplementationsEffectFieldLocator = $("#entityCompanyPurposesExecution"),
    // Телефон исполнителя
    requestsExecutorsPhoneFieldLocator = $(".js-mate-insertion-executorPhoneField input"),

    // Элемент спецификации заявки Оборудование
    specificationsRangField = $(".rang"), // Ранг
            specificationsRangInputField = $("[name='rang']"), // input Ранг
    //            specificationsRangInputFieldSpo = $(".k-numerictextbox"), // input Ранг СПО?
    specificationsRecipientField = $(".recipient"), // Получатель
            specificationsQuantityField = $(".quantity"), // Кол-во ФЗ
            specificationsReasonField = $(".reason"), // Обоснование
            specificationsReasonTextareaField = $(".reason textarea"), // textarea Обоснование
            commentCkiField = $(".commentAgreedQuantity"), // Комментарий ЦКИ
            pktbCttField = $(".statePKTB"), // ПКТБ-ЦЦТ


    // Радио, чекбоксы
    // Наличие СЗИ - СЗИ отсутствует
    requestsNoSziRadioLocator = $(byText("СЗИ отсутствует")).parent().parent(),
    // Обрабатываемая информация - Персональные данные Нет
    requestsPersonalInfoNoRadioBtn = $(byText("Персональные данные")).parent().sibling(0).$(byText("Нет")).parent().parent(),
    // Обрабатываемая информация - Коммерческая тайна Нет
    requestsBusinessSecretNoRadioBtn = $(byText("Коммерческая тайна")).parent().sibling(0).$(byText("Нет")).parent().parent(),


    // Кнопки
    editBtn = $(".link-edit"), // Редактировать на верхней панели
            actionsBtn = $("#object-action-menu"), // кнопка Действия на хедере
            sendToCkiActionBtn = $("[data-id='5']"), // Кнопка 'Направить в ЦКИ' через 'Действия'
            editAfterChangeStatusBtn = $(".pi-form__footer .btnSave"), // Редактировать на футере при смене статуса
            saveOnFooterBtn = $(".btnSave"), // Сохранить заявку после редактирования футер
            addAsuBmCurrentProcessDocumentBtn = $(".js-mate-insertion-asuBmCurrentProcessDocumentField .b-validation button").parent(),
            addAsuBmPlannedProcessDocumentBtn = $(".js-mate-insertion-asuBmPlannedProcessDocumentField .b-validation button"),
            requestsReasonToDoLocator = $("#entityReason"),
            addDocumentBtnLocator = $("#documentList .link-add"), // Добавить документ
            requestsFzBtnLocator = $(".js-mate-insertion-customersField button"), // Функциональные заказчики заявки (автоматизация)
            requestsResposiblesOnIntegrationObjectsBtnLocator = $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField .link-add"), // Ответственные на объектах внедрения
            addHardwareSpecificationsElementBtnLocator = $(".hardwareSpecificationsField .pi-table-cell_fixed button"), // 'Добавить элемент' на вкладке 'Спецификация' Оборудование
            addSpoSpecificationsElementBtnLocator = $(".spoSpecificationsField .pi-table-cell_fixed button"), // 'Добавить элемент' на вкладке 'Спецификация' СПО

    // Вкладки заявки (автоматизация)
    soderganieZaiyavkiTabLocator = $("[data-code='tabGeneral']"), // Содержание заявки
            informatsiiaTabLocator = $("[data-code='tabCKI']"), // Информация
            documentsTabLocator = $("[data-code='tabDocuments']"), // Документы
            specificationHardwareTabLocator = $("[data-code='tabHardwareSpecification']"), // Спецификация
            specificationSpoTabLocator = $("[data-code='tabSpoSpecification']"), // Спецификация

    //Статусы заявки (автоматизация)
    requestsStatusBtnLocator = $("[aria-owns='dropDownSelect_statusField_listbox']"),
            draftStatusLocator = $(".k-animation-container #dropDownSelect_statusField-list").$(byText("Черновик")),
            sentCkiStatusLocator = $(".k-animation-container #dropDownSelect_statusField-list").$(byText("Направлена в ЦКИ")),


    // Дропдауны при редактировании
    // Тип заявки  (автоматизация)
    requestsTypeBtnLocator = $("[aria-owns='dropDownSelect_typeField_listbox'] .k-dropdown-wrap"),
            requestsTypeZayavkaFzLocator = $(".k-animation-container #dropDownSelect_typeField_listbox")
                    .$(byText("Заявка ФЗ")),
            requestsProgectDirectionBtnLocator = $(".js-mate-insertion-strategyCTField span.field__select"), // кнопка модалки проект или направление
            requestsTypePoruchenieLocator = $(".k-animation-container #dropDownSelect_typeField_listbox")
                    .$(byText("Поручение")),
            requestsTypePoruchenieCkiLocator = $(".k-animation-container #dropDownSelect_typeField_listbox")
                    .$(byText("Поручение ЦКИ")),
            requestsTypeDorogiLocator = $(".k-animation-container #dropDownSelect_typeField_listbox")
                    .$(byText("Дороги")),
            requestsTypeTtsLocator = $(".k-animation-container #dropDownSelect_typeField_listbox")
                    .$(byText("ТТС")),
    // Категория заявки  (автоматизация)
    requestsCaregoryBtnLocator = $("[aria-owns='dropDownSelect_categoryField_listbox'] .k-select"),
            requestsCaregoryRazvitieLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Развитие")),
            requestsCaregoryRazvitieNovoiSistemiLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Разработка новой системы")),
            requestsCaregoryTiragirovanieLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Тиражирование")),
            requestsCaregoryMigraciaLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Миграция")),
    // Категория заявки  (автоматизация) Оборудование
    requestsCaregoryPostavkaOborLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
            .$(byText("Поставка оборудования (с 2021)")),
    // Категория заявки  (автоматизация) СПО
    requestsCaregoryPriobretenieSpoLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
            .$(byText("Приобретение стандартного ПО (с 2021)")),
    // Категория заявки  (автоматизация) Сопровождение
    requestsCaregoryAvtorskaiaPoddergka = $(".k-animation-container #dropDownSelect_categoryField_listbox")
            .$(byText("Авторская поддержка")),
            requestsCaregoryNastroikaPoLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Настройка и модификация ПО (по заявкам)")),
    // Категория заявки  (автоматизация) Информационные услуги
    requestsCaregoryTechPoddergka = $(".k-animation-container #dropDownSelect_categoryField_listbox")
            .$(byText("Тех. поддержка ПАК")),
            requestsCaregoryInfoServicesLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Информационные услуги")),
            requestsCaregoryObuchenieLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Обучение")),
            requestsCaregoryRazrabotkaNmdLocator = $(".k-animation-container #dropDownSelect_categoryField_listbox")
                    .$(byText("Разработка НМД")),

    // Приоритет заявки (автоматизация)
    requestsPriorityBtnLocator = $("[aria-owns='dropDownSelect_priorityField_listbox'] .k-select"),
            requestsPriorityVisokiiLocator = $(".k-animation-container #dropDownSelect_priorityField_listbox")
                    .$(byText("Высокий")),
            requestsPriorityNormalniiLocator = $(".k-animation-container #dropDownSelect_priorityField_listbox")
                    .$(byText("Нормальный")),
            requestsPriorityNizskiiLocator = $(".k-animation-container #dropDownSelect_priorityField_listbox")
                    .$(byText("Низкий")),
            requestsPriorityNulevoiLocator = $(".k-animation-container #dropDownSelect_priorityField_listbox")
                    .$(byText("Нулевой")),
    // Год заявки (автоматизация)
    requestsYearBtnLocator = $("[aria-owns='dropDownSelect_yearField_listbox'] .k-select"),
            requestsCurrentYearLocator = $(".k-animation-container #dropDownSelect_yearField_listbox")
                    .$(byText(currentYear)),
            requestsCurrentYearPlus1Locator = $(".k-animation-container #dropDownSelect_yearField_listbox")
                    .$(byText(currentYearPlus1)),
            requestsCurrentYearPlus2Locator = $(".k-animation-container #dropDownSelect_yearField_listbox")
                    .$(byText(currentYearPlus2)),
    // Программа заявки (автоматизация)
    requestsProgramBtnLocator = $("[aria-owns='dropDownSelect_programField_listbox'] .k-select"),
            requestsProgramPCLocator = $(".k-animation-container #dropDownSelect_programField_listbox")
                    .$(byText("ПЦ")),
            requestsProgramIsugtLocator = $(".k-animation-container #dropDownSelect_programField_listbox")
                    .$(byText("ИСУЖТ"));


    public ElementsCollection
            // Списки элементов дропдаунов в завке (автоматизация) при редактировании;
            requestsTypeList = $$(".k-animation-container #dropDownSelect_typeField_listbox[aria-hidden='false'] li"),
            requestsCaregoryList = $$(".k-animation-container #dropDownSelect_categoryField_listbox[aria-hidden='false'] li"),
            requestsPriorityList = $$(".k-animation-container #dropDownSelect_priorityField_listbox[aria-hidden='false'] li"),
            requestsProgramList = $$(".k-animation-container #dropDownSelect_programField_listbox[aria-hidden='false'] li");


    public RequestAutomationPage shouldBeRequestAutoPage() {
        $(".work-space-content-header h3").shouldHave(exactText("Заявки на автоматизацию"));
        return this;
    }

    public RequestAutomationPage goToRequestAutoPage() {
        open("/#?page=request&owner=mine");
        $(".button-close-sidebar").hover().click();
        return this;
    }

    // Получить адрес текущей страницы
    public String getPageUrl() {
        String pageUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        return pageUrl;
    }

    // Открыть заявку на редактирование
    public RequestAutomationPage openRequestAutoToEdit() {
        editBtn.click();
        sleep(3000);
        requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(5));
        return this;
    }

    public RequestAutomationPage fillFullAndShortNameFiels() {
        requestsHeaderBlock.shouldBe(visible);
        soderganieZaiyavkiTabLocator.hover().click();
        sleep(1500);
        requestsFullNameLocator.hover().click();
        requestsFullNameLocator.setValue(requestsFullName);
        requestsShortNameLocator.setValue(requestsShortName);
        return this;
    }

    public RequestAutomationPage fillUpdatedFullAndShortNameFiels() {
        requestsHeaderBlock.shouldBe(visible);
        requestsFullNameLocator.hover().click();
        requestsFullNameLocator.setValue(requestsFullNameUpdated);
        requestsShortNameLocator.setValue(requestsShortNameUpdated);
        return this;
    }

    // Заполнить обязательные поля на вкладке Содержание заявки Автоматизация Б-П в статусе Направлено в ЦКИ
    public RequestAutomationPage fillRequiredFielsOfRequestAutoSendToCki() {
        // Тип "Заявка ФЗ"
        requestsTypeBtnLocator.hover().click();
        requestsTypeZayavkaFzLocator.hover().click();
        // Категория "Развитие"
        requestsCaregoryBtnLocator.hover().click();
        requestsCaregoryRazvitieLocator.click();
        // Приоритет "Высокий"
        requestsPriorityBtnLocator.hover().click();
        requestsPriorityVisokiiLocator.click();
        // Год
//            requestsYearBtnLocator.click();
        // Программа "ПЦ"
        requestsProgramBtnLocator.click();
        requestsProgramPCLocator.click();
        // Функциональные заказчики
        setFunctionalCustomer(15);
        // Руководитель основного ФЗ заявки (автоматизация) - Должность, ИОФ
        setHeadOfMainFz();
        // Ответственные от ФЗ за подготовку заявки
        setFzResponsiblesForRequest();
        // Ожидаемые сроки реализации
//            requestAutoPage.setExpectedImplementationDates(currentDateDdMmYyyy, currentDateDdMmYyyyPlus30Days);
        // Полигон внедрения
        setRequestsPolygonOfUse();
        // Требование к времени восстановления системы, Формат предоставления услуг, Максимальное время выполнения запросов
        setServiceTimesForSystem();
        // Основания для выполнения работ
        requestsReasonToDoFieldLocator.scrollIntoView(true).setValue(requestsReasonToDo);
        // Назначение системы
        requestsSystemPurposeFieldLocator.setValue(systemPurposeField);
        // Цели работ
        requestsWorksPurposeFieldLocator.scrollIntoView(true).setValue("Цели работ");
        // Наличие СЗИ - СЗИ отсутствует
        requestsNoSziRadioLocator.click();
        // Обрабатываемая информация - Персональные данные Нет
        requestsPersonalInfoNoRadioBtn.click();
        // Обрабатываемая информация - Коммерческая тайна Нет
        requestsBusinessSecretNoRadioBtn.click();
        sleep(3000);
        // Перспективы развития
        requestsDevelopmentProspectsFieldLocator.scrollIntoView(true)
                .setValue("Перспективы развития прекрасные");
        // Связь с направлением ЦТ
        requestsProgectDirectionBtnLocator.click();
        modalWindowsPage.firstRadioElementOnModal.click();
        modalWindowsPage.chooseOnModalBtn.click();
        // Текущее и Планируемое количество
        setCurrentPlannedSysremsUsers();
        // Характеристика объектов автоматизации
        requestsAutomationObjectFeaturesFieldLocator
                .setValue("Характеристика объектов автоматизации заявки (автоматизация)");
        // Описание существующих бизнес процессов
        requestsCurrentBPDiscriptionFieldLocator
                .setValue("Описание существующих бизнес процессов заявки (автоматизация)");
        // Ссылка на существующие процессы в АСУ БМ
        requestsCurrentProcessLinkFieldLocator.scrollIntoView(true)
                .setValue("Ссылка на существующие процессы в АСУ БМ заявки (автоматизация)");
        // Документ «Существующие процессы в АСУ БМ»
        addAsuBmCurrentProcessDocument(attachFile);
        // Ссылка на планируемые процессы в АСУ БМ
        requestsAsuBmPlannedProcessLinkFieldLocator
                .scrollIntoView(true).setValue("Ссылка на планируемые процессы в АСУ БМ заявки (автоматизация)");
        // Документ «Планируемые процессы в АСУ БМ»
        addAsuBmPlannedProcessDocument(attachFile);
        // Описание текущего состояния
        requestsCurrentConditionFieldLocator.scrollIntoView(true)
                .setValue("Описание текущего состояния заявки (автоматизация)");
        // Предпосылки возникновения потребности в автоматизации
        requestsPreconditionToAutomateFieldLocator
                .setValue("Предпосылки возникновения потребности в автоматизации заявки (автоматизация)");
        // Предполагаемые изменения в бизнес-процессах и организационной структуре
        requestsExpectedChangesOfBpAndStructureFieldLocator.scrollIntoView(true)
                .setValue("Предполагаемые изменения в бизнес-процессах и организационной структуре заявки (автоматизация)");
        // Предложения по автоматизации бизнес-процессов
        requestsProposalsToAutomateBpFieldLocator
                .setValue("Предложения по автоматизации бизнес-процессов заявки (автоматизация)");
        // Ожидаемый эффект от реализации предлагаемого решения - Выполнение стратегических целей компании
        requestsImplementationsEffectFieldLocator
                .setValue("Выполнение стратегических целей компании заявки (автоматизация)");
        return this;
    }

    public RequestAutomationPage createRequestAuto() {
        $("#request-new").click();
        $x("//a[text() = 'Автоматизация']").click();
        requestsHeaderBlock.shouldBe(visible);
        return this;
    }

    public void saveRequest() {
//        saveOnFooterBtn.click();
        saveOnFooterBtn.shouldBe(enabled, Duration.ofSeconds(5)).hover().click();
        requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(5));
    }

    public RequestAutomationPage createRequestAutoInDraftStatusFullPath() {
        goToRequestAutoPage().createRequestAuto().fillFullAndShortNameFiels();
//        saveOnFooterBtn.click();
        saveRequest();
        return this;
    }

    public RequestAutomationPage createRequestAutoInfoServices() {
        $("#request-new").click();
        $x("//a[text() = 'Инф. услуги']").click();
        return this;
    }

    public RequestAutomationPage createRequestAutoInfoServicesInDraftStatusFullPath() {
        goToRequestAutoPage().createRequestAutoInfoServices().fillFullAndShortNameFiels();
//        saveOnFooterBtn.click();
        saveRequest();
        return this;
    }

    public RequestAutomationPage createRequestAutoEquipment() {
        $("#request-new").click();
        $(".button-close-sidebar");
        $x("//a[text() = 'Оборудование']").click();
        return this;
    }

    public RequestAutomationPage createRequestAutoEquipmentInDraftStatusFullPath() {
        goToRequestAutoPage().createRequestAutoEquipment()
                .fillFullAndShortNameFiels().setFunctionalCustomer(5);
//        requestsFullNameLocator.hover().setValue(requestsFullName);
//        requestsShortNameLocator.setValue(requestsShortName);
//        saveOnFooterBtn.click();
        saveRequest();
        return this;
    }

    public RequestAutomationPage createRequestAutoSupport() {
        $("#request-new").click();
        $x("//a[text() = 'Сопровождение']").click();
        return this;
    }

    public RequestAutomationPage createRequestAutoSupportInDraftStatusFullPath() {
        goToRequestAutoPage().createRequestAutoSupport().fillFullAndShortNameFiels();
//        requestsFullNameLocator.hover().setValue(requestsFullName);
//        requestsShortNameLocator.setValue(requestsShortName);
//        saveOnFooterBtn.click();
        saveRequest();
        return this;
    }

    public RequestAutomationPage createRequestAutoSpo() {
        $("#request-new").click();
        $x("//a[text() = 'СПО']").click();
        return this;
    }

    public RequestAutomationPage createRequestAutoSpoInDraftStatusFullPath() {
        goToRequestAutoPage().createRequestAutoSpo().fillFullAndShortNameFiels();
//        requestsFullNameLocator.hover().setValue(requestsFullName);
//        requestsShortNameLocator.setValue(requestsShortName);
        setFunctionalCustomer(5);
//        saveOnFooterBtn.click();
        saveRequest();
        return this;
    }

    // 'Направить в ЦКИ' через 'Действия'
    public RequestAutomationPage sentToCkiAction() {
        actionsBtn.shouldBe(visible, Duration.ofSeconds(15)).click();
        sendToCkiActionBtn.click();
        return this;
    }

    // Функциональные заказчики
    public RequestAutomationPage setFunctionalCustomer(int sec) {
        requestsProgramBtnLocator.scrollIntoView(true);
        requestsFzBtnLocator.click();
        sleep(1000);
        $(".modal .checkbox-input>input")
                .should(enabled, Duration.ofSeconds(sec)).click();
        $(".modal-footer .btn-success")
                .should(enabled, Duration.ofSeconds(sec)).click();
        sleep(1000);
        return this;
    }

    // Куратор проекта - Должность, ИОФ
    public RequestAutomationPage setProjectsCurator() {
        requestsProjectsCuratorPositionFieldLocator.scrollIntoView(true).setValue(position);
        requestsProjectsCuratorNameFieldLocator.setValue(iof);
        return this;
    }

    // Руководитель основного ФЗ - Должность, ИОФ
    public RequestAutomationPage setHeadOfMainFz() {
//        requestsFzHeadsPositionFieldLocator.scrollIntoView(true).click();
        requestsFzHeadsPositionFieldLocator.scrollIntoView(true).setValue(position);
        requestsFzHeadsNameFieldLocator.setValue(iof);
        return this;
    }

    // Cогласующие лица
    public RequestAutomationPage setApprovingPersons() {
        requestsFzChefHeader.scrollIntoView(true);
        addApprovingPersonBtnLocator.click();
        requestsApprovingPersonsDepartmentsNameFieldLocator.setValue(departmentsName);
        requestsApprovingPersonsNameFieldLocator.setValue(iof);
        return this;
    }

    // Ответственные от ФЗ за подготовку заявки - ФИО, Должность, телефон, мейл
    public RequestAutomationPage setFzResponsiblesForRequest() {
        requestsFzChefHeader.scrollIntoView(true);
        requestsFzResponsiblesBtnLocator.click();
        requestsPreparationResponsibleNameFieldLocator.setValue(fio);
        requestsPreparationResponsiblePositionFieldLocator.setValue(position);
        requestsPreparationResponsiblePhoneFieldLocator.setValue(phone);
        requestsPreparationResponsibleEmailFieldLocator.setValue(email);
        return this;
    }

    // Ожидаемые сроки реализации - Наименование, дата Начало , дата Окончание
    public RequestAutomationPage setExpectedImplementationDates(String dateStart, String dateEnd) {
        requestsExpectedImplementationTimelineBtnLocator.scrollIntoView(true).click();
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(2)").scrollIntoView(true).click();
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(2) input").setValue("Наименование этапа");
        // Начало
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(3)").click();
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(3) input").setValue(dateStart);
        // Окончание
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(4)").click();
        $(".js-mate-insertion-plannedPeriodsGrid td:nth-child(4) input").scrollIntoView(true).setValue(dateEnd);
        return this;
    }

    // Полигон внедрения
    public RequestAutomationPage setRequestsPolygonOfUse() {
        termsLocator.scrollIntoView(true);
        addPolygonBtnLocator.click();
        $(".js-mate-insertion-polygonsGrid .grid-field-name").click();
        $(".js-mate-insertion-polygonsGrid .grid-field-name input").setValue("Наименование Полигона внедрения");
        $(".js-mate-insertion-polygonsGrid .grid-field-description").doubleClick();
        $(".js-mate-insertion-polygonsGrid .grid-field-description input").setValue("Адрес Полигона внедрения");
        return this;
    }

    //  Время восстановления системы, Формат услуг, Максимальное время выполнения
    public RequestAutomationPage setServiceTimesForSystem() {
        addPolygonBtnLocator.scrollIntoView(true);
        // Требование к времени восстановления системы - 24h
        $(".js-mate-insertion-systemRecoveryTimeField [value='24h']").click();
        // Формат предоставления услуг - 8x5 (будни)
        $(".js-mate-insertion-serviceScheduleTypeField .pi-btn-radio:nth-child(3)").click();
        // Максимальное время выполнения запросов - 48ч
        $(".js-mate-insertion-maxRequestProcessingTimeField [value='48h']").click();
        return this;
    }

    // Текущее и Планируемое количество
    public RequestAutomationPage setCurrentPlannedSysremsUsers() {
        $(".js-mate-insertion-systemUsersCurrentCountField input").scrollIntoView(true).setValue("3");
        $(".js-mate-insertion-systemUsersPlannedCountField input").setValue("9");
        return this;
    }

    // Добавить Документ «Существующие процессы в АСУ БМ»
    public RequestAutomationPage addAsuBmCurrentProcessDocument(String attachFile) {
        requestsCurrentBPDiscriptionFieldLocator.scrollIntoView(true);
        addAsuBmCurrentProcessDocumentBtn.hover().click();
        $(".modal-content .form-control").setValue("autoTest document Существующие процессы в АСУ БМ");
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").shouldBe(enabled, Duration.ofSeconds(5)).click();
        return this;
    }

    // Добавить Документ «Планируемые процессы в АСУ БМ»
    public RequestAutomationPage addAsuBmPlannedProcessDocument(String attachFile) {
        requestsAsuBmPlannedProcessLinkFieldLocator.scrollIntoView(true);
        addAsuBmPlannedProcessDocumentBtn.hover().click();
        $(".modal-content .form-control").setValue("autoTest document Планируемые процессы в АСУ БМ");
        //        $(".modal-body .k-input").click();
//        $x("//*[text() = 'Планируемые процессы в АСУ БМ']").click();
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").shouldBe(enabled, Duration.ofSeconds(5)).click();
        return this;
    }

    // Заявка
    public void addRequestDocumentOnModal(String attachFile) {
        $(".modal-content .form-control").setValue("autoTest document Заявка");
        $(".modal-body .k-input").click();
        $x("//li/span[text() = 'Заявка']").click();
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").click();
    }

    // Функциональные требования
    public void addFunctionalRequirmentsDocumentOnModal(String attachFile) {
        $(".modal-content .form-control").setValue("autoTest document Функциональные требования");
        $(".modal-body .k-input").click();
        $x("//li/span[text() = 'Функциональные требования']").click();
        $(".k-upload-button input").uploadFromClasspath("files/" + attachFile);
        $(".footer-wrap .btn-success").click();
    }

    // Ответственные на объектах внедрения
    public RequestAutomationPage setResposiblesOnIntegrationObj() {
        requestsResposiblesOnIntegrationObjectsBtnLocator.scrollIntoView(true).click();
        $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField [name='name']").scrollIntoView(true).setValue("А.О.Кур");
        $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField [name='position']").setValue("должность");
        $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField [name='phone']").setValue("телефон)");
        $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField [name='comment']").setValue("комментарий");
        return this;
    }

    public String getIdFromReguestsCard() {
        String idFromReguestsCard = requestsIdLocator.scrollIntoView(true).getText();
        String regexGetId = "\\d+";
        Pattern p = Pattern.compile(regexGetId);
        Matcher m = p.matcher(idFromReguestsCard);
        String id = null;
        while (m.find()) {
            id = m.group();
        }
        return id;
//        System.out.println("id = " + id);
    }

    public void deleteRequestApi(String authCookie, String requestId) {
        requestData.put("deleted", "true");
        given()
                .spec(requestSpec)
                .body(requestData)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .put(BASE_URL + "/requests/" + requestId)
                .then()
                .log().all()
                .statusCode(200)
                .body("deleted", is(true));
    }

    public void deleteRequestByIdOnCardApi(String authCookie) {
//        requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(10));
        String requestId = getIdFromReguestsCard();
        deleteRequestApi(authCookie, requestId);
    }


    public void openRequestsDocementsTabEditModeByIdFromUi(int kindId) {
        String requestId = getIdFromReguestsCard();
        open("/#?jump=request&subpage=edit&id=" + requestId + "&kindId=" + kindId + "&tab=tabDocuments");
    }

    // Автоматизация бизнес-процессов
    public RequestKind requestAutoKind = new RequestKind(
            PPOBPM,
            7,
            "Автоматизация бизнес-процессов",
            "Автоматизация"
    );

    public RequestStatus draftRequestStatus = new RequestStatus(
            DRAFT,
            1
    );

    // Автоматизация бизнес-процессов
    public RequestAuto newRequestAuto = new RequestAuto(
            requestsFullName,
            requestsShortName,
//            "request",
            Calendar.getInstance().get(Calendar.YEAR) + 1,
            true,
            requestAutoKind,
            draftRequestStatus
    );


    // создать заявку автоматизация Serialized
    public Response createRequestAutoApi(String authCookie, RequestAuto requestAuto) {
        Response newRequestAutoResponse =
                given()
                        .spec(requestSpec)
                        .body(requestAuto)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/requests");
        return newRequestAutoResponse;
    }

    // создать заявку автоматизация и получить ее id
    public int createRequestAutoAndGetIdApi(String authCookie, RequestAuto requestAuto) {
        int newRequestAutoId =
                createRequestAutoApi(authCookie, requestAuto)
                        .then()
                        .log().all()
                        .extract().response().body().path("id");
        return newRequestAutoId;
    }

    // получить заявку по id
    public Response createAndGetRequestByIdApi(String authCookie, RequestAuto requestAuto) {
        long newRequestId = createRequestAutoAndGetIdApi(authCookie, requestAuto);
        Response getRequestByIdResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/requests/" + newRequestId);
        return getRequestByIdResponse;
    }


}