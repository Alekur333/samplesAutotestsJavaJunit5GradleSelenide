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

@DisplayName("Проверки Заявок (автоматизация) Сопровождение")
@Tags({@Tag("ui"), @Tag("requestAutoSupport"), @Tag("regress")})
public class TestRequestAutoSupportUi extends TestBaseUi {

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    public int sec = 10;

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("requierdFields")})
    @DisplayName("Тест обязательности полей в новой Заявке (автоматизация) Сопровождение статус Черновик")
    void requestAutoSupportNewRequierdFieldsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            open("/#?page=request&owner=mine");
        });
        step("Открыть страницу создания  новой заявки", () -> {
            $("#request-new").click();
            $x("//a[text() = 'Сопровождение']").click();
            $("[title='Новая заявка']")
                    .shouldHave(text("Новая заявка (Заявка на сопровождение программного обеспечения)"));
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
    @DisplayName("Создание новой заявки (автоматизация) Сопровождение с заполнением обязательных полей статус Черновик")
    void requestAutoSupportNewOnlyRequiredFieldsSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoSupport();
        });
        step("Запонить поля 'Полное наименование', 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Нажать кнопку Сохранить", () -> {
            $(".btnSave").click();
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
    @DisplayName("Редактирование заявки (автоматизация) Сопровождение с изменением и добавлением полей статус Черновик")
    void requestAutoSupportUpdateAndSaveTest() {
        step("Открыть страницу создания  новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage().createRequestAutoSupport();
        });
        step("Заполнить обязательное поле 'Полное наименование' и 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Нажать кнопку Сохранить", () -> {
            requestAutoPage.saveOnFooterBtn.click();
        });
        step("Отредактировать заявку и сохранить", () -> {
            step("Открыть заявку на редактирование", () -> {
                requestAutoPage.openRequestAutoToEdit();
            });
            step("Отредактировать обязательное поле 'Полное наименование' и 'Краткое наименование'", () -> {
                requestAutoPage.fillUpdatedFullAndShortNameFiels();
            });
            step("Нажать кнопку Сохранить", () -> {
                requestAutoPage.saveOnFooterBtn.click();
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
    @DisplayName("Проверка содержания дропдауна 'Тип' заявки (автоматизация) Сопровождение")
    void requestAutoSupportDropdownTypeTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAutoSupport();
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
    @DisplayName("Проверка содержания дропдауна 'Категория' заявки (автоматизация) Сопровождение")
    void requestAutoSupportCaregoryDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAutoSupport();
        });
        step("Проверить содержание дропдауна 'Категория' заявки", () -> {
            step("Есть пункт \"Авторская поддержка\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryBtnLocator.click();
                requestAutoPage.requestsCaregoryAvtorskaiaPoddergka
                        .shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("Есть пункт \"Настройка и модификация ПО (по заявкам)\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryNastroikaPoLocator
                        .shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 2 пункта", () -> {
                requestAutoPage.requestsCaregoryList.shouldHave(size(2));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Приоритет' заявки (автоматизация) Сопровождение")
    void requestAutoSupportPriorityDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAutoSupport();
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
    @DisplayName("Проверка содержания дропдауна 'Программа' заявки автоматизация (Сопровождение)")
    void requestAutoSupportProgramDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация)", () -> {
            requestAutoPage.goToRequestAutoPage();
        });
        step("Открыть страницу создания новой заявки", () -> {
            requestAutoPage.createRequestAutoSupport();
        });
        step("Проверить содержание дропдауна 'Программа' заявки", () -> {
            step("Есть пункт \"ПЦ\" в дропдауне", () -> {
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
    @DisplayName("Проверка подсветки обязательных полей в статусе 'Направлена в ЦКИ' заявки на автоматизацию (Сопровождение)")
    void requestAutoSupportRequierdFieldsBrightsBySentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoSupportInDraftStatusFullPath();
        });
        requestAutoPage.editBtn.click();
        sleep(2000);
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.hover().click();
            requestAutoPage.requestsStatusBtnLocator.click();
            requestAutoPage.sentCkiStatusLocator.click();
        });
//        step("Кнопка Редактировать на футере при смене статуса", () -> {
//            requestAutoPage.editAfterChangeStatusBtn.click();
//        });
//        sleep(1000);
        step("Подсвечены обязательными вкладки Содержание, Документы", () -> {
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"), Duration.ofSeconds(5));
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
            // Основания для выполнения работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-reasonField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-reasonField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Назначение системы со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-systemPurposeField .necessary").shouldHave(text("*"));
            $(".js-mate-insertion-systemPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
            // Цели работ со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-createPurposeField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-createPurposeField .b-validation").shouldHave(cssClass("b-validation_show_error"));
        });
        //        step("Есть признаки обязательности у вкладки 'Документы' и кнопки 'Добавить документ'", () -> {
//            requestAutoPage.documentsTabLocator.hover();
//            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"));
//            requestAutoPage.documentsTabLocator.click();
//            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style"));
//        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Процесс стартует по заявке на автоматизацию (Сопровождение) после перевода в статус 'Направлена в ЦКИ', заполнения обязательных полей и сохранения")
    void requestAutoSupportHaveStartedProcessAfterFillinSentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoSupportInDraftStatusFullPath();
        });
        sleep(2000);
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.click();
            requestAutoPage.requestsStatusBtnLocator.click();
            requestAutoPage.sentCkiStatusLocator.click();
        });
        step("Кнопка Редактировать на футере при смене статуса", () -> {
            requestAutoPage.editAfterChangeStatusBtn.shouldBe(visible, Duration.ofSeconds(10)).click();
            sleep(2000);
        });
        step("Добавить обязательные документы", () -> {
            requestAutoPage.requestsFullNameReadModeLocator.scrollIntoView(true);
            requestAutoPage.documentsTabLocator.click();
            step("Добавить документ Заявка", () -> {
                requestAutoPage.addDocumentBtnLocator.click();
                modalWindowsPage.addRequestDocumentOnModal(attachFile);
            });
        });
        step("Вернуться во вкладку Содержание заявки и Заполнить обязательные поля", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            step("Тип - 'Заявка ФЗ'", () -> {
                requestAutoPage.requestsTypeBtnLocator.click();
                requestAutoPage.requestsTypeZayavkaFzLocator.click();
            });
            step("Категория- 'Авторская поддержка'", () -> {
                requestAutoPage.requestsCaregoryBtnLocator.click();
                requestAutoPage.requestsCaregoryAvtorskaiaPoddergka.click();
            });
            step("Приоритет - 'Высокий'", () -> {
                requestAutoPage.requestsPriorityBtnLocator.click();
                requestAutoPage.requestsPriorityVisokiiLocator.click();
            });
            step("Функциональные заказчики", () -> {
                requestAutoPage.setFunctionalCustomer(5);
            });
            step("Руководитель основного ФЗ - Должность, ИОФ", () -> {
                requestAutoPage.setHeadOfMainFz();
            });
            step("Ответственные от ФЗ за подготовку заявки", () -> {
                requestAutoPage.setFzResponsiblesForRequest();
            });
            step("Основания для выполнения работ", () -> {
                requestAutoPage.requestsReasonToDoFieldLocator.scrollIntoView(true)
                        .setValue(requestAutoPage.requestsReasonToDo);
            });
            step("Назначение системы", () -> {
                requestAutoPage.requestsSystemPurposeFieldLocator.setValue(requestAutoPage.systemPurposeField);
            });
            step("Цели работ", () -> {
                requestAutoPage.requestsWorksPurposeFieldLocator.scrollIntoView(true).setValue("Цели работ");
            });
        });

//        requestAutoPage.saveOnFooterBtn.shouldBe(enabled, Duration.ofSeconds(5)).click();
        requestAutoPage.saveOnFooterBtn.shouldBe(enabled, Duration.ofSeconds(5)).click();
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
//                requestAutoPage.requestsFooterRedactMode.shouldNot(exist);
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
                requestAutoPage.requestsFooterRedactMode.shouldNot(exist, Duration.ofSeconds(5));
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