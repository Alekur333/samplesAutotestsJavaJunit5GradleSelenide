package aleks.kur.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import lombok.val;

import java.util.*;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.pages.SqlRequestsPage.*;
import static ru.progredis.pages.UsersPage.*;
import static ru.progredis.tests.TestBase.*;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class PcDzoPage {

    public SelenideElement
            // раздел ПЦ ДЗО
            headerNameLocatorPcDzoPage = $(".MuiTypography-root.MuiTypography-medium"),
            filtersBtnOnPcDzoPage = $("[data-testid='@app4/actions/filters-expand']"),
            searchFormOnPcDzoPage = $("[data-testid='@app4/search-slot']"),
            yearFilterOnPcDzoPage = $("[data-testid='@app4/actions-slot']").$(".MuiStack-root", 1),
            reportsBtnOnPcDzoPage = $("button[title='Отчеты']"),
            btnMenuWindowOnPcDzoPage = $(".MuiPaper-root>ul.MuiList-root[role='menu']"),
            excelExportBtnOnPcDzoPage = $("button[title='Экспорт в Excel']"),
            actionsBtnOnPcDzoPage = $("button[data-testid='@app4/actions/actions-button']"),
    // таблица ПЦ ДЗО
    column1HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoIsBlocked']"),
            column2HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='0']"),
            column3HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='pcIsRequired']"),
            column4HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='responsibleFromCkiPerson']"),
            column5_6HeaderOnPcDzoListTable = $(".ag-header-row>[col-id='2_0']"),
            column5HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoProgram.date']"),
            column6HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoProgram.statusName']"),
            column7_8HeaderOnPcDzoListTable = $(".ag-header-row>[col-id='3_0']"),
            column7HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoAdjustmentProgram.date']"),
            column8HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoAdjustmentProgram.statusName']"),
            column9_10HeaderOnPcDzoListTable = $(".ag-header-row>[col-id='4_0']"),
            column9HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoFirstHalfYearReport.date']"),
            column10HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoFirstHalfYearReport.statusName']"),
            column11_12HeaderOnPcDzoListTable = $(".ag-header-row>[col-id='5_0']"),
            column11HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoYearReport.date']"),
            column12HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='dzoYearReport.statusName']"),
            column13HeaderOnPcDzoListTable = $(".ag-header-row-column>[col-id='6']"),
            column1HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div.ag-column-first"),
            column2HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='documentStatus.name']"),
            column3HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='baseDocumentId']"),
            column4HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='documentType.shortName']"),
            column5HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='name']"),
            column6HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='userVersion']"),
            column7HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div[col-id='createTime']"),
            column8HeaderOnDzoCardTableLocator = $(".ag-header-row-column>div.ag-column-last"),


    // карточка ПЦ ДЗО
    headerNameLocatorPcDzoCard = $("[data-testid='@app4/title']"), // заголовок карточки
            backToListFromDzoCardLocator = $("a[data-testid='@app4/router-link']"), // кнопка «К перечню ПЦ ДЗО»
            respDzoBlockLocatorPcDzoCard = $("[data-testid='@app4/DZOCardHeader/DZOResponsiblies']"), // Ответственные от ДЗО
            respCkiDzoBlockLocatorPcDzoCard = $("[data-testid='@app4/DZOCardHeader/CKIResponsible']"), // Ответственный от ЦКИ за ДЗО
            tableHeaderLocatorPcDzoCard = $("[data-testid='@app4/DZOCardHeader/TableHeader']"), // заголовок таблицы
            addDocBtnLocatorPcDzoCard = $("button[data-testid='@app4/DZOCardHeader/actions/add-document']"), // кнопка Добавить документ
            addDocWindowOnDzoCardLocator = $(".MuiDialog-container>.MuiPaper-root"), // окно по кнопке Добавить документ
            addDocWindowHeaderOnDzoCardLocator = addDocWindowOnDzoCardLocator.$("h2>.MuiStack-root"), // заголовок окна по кнопке Добавить документ
            addDocWindowCloseCrossOnDzoCardLocator = addDocWindowOnDzoCardLocator.$("button[data-testid='@app4/DZOCardHeader/actions/close-button']"), // кнопка Закрыть заголовка окна по кнопке Добавить документ
            addDocWindowYearLabelInDzoCardLocator = addDocWindowOnDzoCardLocator.$(byText("Год")), // имя блока Год окна по кнопке Добавить документ
            addDocWindowYearSelectionInDzoCardLocator = addDocWindowYearLabelInDzoCardLocator.sibling(0), // выбор года окна по кнопке Добавить документ
            addDocWindowDropdownListInDzoCardLocator = $("div#menu- .MuiMenu-list"), // список дропдаунов окна по кнопке Добавить документ
            addDocWindowNotificationInDzoCardLocator = $("div[data-testid=\"@new-document-dialog\"] p.MuiTypography-root"), // строка предупреждения об отсуствии ПЦ окна по кнопке Добавить документ
            addDocWindowDocTypeLabelInDzoCardLocator = addDocWindowOnDzoCardLocator.$(byText("Тип документа")), // имя блока Тип документа окна по кнопке Добавить документ
            addDocWindowDocTypeSelectionInDzoCardLocator = addDocWindowDocTypeLabelInDzoCardLocator.sibling(0), // выбор Типа документа окна по кнопке Добавить документ
            addDocWindowUploadBtnInDzoCardLocator = addDocWindowOnDzoCardLocator.$("label[data-testid='@app4/DZOCardHeader/actions/upload-button']"), // кнопка загрузки файлов окна по кнопке Добавить документ
            addDocWindowUploadFileInputInDzoCardLocator = addDocWindowUploadBtnInDzoCardLocator.$("input"), // input загрузки файлов окна по кнопке Добавить документ
            addDocWindowCancelBtnInDzoCardLocator = addDocWindowOnDzoCardLocator.$("button[data-testid='@app4/DZOCardHeader/actions/cancel']"), // кнопка Отменить окна по кнопке Добавить документ
            addDocWindowConfirmBtnInDzoCardLocator = addDocWindowOnDzoCardLocator.$("button[data-testid='@app4/DZOCardHeader/actions/confirm']"), // кнопка Отменить окна по кнопке Добавить документ

    tableLocatorPcDzoCard = $("div[data-testid='@app4/DZODocumentsGrid'] .ag-root-wrapper") // таблица
            ;
    public ElementsCollection
            columnsOnDzoTables = $$(".ag-header-row-column>div"),
            docsRowsOnDzoTables = $$(".ag-center-cols-container>.ag-row"),
            docTypeNamesInTableRowOnDzoTables = $$(".ag-center-cols-container>.ag-row div[col-id='documentType.shortName'] span.ag-cell-value"), // ячейки типа дока в таблице
            docNamesInTableRowOnDzoTables = $$(".ag-center-cols-container>.ag-row div[col-id='name'] a"); // ячейки имени дока в таблице

    public String
            column1NameOnPcDzoListTable = "",
            column2NameOnPcDzoListTable = "ДЗО",
            column3NameOnPcDzoListTable = "ПЦ обязательна",
            column4NameOnPcDzoListTable = "Отв. от ЦКИ",
            column5_6NameOnPcDzoListTable = "Программа",
            column5NameOnPcDzoListTable = "Дата",
            column6NameOnPcDzoListTable = "Статус",
            column7_8NameOnPcDzoListTable = "Корректировка",
            column7NameOnPcDzoListTable = "Дата",
            column8NameOnPcDzoListTable = "Статус",
            column9_10NameOnPcDzoListTable = "Отчет 1 полугодие",
            column9NameOnPcDzoListTable = "Дата",
            column10NameOnPcDzoListTable = "Статус",
            column11_12NameOnPcDzoListTable = "Отчет год",
            column11NameOnPcDzoListTable = "Дата",
            column12NameOnPcDzoListTable = "Статус",
            column13NameOnPcDzoListTable = "",
            column1NameOnDzoCardTable = "",
            column2NameOnDzoCardTable = "Статус",
            column3NameOnDzoCardTable = "ID Документа",
            column4NameOnDzoCardTable = "Тип",
            column5NameOnDzoCardTable = "Название документа",
            column6NameOnDzoCardTable = "Версия",
            column7NameOnDzoCardTable = "Дата версии",
            column8NameOnDzoCardTable = "",
            addDocWindowNotificationInDzoCard = "При отсутствии согласованной или утвержденной программы цифровизации загрузка данного документа невозможна";

    // список ролей раздела пц дзо
    final public static String validRolesCanSeePcDzoOnSideBarMenu =
            respDzoTechnoprom1 + "\n" +
                    respToAssignCoordinatorDzoCfto1 + "\n" +
                    coordinatingDzoCfto1 + "\n" +
                    respCkiDzo1 + "\n" +
                    baDzo1 + "\n" +
                    testAuditor1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    // список ролей пц дзо, которые видят все дзо в разделе
    final public static String validRolesCanSeeAllPcDzoInSection =
            respCkiDzo1 + "\n" +
                    baDzo1 + "\n" +
                    testAuditor1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    // список ролей, которым доступна кнопка "Действия"
    final public static String validRolesCanUseAddDocumentBtnOnDzoZeldortrestCard =
            respDzoZeldortrest1 + "\n" +
                    baDzo1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    // список ролей, которым доступна кнопка "Добавить документ"
    final public static String validRolesCanUseActionsBtnInPcDzoInSection =
            baDzo1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    // список ролей, которым не доступна кнопка "Действия"
    final public static String invalidRolesCantUseActionsBtnInPcDzoInSection =
            respToAssignCoordinatorDzoCfto1 + "\n" +
                    coordinatingDzoCfto1 + "\n" +
                    respCkiDzo1 + "\n" +
                    testAuditor1 + "\n" +
                    respForRequests_1;

    // список ролей пц дзо, которые видят перечень только тех ДЗО, в согласовании ПЦ которых участвует его организация
    final public static String validRolesCanSeeOnlyCoordinatedByThemDzosInSection =
            respToAssignCoordinatorDzoCfto1 + "\n" +
                    coordinatingDzoBase57_1;

    final public static String invalidRolesCanNotSeePcDzoOnSideBarMenu =
            FCI_1 + "\n" +
                    executor_1;

    // редактирование атрибута isDPO / Контрагент ДЗО для организации
    // "CodeEKASUFR": "0300000570", // codeEkasufr в АСУ ПИ
    // "Name":  "", // shortName в АСУ ПИ
    // "NameFull": "", // name в АСУ ПИ
    // "NameShort":  "", // telegraphName в АСУ ПИ
    public String isDzoAbiAsuKpiJson(Boolean isDzo) {
        String asuKpiJson = "[" +
                "{\n" +
                "                \"Code\": \"627\",\n" +
                "                \"CodeEKASUFR\": \"1000389185\",\n" +
                "                \"Name\": \"Аби для автотестов, не редактировать!!!\",\n" +
                "                \"NameFull\": \" ООО \\\"Аби\\\" для автотестов, не использовать в Ручном тестировании!!!\",\n" +
                "                \"NameShort\": \"ООО \\\"Аби\\\"\",\n" +
                "                \"isFZ\": false,\n" +
                "                \"isPR\": false,\n" +
                "                \"typeContr\": \"4\",\n" +
                "                \"isDPO\": " + isDzo + ",\n" +
                "                \"blocked\": false,\n" +
                "                \"deleted\": false\n" +
                "            }" +
                "]";
        return asuKpiJson;
    }

    public String isDzoStromirAsuKpiJson(Boolean isDzo) {
        String asuKpiJson = "[" +
                "{\n" +
                "                \"Code\": \"856\",\n" +
                "                \"CodeEKASUFR\": null,\n" +
                "                \"Name\": \"СТРОМИР для автотестов, не редактировать!!!\",\n" +
                "                \"NameFull\": \"СТРОМИР для автотестов, не использовать в Ручном тестировании!!!\",\n" +
                "                \"NameShort\": \"СТРОМИР для автотестов, не редактировать!!!\",\n" +
                "                \"isFZ\": false,\n" +
                "                \"isPR\": false,\n" +
                "                \"typeContr\": \"4\",\n" +
                "                \"isDPO\": " + isDzo + ",\n" +
                "                \"blocked\": false,\n" +
                "                \"deleted\": false\n" +
                "            }" +
                "]";
        return asuKpiJson;
    }

