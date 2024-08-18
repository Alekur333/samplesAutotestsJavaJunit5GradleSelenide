package aleks.kur.tests.requestAutomation;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static ru.progredis.pages.RequestAutomationPage.fillRequiredFieldsNotificationOnFooter;

//@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Проверки Заявок (на информационные услуги)")
@Tags({@Tag("ui"), @Tag("requestAutoInfoServices"), @Tag("regress")})
public class TestRequestAutoInfoServicesUi extends TestBaseUi {

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    public int sec = 10;

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("requierdFields")})
    @DisplayName("Тест обязательности полей в новой Заявке (на информационные услуги) в статусе Черновик")
    void requestAutoInfoServicesNewRequierdFieldsTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
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
    @DisplayName("Создание новой заявки (на информационные услуги) с заполнением обязательных полей")
    void requestAutoInfoServicesNewOnlyRequiredFieldsSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
        });
        step("Ввести текст в обязательное поле 'Полное наименование'", () -> {
            requestAutoPage.requestsFullNameLocator.hover().setValue(requestAutoPage.requestsFullName);
        });
        step("Ввести текст в обязательное поле 'Краткое наименование'", () -> {
            requestAutoPage.requestsShortNameLocator.setValue(requestAutoPage.requestsShortName);
        });
        step("Нажать кнопку Сохранить", () -> {
//            requestAutoPage.saveOnFooterBtn.click();
            requestAutoPage.saveRequest();

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
    @DisplayName("Редактирование заявки (на информационные услуги) с изменением и добавлением данных полей")
    void requestAutoInfoServicesUpdateAndSaveTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInfoServicesInDraftStatusFullPath();
        });
        step("Отредактировать заявку и сохранить", () -> {
            step("Открыть заявку на редактирование", () -> {
                requestAutoPage.editBtn.click();
            });
            step("Ввести текст в обязательное поле 'Полное наименование'", () -> {
                requestAutoPage.requestsFullNameLocator.scrollIntoView(true).setValue(requestAutoPage.requestsFullNameUpdated);
            });
            step("Ввести текст в обязательное поле 'Краткое наименование'", () -> {
                requestAutoPage.requestsShortNameLocator.setValue(requestAutoPage.requestsShortNameUpdated);
            });
            step("Нажать кнопку Сохранить", () -> {
                requestAutoPage.saveRequest();
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
    @DisplayName("Проверка содержания дропдауна 'Тип' заявки (на информационные услуги)")
    void requestAutoInfoServicesDropdownTypeTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
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
    @DisplayName("Проверка содержания дропдауна 'Категория' заявки (на информационные услуги)")
    void requestAutoInfoServicesCaregoryDropdownsTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
        });
        step("Проверить содержание дропдауна 'Категория' заявки", () -> {
            step("Есть пункт 'Тех. поддержка ПАК' ", () -> {
                requestAutoPage.requestsCaregoryBtnLocator.click();
                requestAutoPage.requestsCaregoryTechPoddergka.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Информационные услуги' ", () -> {
                requestAutoPage.requestsCaregoryInfoServicesLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Обучение' ", () -> {
                requestAutoPage.requestsCaregoryObuchenieLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Разработка НМД' ", () -> {
                requestAutoPage.requestsCaregoryRazrabotkaNmdLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 4 пункта", () -> {
                requestAutoPage.requestsCaregoryList.shouldHave(size(4));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Приоритет' заявки (на информационные услуги)")
    void requestAutoInfoServicesPriorityDropdownsTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
        });
        step("Проверить содержание дропдауна 'Приоритет' заявки", () -> {
            step("Есть пункт 'Высокий' в дропдауне", () -> {
                requestAutoPage.requestsPriorityBtnLocator.click();
                requestAutoPage.requestsPriorityVisokiiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Нормальный' в дропдауне", () -> {
                requestAutoPage.requestsPriorityNormalniiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Нормальный' в дропдауне", () -> {
                requestAutoPage.requestsPriorityNormalniiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Низкий' в дропдауне", () -> {
                requestAutoPage.requestsPriorityNizskiiLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт 'Нулевой' в дропдауне", () -> {
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
    @DisplayName("Проверка содержания дропдауна 'Программа' заявки (на информационные услуги)")
    void requestAutoInfoServicesProgramDropdownsTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoInfoServices();
        });
        step("Проверить содержание дропдауна 'Программа' заявки", () -> {
            step("Есть пункт 'ПЦ' в дропдауне", () -> {
                requestAutoPage.requestsProgramBtnLocator.click();
                requestAutoPage.requestsProgramPCLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 1 пункт", () -> {
                requestAutoPage.requestsProgramList.shouldHave(size(1));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Проверка подсветки обязательных полей после перевода в 'Направлена в ЦКИ' в режиме просмотра заявки (на информационные услуги)")
    void requestAutoInfoServicesRequierdFieldsBrightsBySentCkiStatusInViewModeTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInfoServicesInDraftStatusFullPath();
        });
//        sleep(2000);

        step("Кнопка 'Направить в ЦКИ' через 'Действия'", () -> {
            requestAutoPage.sentToCkiAction();
        });
        step("Перейти в режим редактирования ", () -> {
            requestAutoPage.editAfterChangeStatusBtn.click();
        });
//        step("Редактировать заявку ", () -> {
//            requestAutoPage.editBtn.click();
//        });
//        sleep(1500);
//        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
//            requestAutoPage.informatsiiaTabLocator.click();
//            requestAutoPage.requestsStatusBtnLocator.click();
//            requestAutoPage.sentCkiStatusLocator.click();
//        });
        step("Есть предупреждение по заполнению обязательных полей на футере", () -> {
            $(".js-invalid-notice")
                    .shouldHave(text(fillRequiredFieldsNotificationOnFooter));
        });
        step("Вернуться во вкладку Содержание заявки и проверить, что обязательные поля со звездочкой и подсвечены (из настроек вида заявки)", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            step("Тип Заявки со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-typeField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_typeField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Категория со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-categoryField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_categoryField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Приоритет со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-priorityField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_priorityField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Год со звездочкой", () -> {
                $(".js-mate-insertion-yearField .necessary").scrollIntoView(true).shouldHave(text("*"));
            });
            step("Программа со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-programField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $("#dropDownWrapper_programField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Функциональные заказчики со звездочкой, кнопка добавления с подсветкой", () -> {
                $(".js-mate-insertion-customersField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-customersField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Руководитель основного ФЗ со звездочкой, поля ввода должность, ФИО с подсветкой", () -> {
                $(".js-mate-insertion-customerManagerField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-customerManagerField input[name='position']").parent().shouldHave(cssClass("b-validation_show_error"));
                $(".js-mate-insertion-customerManagerField input[name='name']").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Ответственные от ФЗ за подготовку заявки со звездочкой, кнопка добавления с подсветкой", () -> {
                $(".js-mate-insertion-preparationResponsibleField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-preparationResponsibleField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Основания для выполнения работ со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-reasonField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-reasonField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Цели работ со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-createPurposeField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-createPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Описание текущего состояния со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-stateDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-stateDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предпосылки возникновения потребности в автоматизации со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-automationPreconditionField .necessary").shouldHave(text("*"));
                $(".js-mate-insertion-automationPreconditionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предполагаемые изменения в бизнес-процессах и организационной структуре со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-supposeChangesField .necessary").shouldHave(text("*"));
                $(".js-mate-insertion-supposeChangesField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предложения по автоматизации бизнес-процессов со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-automationOfferField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-automationOfferField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
        });
        step("Есть признаки обязательности у кнопки 'Добавить документ'", () -> {
            requestAutoPage.documentsTabLocator.scrollIntoView(true).click();
//            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"));
//            requestAutoPage.documentsTabLocator.click();
            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style"));
        });
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });

    }
    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Проверка подсветки обязательных полей после перевода в 'Направлена в ЦКИ' из режима редактирования заявки (на информационные услуги)")
    void requestAutoInfoServicesRequierdFieldsBrightsBySentCkiStatusInEditModeTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInfoServicesInDraftStatusFullPath();
        });
        step("Перейти в режим редактирования ", () -> {
            requestAutoPage.openRequestAutoToEdit();
        });
//        step("Кнопка 'Направить в ЦКИ' через 'Действия'", () -> {
//            requestAutoPage.sentToCkiAction();
//        });
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.hover().click();
            requestAutoPage.requestsHeaderBlock.shouldBe(visible, Duration.ofSeconds(5));
            requestAutoPage.requestsStatusBtnLocator.click();
            sleep(2500);
            requestAutoPage.sentCkiStatusLocator.click();
        });
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
        });
        step("Есть предупреждение по заполнению обязательных полей на футере", () -> {
            $(".js-invalid-notice")
                    .shouldHave(text(fillRequiredFieldsNotificationOnFooter));
        });
        step("Вернуться во вкладку Содержание заявки и проверить, что обязательные поля со звездочкой и подсвечены (из настроек вида заявки)", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            step("Тип Заявки со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-typeField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_typeField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Категория со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-categoryField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_categoryField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Приоритет со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-priorityField .necessary").shouldHave(text("*"));
                $("#dropDownWrapper_priorityField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Год со звездочкой", () -> {
                $(".js-mate-insertion-yearField .necessary").scrollIntoView(true).shouldHave(text("*"));
            });
            step("Программа со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-programField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $("#dropDownWrapper_programField").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Функциональные заказчики со звездочкой, кнопка добавления с подсветкой", () -> {
                $(".js-mate-insertion-customersField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-customersField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Руководитель основного ФЗ со звездочкой, поля ввода должность, ФИО с подсветкой", () -> {
                $(".js-mate-insertion-customerManagerField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-customerManagerField input[name='position']").parent().shouldHave(cssClass("b-validation_show_error"));
                $(".js-mate-insertion-customerManagerField input[name='name']").parent().shouldHave(cssClass("b-validation_show_error"));
            });
            step("Ответственные от ФЗ за подготовку заявки со звездочкой, кнопка добавления с подсветкой", () -> {
                $(".js-mate-insertion-preparationResponsibleField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-preparationResponsibleField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Основания для выполнения работ со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-reasonField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-reasonField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Цели работ со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-createPurposeField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-createPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Описание текущего состояния со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-stateDescriptionField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-stateDescriptionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предпосылки возникновения потребности в автоматизации со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-automationPreconditionField .necessary").shouldHave(text("*"));
                $(".js-mate-insertion-automationPreconditionField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предполагаемые изменения в бизнес-процессах и организационной структуре со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-supposeChangesField .necessary").shouldHave(text("*"));
                $(".js-mate-insertion-supposeChangesField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
            step("Предложения по автоматизации бизнес-процессов со звездочкой, поле ввода с подсветкой", () -> {
                $(".js-mate-insertion-automationOfferField .necessary").scrollIntoView(true).shouldHave(text("*"));
                $(".js-mate-insertion-automationOfferField .js-mate-root-shell .b-validation").shouldHave(cssClass("b-validation_show_error"));
            });
        });
        step("Есть признаки обязательности у кнопки 'Добавить документ'", () -> {
            requestAutoPage.documentsTabLocator.scrollIntoView(true).click();
//            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"));
//            requestAutoPage.documentsTabLocator.click();
            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style"));
        });
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });

    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Процесс стартует по заявке автоматизация (на информационные услуги) после перевода в статус 'Направлена в ЦКИ', заполнения обязательных полей и сохранения")
    void requestAutoInfoServicesHaveStartedProcessAfterFillinSentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoInfoServicesInDraftStatusFullPath();
        });
        sleep(2000);
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ, вернуться во вкладку Содержание заявки", () -> {
            requestAutoPage.informatsiiaTabLocator.hover().click();
            sleep(2000);
            requestAutoPage.requestsStatusBtnLocator.click();
            requestAutoPage.sentCkiStatusLocator.click();
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
        });
        step("Кнопка Редактировать на футере при смене статуса", () -> {
            requestAutoPage.editAfterChangeStatusBtn.shouldBe(visible, Duration.ofSeconds(10)).click();
        });
        step("Заполнить обязательные поля на вкладке Содержание заявки", () -> {
            // Тип "Заявка ФЗ"
            requestAutoPage.requestsTypeBtnLocator.click();
            requestAutoPage.requestsTypeZayavkaFzLocator.click();
            // Категория "Развитие"
            requestAutoPage.requestsCaregoryBtnLocator.click();
            requestAutoPage.requestsCaregoryTechPoddergka.click();
            // Приоритет "Высокий"
            requestAutoPage.requestsPriorityBtnLocator.click();
            requestAutoPage.requestsPriorityVisokiiLocator.click();
            // Год
//            requestAutoPage.requestsYearBtnLocator.click();
            // Программа "ПЦ"
            requestAutoPage.requestsProgramBtnLocator.scrollIntoView(true).click();
            requestAutoPage.requestsProgramPCLocator.click();
            // Функциональные заказчики
            requestAutoPage.setFunctionalCustomer(5);
            // Руководитель основного ФЗ заявки (автоматизация) - Должность, ИОФ
            requestAutoPage.setHeadOfMainFz();
            // Ответственные от ФЗ за подготовку заявки
            requestAutoPage.setFzResponsiblesForRequest();
            // Основания для выполнения работ
            requestAutoPage.requestsReasonToDoFieldLocator.scrollIntoView(true).setValue(requestAutoPage.requestsReasonToDo);
            // Цели работ
            requestAutoPage.requestsWorksPurposeFieldLocator.scrollIntoView(true).setValue("Цели работ");
            // Описание текущего состояния
            requestAutoPage.requestsCurrentConditionFieldLocator.scrollIntoView(true)
                    .setValue("Описание текущего состояния заявки (автоматизация)");
            // Предпосылки возникновения потребности в автоматизации
            requestAutoPage.requestsPreconditionToAutomateFieldLocator
                    .setValue("Предпосылки возникновения потребности в автоматизации заявки (автоматизация)");
            // Предполагаемые изменения в бизнес-процессах и организационной структуре
            requestAutoPage.requestsExpectedChangesOfBpAndStructureFieldLocator.scrollIntoView(true)
                    .setValue("Предполагаемые изменения в бизнес-процессах и организационной структуре заявки (автоматизация)");
            // Предложения по автоматизации бизнес-процессов
            requestAutoPage.requestsProposalsToAutomateBpFieldLocator
                    .setValue("Предложения по автоматизации бизнес-процессов заявки (автоматизация)");
        });
        step("Перейти на вкладку Документы и Добавить обязательные документы", () -> {
            requestAutoPage.requestsFullNameReadModeLocator.scrollIntoView(true);
            requestAutoPage.documentsTabLocator.click();
            // Заявка
            requestAutoPage.addDocumentBtnLocator.click();
            modalWindowsPage.addRequestDocumentOnModal(attachFile);
        });
