package aleks.kur.tests.requestAutomation;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static ru.progredis.pages.RequestAutomationPage.*;

@DisplayName("Проверки Заявок (автоматизация)")
@Tags({@Tag("ui"), @Tag("requestAuto"), @Tag("regress")})
public class TestRequestAutoAutomationUi extends TestBaseUi {

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    public int sec = 10;

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("requierdFields")})
    @DisplayName("Тест обязательности полей в новой Заявке (автоматизация) в статусе Черновик")
    void requestAutoNewRequierdFieldsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            open("/#?page=request&owner=mine");
        });
        step("Открыть страницу создания  новой заявки", () -> {
            $("#request-new").click();
            $x("//a[text() = 'Автоматизация']").click();
            $("[title='Новая заявка']")
                    .shouldHave(text("Новая заявка (Автоматизация бизнес-процессов)"));
        });
        step("Ввести текст в необязательное поле 'Основания для выполнения работ' ", () -> {
            $("#entityReason").setValue(requestAutoPage.requestsReasonToDo);
        });
        step("Кнопка Сохранить не активна", () -> {
            $(".btnSave").shouldBe(disabled);
        });
        step("Должно быть предупреждение по заполнению обязательных ролей", () -> {
            $(".js-invalid-notice")
                    .shouldHave(text(fillRequiredFieldsNotificationOnFooter));
        });
        step("Обязательное поле 'Полное наименование' помечено красным", () -> {
            $("#name-field-entityName").parent().parent()
                    .shouldHave(cssClass("b-validation_show_error"));
        });
        step("Обязательное поля 'Краткое наименование' помечено красным", () -> {
            $("#name-field-entityShortName").parent().parent()
                    .shouldHave(cssClass("b-validation_show_error"));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("create")})
    @DisplayName("Создание новой заявки (автоматизация) с заполнением обязательных полей в статусе Черновик")
    void requestAutoNewOnlyRequiredFieldsSaveTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            open("/#?page=request&owner=mine");
        });
        step("Открыть страницу создания  новой заявки", () -> {
            $("#request-new").click();
            $x("//a[text() = 'Автоматизация']").click();
        });
        step("Ввести текст в обязательное поле 'Полное наименование'", () -> {
            $("#name-field-entityName").hover().setValue(requestAutoPage.requestsFullName);
        });
        step("Ввести текст в обязательное поле 'Краткое наименование'", () -> {
            $("#name-field-entityShortName").setValue(requestAutoPage.requestsShortName);
        });
        step("Нажать кнопку Сохранить", () -> {
            $(".btnSave").doubleClick();
        });
        step("Заявка сохранилась. Есть id, значения обязательных полей отображаются", () -> {
            step("Полное наименование после сохранения соотвествует введенному", () -> {
                requestAutoPage.requestsFullNameReadModeLocator
                        .shouldHave(exactText(requestAutoPage.requestsFullName));
            });
            step("Краткое наименование после сохранения соотвествует введенному", () -> {
                requestAutoPage.requestsShortNameReadModeLocator
                        .shouldHave(exactText(requestAutoPage.requestsShortName));
            });
            step("Есть ID новой заявки после сохранения", () -> {
                requestAutoPage.requestsIdLocator.shouldNotBe(empty);
            });
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("update")})
    @DisplayName("Редактирование заявки (автоматизация) с изменением и добавлением данных полей")
    void requestAutoUpdateAndSaveTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInDraftStatusFullPath();
        });
        step("Отредактировать заявку и сохранить", () -> {
            step("Открыть заявку на редактирование", () -> {
                requestAutoPage.editBtn.click();
            });
            step("Редактировать поля 'Полное наименование', 'Краткое наименование'", () -> {
                requestAutoPage.fillUpdatedFullAndShortNameFiels();
            });
            step("Нажать кнопку Сохранить", () -> {
                $(".btnSave").click();
            });
        });
        step("Заявка сохранилась. Значения измененных и добавленных полей отображаются", () -> {
            step("Полное наименование после сохранения соотвествует введенному", () -> {
                requestAutoPage.requestsFullNameReadModeLocator
                        .shouldHave(exactText(requestAutoPage.requestsFullNameUpdated));
            });
            step("Краткое наименование после сохранения соотвествует введенному", () -> {
                requestAutoPage.requestsShortNameReadModeLocator
                        .shouldHave(exactText(requestAutoPage.requestsShortNameUpdated));
            });
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Тип' заявки (автоматизация)")
    void requestAutoDropdownTypeTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAuto();
        });
        step("Проверить содержание дропдауна 'Тип' заявки", () -> {
            step("Есть пункт \"Заявка ФЗ\" в дропдауне", () -> {
                requestAutoPage.requestsTypeBtnLocator.click();
                requestAutoPage.requestsTypeZayavkaFzLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Поручение\" в дропдауне", () -> {
                requestAutoPage.requestsTypePoruchenieLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Поручение ЦКИ\" в дропдауне", () -> {
                requestAutoPage.requestsTypePoruchenieCkiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Дороги\" в дропдауне", () -> {
                requestAutoPage.requestsTypeDorogiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"ТТС\" в дропдауне", () -> {
                requestAutoPage.requestsTypeTtsLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 5 пунктов", () -> {
                requestAutoPage.requestsTypeList.shouldHave(size(5));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Категория' заявки (автоматизация)")
    void requestAutoCaregoryDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAuto();
        });
        step("Проверить содержание дропдауна 'Категория' заявки", () -> {
            step("Есть пункт \"Развитие\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryBtnLocator.click();
                requestAutoPage.requestsCaregoryRazvitieLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Разработка новой системы\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryRazvitieNovoiSistemiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Тиражирование\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryTiragirovanieLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Миграция\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryMigraciaLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 4 пункта", () -> {
                requestAutoPage.requestsCaregoryList.shouldHave(size(4));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Приоритет' заявки (автоматизация)")
    void requestAutoPriorityDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAuto();
        });
        step("Проверить содержание дропдауна 'Приоритет' заявки", () -> {
            step("Есть пункт \"Высокий\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityBtnLocator.click();
                requestAutoPage.requestsPriorityVisokiiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Нормальный\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityNormalniiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Нормальный\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityNormalniiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Низкий\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityNizskiiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Нулевой\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityNulevoiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 4 пункта", () -> {
                requestAutoPage.requestsPriorityList.shouldHave(size(4));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Программа' заявки (автоматизация)")
    void requestAutoProgramDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAuto();
        });
        step("Проверить содержание дропдауна 'Программа' заявки", () -> {
            step("Есть пункт \"ПЦ\" в дропдауне", () -> {
                requestAutoPage.requestsProgramBtnLocator.click();
                requestAutoPage.requestsProgramPCLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"ИСУЖТ\" в дропдауне", () -> {
                requestAutoPage.requestsProgramIsugtLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 2 пункта", () -> {
                requestAutoPage.requestsProgramList.shouldHave(size(2));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Проверка подсветки обязательных полей после перевода в статус 'Направлена в ЦКИ' из кнопки Действия заявки автоматизация")
    void requestAutoRequierdFieldsBrightsBySentCkiStatusInActionsBtnTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInDraftStatusFullPath();
        });
        step("Кнопка 'Направить в ЦКИ' через 'Действия'", () -> {
            requestAutoPage.sentToCkiAction();
        });
        step("Перейти в режим редактирования ", () -> {
            requestAutoPage.editAfterChangeStatusBtn.click();
        });
        requestAutoPage.requestsHeaderBlockEditMode.shouldBe(visible, Duration.ofSeconds(10));

        step("Есть предупреждение по заполнению обязательных полей на футере", () -> {
            $(".js-invalid-notice")
                    .shouldHave(text(fillRequiredFieldsNotificationOnFooter));
        });
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().scrollIntoView(true)
                    .shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
        });
        step("На вкладке 'Содержание заявки' проверить, что обязательные поля со звездочкой и подсвечены (из настроек вида заявки)", () -> {
//            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            // Тип Заявки со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-typeField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_typeField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Категория со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-categoryField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_categoryField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Приоритет со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-priorityField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_priorityField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Год со звездочкой
            $(".js-mate-insertion-yearField .necessary").scrollIntoView(true).shouldHave(text("*"));
            // Программа со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-programField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $("#dropDownWrapper_programField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Функциональные заказчики со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-customersField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-customersField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Руководитель основного ФЗ со звездочкой, поля ввода должность, ФИО с подсветкой
            $(".js-mate-insertion-customerManagerField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-customerManagerField input[name='position']").parent().shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-customerManagerField input[name='name']").parent().shouldHave(cssClass("b-validation_show_error"));
            // Ответственные от ФЗ за подготовку заявки со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-preparationResponsibleField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-preparationResponsibleField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Ожидаемые сроки реализации со звездочкой, кнопка добавления с подсветкой
//            $(".js-mate-insertion-plannedPeriodsGrid .necessary").scrollIntoView(true).shouldHave(text("*"));
//            $(".js-mate-insertion-plannedPeriodsGrid button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Полигон внедрения со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-polygonsGrid .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-polygonsGrid button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Требование к времени восстановления системы со звездочкой, форма выбора с подсветкой
//            $(".js-mate-insertion-systemRecoveryTimeField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-systemRecoveryTimeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Формат предоставления услуг со звездочкой, форма выбора с подсветкой
//            $(".js-mate-insertion-serviceScheduleTypeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-serviceScheduleTypeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Максимальное время выполнения запросов со звездочкой, форма выбора с подсветкой
//            $(".js-mate-insertion-maxRequestProcessingTimeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-maxRequestProcessingTimeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Основания для выполнения работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-reasonField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-reasonField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Назначение системы со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemPurposeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-systemPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Цели работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-createPurposeField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-createPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));

            // Наличие СЗИ со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-hasSecurityToolsField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-hasSecurityToolsField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Персональные данные со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-personalDataField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-personalDataField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Коммерческая тайна со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-tradeSecretField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-tradeSecretField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Перспективы развития со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-evolutionPerspectiveField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-evolutionPerspectiveField .b-validation").scrollIntoView(true).shouldHave(cssClass("b-validation_show_error"));

            // Связь с направлением ЦТ со звездочкой, элемент ввода с подсветкой
            $(".js-mate-insertion-strategyCTField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-strategyCTField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Текущее количество со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemUsersCurrentCountField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-systemUsersCurrentCountField .js-mate-root-shell .b-validation").scrollIntoView(true).shouldHave(cssClass("b-validation_show_error"));
            // Планируемое количество со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemUsersPlannedCountField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-systemUsersPlannedCountField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Характеристика объектов автоматизации со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-componentAutomationFeatureField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-componentAutomationFeatureField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Описание существующих бизнес процессов со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-existingBPDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-existingBPDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Ссылка на существующие процессы в АСУ БМ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-asuBmCurrentProcessLinkField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmCurrentProcessLinkField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Документ «Существующие процессы в АСУ БМ» со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Планируемые бизнес-процессы со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-asuBmPlannedProcessLinkField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmPlannedProcessLinkField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Документ «Планируемые процессы в АСУ БМ» со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Описание текущего состояния со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-stateDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-stateDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предпосылки возникновения потребности в автоматизации со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-automationPreconditionField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-automationPreconditionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предполагаемые изменения в бизнес-процессах и организационной структуре со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-supposeChangesField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-supposeChangesField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предложения по автоматизации бизнес-процессов со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-automationOfferField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-automationOfferField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Ожидаемый эффект от реализации предлагаемого решения со звездочкой, поля ввода с подсветкой
            $(".js-mate-insertion-expectedEffectWrapper .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-companyPurposesExecutionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-financialAndEconomicPerformanceField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-qualityPerformanceField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
        });

//        step("Есть признаки обязательности у вкладки 'Документы' и кнопки 'Добавить документ'", () -> {
//            requestAutoPage.documentsTabLocator.scrollIntoView(true);
//            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"));
//            requestAutoPage.documentsTabLocator.click();
//            requestAutoPage.addDocumentBtnLocator.shouldHave(cssValue("style", "color: red;"));
//        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });

    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Проверка подсветки обязательных полей в статусе 'Направлена в ЦКИ' из режима редактирования заявки (автоматизация)")
    void requestAutoRequierdFieldsBrightsBySentCkiStatusInEditModeTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInDraftStatusFullPath();
        });
        step("Перейти в режим редактирования ", () -> {
            requestAutoPage.editBtn.click();
            sleep(2000);
        });
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.shouldBe(visible, Duration.ofSeconds(10)).click();
            requestAutoPage.requestsStatusBtnLocator.shouldBe(visible, Duration.ofSeconds(10)).click();
            requestAutoPage.sentCkiStatusLocator.hover().click();
        });
        step("Есть предупреждение по заполнению обязательных полей на футере", () -> {
            $(".js-invalid-notice")
                    .shouldHave(text(fillRequiredFieldsNotificationOnFooter));
        });
        step("Вернуться во вкладку Содержание заявки и проверить, что обязательные поля со звездочкой и подсвечены (из настроек вида заявки)", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            // Тип Заявки со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-typeField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_typeField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Категория со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-categoryField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_categoryField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Приоритет со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-priorityField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_priorityField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Год со звездочкой
            $(".js-mate-insertion-yearField .necessary").scrollIntoView(true).shouldHave(text("*"));
            // Программа со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-programField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $("#dropDownWrapper_programField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Функциональные заказчики со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-customersField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-customersField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Руководитель основного ФЗ со звездочкой, поля ввода должность, ФИО с подсветкой
            $(".js-mate-insertion-customerManagerField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-customerManagerField input[name='position']").parent().shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-customerManagerField input[name='name']").parent().shouldHave(cssClass("b-validation_show_error"));
            // Ответственные от ФЗ за подготовку заявки со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-preparationResponsibleField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-preparationResponsibleField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Полигон внедрения со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-polygonsGrid .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-polygonsGrid button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Требование к времени восстановления системы со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-systemRecoveryTimeField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-systemRecoveryTimeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Формат предоставления услуг со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-serviceScheduleTypeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-serviceScheduleTypeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Максимальное время выполнения запросов со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-maxRequestProcessingTimeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-maxRequestProcessingTimeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Основания для выполнения работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-reasonField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-reasonField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Назначение системы со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemPurposeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-systemPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Цели работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-createPurposeField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-createPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Наличие СЗИ со звездочкой, форма выбора с подсветкой
            $(".js-mate-insertion-hasSecurityToolsField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-hasSecurityToolsField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Перспективы развития со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-evolutionPerspectiveField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-evolutionPerspectiveField .b-validation").scrollIntoView(true).shouldHave(cssClass("b-validation_show_error"));
            // Связь с направлением ЦТ со звездочкой, элемент ввода с подсветкой
            $(".js-mate-insertion-strategyCTField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-strategyCTField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Текущее количество со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemUsersCurrentCountField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-systemUsersCurrentCountField .js-mate-root-shell .b-validation").scrollIntoView(true).shouldHave(cssClass("b-validation_show_error"));
            // Планируемое количество со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemUsersPlannedCountField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-systemUsersPlannedCountField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Характеристика объектов автоматизации со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-componentAutomationFeatureField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-componentAutomationFeatureField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Описание существующих бизнес процессов со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-existingBPDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-existingBPDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Ссылка на существующие процессы в АСУ БМ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-asuBmCurrentProcessLinkField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmCurrentProcessLinkField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Документ «Существующие процессы в АСУ БМ» со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-asuBmCurrentProcessDocumentField button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Планируемые бизнес-процессы со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-asuBmPlannedProcessLinkField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmPlannedProcessLinkField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Документ «Планируемые процессы в АСУ БМ» со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-asuBmPlannedProcessDocumentField button.btn-link").shouldHave(cssClass("validation-element_btn"));
            // Описание текущего состояния со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-stateDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-stateDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предпосылки возникновения потребности в автоматизации со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-automationPreconditionField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-automationPreconditionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предполагаемые изменения в бизнес-процессах и организационной структуре со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-supposeChangesField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-supposeChangesField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Предложения по автоматизации бизнес-процессов со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-automationOfferField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-automationOfferField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Ожидаемый эффект от реализации предлагаемого решения со звездочкой, поля ввода с подсветкой
            $(".js-mate-insertion-expectedEffectWrapper .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-companyPurposesExecutionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-financialAndEconomicPerformanceField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            $(".js-mate-insertion-qualityPerformanceField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
        });
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.scrollIntoView(true);
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(10));
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(10));
        });
        step("Есть признак обязательности у кнопки 'Добавить документ'", () -> {
            requestAutoPage.requestsFullNameReadModeLocator.scrollIntoView(true);
            requestAutoPage.documentsTabLocator.click();
            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style"));
//            requestAutoPage.addDocumentBtnLocator.shouldHave(cssValue("style", "\"color: red;\""));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });

    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Процесс стартует по заявке Автоматизация после перевода в статус 'Направлена в ЦКИ' во вкладке Информация, заполнения обязательных полей и сохранения")
    void requestAutoHaveStartedProcessAfterFillingSentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInDraftStatusFullPath();
        });
        requestAutoPage.requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(5));
        step("Кнопка 'Направить в ЦКИ' через 'Действия'", () -> {
            requestAutoPage.sentToCkiAction();
        });
        step("Кнопка Редактировать на футере при смене статуса", () -> {
            requestAutoPage.editAfterChangeStatusBtn.shouldBe(visible, Duration.ofSeconds(15)).click();
        });
        requestAutoPage.requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(10));
        step("Заполнить обязательные поля на вкладке Содержание заявки", () -> {
            requestAutoPage.fillRequiredFielsOfRequestAutoSendToCki();
        });
        step("Перейти на вкладку Документы и Добавить обязательные документы", () -> {
            requestAutoPage.requestsFullNameReadModeLocator.scrollIntoView(true);
            requestAutoPage.documentsTabLocator.click();
            // Заявка
            requestAutoPage.addDocumentBtnLocator.click();
            modalWindowsPage.addRequestDocumentOnModal(attachFile);
            // Функциональные требования
            requestAutoPage.addDocumentBtnLocator.click();
            modalWindowsPage.addFunctionalRequirmentsDocumentOnModal(attachFile);
        });
        requestAutoPage.saveOnFooterBtn.click();
        sleep(2000);
        // Процессы по заявкам автоматизация (любого вида, кроме ИТ-услуги), направленным в ЦКИ
        // с 01.02 по 30.04 включительно, стартуют сразу.
        // Если заявка на автоматизацию направлена в ЦКИ после 30.04,
        // процесс по ней не запускается. 01.02 следующего года выполняется проверка
        // и старт процессов по всем таким заявкам.
        if (today.isAfter(dayBeforeOfPeriodForProcessImmediateStart) && today.isBefore(dayAfterOfPeriodForProcessImmediateStart)) {
            System.out.println("День направления в ЦКИ " + today + " попал под условие с 01.02 по 30.04 включительно, поэтому процесс должен стартовать сразу, а не с 1.02 следующего года");
//            step("Закрыть окно сообщения о запуске процесса", () -> {
////                    modalWindowsPage.processStartedCloseWindowBtn.shouldBe(visible, Duration.ofSeconds(30)).click();
//            });
            refresh();
            step("Cтатус Направлена в ЦКИ в заголовке после сохранения", () -> {
                requestAutoPage.requestsStatusOnHeaderLocator.shouldHave(text("Направлена в ЦКИ"));
                requestAutoPage.requestsFooterRedactMode.shouldNot(exist, Duration.ofSeconds(10));
            });
            step("Есть элементы 'Заявка идет по процессу', 'ID процесса' в заголовке", () -> {
                requestAutoPage.requestsProcessGoOnHeaderLocator.shouldHave(text("Заявка идет по процессу"));
                requestAutoPage.requestsProcessIdOnHeaderLocator.shouldHave(text("ID процесса"));
            });
            step("Процесс запущен сразу, так как дата направления в ЦКИ попадает в период с 01.02 по 30.04 включительно. Есть ссылки на задачи в меню 'ID процесса'", () -> {
                requestAutoPage.requestsProcessIdOnHeaderLocator.click();
                requestAutoPage.firsLinkToProcessTaskOnHeaderLocator.should(exist);
            });
        } else {
            System.out.println("День направления в ЦКИ " + today + " не попал под условие с 01.02 по 30.04 включительно, поэтому процесс должен стартовать с 1.02 следующего года");
            step("Cтатус Направлена в ЦКИ в заголовке после сохранения", () -> {
                requestAutoPage.requestsStatusOnHeaderLocator.shouldHave(text("Направлена в ЦКИ"));
//                requestAutoPage.requestsFooterRedactMode.shouldNot(exist, Duration.ofSeconds(10));
            });
            step("Процесс не запущен и ждет активации с 1.02. Нет 'ID процесса'", () -> {
                requestAutoPage.requestsProcessGoOnHeaderLocator.shouldNot(exist);
                requestAutoPage.requestsProcessIdOnHeaderLocator.shouldNot(exist);
            });
        }
        sleep(2000);
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoTypes")})
    @DisplayName("Тип заявки (автоматизация) 'Заявка ФЗ' сохраняется")
    void requestAutoTypeZayavkaFzSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать тип 'Заявка ФЗ' и сохранить", () -> {
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypeZayavkaFzLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Тип 'Заявка ФЗ' сохранен в заявке", () -> {
            requestAutoPage.requestsTypeFieldLocator.shouldHave(text("Заявка ФЗ"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoTypes")})
    @DisplayName("Тип заявки (автоматизация) 'Поручение' сохраняется")
    void requestAutoTypePoruchenieSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать тип 'Поручение' и сохранить", () -> {
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypePoruchenieLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Тип 'Поручение' сохранен в заявке", () -> {
            requestAutoPage.requestsTypeFieldLocator.shouldHave(text("Поручение"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoTypes")})
    @DisplayName("Тип заявки (автоматизация) 'Поручение ЦКИ' сохраняется")
    void requestAutoTypePoruchenieCkiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать тип 'Поручение ЦКИ' и сохранить", () -> {
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypePoruchenieCkiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Тип 'Поручение ЦКИ' сохранен в заявке", () -> {
            requestAutoPage.requestsTypeFieldLocator.shouldHave(text("Поручение ЦКИ"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoTypes")})
    @DisplayName("Тип заявки (автоматизация) 'Дороги' сохраняется")
    void requestAutoTypeDorogiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать тип 'Дороги' и сохранить", () -> {
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypeDorogiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Тип 'Дороги' сохранен в заявке", () -> {
            requestAutoPage.requestsTypeFieldLocator.shouldHave(text("Дороги"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoTypes")})
    @DisplayName("Тип заявки (автоматизация) 'ТТС' сохраняется")
    void requestAutoTypesSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать тип 'ТТС' и сохранить", () -> {
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypeTtsLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Тип 'ТТС' сохранен в заявке", () -> {
            requestAutoPage.requestsTypeFieldLocator.shouldHave(text("ТТС"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoCategories")})
    @DisplayName("Категория заявки (автоматизация) 'Развитие' сохраняется")
    void requestAutoCaregoryRazvitieSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать категорию 'Развитие' и сохранить", () -> {
            requestAutoPage.requestsFullNameLocator.scrollIntoView(true);
            requestAutoPage.requestsCaregoryBtnLocator.click();
            requestAutoPage.requestsCaregoryRazvitieLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Категория 'Развитие' сохранена в заявке", () -> {
            requestAutoPage.requestsCategoryFieldLocator.shouldHave(text("Развитие"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoCategories")})
    @DisplayName("Категория заявки (автоматизация) 'Разработка новой системы' сохраняется")
    void requestAutoCaregoryRazvitieNovoiSistemiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать категорию 'Разработка новой системы' и сохранить", () -> {
            requestAutoPage.requestsFullNameLocator.scrollIntoView(true);
            requestAutoPage.requestsCaregoryBtnLocator.click();
            requestAutoPage.requestsCaregoryRazvitieNovoiSistemiLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Категория 'Разработка новой системы' сохранена в заявке", () -> {
            requestAutoPage.requestsCategoryFieldLocator.shouldHave(text("Разработка новой системы"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoCategories")})
    @DisplayName("Категория заявки (автоматизация) 'Тиражирование' сохраняется")
    void requestAutoCaregoryTiragirovanieSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать категорию 'Тиражирование' и сохранить", () -> {
            requestAutoPage.requestsFullNameLocator.scrollIntoView(true);
            requestAutoPage.requestsCaregoryBtnLocator.click();
            requestAutoPage.requestsCaregoryTiragirovanieLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Категория 'Тиражирование' сохранена в заявке", () -> {
            requestAutoPage.requestsCategoryFieldLocator.shouldHave(text("Тиражирование"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoCategories")})
    @DisplayName("Категория заявки (автоматизация) 'Миграция' сохраняется")
    void requestAutoCaregoryMigraciaSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        refresh();
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать категорию 'Миграция' и сохранить", () -> {
            requestAutoPage.requestsFullNameLocator.scrollIntoView(true);
            requestAutoPage.requestsCaregoryBtnLocator.click();
            requestAutoPage.requestsCaregoryMigraciaLocator.click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Категория 'Миграция' сохранена в заявке", () -> {
            requestAutoPage.requestsCategoryFieldLocator.shouldHave(text("Миграция"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoPriority")})
    @DisplayName("Приоритет 'Высокий' сохраняется в заявке (автоматизация)")
    void requestAutoPriorityVisokiiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать приоритет 'Высокий' и сохранить", () -> {
            requestAutoPage.requestsPriorityBtnLocator.click();
            requestAutoPage.requestsPriorityVisokiiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Приоритет 'Высокий' сохранен в заявке", () -> {
            requestAutoPage.requestsPriorityFieldLocator.shouldHave(text("Высокий"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoPriority")})
    @DisplayName("Приоритет 'Нормальный' сохраняется в заявке (автоматизация)")
    void requestAutoPriorityNormalniiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать приоритет 'Нормальный' и сохранить", () -> {
            requestAutoPage.requestsPriorityBtnLocator.click();
            requestAutoPage.requestsPriorityNormalniiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Приоритет 'Нормальный' сохранен в заявке", () -> {
            requestAutoPage.requestsPriorityFieldLocator.shouldHave(text("Нормальный"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoPriority")})
    @DisplayName("Приоритет 'Низкий' сохраняется в заявке (автоматизация)")
    void requestAutoPriorityNizskiiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать приоритет 'Низкий' и сохранить", () -> {
            requestAutoPage.requestsPriorityBtnLocator.click();
            requestAutoPage.requestsPriorityNizskiiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Приоритет 'Низкий' сохранен в заявке", () -> {
            requestAutoPage.requestsPriorityFieldLocator.shouldHave(text("Низкий"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoPriority")})
    @DisplayName("Приоритет 'Нулевой' сохраняется в заявке (автоматизация)")
    void requestAutoPriorityNulevoiSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать приоритет 'Нулевой' и сохранить", () -> {
            requestAutoPage.requestsPriorityBtnLocator.click();
            requestAutoPage.requestsPriorityNulevoiLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Приоритет 'Нулевой' сохранен в заявке", () -> {
            requestAutoPage.requestsPriorityFieldLocator.shouldHave(text("Нулевой"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoYear")})
    @DisplayName("Год текущий сохраняется в заявке (автоматизация)")
    void requestAutoCurrentYearSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать Год текущий и сохранить", () -> {
            requestAutoPage.requestsYearBtnLocator.click();
            requestAutoPage.requestsCurrentYearLocator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Год текущий сохранен в заявке", () -> {
            requestAutoPage.requestsYearFieldLocator.shouldHave(text(requestAutoPage.currentYear));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoYear")})
    @DisplayName("Год текущий + 1 сохраняется в заявке (автоматизация)")
    void requestAutoCurrentYearPlus1SaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать Год текущий + 1 и сохранить", () -> {
            requestAutoPage.requestsYearBtnLocator.click();
            requestAutoPage.requestsCurrentYearPlus1Locator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Год текущий + 1 сохранен в заявке", () -> {
            requestAutoPage.requestsYearFieldLocator.shouldHave(text(requestAutoPage.currentYearPlus1));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoYear")})
    @DisplayName("Год текущий + 2 сохраняется в заявке (автоматизация)")
    void requestAutoCurrentYearPlus2SaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать Год текущий + 2 и сохранить", () -> {
            requestAutoPage.requestsYearBtnLocator.click();
            requestAutoPage.requestsCurrentYearPlus2Locator.hover().click();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Год текущий + 2 сохранен в заявке", () -> {
            requestAutoPage.requestsYearFieldLocator.shouldHave(text(requestAutoPage.currentYearPlus2));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoHeadOfMainFz")})
    @DisplayName("Поля 'Руководитель основного ФЗ' сохраняются в заявке (автоматизация)")
    void requestAutoHeadOfMainFzSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Заполнить поля Должность, И.О.Фамилия и сохранить", () -> {
            requestAutoPage.setHeadOfMainFz();
            requestAutoPage.saveOnFooterBtn.doubleClick();
        });
        step("Должность, И.О.Фамилия сохранились в заявке", () -> {
            requestAutoPage.requestsFzHeadsPositionLocator.shouldHave(text(requestAutoPage.position));
            requestAutoPage.requestsFzHeadsNameLocator.shouldHave(text(requestAutoPage.iof));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoMainCustomer"), @Tag("requestAutoExtraCustomer")})
    @DisplayName("'Основной и дополнительный ФЗ' сохраняются в заявке (автоматизация)")
    void requestAutoFunctionalCustomerSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        sleep(1000);
        step("Выбрать первую организацию в окне ФЗ и сохранить заявку", () -> {
            requestAutoPage.requestsProgramBtnLocator.scrollIntoView(true);
            requestAutoPage.requestsFzBtnLocator.click();
            String mainFzShortName = modalWindowsPage.firstItemsShortNameOnModal
                    .shouldBe(visible, Duration.ofSeconds(15)).getText();
            modalWindowsPage.firstCheckboxElementOnModal.click();
            modalWindowsPage.chooseOnModalBtn.click();
            requestAutoPage.saveOnFooterBtn.click();
            requestAutoPage.requestsHeaderBlock.shouldBe(visible);
            step("Основной ФЗ сохранился в заявке", () -> {
//                requestAutoPage.requestsShortNameReadModeLocator.scrollIntoView(true);
                requestAutoPage.requestsMainCustomerLocator.shouldHave(text(mainFzShortName));
            });
        });
        step("Открыть заявку в режиме редактирования", () -> {
            requestAutoPage.editBtn.click();
        });
        step("Выбрать вторую организацию в окне ФЗ и сохранить заявку", () -> {
//            requestAutoPage.requestsProgramBtnLocator.scrollIntoView(true);
            requestAutoPage.requestsFzBtnLocator.shouldBe(enabled, Duration.ofSeconds(5)).click();
            String extraFzShortName = modalWindowsPage.secondItemsShortNameOnModal
                    .shouldBe(visible, Duration.ofSeconds(15)).getText();
            modalWindowsPage.secondItemsShortNameOnModal.click();
            modalWindowsPage.chooseOnModalBtn.click();
            requestAutoPage.saveOnFooterBtn.click();
            requestAutoPage.requestsHeaderBlock.shouldBe(visible);
            step("Дополнительный ФЗ сохранился в заявке", () -> {
                requestAutoPage.requestsExtraCustomerLocator.shouldHave(text(extraFzShortName));
            });
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoProjectsCurator")})
    @DisplayName("Поле 'Куратор проекта' сохраняется в заявке (автоматизация)")
    void requestAutoProjectsCuratorSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Заполнить поля Должность, И.О.Фамилия и сохранить", () -> {
            requestAutoPage.setProjectsCurator();
            requestAutoPage.saveOnFooterBtn.doubleClick();
            sleep(2000);
        });
        step("Должность, И.О.Фамилия Куратора проекта сохранились в заявке", () -> {
            requestAutoPage.requestsProjectsCuratorPositionLocator.shouldHave(text(requestAutoPage.position));
            requestAutoPage.requestsProjectsCuratorNameLocator.shouldHave(text(requestAutoPage.iof));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoApprovingPersons")})
    @DisplayName("Поле 'Согласующие лица' сохраняется в заявке (автоматизация)")
    void requestAutoApprovingPersonsSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
            sleep(1000);
        });
        step("Заполнить поля Краткое название подразделения, И.О.Фамилия и сохранить", () -> {
            requestAutoPage.setApprovingPersons();
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Краткое название подразделения, И.О.Фамилия сохранились в заявке", () -> {
            requestAutoPage.requestsApprovingPersonsDepartmentsNameLocator.scrollIntoView(true)
                    .shouldHave(text(requestAutoPage.departmentsName));
            requestAutoPage.requestsApprovingPersonsNameLocator.shouldHave(text(requestAutoPage.iof));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoFzResponsibles")})
    @DisplayName("Поле 'Ответственные от ФЗ за подготовку заявки' сохраняется в заявке (автоматизация)")
    void requestAutoFzResponsiblesSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        refresh();
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Заполнить поля Краткое название подразделения, И.О.Фамилия и сохранить", () -> {
            requestAutoPage.setFzResponsiblesForRequest();
            requestAutoPage.saveOnFooterBtn.doubleClick();
        });
        step("ФИО, Должность, телефон, емейл сохранились в заявке", () -> {
            requestAutoPage.requestsPreparationResponsibleNameLocator.scrollIntoView(true)
                    .shouldHave(text(requestAutoPage.fio));
            requestAutoPage.requestsPreparationResponsiblePositionLocator.shouldHave(text(requestAutoPage.position));
            requestAutoPage.requestsPreparationResponsiblePhoneAndEmailLocator
                    .shouldHave(text(requestAutoPage.phone + ", " + requestAutoPage.email));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requestAutoAttributesSavings"), @Tag("requestAutoReasonToDo")})
    @DisplayName("Поле 'Основания для выполнения работ' сохраняется в заявке (автоматизация)")
    void requestAutoReasonToDoSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAuto();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Заполнить полe Основания для выполнения работ и сохранить", () -> {
            requestAutoPage.requestsReasonToDoFieldLocator.scrollIntoView(true).setValue(requestAutoPage.requestsReasonToDo);
            requestAutoPage.saveOnFooterBtn.doubleClick();
        });
        step("Основания для выполнения работ сохранилось в заявке", () -> {
            requestAutoPage.requestsAutoReasonToDoLocator.scrollIntoView(true)
                    .shouldHave(text(requestAutoPage.requestsReasonToDo));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

}