/*    public String isDzoTechnopromAsuKpiJson(Boolean isDzo) {
        String asuKpiJson = "[" +
                "{\n" +
                "                \"Code\": \"1052\",\n" +
                "                \"CodeEKASUFR\": \"1000627131\",\n" +
                "                \"Name\": \"Технопром для автотестов, не редактировать!!!\",\n" +
                "                \"NameFull\": \"ООО «Технопром» для автотестов, не использовать в Ручном тестировании!!!\",\n" +
                "                \"NameShort\": \"Технопром для автотестов, не редактировать!!!\",\n" +
                "                \"isFZ\": false,\n" +
                "                \"isPR\": false,\n" +
                "                \"typeContr\": \"4\",\n" +
                "                \"isDPO\": " + isDzo + ",\n" +
                "                \"blocked\": false,\n" +
                "                \"deleted\": false\n" +
                "            }" +
                "]";
        return asuKpiJson;
    }*/

    public String isDzoZeldortrestAsuKpiJson(Boolean isDzo) {
        String asuKpiJson = "[" +
                "{\n" +
                "                \"Code\": \"111\",\n" +
                "                \"CodeEKASUFR\": null,\n" +
                "                \"Name\": \"Желдортрест для автотестов, не редактировать!!!\",\n" +
                "                \"NameFull\": \"Желдортрест для автотестов, не использовать в Ручном тестировании!!!\",\n" +
                "                \"NameShort\": \"\",\n" +
                "                \"isFZ\": false,\n" +
                "                \"isPR\": false,\n" +
                "                \"typeContr\": \"4\",\n" +
                "                \"isDPO\": " + isDzo + ",\n" +
                "                \"blocked\": false,\n" +
                "                \"deleted\": false\n" +
                "            }" +
                "]";
        return asuKpiJson;
    }

    // список ДЗО
    public Response getDzoListApi(String authCookie, String year) {
        Response list =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/dzo/list/" + year);
        return list;
    }

    // карточка ДЗО
    public Response getDzoCardApi(String authCookie, int dzoId) {
        Response dzo =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/dzo/" + dzoId)
                        .then().log().ifValidationFails()
                        .statusCode(200)
                        .extract().response();
        return dzo;
    }

    // получение из БД списка ДЗО по организации согласующего
    public Set<Integer> dzoIdsOnlyCoordinatingByUserFromDbSet(int coordinatorOrganizationId) {

        String
                tableNameV4 = "approval_route_dpo_ids",
                fieldNameV4 = "dpo_ids",
                joinConditionV4 = "LEFT JOIN " +
                        "dpo_coordinator coord on coord.route = x.approval_route_id ",
                conditionsV4 =
                        "coord.organization_id = " + coordinatorOrganizationId;
        Set<Integer> dzoIdsFromDbV4Set = selectIdsByConditionsDbTableV4(tableNameV4, fieldNameV4, joinConditionV4, conditionsV4);
        System.out.println("ids from db V4 table approval_route_dpo_ids = " + dzoIdsFromDbV4Set);

        if (dzoIdsFromDbV4Set.isEmpty()) {
            return Collections.emptySet(); // если нет id, сразу возвращаем пустое множество
        }

        String
                dzoIdsFromDbV4String = dzoIdsFromDbV4Set.toString().replaceAll("[\\[\\]]", ""), // убираем квадратные скобки
                tableNameV2 = "organization",
                fieldNameV2 = "id",
                joinConditionV2 = "",
                conditionsV2 =
                        "id IN (" + dzoIdsFromDbV4String + ") AND counterparty_dpo=true AND deleted=false AND blocked=false";
        Set<Integer> dzoIdsFromDbV2Set = selectIdsByConditionsDbTableV2(tableNameV2, fieldNameV2, joinConditionV2, conditionsV2);
        System.out.println("ids from db V2 table organization = " + dzoIdsFromDbV2Set);

        return dzoIdsFromDbV2Set;
    }

    // В json тесторг-ДЗО заполняем Ответственный от ЦКИ за ДЗО, обязательно Предоставление ПЦ, updateTime
    // и преобразовать обратно в строку JSON
    public static String editCkiRespDzoAndPcRequiredAttrsInOrganizationCard(String initialJsonString, int responsibleFromCkiForDpoId, String provisionOfPcDpoState) {
        // заменить значение нужного ключа в json и преобразовать обратно в строку JSON
        String updatedJsonString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(initialJsonString);

            // Получаем узел responsibleFromCkiForDpo
            JsonNode responsibleFromCkiForDpoNode = rootNode.path("responsibleFromCkiForDpo");

            // Если узел responsibleFromCkiForDpo не является объектом, создаем новый объект
            if (!responsibleFromCkiForDpoNode.isObject()) {
                ObjectNode responsibleFromCkiForDpoObject = mapper.createObjectNode();
                responsibleFromCkiForDpoObject.put("id", responsibleFromCkiForDpoId);
                // Приводим корневой узел к ObjectNode и вставляем новый объект в поле responsibleFromCkiForDpo
                ((ObjectNode) rootNode).set("responsibleFromCkiForDpo", responsibleFromCkiForDpoObject);
            } else {
                // Приводим существующий узел к ObjectNode и вставляем значение id
                ((ObjectNode) responsibleFromCkiForDpoNode).put("id", responsibleFromCkiForDpoId);
            }

            // вставляем значение provisionOfPcDpo
            ((ObjectNode) rootNode).put("provisionOfPcDpo", provisionOfPcDpoState);

            // Преобразуем обратно в строку JSON
            updatedJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedJsonString;
    }

    // заполняем в организации-ДЗО по его asuKpiId поля "Ответственный от ЦКИ за ДЗО", "обязательно Предоставление ПЦ"
    public static Response fillCkiRespDzoAndPcRequiredAttrsInOrganizationCard(String authCookie, String asuKpiId) {
        // Определяем id 1го юзера с ролью Ответственный от ЦКИ за ДЗО
        int firstPersonInRoleId = usersPage.getIdFirstPersonInRole(authCookie, "RESPONSIBLE_PC_DPO");
        // Определяем id тесторганизации на стенде по asuKpiId
        int orgId = organizationPage.getOrgIdByAsuKpiIdApi(authCookie, asuKpiId);
        // Забираем тело тесторг ДЗО и заполняем в нем Ответственный от ЦКИ за ДЗО, обязательно Предоставление ПЦ
        val org = organizationPage.getOrganizationByIdApi(authCookie, orgId);
        val initialJsonString = org.getBody().asString();
        val editedJsonString = editCkiRespDzoAndPcRequiredAttrsInOrganizationCard
                (initialJsonString, firstPersonInRoleId, "MANDATORY");
        // Редактируем тесторганизацию запросом PUT с новым телом
        val editedOrg = organizationPage.putOrganizationByIdApi(authCookie, orgId, editedJsonString);
        return editedOrg;
    }

    ;

    // проверка тесторганизации по asuKpiId на признак ДЗО. Если нет, то активация ДЗО и заполнение поля Ответственный от ЦКИ за ДЗО
    public static void testOrgShouldBeDzoWithRespCkiAndPcRequired(String authCookie, String asuKpiId, String isDzoTrueJson) {
        boolean isDzoTestOrg = organizationPage
                .getShortOrgCardByAsuKpiIdApi(authCookie, asuKpiId)
                .then().extract().path("isCounterpartyDpo[0]");
        System.out.println("isDzoTestOrg = " + isDzoTestOrg);
        if (!isDzoTestOrg) {
//            Делаем запрос из АСУ КПИ с атрибутом isDPO:true для тесторганизации
            integrationPage.asuKpiIntegrationApi(authCookie, isDzoTrueJson);
//            step("Заполняем в организации-ДЗО по его asuKpiId поля \"Ответственный от ЦКИ за ДЗО\", \"обязательно Предоставление ПЦ\"", () -> {
            fillCkiRespDzoAndPcRequiredAttrsInOrganizationCard(authCookie, asuKpiId);
        }
    }

    // действие на странице ПЦ ДЗО - Запросить загрузку документа ДЗО по типу
    // dp / ПЦ, report_half_dp / отчет за 1 полугодие, report_dp / отчетов за год
    public Response requestDzoDocApi(String authCookie, String dzoDocType) {
        Response requestPcDzo =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/dzo/notify/" + dzoDocType)
                        .then().log().ifValidationFails().statusCode(200)
                        .extract().response();
        return requestPcDzo;
    }

    // запрос группы документов ПЦ ДЗО
    public Response getPcDzoDocumentGroup() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("q", "ПЦ ДЗО");
        Response pcDzoDocGroupe = DirectoriesPage.getDocumentGroupsDirectoryApi(AuthApiPage.getAuthCookie(login, passwd), queryParams);
        return pcDzoDocGroupe;
    }

    // краткие имена доков ДЗО 2го уровня
    final public static String dzoSecondLevelDocTypeNames =
            "adjustment_dp, Корректировка ПЦ ДЗО\n" +
                    "approved_adj_dp, Утв. корректировка\n" +
                    "report_half_dp, Отчет 1 полугодие\n" +
                    "approved_rep_half_dp, Утв. отчет 1 полугодие\n" +
                    "report_dp, Отчет год\n" +
                    "approved_rep_dp, Утв. отчет год\n";

    // коды, краткие имена доков ДЗО 1го уровня
    final public static String dzoFirstLevelDocTypes =
            "dp, ПЦ ДЗО\n" +
                    "approved_dp, Утв. ПЦ ДЗО";

    // запрос карточки ДЗО по id
    public Response dzoCardApi(String authCookie, int dzoId) {
        Response dzoCard =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/dzo/" + dzoId)
                        .then().log().ifValidationFails().statusCode(200)
                        .extract().response();
        return dzoCard;
    }

    // удалить все документы ДЗО
    public void deleteAllDzoDocsByItsIdApi(String authCookie, int dzoId) {
        List dzoDocIdsToDelete = dzoCardApi(
                authCookie, dzoId)
                .then()
                .log().ifValidationFails()
                .assertThat().statusCode(200)
                .extract().response().path("documents.id");
//        System.out.println(requestIdsToDeleteByName);
        int idsSize = dzoDocIdsToDelete.size();
//        System.out.println(idsSize);
        int i;
        for (i = 0; i < idsSize; i++) {
            System.out.println("id to del " + dzoDocIdsToDelete.get(i));
            DocumentsPage.deleteDocumentByIdApi(authCookie, (Integer) dzoDocIdsToDelete.get(i));
        }
    }

    // Добавить файл в документ в окне карточки ДЗО
    public void uploadFileToDocOnAddDocWindowInDzoCard(String attachFile) {
        addDocWindowUploadFileInputInDzoCardLocator.uploadFromClasspath("files/" + attachFile);
    }

    // добавить документ по типу в карточку ДЗО на ui
    // dp / ПЦ ДЗО , adjustment_dp / Корректировка ПЦ ДЗО ,
    // approved_dp / Утв. ПЦ ДЗО , approved_adj_dp / Утв. корректировка ,
    // report_half_dp / Отчет 1 полугодие, report_dp / Отчет год
    // approved_rep_half_dp / Утв. отчет 1 полугодие , approved_rep_dp / Утв. отчет год ,
    public void addDzoDocByTypeInCardUi(String docTypeCode) {
        // получить краткое имя по коду типа дока
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("code", docTypeCode);
        Response pcDzoDocType = DirectoriesPage.getDocumentTypesDirectoryApi(AuthApiPage.getAuthCookie(login, passwd), queryParams);
        String shortName = pcDzoDocType.then().extract().path("items.shortName[0]");
        // добавляем в карточке ДЗО по shortName документ
        addDocBtnLocatorPcDzoCard.click();
        addDocWindowDocTypeSelectionInDzoCardLocator.click();
        addDocWindowDropdownListInDzoCardLocator.$(byText(shortName)).click();
        uploadFileToDocOnAddDocWindowInDzoCard(attachFile1);
        addDocWindowConfirmBtnInDzoCardLocator.shouldBe(Condition.enabled).click();
    }

    // создать документ ДЗО в любом статусе draft/ Черновик/ 1, approved/ Утверждён/ 4, agreed/ На подписании/ 3 (уже согласован)
    public Response createDzoDocAnyStatusApi(String authCookie, String docTypeCode, int docStatusId, int testDzoId, int fileId) {

        String docName = "Имя документа ДЗО " + docTypeCode + " Autotest " + faker.lorem().fixedString(3);

        String newDocVersionBodyData =
                "{\n" +
                        "  \"documentFiles\": [\n" +
                        "    {\n" +
                        "      \"file\": {\n" +
                        "        \"id\": " + fileId +
                        "      }\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"year\": " + currentYear + ",\n" +
                        "  \"name\": \"" + docName + "\",\n" +
                        "  \"documentType\": {\n" +
                        "    \"code\": \"" + docTypeCode + "\"\n" +
                        "  },\n" +
                        "  \"documentStatus\": {\n" +
                        "    \"id\": " + docStatusId + "\n" +
                        "  },\n" +
                        "  \"organization\": {\n" +
                        "    \"id\": " + testDzoId + "\n" +
                        "  }\n" +
                        "}";

        Response dzoDocResp =
                given()
                        .spec(requestSpec)
                        .body(newDocVersionBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/documents")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response();
        return dzoDocResp;

    }


}