//        requestAutoPage.saveOnFooterBtn.click();
        requestAutoPage.saveRequest();
        sleep(2000);
        // Процессы по заявкам автоматизация (любого вида, кроме ИТ-услуги), направленным в ЦКИ
        // с 01.02 по 30.04 включительно, стартуют сразу.
        // Если заявка на автоматизацию направлена в ЦКИ после 30.04,
        // процесс по ней не запускается. 01.02 следующего года выполняется проверка
        // и старт процессов по всем таким заявкам.
        if (today.isAfter(dayBeforeOfPeriodForProcessImmediateStart) && today.isBefore(dayAfterOfPeriodForProcessImmediateStart)) {
            System.out.println("День направления в ЦКИ " + today + " попал под условие с 01.02 по 30.04 включительно, поэтому процесс должен стартовать сразу, а не с 1.02 следующего года");
            refresh();
            step("Cтатус Направлена в ЦКИ в заголовке после сохранения, заявка в режиме просмотра", () -> {
                requestAutoPage.requestsStatusOnHeaderLocator.shouldHave(text("Направлена в ЦКИ"));
                requestAutoPage.requestsFooterRedactMode.shouldNot(exist);
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
            refresh();
            step("Cтатус Направлена в ЦКИ в заголовке после сохранения, заявка в режиме просмотра", () -> {
                requestAutoPage.requestsStatusOnHeaderLocator.shouldHave(text("Направлена в ЦКИ"));
                requestAutoPage.requestsFooterRedactMode.shouldNot(exist);
            });
            step("Процесс не запущен и ждет активации с 1.02. Нет 'ID процесса'", () -> {
                requestAutoPage.requestsProcessGoOnHeaderLocator.shouldNot(exist);
                requestAutoPage.requestsProcessIdOnHeaderLocator.shouldNot(exist);
            });
        }
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }




}