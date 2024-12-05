package aleks.kur.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import ru.progredis.models.contract.ContractCalendarPlanWorkPost;
import ru.progredis.models.general.IdFullShort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.pages.SqlRequestsPage.updateDbTableV2LongFieldByObjectId;
import static ru.progredis.pages.SqlRequestsPage.updateDbTableV4LongFieldByObjectId;
import static ru.progredis.tests.TestBase.*;
import static ru.progredis.tests.TestBase.currentDateYyyyMmDdHyphenPlusOrMinusMonthes;
import static ru.progredis.tests.TestBase.documentsPage;
import static ru.progredis.tests.TestBase.dtf_dMy_Dots_Stat;
import static ru.progredis.tests.TestBase.filesPage;
import static ru.progredis.tests.TestBase.login;
import static ru.progredis.tests.TestBase.passwd;
import static ru.progredis.tests.TestBase.usersPage;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class ContractsPage {
    private final PlanPiWorksPage planPiWorksPage = new PlanPiWorksPage();
    private final MainPage mainPage = new MainPage();

    public Map<String, String> requestData = new HashMap<>();

    public SelenideElement
            // заголовок
            headerNameLocator = $(".work-space-content-header h3"),

    // вкладка Ввод в ОЭ/ПЭ
    putInOePeTab = $("[data-code='tabCommissioning']"),
            calendarPlanIframe = $("#commissionList>iframe"), // фрейм страницы вкладки Ввод в ОЭ/ПЭ
            processTasksModalForCalendarPlanWorkframe = $("#iframe-modal-root iframe"), // фрейм модалки задач по процессу работы КП
            calendarPlanHeading = $(byText("Календарный план договора")), // заголовок
            calendarPlanEditBtn = $("button[title='Редактировать']"), // кнопка Редактировать КП
            calendarPlanRecountStagesBtn = $("button.MuiButtonBase-root", 1), // кнопка Пересчитать этапы ввода
            commissioningHeading = $(byText("Ввод в эксплуатацию")), // заголовок Ввод в эксплуатацию
            dropdownContractSubtypeTabCommissioningBtn = $("div#mui-component-select-subtypeId").parent(), // дропдаун Выберите подтип договора
            dropdownContractSubtypeListTabCommissioning = $("ul.MuiMenu-list").parent(), // список дропдауна Выберите подтип договора
            dropdownContractSubtypeValueDefault = dropdownContractSubtypeListTabCommissioning.$("li", 0), // "Выберите подтип договора" дропдауна Выберите подтип договора
            dropdownContractSubtypeValueAdaptationAndImplementation = dropdownContractSubtypeListTabCommissioning.$("li", 1), // "Адаптация и внедрение" дропдауна Выберите подтип договора
            dropdownContractSubtypeValueExtention = dropdownContractSubtypeListTabCommissioning.$("li", 2), // "Развитие" дропдауна Выберите подтип договора
            dropdownContractSubtypeValueDevelopment = dropdownContractSubtypeListTabCommissioning.$("li", 3), // "Разработка" дропдауна Выберите подтип договора
            warningNoDetailedPlanOnTabCommissioning = $("p[data-testid='@app4/Typography']"), // предупреждение - нужен Не развернутый КП
            tableHeadOnTabCommissioning = $("thead.MuiTableHead-root"), // строка заголовков таблицы
            addBtnOnTabCommissioning = $("div.MuiStack-root").$(withText("Добавить")), // кнопка Добавить
            cancelBtnOnTabCommissioning = $("div.MuiStack-root").$(withText("Отмена")), // кнопка Отмена
            saveBtnOnTabCommissioning = $("div.MuiStack-root").$(withText("Сохранить")), // кнопка Сохранить
            orderedNumberCellOnTabCommissioningTable = $("input[name='calendarPlan.0.number']").parent(), // ячейка таблицы № п/п
            nameCellOnTabCommissioningTable = $("input[name='calendarPlan.0.name']").parent(), // ячейка таблицы Наименование работ
            asuCellOnTabCommissioningTable = $("input[name='calendarPlan.0.asuId']").parent(), // ячейка таблицы АСУ
            asuListOnTabCommissioningTable = $("div[id='menu-calendarPlan.0.asuId']  ul[role='listbox']"), // список АСУ в дропдауне
            startDateCellOnTabCommissioningTable = $("input[placeholder='ДД.ММ.ГГГГ']", 0).parent(), // ячейка таблицы Начало
            endDateCellOnTabCommissioningTable = $("input[placeholder='ДД.ММ.ГГГГ']", 1).parent(), // ячейка таблицы Окончание
            requiredOeCellOnTabCommissioningTable = $("td.MuiTableCell-root input[type='checkbox']"), // ячейка таблицы Требуется ОЭ
            contractCalendarPlanBlockOnTabCommissioningTable = $(byText("Календарный план договора")).parent().parent(), // блок Календарный план договора
            comissioningBlockOnTabCommissioning = $(".MuiBox-root>.MuiStack-root>.MuiBox-root>.MuiStack-root>.MuiStack-root"), // 1й блок Ввод в эксплуатацию
            processTasksModalForCalendarPlanWorkOnTabCommissioning = $("div[data-testid='@app4/dialog'] div.MuiPaper-elevation"), // модалка Текущие задачи по id процесса работы


    // меню Действия
    actionsBtn = $("#agreementnolimited").parent(),
            FormSoftwareAcceptanceCommitteeBtn = $(".pi-dropdown-extra>ul").$(byText("Собрать предложения в комиссию по приемке ПО")),
    //  таблица документов
    stateIconOnDocString = $("tr.k-master-row [role='gridCell'] svg", 0), // первая иконка состояния процесса
            documetsTableInContract = $("#documentList") // таблица доков
                    ;
    // таблица работ календарного плана договора
    public ElementsCollection
            contractCalendarPlanWorks = $$("div.ag-root[aria-colcount='7'] div.ag-row"), // строки работ в режиме просмотра КП на фрейме
            contractCalendarPlanWorksEditMode = $$("tbody.MuiTableBody-root tr"), // строки работ в режиме редактирования КП на фрейме
            deleteCalendarPlanWorkBtnOnTabCommissioning = $$("button[data-testid='@app4/actions/icon-button']"), // кнопка Удалить в строке работ КП договора
            contractCalendarPlanWorkNameOnTabCommissioning = contractCalendarPlanBlockOnTabCommissioningTable.$$("div[row-id]>div[col-id='name']"), // имя работы на блоке Календарный план договора
            contractCalendarPlanWorkProcessIdOnTabCommissioning = $$("div.ag-cell[col-id='processId'] button.MuiButton-root"), // номер процесса работы на блоке Календарный план договора
            processTaskRowsOnModalForCalendarPlanWork = processTasksModalForCalendarPlanWorkOnTabCommissioning.$$("div.ag-center-cols-container>div.ag-row"), // строки задач процесса в модалке Текущие задачи по id процесса работы

    // блок Ввод в эксплуатацию
    comissioningBlockAllOnTabCommissioning = $$(".MuiBox-root>.MuiStack-root>.MuiBox-root>.MuiStack-root>.MuiStack-root"), // блоки Ввод в эксплуатацию
            comissioningBlockNameOnTabCommissioning = comissioningBlockOnTabCommissioning.$$(".MuiBox-root"), // имя блока Ввод в эксплуатацию
            comissioningBlockCommissionHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$(byText("Комиссия для ввода в эксплуатацию")), // заголовок Комиссии в блоке Ввод в эксплуатацию
            comissioningBlockEsppNumberHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$(byText("Номер проекта ЕСПП:")), // заголовок номера ЕСПП в блоке Ввод в эксплуатацию
            comissioningBlockTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$(byText("Ход ввода в эксплуатацию")), // заголовок таблицы Хода ввода в блоке Ввод в эксплуатацию
//            comissioningBlockTermOrdBtnOnTabCommissioning = comissioningBlockOnTabCommissioning.$$(byText("Срок по ОРД")), // кнопка Срок по ОРД
            comissioningBlockTermOrdBtnOnTabCommissioning = $$x("//button[text()='Срок по ОРД']"), // кнопка Срок по ОРД
            comissioningBlockEditChairmanBtnOnTabCommissioning = $(byText("Председатель")).parent().$$("button"), // кнопка Редактировать Председатель
            comissioningBlockEditDeputyChairmanBtnOnTabCommissioning = $(byText("Зам. председателя")).parent().$$("button"), // кнопка Редактировать Председатель
            comissioningBlockEditMembersBtnOnTabCommissioning = $(byText("Участники:")).parent().$$("button"), // кнопка Редактировать Участники
            comissioningBlockExportExcelBtnOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("a.MuiIconButton-root"), // кнопка Экспорт в Excel
            comissioningBlockOrderedNumberCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='ag-Grid-AutoColumn']"), // ячейка колонки № заголовка таблицы Ход ввода
            comissioningBlockNameCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='name']"), // ячейка колонки Название заголовка таблицы Ход ввода
            comissioningBlockStatusCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='status']"), // ячейка колонки Статус заголовка таблицы Ход ввода
            comissioningBlockEsppObjectCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='esppObject']"), // ячейка колонки Объект ЕСПП заголовка таблицы Ход ввода
            comissioningBlockTermOrdCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='ordDate']"), // ячейка колонки Срок по ОРД заголовка таблицы Ход ввода
            comissioningBlockStartCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='1']>div[col-id='0_0']"), // ячейка колонки Начало заголовка таблицы Ход ввода
            comissioningBlockStartDatePlanCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='startDatePlan']"), // ячейка колонки Начало-план заголовка таблицы Ход ввода
            comissioningBlockStartDateFactCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='startDateFact']"), // ячейка колонки Начало-факт заголовка таблицы Ход ввода
            comissioningBlockEndCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='1']>div[col-id='1_0']"), // ячейка колонки Окончание заголовка таблицы Ход ввода
            comissioningBlockEndDatePlanCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='endDatePlan']"), // ячейка колонки Окончание-план заголовка таблицы Ход ввода
            comissioningBlockEndDateFactCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='endDateFact']"), // ячейка колонки Окончание-факт заголовка таблицы Ход ввода
            comissioningBlockLagCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='1']>div[col-id='2_0']"), // ячейка колонки Отставание заголовка таблицы Ход ввода
            comissioningBlockLagStartCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='beginDelayInDays']"), // ячейка колонки Отставание-начало заголовка таблицы Ход ввода
            comissioningBlockLagEndCellOnTableHeaderOnTabCommissioning = comissioningBlockOnTabCommissioning.$$("div[aria-rowindex='2']>div[col-id='endDelayInDays']") // ячейка колонки Отставание-окончание заголовка таблицы Ход ввода
                    ;

    public String
            contractsPageLink = "#?page=reference&subpage=directions", // страница Направления
            contractsName = "ContractsNameAutoTest_" + faker.lorem().characters(3),
            contractsFullName = "directionFullNameAutoTest_" + faker.lorem().characters(3),
            contractsDescription = "directionDescriptionAutoTest_" + faker.lorem().characters(3),
            warningNoDetailedPlanOnTabCommissioningText = "Обратите внимание - следует вводить НЕ развернутый календарный план";

    public void stateIconOnDocStringSvgAttrsApp4Checking() {
        stateIconOnDocString.$("circle")
                .shouldHave(
                        attribute("cx", "32"),
                        attribute("cy", "32"),
                        attribute("r", "25"));
        stateIconOnDocString.$("rect")
                .shouldHave(
                        attribute("x", "25"),
                        attribute("y", "25"),
                        attribute("width", "14"),
                        attribute("height", "14")
                );
    }

    public void addStageInCalendarPlanOnContractComissioningOePeTab() {
        mainPage.closeSidebarBtn.click();
        switchTo().frame(calendarPlanIframe);
        calendarPlanEditBtn.click();
        addBtnOnTabCommissioning.click();
    }

    public void openContractsCardCommissioningTab(int contractsId) {
        String linkToContractsCardDocsTab = "#?jump=agreementnolimited&subpage=show&id=" + contractsId + "&tab=tabCommissioning";
        open(linkToContractsCardDocsTab);
    }

    public void openContractsCardDocsTab(int contractsId) {
        String linkToContractsCardDocsTab = "#?jump=agreementnolimited&subpage=show&id=" + contractsId + "&tab=tabDocuments";
        open(linkToContractsCardDocsTab);
    }

    public void openContractsCardMainInfoTab(int contractsId) {
        String linkToContractsCardMainTab = "#?jump=agreementnolimited&subpage=show&id=" + contractsId;
        open(linkToContractsCardMainTab);
    }

    // заполнить все обязательные поля в строке этапа клендарного плана на странице
    public void fillRequiredFieldsForStageOfCalendarPlanOnTabCommissioningTableUi(int contractId, int numberOfMonthesToFinishDate, String orderedNumber) {
        step("Перейти на вкладку 'Ввод в ОЭ/ПЭ' тестдоговора в режиме редактирования во фрейм с элементами", () -> {
            openContractsCardCommissioningTab(contractId);
            mainPage.closeSidebarBtn.click();
            switchTo().frame(calendarPlanIframe);
            calendarPlanEditBtn.click();
            addBtnOnTabCommissioning.click();
        });
        step("Заполнить все обязательные поля для добавления этапа КП", () -> {
            // подтип договора Адаптация и внедрение
            dropdownContractSubtypeTabCommissioningBtn.click();
            dropdownContractSubtypeValueAdaptationAndImplementation.click();
            // название этапа
            nameCellOnTabCommissioningTable.$("input").setValue("Тест-этап календарного плана");
            // дата начала / окончания
            String nowDate_dMy = dtf_dMy_Dots_Stat.format(LocalDateTime.now());
            String nowDatePlusMonthes_dMy = dtf_dMy_Dots_Stat.format(LocalDateTime.now().plusMonths(numberOfMonthesToFinishDate));
            startDateCellOnTabCommissioningTable.click();
            startDateCellOnTabCommissioningTable.$("input").sendKeys(nowDate_dMy);
            endDateCellOnTabCommissioningTable.click();
            endDateCellOnTabCommissioningTable.$("input").sendKeys(nowDatePlusMonthes_dMy);
            // Вставить значение в поле №
            orderedNumberCellOnTabCommissioningTable.$("input").setValue(orderedNumber);
        });

    }

    // тело нового договора
    public Map<String, Object> newContractBodyWithWorkHashJson(String contractsName, int contractsKindId, int contractsTypeId, int[] planPiWorkId) {
        String cookie = AuthApiPage.getAuthCookie(login, passwd);
        int agreementManagerId = usersPage.getIdFirstPersonInRole(cookie, "AGREEMENT_MANAGER");
        String shortNameKind = switch (contractsKindId) {
            case 1 -> "Оборудование";
            case 2 -> "СПО";
            case 3 -> "ППО";
            default -> null;
        };
        String shortNameType = switch (contractsTypeId) {
            case 1 -> "Договор";
            case 2 -> "Ордер-заказ";
            case 3 -> "Заказ-наряд";
            case 4 -> "Договор рамочный";
            default -> null;
        };
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", contractsName);
        Map<String, Object> agreementType = new HashMap<>();
        agreementType.put("id", contractsTypeId);
        agreementType.put("shortName", shortNameType);
        requestData.put("agreementType", agreementType);
        Map<String, Object> agreementKind = new HashMap<>();
        agreementKind.put("id", contractsKindId);
        agreementKind.put("shortName", shortNameKind);
        requestData.put("agreementKind", agreementKind);
        Map<String, Object> agreementStatus = new HashMap<>();
        agreementStatus.put("id", 2);
        agreementStatus.put("code", "draft");
        requestData.put("agreementStatus", agreementStatus);
        Map<String, Object> agreementManager = new HashMap<>();
        agreementManager.put("id", agreementManagerId);
        requestData.put("agreementManager", agreementManager);
        List<Map<String, Object>> executors = new ArrayList<>();
        Map<String, Object> executor = new HashMap<>();
        executor.put("main", true);
        Map<String, Integer> executorOrgId = new HashMap<>();
        executorOrgId.put("id", 340);
        executor.put("organization", executorOrgId);
        executors.add(executor);
        requestData.put("executors", executors);
        List<Map<String, Object>> customers = new ArrayList<>();
        Map<String, Object> customer = new HashMap<>();
        customer.put("main", true);
        Map<String, Integer> customerOrgId = new HashMap<>();
        customerOrgId.put("id", 11);
        customer.put("organization", customerOrgId);
        customers.add(customer);
        requestData.put("customers", customers);
        List<Map<String, Object>> worksList = new ArrayList<>();
        for (int id : planPiWorkId) {
            Map<String, Object> work = new HashMap<>();
            work.put("id", id);
            worksList.add(work);
        }
        requestData.put("planPiWorks", worksList);
        return requestData;
    }

    // создать договор с работами плана ПИ и вернуть id договора
    public int createContractWithPlanPiWorksAndGetIdApi(String authCookie, String contractsName, int contractKindId, int contractTypeId, int numberOfPlanPiWorks, int[] asuId) {
        // создать работы плана Пи в количестве numberOfWorks с АСУ из asuForPlanPiWorksId
        int[] planPiWorkIdArr = planPiWorksPage.createdSeveralPlanPiWorksList(authCookie, numberOfPlanPiWorks, asuId);
        // создать тело запроса
        Map<String, Object> contractBody = newContractBodyWithWorkHashJson(contractsName, contractKindId, contractTypeId, planPiWorkIdArr);
        // сделать запрос
        int contractsId =
                given()
                        .spec(requestSpec)
                        .body(contractBody)
                        .cookie("PLAY_SESSION", authCookie)
//                        .log().all()
                        .when()
                        .post(BASE_URL + "/agreements")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        return contractsId;
    }

    // создать договор с работами плана ПИ и работами календарного плана и вернуть id договора
    public int idOfCreatedContractWithPlanPiWorksAndCalendarPlanWorksApi(
            String authCookie, String contractsName, int contractKindId, int contractTypeId,
            int numberOfPlanPiWorks, int[] asuId, int contractSubtypeId, int numberOfCalendarPlanWorks
    ) throws InterruptedException {
        // Создать новый договор ППО с подтипом
        int contractId = createContractWithPlanPiWorksAndGetIdApi(authCookie, contractsName, contractKindId, contractTypeId, numberOfPlanPiWorks, asuId);
        putContractSubtypeApi(authCookie, contractId, contractSubtypeId).then().assertThat().statusCode(200);
        Thread.sleep(1000);

        // Создать работы КП договора
        ContractCalendarPlanWorkPost[] works = createdSeveralCalendarPlanWorksArr(contractId, numberOfCalendarPlanWorks);
        Response workResp = postContractCalendarPlanWithPreparedWorksApi(authCookie, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        return contractId;
    }

    // создать договор с работами плана ПИ, работами календарного плана, комиссией и вернуть id договора
    public int idOfCreatedContractPpoWithPlanPiWorksAndCalendarPlanWorkWithCommissionApi(String authCookieSa) throws InterruptedException {
        step("Создать договор", () -> {
        });
        int contractWithCalendarPlanWorksId = idOfCreatedContractWithPlanPiWorksAndCalendarPlanWorksApi(
                authCookieSa, contractsName, 3, 1, 2, PlanPiWorksPage.asuForPlanPiWorksId,
                1, 2);

        step("Создать в договоре новый документ типа ТЗ и Стартовать процесс по нему", () -> {
        });
        int docTypeId = 45; // Техническое задание на систему (ТЗ)
        int fileId = filesPage.uploadFileAndGetIdApi(authCookieSa, filesPage.testUploadFile);
        int docId = documentsPage.createDocumentInContractAndGetIdApi(authCookieSa, documentsPage.docName, documentsPage.decimalNumber, docTypeId, contractWithCalendarPlanWorksId, fileId);
        documentsPage.startProcessOfReportingDocumentApi(authCookieSa, docId);

        step("По id договора взять id комплекта доков и добавить в него любую Комиссию для ввода в эксплуатацию в таблице БД document_kit", () -> {
        });
        int firstDocKitId = ContractsPage.firstDocKitIdInContractApi(authCookieSa, contractWithCalendarPlanWorksId);
        updateDbTableV2LongFieldByObjectId("document_kit", "agreement_commission_id", 11L, (long) firstDocKitId);

        step("Добавить docKitId в работу КП договора в таблице БД agreement_schedule", () -> {
        });
        Response getCalendarPlanWorks = ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractWithCalendarPlanWorksId)
                .then().log().ifValidationFails().assertThat().statusCode(200).extract().response();
        ContractCalendarPlanWorkPost[] getCalendarPlanWorksAsClass = getCalendarPlanWorks.as(ContractCalendarPlanWorkPost[].class);
        long calendarPlanWorkId = getCalendarPlanWorksAsClass[0].getId();
        updateDbTableV4LongFieldByObjectId("agreement_schedule", "document_kit_id", (long) firstDocKitId, calendarPlanWorkId);

        return contractWithCalendarPlanWorksId;
    }

    public void deleteContractApi(String authCookie, int contractsId) {
        given()
                .spec(requestSpec)
//                .body("[]")
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .delete(BASE_URL + "/agreements/" + contractsId)
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }


    public static Response getContractApi(String authCookie, int contractsId) {
        return given()
                .spec(requestSpec)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/agreements/" + contractsId)
                .then().extract().response();
    }

    // по id договора взять id 1го document_kit
    public static int firstDocKitIdInContractApi(String authCookie, int contractId) {
        return getContractApi(authCookie, contractId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().body().path("documentKits.id[0]");
    }

    // элементы-атрибуты для отображения в календарном плане вкладки Ввод в ОЭ/ПЭ
    public static Response getContractScheduleElementsData(String authCookie, int contractId) {
        Response contactElementsData =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/agreement-schedule/agreementScheduleData/" + contractId)
                        .then()
                        .extract().response();
        return contactElementsData;
    }

    // добавление подтипа договора вкладки Ввод в ОЭ/ПЭ
    public static Response putContractSubtypeApi(String authCookie, int contractId, int subtypeId) {
        Map<String, Integer> agreementSubType = new HashMap<>();
        agreementSubType.put("id", subtypeId);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("agreementSubType", agreementSubType);
        Response subtypeData =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(requestData)
                        .when()
                        .put(BASE_URL + "/agreements/subtype/" + contractId)
                        .then()
                        .extract().response();
        return subtypeData;
    }

    // работа календарного плана договора
    static final IdFullShort.IdFullShortBuilder idFullShortBuilder = IdFullShort.builder();

    static IdFullShort asuIdOnly(int asuId) {
        return idFullShortBuilder.id(asuId).build();
    }

    public static String contractCalendarPlanWorkName = "Работа КП договора autotest " + faker.lorem().characters(5);
    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkNewBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWork(int contractId, String number, String planWorkName, Integer asuId, String startDate, String endDate, Boolean needOE) {
        return contractCalendarPlanWorkNewBuilder
                .agreementId(contractId)
                .number(number)
                .name(planWorkName)
                .asu(asuIdOnly(asuId))
                .startDate(startDate)
                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    public static ContractCalendarPlanWorkPost[] createdSeveralCalendarPlanWorksArr(int contractId, int numberOfCalendarPlanWorks) {
        // создать работы в количестве numberOfCalendarPlanWorks
        List<ContractCalendarPlanWorkPost> worksList = new ArrayList<>();
        for (int i = 0; i < numberOfCalendarPlanWorks; i++) {
            ContractCalendarPlanWorkPost work = newCalendarPlanWork(contractId, String.valueOf(i + 1),
                    contractCalendarPlanWorkName + "_" + (i + 1), PlanPiWorksPage.asuForPlanPiWorksId[0],
                    currentDateYyyyMmDdHyphenPlusOrMinusMonthes(0), currentDateYyyyMmDdHyphenPlusOrMinusMonthes(6), true);
            worksList.add(work);
        }
        return worksList.toArray(new ContractCalendarPlanWorkPost[0]);
    }

    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkWithoutNumberBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWorkWithoutNumber(int contractId, String planWorkName, Integer asuId, String startDate, String endDate, Boolean needOE) {
        return contractCalendarPlanWorkWithoutNumberBuilder
                .agreementId(contractId)
//                .number(number)
                .name(planWorkName)
                .asu(asuIdOnly(asuId))
                .startDate(startDate)
                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkWithoutNameBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWorkWithoutName(int contractId, String number, Integer asuId, String startDate, String endDate, Boolean needOE) {
        return contractCalendarPlanWorkWithoutNameBuilder
                .agreementId(contractId)
                .number(number)
//                .name(planWorkName)
                .asu(asuIdOnly(asuId))
                .startDate(startDate)
                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkWithoutAsuBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWorkWithoutAsu(int contractId, String number, String planWorkName, String startDate, String endDate, Boolean needOE) {
        return contractCalendarPlanWorkWithoutAsuBuilder
                .agreementId(contractId)
                .number(number)
                .name(planWorkName)
//                .asu(asuIdOnly(asuId))
                .startDate(startDate)
                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkWithoutStartDateBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWorkWithoutStartDate(int contractId, String number, String planWorkName, Integer asuId, String endDate, Boolean needOE) {
        return contractCalendarPlanWorkWithoutStartDateBuilder
                .agreementId(contractId)
                .number(number)
                .name(planWorkName)
                .asu(asuIdOnly(asuId))
//                .startDate(startDate)
                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    static ContractCalendarPlanWorkPost.ContractCalendarPlanWorkPostBuilder contractCalendarPlanWorkWithoutEndDateBuilder = ContractCalendarPlanWorkPost.builder();

    public static ContractCalendarPlanWorkPost newCalendarPlanWorkWithoutEndDate(int contractId, String number, String planWorkName, Integer asuId, String startDate, Boolean needOE) {
        return contractCalendarPlanWorkWithoutEndDateBuilder
                .agreementId(contractId)
                .number(number)
                .name(planWorkName)
                .asu(asuIdOnly(asuId))
                .startDate(startDate)
//                .endDate(endDate)
                .needOE(needOE)
                .deleted(false)
                .id(null)
                .build();
    }

    // добавить работы календарного плана договора вкладки Ввод в ОЭ/ПЭ
    // единственную работу
    public static Response postContractCalendarPlanSingleWorkApi(String authCookie, int contractId, String number, String planWorkName, Integer asuId, String startDate, String endDate, Boolean needOE) {
        ContractCalendarPlanWorkPost work = newCalendarPlanWork(contractId, number, planWorkName, asuId, startDate, endDate, needOE);
        ContractCalendarPlanWorkPost[] works = {work};
        return given()
                .spec(requestSpec)
                .log().all()
                .cookie("PLAY_SESSION", authCookie)
                .body(works)
                .when()
                .post(BASE_URL + "/app4/agreement-schedule")
                .then().log().all()
                .extract().response();
    }

    // с предзаполненными работами КП
    public static Response postContractCalendarPlanWithPreparedWorksApi(String authCookie, ContractCalendarPlanWorkPost[] works) {
        return given()
                .spec(requestSpec)
                .log().all()
                .cookie("PLAY_SESSION", authCookie)
                .body(works)
                .when()
                .post(BASE_URL + "/app4/agreement-schedule")
                .then().log().all()
                .extract().response();
    }

    // получить работы календарного плана договора
    public static Response getContractCalendarPlanWorksApi(String authCookie, int contractId) {
        return given()
                .spec(requestSpec)
                .log().all()
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/app4/agreement-schedule/allByAgreementId/" + contractId)
                .then().log().all()
                .extract().response();
    }


}
