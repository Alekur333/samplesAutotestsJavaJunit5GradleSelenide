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

@DisplayName("Проверки Заявок на автоматизацию (Оборудование)")
@Tags({@Tag("ui"), @Tag("requestAutoEquipment"), @Tag("regress")})
public class TestRequestAutoEquipmentUi extends TestBaseUi {

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }
    String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    public int sec = 10;

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("requierdFields")})
    @DisplayName("Тест обязательности полей в новой Заявке на автоматизацию (Оборудование) в статусе Черновик")
    void requestAutoEquipmentNewRequierdFieldsTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
            $("[title='Новая заявка']")
                    .shouldHave(text("Новая заявка (Оборудование)"));
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
        step("Функциональные заказчики со звездочкой, кнопка добавления с подсветкой", () -> {
            $(".js-mate-insertion-customersField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-customersField .b-validation").shouldHave(cssClass("b-validation_show_error"));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("create")})
    @DisplayName("Создание новой заявки на автоматизацию (Оборудование) с заполнением обязательных полей")
    void requestAutoEquipmentNewOnlyRequiredFieldsSaveTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
        });
        step("Ввести текст в обязательное поле 'Полное наименование' и 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать обязательного Функционального заказчика", () -> {
            requestAutoPage.setFunctionalCustomer(sec);
        });
        step("Нажать кнопку Сохранить", () -> {
//            $(".btnSave").click();
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
    @DisplayName("Редактирование заявки на автоматизацию (Оборудование) с изменением и добавлением данных полей")
    void requestAutoEquipmentUpdateAndSaveTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
        });
        step("Ввести текст в обязательное поле 'Полное наименование' и 'Краткое наименование'", () -> {
            requestAutoPage.fillFullAndShortNameFiels();
        });
        step("Выбрать обязательного Функционального заказчика", () -> {
            requestAutoPage.setFunctionalCustomer(sec);
        });
        step("Нажать кнопку Сохранить", () -> {
//            requestAutoPage.saveOnFooterBtn.click();
            requestAutoPage.saveRequest();
        });
        step("Отредактировать заявку и сохранить", () -> {
            step("Открыть заявку на редактирование", () -> {
                requestAutoPage.editBtn.click();
            });
            step("Редактировать текст в поле 'Полное наименование' и 'Краткое наименование'", () -> {
                requestAutoPage.fillUpdatedFullAndShortNameFiels();
            });
            step("Нажать кнопку Сохранить", () -> {
//                $(".btnSave").click();
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
    @DisplayName("Проверка содержания дропдауна 'Тип' на автоматизацию (Оборудование)")
    void requestAutoEquipmentDropdownTypeTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
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
    @DisplayName("Проверка содержания дропдауна 'Категория' на автоматизацию (Оборудование)")
    void requestAutoEquipmentCaregoryDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
        });
        step("Проверить содержание дропдауна 'Категория' заявки", () -> {
            step("Есть пункт \"Поставка оборудования (с 2021)\" в дропдауне", () -> {
                requestAutoPage.requestsCaregoryBtnLocator.click();
                requestAutoPage.requestsCaregoryPostavkaOborLocator.shouldBe(visible, Duration.ofSeconds(sec));
            });
            step("В дропдауне всего 1 пункт", () -> {
                requestAutoPage.requestsCaregoryList.shouldHave(size(1));
            });
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke"), @Tag("dropdown")})
    @DisplayName("Проверка содержания дропдауна 'Приоритет' заявки на автоматизацию (Оборудование)")
    void requestAutoEquipmentPriorityDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
        });
        step("Проверить содержание дропдауна 'Приоритет' заявки", () -> {
            step("Есть пункт \"Высокий\" в дропдауне", () -> {
                requestAutoPage.requestsPriorityBtnLocator.click();
                requestAutoPage.requestsPriorityVisokiiLocator.shouldBe(visible, Duration.ofSeconds(sec));
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
    @DisplayName("Проверка содержания дропдауна 'Программа' заявки на автоматизацию (Оборудование)")
    void requestAutoEquipmentProgramDropdownsTest() {
        step("Перейти в раздел Заявки (автоматизация) и открыть страницу создания новой заявки", () -> {
            requestAutoPage.goToRequestAutoPage();
            requestAutoPage.createRequestAutoEquipment();
        });
        refresh();
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
    @DisplayName("Проверка подсветки обязательных полей в статусе 'Направлена в ЦКИ' заявки на автоматизацию (Оборудование)")
    void requestAutoEquipmentRequierdFieldsBrightsBySentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoEquipmentInDraftStatusFullPath();
        });
        step("Перейти в режим редактирования ", () -> {
            requestAutoPage.openRequestAutoToEdit();
        });
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.click();
            requestAutoPage.requestsStatusBtnLocator.click();
            requestAutoPage.sentCkiStatusLocator.click();
        });
        step("Подсвечены обязательными вкладки Содержание, Спецификации, Документы", () -> {
            requestAutoPage.documentsTabLocator.parent().shouldHave(cssClass("invalid"));
            requestAutoPage.soderganieZaiyavkiTabLocator.parent().shouldHave(cssClass("invalid"));
            requestAutoPage.specificationHardwareTabLocator.parent().shouldHave(cssClass("invalid"));
        });
        sleep(2000);
        step("Вернуться во вкладку Содержание заявки и проверить, что обязательные поля со звездочкой и подсвечены (из настроек вида заявки)", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            // Тип Заявки со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-typeField .necessary").shouldHave(text("*"));
            $("#dropDownWrapper_typeField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Год со звездочкой
            $(".js-mate-insertion-yearField .necessary").scrollIntoView(true).shouldHave(text("*"));
            // Программа со звездочкой, поле ввода с подсветкой
            $(".js-mate-insertion-programField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $("#dropDownWrapper_programField").parent().shouldHave(cssClass("b-validation_show_error"));
            // Телефон исполнителя со звездочкой, поле ввода с подсветкой
//            $(".js-mate-insertion-executorPhoneField .necessary").scrollIntoView(true).shouldHave(text("*"));
            requestAutoPage.requestsExecutorsPhoneFieldLocator.parent().shouldHave(cssClass("b-validation_show_error"));
            // Ответственные на объектах внедрения со звездочкой, кнопка добавления с подсветкой
            $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField .necessary").scrollIntoView(true).shouldHave(text("*"));
            $(".js-mate-insertion-responsiblePersonsOnInceptionObjectsField .b-validation").shouldHave(cssClass("b-validation_show_error"));
        });
        step("Есть подсветка кнопки 'Добавить элемент' на вкладке 'Спецификация'", () -> {
            requestAutoPage.specificationHardwareTabLocator.scrollIntoView(true);
            requestAutoPage.specificationHardwareTabLocator.click();
            requestAutoPage.addHardwareSpecificationsElementBtnLocator.shouldHave(cssClass("validation-element_btn"));
        });
        step("Есть подсветка кнопки 'Добавить документ' на вкладке 'Документы'", () -> {
            requestAutoPage.documentsTabLocator.scrollIntoView(true);
            requestAutoPage.documentsTabLocator.click();
            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style"));
//            requestAutoPage.addDocumentBtnLocator.shouldHave(attribute("style=color: red;"));
        });
        step("Удалить созданную заявку", () -> {
            requestAutoPage.deleteRequestByIdOnCardApi(authCookie);
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("regress"), @Tag("requierdFields")})
    @DisplayName("Процесс стартует по заявке Оборудование после перевода в статус 'Направлена в ЦКИ', заполнения обязательных полей и сохранения")
    void requestAutoEquipmentHaveStartedProcessAfterFillinSentCkiStatusTest() {
        step("Создать тестовую зявку в статусе Черновик", () -> {
            requestAutoPage.createRequestAutoEquipmentInDraftStatusFullPath();
        });
        sleep(1500);
        step("Перейти во вкладку Информация и выбрать статус Направлена в ЦКИ", () -> {
            requestAutoPage.informatsiiaTabLocator.click();
            requestAutoPage.requestsStatusBtnLocator.click();
            requestAutoPage.sentCkiStatusLocator.click();
        });
        step("Кнопка Редактировать на футере при смене статуса", () -> {
            requestAutoPage.editAfterChangeStatusBtn.shouldBe(visible, Duration.ofSeconds(10)).click();
        });
        sleep(2000);
        step("Вернуться во вкладку Содержание заявки и Заполнить обязательные поля", () -> {
            requestAutoPage.soderganieZaiyavkiTabLocator.click();
            step("Тип 'Заявка ФЗ'", () -> {
                requestAutoPage.requestsTypeBtnLocator.click();
                requestAutoPage.requestsTypeZayavkaFzLocator.click();
            });
            step("Программа 'ПЦ'", () -> {
                requestAutoPage.requestsProgramBtnLocator.scrollIntoView(true).click();
                requestAutoPage.requestsProgramPCLocator.click();
            });
            step("Телефон исполнителя", () -> {
                requestAutoPage.requestsExecutorsPhoneFieldLocator.scrollIntoView(true);
                requestAutoPage.requestsExecutorsPhoneFieldLocator.setValue("Телефон исполнителя autoTest");
            });
            step("Ответственные на объектах внедрения", () -> {
                requestAutoPage.setResposiblesOnIntegrationObj();
            });
        });
        step("Перейти на вкладку Спецификации и Добавить элемент спецификации", () -> {
            requestAutoPage.specificationHardwareTabLocator.scrollIntoView(true).click();
            requestAutoPage.addHardwareSpecificationsElementBtnLocator.scrollIntoView(true).click();
            modalWindowsPage.firstCheckboxElementOnModal.click();
            modalWindowsPage.chooseOnModalBtn.click();
        });
        step("Заполнить обязательные атрибуты элемента", () -> {
            step("Ранг", () -> {
                requestAutoPage.specificationsRangField.shouldBe(visible, Duration.ofSeconds(2)).hover().click();
                requestAutoPage.specificationsRangInputField.shouldBe(visible, Duration.ofSeconds(2)).setValue("3");
            });
            step("Получатель", () -> {
                requestAutoPage.specificationsRecipientField.hover().doubleClick();
                modalWindowsPage.firstRadioElementOnModal.click();
                modalWindowsPage.chooseOnModalBtn.click();
            });
            requestAutoPage.pktbCttField.scrollIntoView(true);
            step("Кол-во ФЗ", () -> {
                requestAutoPage.specificationsQuantityField.hover().click();
                modalWindowsPage.specificationsFistRequestedQuantityField.click();
                modalWindowsPage.specificationsFirstRequestedQuantityInputField.setValue("3");
                modalWindowsPage.chooseOnModalBtn.click();
            });
            step("Обоснование", () -> {
                requestAutoPage.commentCkiField.scrollIntoView(true);
                requestAutoPage.specificationsReasonField.hover().doubleClick();
                requestAutoPage.specificationsReasonTextareaField.setValue("Обоснование autoTest");
                requestAutoPage.requestsFooterRedactMode.hover().click();
            });
        });
        step("Добавить обязательные документы", () -> {
            requestAutoPage.documentsTabLocator.click();
            step("Добавить документ Заявка", () -> {
                requestAutoPage.addDocumentBtnLocator.click();
                modalWindowsPage.addRequestDocumentOnModal(attachFile);
//                sleep(1000);
            });
        });
        step("Сохранить заявку", () -> {
//            requestAutoPage.saveOnFooterBtn.shouldBe(enabled, Duration.ofSeconds(10)).click();
            requestAutoPage.saveRequest();
        });
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

