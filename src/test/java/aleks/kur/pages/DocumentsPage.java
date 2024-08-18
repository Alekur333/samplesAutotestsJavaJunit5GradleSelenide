package aleks.kur.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class DocumentsPage {

    protected ContractsPage contractsPage = new ContractsPage();
    protected FilesPage filesPage = new FilesPage();

    public SelenideElement
            documentsTableOnDocsTab = $("#documentList"),
            headerOnDocsCard = $(".document-display-header>div:nth-child(2)"),
            docsNameOnDocsCard = $(".document-display-header>div:nth-child(3)"),
            decimalNumberOnDocsCard = $(".document-display-header>div:nth-child(4)>span"),
            docsTypeOnDocsCard = $(".document-display-header>div:nth-child(5)"),
            contractsNameOnDocsCard = $(".document-display-header>div:nth-child(6)>a"),
            docIdOnDocsCard = $(".document-display-header>div:nth-child(1)>div:nth-child(1)"),
            docIdOnDocsCardApp4 = $(".MuiChip-label"),
            docVersionIdOnDocsCard = $(".document-display-header>div:nth-child(1)>div:nth-child(2)"),
            tnEasdElementOnDocsCard = $(".document-display-header>div:nth-child(1)>div:nth-child(3)"),
            docStatusOnDocsCard = $(".document-display-header>div:nth-child(7)>div:nth-child(1)"),
            approvalStartTimeStatusOnDocsCard = $(".document-display-header>div:nth-child(7)>div:nth-child(2)"),
            approvalInitiatorOnDocsCard = $(".document-display-header>div:nth-child(7)>div:nth-child(3)"),
            currentVersionOnDocsCard = $(".document-display-header>div:nth-child(7)>div:nth-child(4)"),
            tabsBlockOnDocsCard = $(".document-tabs"),
            firstTabOnDocsCard = $(".document-tabs li:nth-child(1)"),
            firstTabNameOnDocsCard = $(".document-tabs li:nth-child(1)>span"),
            secondTabNameOnDocsCard = $(".document-tabs li:nth-child(2)>span"),
            thirdTabNameOnDocsCard = $(".document-tabs li:nth-child(3)>span"),
            firstDocStringOnDocsTab = $(".k-master-row"),
            editBtnOnDocStringOnDocsTab = $(".i-edit").parent(),
            deleteBtnOnDocStringOnDocsTab = $(".i-delete").parent(),
            startProcessBtnOnDocStringOnDocsTab = $(".i-play").parent(),
            stopProcessBtnOnDocStringOnDocsTab = $(".i-stop"),
            attachFilesBtnOnEditDocModal = $(".k-upload-button>input"),
            attachFilesBtnOnEditDocModalApp4 = $("[data-testid='@app4/document-card/summary/actions/upload-button']>input"),
            saveBtnOnEditDocModal = $(".footer-wrap>.btn-success"),
            saveBtnOnEditDocModalApp4 = $("[data-testid='@app4/document-card/summary/actions/confirm']"),
            deleteFirstFileBtnOnEditDocModal = $(".k-file-success span[title='Удалить']"),
            docNameFieldOnEditDocModal = $(".modal-body input.input-large"),
            firstDocNameOnDocsTab = $(".k-master-row>td:nth-child(5)>a>span"),

    // Вкладка Основная информация апп4
    AddNewVersionBtnOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/actions/add-version']"), // кнопка 'Добавить новую версию'
            uploadFilesBtnOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/file-upload']"), // кнопка 'Загрузить файлы' в строке версии
            uploadFilesBtnSvgPathOnVersionStringOnMainTabApp4 = uploadFilesBtnOnVersionStringOnMainTabApp4.$("path", 0), // атрибуты иконки кнопки 'Загрузить файлы' в строке версии
            downloadFilesBtnOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/file-download']", 0), // кнопка 'Скачать файлы' в строке версии
            downloadFilesBtnSvgPathFirstOnVersionStringOnMainTabApp4 = $("a[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/file-download']>span.hovered>svg>path:nth-child(1)"), // Релиз атрибуты из 1 path иконки кнопки 'Скачать файлы' в строке версии
            downloadFilesBtnSvgPathSecondOnVersionStringOnMainTabApp4 = $("a[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/file-download']>span.hovered>svg>path:nth-child(2)"), // Релиз атрибуты из 2 path иконки кнопки 'Скачать файлы' в строке версии
            downloadFilesBtnSvgPathThirdOnVersionStringOnMainTabApp4 = $("a[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/file-download']>span.hovered>svg>path:nth-child(3)"), // Релиз атрибуты из 3 path иконки кнопки 'Скачать файлы' в строке версии
            downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/download-print-document']", 0), // 1я кнопка 'Скачать документ для печати' в строке версии
            downloadRemarksBtnOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/download-remarks']", 0), // кнопка 'Скачать перечень замечаний' в 1й строке версии
            startProcessBtnOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/start-approving-process']", 0), // кнопка 'Начать согласование' в строке 1й версии
            startProcessBtnOnSecondVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/start-approving-process']", 1), // кнопка 'Начать согласование' в строке 2й версии
            startProcessBtnDisabledIconOnVersionStringOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/start-approving-process']>svg", 0), // иконка неактивной кнопки 'Начать согласование' в 1й строке версии
    //            stopProcessBtnOnVersionStringOnMainTabEnabledApp4 = $("button[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/stop-approving-process']", 0), // ХОТФИКС 1кнопка 'Прекратить согласование' в 1й строке версии
    stopProcessBtnOnVersionStringOnMainTabEnabledApp4 = $("button[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/stop-approving-process']", 0), // РЕЛИЗ 1кнопка 'Прекратить согласование' в 1й строке версии
            stopProcessBtnOnVersionStringOnMainTabDisabledApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/version-row-actions/grid-action-panel/stop-approving-process']", 0), // 1кнопка 'Прекратить согласование' в 1й строке версии
            documentVersionsTableOnMainTabApp4 = $("div.ag-root[role='grid']"), // таблица версий документа
            documentStatusColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='documentStatus.name']"), // колонка 'Статус'
            documentStatusColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='documentStatus.name'] .ag-header-cell-text"), // имя колонки 'Статус'
            documentAuthorColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='author.name']"), // колонка 'Автор'
            documentAuthorColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='author.name'] .ag-header-cell-text"), // имя колонки 'Автор'
            documentUploadedColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='0']"), // колонка 'Загружено'
            documentUploadedColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='0'] .ag-header-cell-text"), // имя колонки 'Загружено'
            documentIdProcessColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='approvalProcessId']"), // колонка 'ID процесса'
            documentIdProcessColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='approvalProcessId'] .ag-header-cell-text"), // имя колонки 'ID процесса'
            documentIdVersionColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='documentId']"), // колонка 'ID версии'
            documentIdVersionColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='documentId'] .ag-header-cell-text"), // имя колонки 'ID версии'
            documentSentDateColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='approvalStartTime']"), // колонка 'Направлена'
            documentSentDateColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='approvalStartTime'] .ag-header-cell-text"), // имя колонки 'Направлена'
            firstEmptyColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='ag-Grid-AutoColumn']"), // первая колонка без заголовка
            firstEmptyColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='ag-Grid-AutoColumn'] .ag-header-cell-text"), // имя первой колонки без заголовка
            documentVersionColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='versionNumber']"), // колонка для версии
            documentVersionColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='versionNumber'] .ag-header-cell-text"), // имя колонки для версии
            lastEmptyColumnOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='1']"), // последняя колонка без заголовка
            lastEmptyColumnsNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-header-row-column>div[col-id='1'] .ag-header-cell-text"), // имя последней колонки без заголовка
            firstVersionOnDocumentVersionsTableOnMainTabApp4 = $(".ag-row-group>.ag-cell[col-id='ag-Grid-AutoColumn']", 0).parent(), // ячейка версии 1 в таблице версий
            secondVersionOnDocumentVersionsTableOnMainTabApp4 = $(".ag-row-group>.ag-cell[col-id='ag-Grid-AutoColumn']", 1).parent(), // ячейка версии 2 в таблице версий
            firstVersionNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-row-group>.ag-cell[col-id='ag-Grid-AutoColumn']", 0).$(".MuiTypography-root"), // имя версии 1 в таблице версий
            secondVersionNameOnDocumentVersionsTableOnMainTabApp4 = $(".ag-row-group>.ag-cell[col-id='ag-Grid-AutoColumn']", 1).$(".MuiTypography-root"), // имя версии 2 в таблице версий
            firstFileOfFirstVersionOnDocumentVersionsTableOnMainTabApp4 = $("[data-testid='@app4/document-card/summary/main-info-grid/download']", 0), // файл 1 версии 1 в таблице версий
            statusOfFirstVersionOnDocumentVersionsTableOnMainTabApp4 = $(".ag-body-viewport [col-id='documentStatus.name']", 0).$("span"), // статус версии 1 в таблице версий
            statusOfSecondVersionOnDocumentVersionsTableOnMainTabApp4 = $(".ag-body-viewport [col-id='documentStatus.name']", 1).$("span"), // статус версии 2 в таблице версий


    // Вкладка Ход согласования апп4
    agreementProgressTabApp4 = $(byText("Ход согласования")), // вкладка 'Ход согласования'
            tasksTableOnAgreementProgressTabApp4 = $("div.ag-root"), // таблица задач
            departmentColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div", 0), // колонка 'Подразделение'
            approverColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='executor.name']"), // колонка 'Согласующий'
            approverColumnsNameOnTasksTabOnAgreementProgressTabApp4 = approverColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Согласующий'
            resultColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='decision']"), // колонка 'Результат'
            resultColumnsNameOnTasksTabOnAgreementProgressTabApp4 = resultColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Результат'
            taskIdColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='id']"), // колонка 'ID задачи'
            taskIdColumnsNameOnTasksTabOnAgreementProgressTabApp4 = taskIdColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'ID задачи'
            taskColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='name']"), // колонка 'Задача'
            taskColumnsNameOnTasksTabOnAgreementProgressTabApp4 = taskColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Задача'
            taskCreationDateColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='createdDate']"), // колонка 'Поставлена'
            taskCreationDateColumnsNameOnTasksTabOnAgreementProgressTabApp4 = taskCreationDateColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Поставлена'
            planFinishDateColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='planFinishDate']"), // колонка 'Поставлена'
            planFinishDateColumnsNameOnTasksTabOnAgreementProgressTabApp4 = planFinishDateColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Поставлена'
            factFinishDateColumnOnTasksTableOnAgreementProgressTabApp4 = $(".ag-header-row-column>div[col-id='factFinishDate']"), // колонка 'Поставлена'
            factFinishDateColumnsNameOnTasksTabOnAgreementProgressTabApp4 = factFinishDateColumnOnTasksTableOnAgreementProgressTabApp4.$(".ag-header-cell-text"), // имя колонки 'Поставлена'
            versionNemberOnTasksTableOnAgreementProgressTabApp4 = $(".ag-center-cols-container .ag-row-first .ag-group-value>span"), // номер версии в таблице задач
            versionStatusOnTasksTableOnAgreementProgressTabApp4 = $(".ag-row-first [col-id='executor.name']>div>.ag-cell-value"), // статус в таблице задач
            btnsPanelOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $(".ag-row-first [col-id='actionButtons']"), // панель кнопок в таблице задач
            btnExportReportAgreementProcessOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $("[data-testid='@app4/document-card/approval-progress-grid/version-row-actions/grid-action-panel/download-process']"), // кнопка 'Экспортировать отчет по процессу согласования' в таблице задач
            btnExportReportAgreementResultOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $("[data-testid='@app4/document-card/approval-progress-grid/version-row-actions/grid-action-panel/download-report']"), // кнопка 'Экспортировать отчет о результатах согласования' в таблице задач
            btnExportVersionsRemarksOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $("[data-testid='@app4/document-card/approval-progress-grid/version-row-actions/grid-action-panel/download-remark-list']"), // кнопка 'Экспортировать замечания к версии' в таблице задач
            btnExportAgreementListOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $("[data-testid='@app4/document-card/approval-progress-grid/version-row-actions/grid-action-panel/download-list']"), // кнопка 'Экспортировать лист согласования' в таблице задач
            btnEditDatesOnVersionStringOnTasksTableOnAgreementProgressTabApp4 = $("[data-testid='@app4/document-card/approval-progress-grid/version-row-actions/grid-action-panel/date-adjustment']"), // кнопка 'Скорректировать даты' в таблице задач

    // Вкладка Замечания апп4
    remarksTabApp4 = $(byText("Замечания")), // вкладка 'Замечания'
            filterBtnRemarksTabApp4 = $("button[data-testid='@app4/document-card/remarks/remark-list-with-handling/actions/filters-expand']"), // кнопка Фильтр во вкладке 'Замечания'
            filterPanelRemarksTabApp4 = $("div[data-testid='@app4/document-card/remarks/remark-list-with-handling/area']"), // панель Фильтры во вкладке 'Замечания'
            filtersDropDownRemarksTabApp4 = filterPanelRemarksTabApp4.$(".MuiFormControl-root"), // дропдаун Фильтров во вкладке 'Замечания'
            docVersionFilterDropDownRemarksTabApp4 = $("div#menu- li", 0), // опция "Версия документа" дропдаун Фильтров во вкладке 'Замечания'
            authorFilterDropDownRemarksTabApp4 = $("div#menu- li", 1), // опция "Автор" дропдаун Фильтров во вкладке 'Замечания'
            authorDepartmentFilterDropDownRemarksTabApp4 = $("div#menu- li", 2), // опция "Подразделение" дропдаун Фильтров во вкладке 'Замечания'
            statusFilterDropDownRemarksTabApp4 = $("div#menu- li", 3), // опция "Статус" дропдаун Фильтров во вкладке 'Замечания'
            searchPanelRemarksTabApp4 = $("div[data-testid='@app4/document-card/remarks/remark-list-with-handling/search-slot']"), // панель поиска во вкладке 'Замечания'
            onlyActualCheckBoxRemarksTabApp4 = $("label.MuiFormControlLabel-root"), // чек-бокс 'Только не снятые замечания' во вкладке 'Замечания'
            versionsFilterRemarksTabApp4 = $(".MuiInputBase-root .MuiSelect-select").parent().parent(), // дропдаун выбора версий во вкладке 'Замечания'
            exportBtnRemarksTabApp4 = $("button[title='Экспортировать']"), // кнопка экспорта во вкладке 'Замечания'
            fourStringsBtnRemarksTabApp4 = $("button[title='Четыре строки']"), // кнопка 'Четыре строки' во вкладке 'Замечания'
            allStringsBtnRemarksTabApp4 = $("button[title='Все строки']"), // кнопка 'Все строки' во вкладке 'Замечания'
            addRemarkBtnRemarksTabApp4 = $("button[data-testid='@app4/document-card/remarks/remark-list-with-handling/actions/add-remark']"), // кнопка 'Добавить замечание' во вкладке 'Замечания'
            remarksTableRemarksTabApp4 = $("div.ag-root-wrapper"), // таблица замечаний во вкладке 'Замечания'
            remarkNumberColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='1']"), // колонка '№'
            remarkStatusColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='2']"), // колонка 'Статус'
            docVersionColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='3']"), // колонка 'Версия'
            remarkAuthorColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='4']"), // колонка 'Автор'
            docChapterColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='5']"), // колонка 'Раздел документа'
            remarksTextColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='6']"), // колонка 'Замечание'
            remarksCommentColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='7']"), // колонка 'Комментарии проверяющих'
            remarkProcessingColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='8']"), // колонка 'Обработка'
            remarkApprovingColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='9']"), // колонка 'Подтверждение'
            docsAuthorCommentColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='10']"), // колонка 'Комментарий разработчика'
            authorsCommentColumnHeaderOnRemarksTableOnRemarksTabTabApp4 = $("div.ag-header-cell[aria-colindex='11']"), // колонка 'Комментарий автора'
            agreedDecisionColumnHeaderOnRemarksTableOnRemarksTabApp4 = $("div.ag-header-cell[aria-colindex='12']"), // колонка 'Согласованное решение'
            remarkStringFirstOnTableOnRemarksTabApp4 = $("div.ag-center-cols-container>.ag-row", 0), // 1я строка замечания в таблице
            remarkNumberFirstOnTableOnRemarksTabApp4 = remarkStringFirstOnTableOnRemarksTabApp4.$("[col-id='remarkNumber'] a", 0), // номер 1го замечания в таблице
            remarksChapterFirstOnTableOnRemarksTabApp4 = remarkStringFirstOnTableOnRemarksTabApp4.$("[col-id='documentSection']", 0), // номер и имя раздела документа 1го замечания в таблице
            remarksTextsFirstOnTableOnRemarksTabApp4 = remarkStringFirstOnTableOnRemarksTabApp4.$("", 0), // текст, ориг и предл формулировка 1го замечания в таблице
            editRemarkBtnOnStringOnRemarksTabApp4 = $("button[data-testid='@app4/document-card/remarks/remark-list-with-handling/remark-row-actions/grid-action-panel/edit-remark']") // кнопка Редактировать замечание на строка замечания в таблице


                    ;

    public ElementsCollection
            columnsOnDocumentVersionsTableOnMainTabApp4 = $$(".ag-header-row-column>div"),
            columnsOnTasksTableOnAgreementProgressTabApp4 = $$(".ag-header-row-column>div"),
            columnsOnRemarksTableOnRemarksTabApp4 = $$(".ag-header-container div[role='columnheader']"),
            versionNumbersOnTasksTableOnAgreementProgressTabApp4 = $$(".ag-center-cols-container .ag-row-first .ag-group-value>span"),
            departmentValuesOnTaskRowOnAgreementProgressTabApp4 = $$(".ag-center-cols-container .ag-row-no-focus .ag-group-value"), // значение ячейки 'Подразделение' задачи
            agreenentPersonValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='executor.name'] .ag-cell-value"), // значение ячейки 'Согласующий' задачи
            resultValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='decision'] .ag-cell-value"), // значение ячейки 'Результат' задачи
            taskIdValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='id'] .ag-cell-value"), // значение ячейки 'ID задачи' задачи
            taskNameValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='name'] .ag-cell-value"), // значение ячейки 'Задача' задачи
            taskCreatedDateValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='createdDate'] .ag-cell-value"), // значение ячейки 'Поставлена' задачи
            taskPlanFinishDateValuesOnTaskRowOnAgreementProgressTabApp4 = $$("[col-id='planFinishDate'] .ag-cell-value"), // значение ячейки 'План' задачи
            tasksOnTasksTableOnAgreementProgressTabApp4 = $$(".ag-center-cols-container .ag-row-no-focus"), // строки таблицы задач
            btnsEnabledOnVersionSrtingOnAgreementProgressTabApp4 = $$(".ag-row-first [col-id='actionButtons'] .MuiButtonBase-root"),
            btnsDisabledOnVersionSrtingOnAgreementProgressTabApp4 = $$(".ag-row-first [col-id='actionButtons'] .MuiBox-root"),

    // Вкладка Основная информация отчетного документа
    downloadFileBtnOnFileStringOnMainTabApp4 = $$("a[data-testid='@app4/document-card/summary/main-info-grid/file-row-actions/grid-action-panel/file-download']"), // кнопки 'Скачать файлы' в строке файлов
            deleteFileBtnOnFileStringOnMainTabApp4 = $$("[data-testid='@app4/document-card/summary/main-info-grid/file-row-actions/grid-action-panel/file-delete']"), // кнопки 'Удалить файл' в строке файлов
//            filesOfDocVersionOnMainTabApp4 = $$(".ag-center-cols-clipper .ag-row-level-1>.ag-cell:nth-child(1)"), // файлы версии документа
            filesOfDocVersionOnMainTabApp4 = $$("[data-testid='@app4/document-card/summary/main-info-grid/download']"), // файлы версии документа

    filterDropDownOptionsRemarksTabApp4 = $$("div#menu- li"), // опции дропдаун Фильтров во вкладке 'Замечания'
            remarksOnTableRemarksTabApp4 = $$("div.ag-center-cols-container>.ag-row") // строки замечаний в таблице во вкладке 'Замечания'
                    ;

    public File testUploadFile = filesPage.testUploadFile;
    public File testUploadFile1 = filesPage.testUploadFile1;
    public String docName = "docNameAutoTest_" + faker.lorem().characters(3);
    public String decimalNumber = "dec.num.999";
    public String docNameUpdated = "docNameAutoTestUpdated_" + faker.lorem().characters(3);

//    public int contractsId;
//    public int fileId;

    // Добавить файл в документ в окне редактирования app2
    public void uploadFileToDocOnEditModal(String attachFile) {
        attachFilesBtnOnEditDocModal.uploadFromClasspath("files/" + attachFile);
    }

    // Добавить файл в документ в окне редактирования app4
    public void uploadFileToDocOnEditModalApp4(String attachFile) {
        attachFilesBtnOnEditDocModalApp4.uploadFromClasspath("files/" + attachFile);
    }

    // получить id типа дока по имени типа
    public int getDocTypeIdByTypeName(String authCookie, String docTypeName){
        int docTypeId =
                given()
                        .spec(requestSpec)
                        .queryParam("q", docTypeName)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/documenttypes")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("items.id[0]");
        return docTypeId;

    }

    // создать документ и получить его id
    public int createDocumentInContractAndGetIdApi
    (String authCookie, String docName, String decimalNumber, int docTypeId, int contractsId, int fileId) {

        String newDocumentBodyDataApp2 =
                "{" +
                        "\"name\":" + "\"" + docName + "\"" + ", " +
                        "\"decimalNumber\":" + "\"" + decimalNumber + "\"" + ", " +
                        "\"documentType\": " + "{" +
                        "\"id\":" + "\"" + docTypeId + "\"" +
                        "}," +
                        "\"agreement\": " + "{ " +
                        "\"id\":" + "\"" + contractsId + "\"" +
                        "}," +
                        "\"documentFiles\": " + "[" +
                        "{ " +
                        "\"file\": " + "{ " +
                        "\"id\":" + "\"" + fileId + "\"" +
                        "} " + "} " + "]," +
                        "\"documentStatus\": " + "{ " +
                        "\"id\":" + "\"1\"" +
                        "}" +
                        "}";

        int docId =
                given()
                        .spec(requestSpec)
                        .body(newDocumentBodyDataApp2)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/documents")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        return docId;
    }

    // создать документ "Паспорт робота" и получить его id
    // filesPage.testUploadFile
    // src/test/resources/files/null (No such file or directory)
    public int createDocumentInRobotAndGetIdApi
    (String authCookie, int docTypeId, int objectId) {
        int fileId = filesPage.uploadFileAndGetIdApi(authCookie, testUploadFile);
        String newRobotsDocumentBodyDataApp2 =
                "{" +
                        "\"name\":" + "\"" + docName + "\"" + ", " +
                        "\"documentType\": " + "{" +
                        "\"id\":" + "\"" + docTypeId + "\"" +
                        "}," +
                        "\"documentFiles\": " + "[" +
                        "{ " +
                        "\"file\": " + "{ " +
                        "\"id\":" + "\"" + fileId + "\"" +
                        "} " + "} " + "]," +
                        "\"objectId\":" + "\"" + objectId + "\"" + ", " +
                        "\"objectType\":" + "\"ROBOT\"" +
                        "}";
        int docId =
                given()
                        .spec(requestSpec)
                        .body(newRobotsDocumentBodyDataApp2)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/documents")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        return docId;
    }

    // получить все документы по id объекта
    public Response getAllObjectDocuments(String authCookie, int objectId, String objectTypeCode) {
        String requestBody =
                "{" +
                        "\"page\":" + "\"" + 0 + "\"" + ", " +
                        "\"limit\":" + "\"" + 999 + "\"" + ", " +
                        "\"sort\": " + "[" +
                        "{ " +
                        "\"property\": " + "\"id\"" + ", " +
                        "\"direction\":" + "\"asc\"" +
                        "} " + "]," +
                        "\"deleted\":" + "\"false\"" + ", " +
                        "\"blocked\":" + "\"false\"" + ", " +
                        "\"params\": " + "{" +
                        "\"lastVersion\":" + "\"true\"" + ", " +
                        "\"objectId\":" + "\"" + objectId + "\"" + ", " +
                        "\"objectTypeCode\":" + "\"" + objectTypeCode + "\"" +
                        "}" +
                        "}";
        Response objectAllDocuments =
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/document/all");
        return objectAllDocuments;
    }

    // старт процесс отчетного документа по кнопке на интерфейсе в строке документа и строке версии api
    public void startProcessOfReportingDocumentApi(String authCookie, int docId) {
        given()
                .spec(requestSpec)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/documents/" + docId + "/startProcess")
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

    // остановить процесс отчетного документа по кнопке на интерфейсе в строке документа и строке версии api
    public void stopProcessOfReportingDocumentApi(String authCookie, int docId) {
        given()
                .spec(requestSpec)
                .contentType("charset=utf-8")
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .post(BASE_URL + "/documents/" + docId + "/stopProcess")
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

    // получение атрибута документа movingProcess api
    public boolean getAttrOfDocumentMovingProcessApi(String authCookie, int docId) {
        boolean movingProcess =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/documents/" + docId)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("movingProcess");
        return movingProcess;
    }

    // Открыть карточку документа по имени на вкладке Документы
    public void openDocsCardByDocsNameOnDocsTab(String docName) {
        SelenideElement linkToDocsCardByDocsNameOnDocsTa = $(byText(docName)).parent();
        linkToDocsCardByDocsNameOnDocsTa.click();
    }

    // Проверить видимость файла по имени апп2
    public void checkFileNameInDocsCard(String fileName) {
        SelenideElement nameOfFileDocsCard = $(".k-detail-row").$(byText(fileName));
        nameOfFileDocsCard.shouldBe(visible);
    }

    // Проверить видимость файла по имени апп4
    public void checkFileNameInDocsCardApp4(String fileName) {
//        SelenideElement nameOfFileDocsCard = $(".ag-center-cols-clipper").$(byText(fileName));
        SelenideElement nameOfFileDocsCard = $(".ag-center-cols-viewport").$(byText(fileName));
        nameOfFileDocsCard.shouldBe(visible);
    }

    // Сформировать ссылку для возврата из карточки дока в таблицу документов договора
    public String makeLinkToContractsDocsTabOnDocsCard(int objectsId) {
        String linkToContractsCardDocsTab = BASE_URL + "/#?jump=agreementnolimited&subpage=show&id=" + objectsId + "&tab=tabDocuments";
        return linkToContractsCardDocsTab;
    }

    // удалить файл по имени из документа в окне редактирования
    public void deleteFileByNameOnEditDocModal(String fileName) {
        SelenideElement deleteFileByNameBtnOnEditDocModal = $(byText(fileName)).parent().parent().$("span[title='Удалить']");
        deleteFileByNameBtnOnEditDocModal.click();
        saveBtnOnEditDocModal.click();
    }

    // Проверить, что нет файла в карточке документа по имени
    public void fileNotExistByNameOnDocsCard(String fileName) {
        SelenideElement fileByNameOnDocsCard = $(byText(fileName));
        fileByNameOnDocsCard.shouldNot(exist, visible);
    }

    // Проверить, что нет документа по имени
    public void documentNotExistByNameOnDocsTab(String docName) {
        SelenideElement docByNameOnDocsTab = $(byText(docName));
        documentsTableOnDocsTab.shouldBe(visible);
        docByNameOnDocsTab.shouldNot(exist, visible);
    }

    public void openDocsCardMainTabApp4(int docId) {
        String linkToDocsCardMainTab = "/v4/Documents/" + docId;
        open(linkToDocsCardMainTab);
    }

    public void openDocsCardRemarksTabApp4(int docId) {
        String linkToDocsCardMainTab = "/v4/Documents/" + docId + "/remarks";
        open(linkToDocsCardMainTab);
    }

    // Создать в договоре СПО новый документ по id типа и открыть его карточку в апп4
    public void createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(String authCookie, String contractsName, String docName, int docTypeId) {
        step("Создать тестовый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName);
        step("Добавить документ ТЗ в тестовый договор через api", () -> {
        });
        int fileId = filesPage.uploadFileAndGetIdApi(authCookie, testUploadFile);
        int docId = createDocumentInContractAndGetIdApi(authCookie, docName, decimalNumber, docTypeId, contractsId, fileId);
        step("Открыть карточку созданного документа на вкладке 'Основная информация'", () -> {
            openDocsCardMainTabApp4(docId);
        });
    }

    // Создать в договоре ППО новый документ по id типа и открыть его карточку в апп4
    public void createNewDocumentInNewContractPpoByDocTypeIdAndOpenDocsCard(String authCookie, String contractsName, String docName, int docTypeId) {
        step("Создать тестовый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractPpoDraftAndGetIdApi(authCookie, contractsName);
        step("Добавить документ ТЗ в тестовый договор через api", () -> {
        });
        int fileId = filesPage.uploadFileAndGetIdApi(authCookie, testUploadFile);
        int docId = createDocumentInContractAndGetIdApi(authCookie, docName, decimalNumber, docTypeId, contractsId, fileId);
        step("Открыть карточку созданного документа на вкладке 'Основная информация'", () -> {
            openDocsCardMainTabApp4(docId);
        });
    }

    // получить id дока из ui атрибута в карточке
    public String getDocIdFromDocsCard() {
        String idFromReguestsCard = docIdOnDocsCardApp4.scrollIntoView(true).getText();
        String regexGetId = "\\d+";
        Pattern p = Pattern.compile(regexGetId);
        Matcher m = p.matcher(idFromReguestsCard);
        String id = null;
        while (m.find()) {
            id = m.group();
        }
        return id;
    }

    public String getFileIdFromFilesLinkOfFirstVersionOnDocumentVersionsTableOnMainTabApp4() {
        String idFromReguestsCard = firstFileOfFirstVersionOnDocumentVersionsTableOnMainTabApp4.getAttribute("href");
        String regexGetId = "\\d+";
        Pattern p = Pattern.compile(regexGetId);
        Matcher m = p.matcher(idFromReguestsCard);
        String id = null;
        while (m.find()) {
            id = m.group();
        }
        return id;
    }

    // Получить id договорa через api по id документа апп4
    public int getContractIdByDocIdApiApp4(String authCookie, int docId) {
        int contractsId =
                given()
                        .spec(requestSpec)
//                        .body(newContractSpoBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/document/" + docId + "/document-card")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("agreement.id");
        return contractsId;
    }

    // получить json-строку тела из запроса карточки документа
    public String getDocsCardJsonStByDocIdApiApp4(String authCookie, int docId) {
        String jsonResponseSt =
                given()
                        .spec(requestSpec)
//                        .body(newContractSpoBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/document/" + docId + "/document-card")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().asString();
        return jsonResponseSt;
    }


    // Удалить тестовый договор через api по id документа в карточке апп4
    public void deleteContractByDocIdOnDocsCardApi(String authCookie) {
        int docId = Integer.parseInt(getDocIdFromDocsCard());
        int contractsId = getContractIdByDocIdApiApp4(authCookie, docId);
        contractsPage.deleteContractApi(authCookie, contractsId);
    }

    // Добавить новую версию документа по кнопке 'Добавить новую версию' на ui
    public void addNewDocsVersionByAddNewVersionBtnOnMainTabApp4(String attachFile) {
        AddNewVersionBtnOnMainTabApp4.click();
        uploadFileToDocOnEditModalApp4(attachFile);
        sleep(1000);
        saveBtnOnEditDocModalApp4.click();
    }

    // создать новую версию документа api и получить id версии
    public static int createNewDocumentVersionAndGetItsIdApi(String authCookie, int baseDocumentId, int fileId) {

        // some_document.xlsx
        String newDocVersionBodyData = "" +
                "{\"baseDocumentId\":" + baseDocumentId + "," +
                "\"documentFiles\":[" +
                "{\"file\":{" +
                "\"id\":" + fileId +
                "}}" +
                "]}";
        int docId =
                given()
                        .spec(requestSpec)
                        .body(newDocVersionBodyData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/documents/saveVersion")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response().body().path("id");
        return docId;
    }

    // удалить документ по id
    public static Response deleteDocumentByIdApi(String authCookie, int docId) {

        Response deleteDocumentById =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .delete(BASE_URL + "/documents/" + docId)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response();
        return deleteDocumentById;
    }

    public void uploadFilesBtnSvgPathAttrsApp4Checking() {
        uploadFilesBtnSvgPathOnVersionStringOnMainTabApp4
                .shouldHave(attribute("d", "M86 81.989L128 40 170 81.989M128 152L128 40.029M216 152v56a8 8 0 01-8 8H48a8 8 0 01-8-8V152"),
                        attribute("stroke-linecap", "round"),
                        attribute("stroke-width", "16"),
                        attribute("stroke-linejoin", "round"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"));
    }

    public void downloadFilesBtnSvgPathAttrsApp4Checking() {
        downloadFilesBtnSvgPathFirstOnVersionStringOnMainTabApp4
                .shouldHave(attribute("d", "M86 110l42 42 42-42"),
                        attribute("stroke-linecap", "round"),
                        attribute("stroke-width", "16"),
                        attribute("stroke-linejoin", "round"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"));
        downloadFilesBtnSvgPathSecondOnVersionStringOnMainTabApp4
                .shouldHave(attribute("d", "M128 39.97l0 112"),
                        attribute("stroke-linecap", "round"),
                        attribute("stroke-width", "16"),
                        attribute("stroke-linejoin", "round"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"));
        downloadFilesBtnSvgPathThirdOnVersionStringOnMainTabApp4
                .shouldHave(attribute("d", "M224 136v72 0c0 4.41827-3.58173 8-8 8H40v0c-4.41828 0-8-3.58173-8-8v-72"),
                        attribute("stroke-linecap", "round"),
                        attribute("stroke-width", "16"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"));
    }

    public void downloadRegisteredFilesBtnSvgAttrsApp4Checking() {
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(1)")
                .shouldHave(attribute("x", "2"),
                        attribute("width", "11"),
                        attribute("height", "4"),
                        attribute("fill", "#ccc"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(2)")
                .shouldHave(attribute("x", "3"),
                        attribute("y", "12"),
                        attribute("width", "9"),
                        attribute("height", "4"),
                        attribute("fill", "#ccc"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(3)")
                .shouldHave(attribute("y", "5"),
                        attribute("width", "15"),
                        attribute("height", "6"),
                        attribute("fill", "currentColor"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(4)")
                .shouldHave(attribute("y", "11"),
                        attribute("width", "2"),
                        attribute("height", "2"),
                        attribute("fill", "currentColor"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(5)")
                .shouldHave(attribute("x", "13"),
                        attribute("y", "11"),
                        attribute("width", "2"),
                        attribute("height", "2"),
                        attribute("fill", "currentColor"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(6)")
                .shouldHave(attribute("x", "12"),
                        attribute("y", "6"),
                        attribute("width", "1"),
                        attribute("height", "1"),
                        attribute("fill", "white"));
        downloadRegisteredFilesBtnOnVersionStringOnMainTabApp4.$("svg>rect:nth-child(7)")
                .shouldHave(attribute("x", "10"),
                        attribute("y", "6"),
                        attribute("width", "1"),
                        attribute("height", "1"),
                        attribute("fill", "white"));

    }

    public void stopProcessBtnSvgAttrsApp4Checking() {
        stopProcessBtnOnVersionStringOnMainTabDisabledApp4.$("svg>circle")
                .shouldHave(attribute("cx", "32"),
                        attribute("cy", "32"),
                        attribute("stroke-width", "4"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"));
        stopProcessBtnOnVersionStringOnMainTabDisabledApp4.$("svg>rect")
                .shouldHave(attribute("x", "25"),
                        attribute("y", "25"),
                        attribute("stroke-width", "4"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "currentColor"));

    }

    public void downloadRemarksBtnSvgAttrsApp4Checking() {
        downloadRemarksBtnOnVersionStringOnMainTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", "2"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M4,3H20a1,1,0,0,1,1,1V20a1,1,0,0,1-1,1H4a1,1,0,0,1-1-1V4A1,1,0,0,1,4,3Zm8,5h5m-5,4h5m-5,4h5"));
        downloadRemarksBtnOnVersionStringOnMainTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke-width", "2"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M7.5 8L9 8M7.5 12L9 12M7.5 16L9 16"));

    }

    // Кнопки на строке версии в таблице на вкладке 'Ход согласования' документа группы Отчетные апп4
// Иконка кнопки 'Экспортировать отчет по процессу согласования'
    public void btnExportReportAgreementProcessEnabledSvgAttrsApp4Checking() {
        btnExportReportAgreementProcessOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", "20"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M200 176a24 24 0 1 0 0 48 24 24 0 1 0 0-48Z"));
        btnExportReportAgreementProcessOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke-width", "20"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M72 56h96v0c17.6731 0 32 14.3269 32 32 0 17.6731-14.3269 32-32 32H72v0c-22.0914 0-40 17.9086-40 40s17.9086 40 40 40h104"));
    }

    // Иконка кнопки 'Экспортировать отчет о результатах согласования'
    public void btnExportReportAgreementResultEnabledSvgAttrsApp4Checking() {
        btnExportReportAgreementResultOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", "16"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M32 56H224a0 0 0 010 0V192a8 8 0 01-8 8H40a8 8 0 01-8-8V56A0 0 0 0132 56zM32 104L224 104M32 152L224 152M88 104L88 200"));
    }

    // Иконка кнопки 'Экспортировать замечания к версии'
    public void btnExportVersionsRemarksEnabledSvgAttrsApp4Checking() {
        btnExportVersionsRemarksOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", "2"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M4,3H20a1,1,0,0,1,1,1V20a1,1,0,0,1-1,1H4a1,1,0,0,1-1-1V4A1,1,0,0,1,4,3Zm8,5h5m-5,4h5m-5,4h5"));
        btnExportVersionsRemarksOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke-width", "2"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M7.5 8L9 8M7.5 12L9 12M7.5 16L9 16"));
    }

    // Иконка кнопки 'Экспортировать лист согласования'
    public void btnExportAgreementListEnabledSvgAttrsApp4Checking() {
        btnExportAgreementListOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>rect")
                .shouldHave(
                        attribute("width", "11"),
                        attribute("height", "14"),
                        attribute("fill", "#ccc"));
        btnExportAgreementListOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path")
                .shouldHave(
                        attribute("stroke", "currentColor"),
                        attribute("fill", "currentColor"),
                        attribute("d", "M9.5 11.5V6.56752L11.5 8.23419V12V12.5H12H12.8981L10.5 15.2407L8.10188 12.5H9H9.5V12V11.5Z"));
    }

    // Иконка кнопки 'Скорректировать даты'
    public void btnEditDatesRemarksDisabledSvgAttrsApp4Checking() {
        btnEditDatesOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", "16"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M88 24H168V64H88z"));
        btnEditDatesOnVersionStringOnTasksTableOnAgreementProgressTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke-width", "16"),
                        attribute("stroke", "currentColor"),
                        attribute("fill", "none"),
                        attribute("d", "M168 40h32a8 8 0 018 8V216a8 8 0 01-8 8H56a8 8 0 01-8-8V48a8 8 0 018-8H88M96 152L160 152M96 120L160 120"));
    }

    // Кнопки на вкладке 'Замечания' документа группы Отчетные апп4
// Иконка кнопки 'Фильтр'
    public void btnFilterSvgAttrsApp4Checking() {
        filterBtnRemarksTabApp4.$("svg>path")
                .shouldHave(
                        attribute("d", "M4.25 5.61C6.27 8.2 10 13 10 13v6c0 .55.45 1 1 1h2c.55 0 1-.45 1-1v-6s3.72-4.8 5.74-7.39c.51-.66.04-1.61-.79-1.61H5.04c-.83 0-1.3.95-.79 1.61z"));
    }

    // Иконка кнопки 'Экспортировать'
    public void btnExportSvgAttrsApp4Checking() {
        exportBtnRemarksTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke-width", ".5"),
                        attribute("stroke", "#247E4D"),
                        attribute("fill", "#fff"),
                        attribute("d", "m1 2.626 9.3-1.8v1.8h5.955s.566.016.672.37c.063.224.087.458.07.69v10.436a.968.968 0 0 1-.32.728c-.21.13-.457.19-.704.173H10.3v1.81L1 15.024V2.626Z"));
        exportBtnRemarksTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("fill", "#247E4D"),
                        attribute("d", "M11.928 4.571h-1.833v1.354h1.833V4.571ZM15.613 4.571h-3.095v1.354h3.095V4.571ZM11.928 7.048h-1.833v1.354h1.833V7.048ZM15.613 7.048h-3.095v1.354h3.095V7.048ZM11.928 9.525h-1.833v1.354h1.833V9.525ZM15.613 9.525h-3.095v1.354h3.095V9.525ZM11.928 12.002h-1.833v1.354h1.833v-1.354ZM15.613 12.002h-3.095v1.354h3.095v-1.354Z"));
        exportBtnRemarksTabApp4.$("svg>path", 2)
                .shouldHave(
                        attribute("fill", "#247E4D"),
                        attribute("d", "M3.2 6.542h1.188l.688 1.875.844-2.031 1.234-.078-1.422 2.813 1.422 2.875-1.234-.109-.844-2.109-.844 2.031-1.141-.094 1.3-2.594L3.2 6.542Z"));
        exportBtnRemarksTabApp4.$("svg>g>path")
                .shouldHave(
                        attribute("fill", "#fff"),
                        attribute("d", "M8.5 2.606h1.415v12.031H8.5V2.607Z"));
    }

    // Иконка кнопки 'Четыре строки'
    public void btnFourStringsSvgAttrsApp4Checking() {
        fourStringsBtnRemarksTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 1.3999H0.5V4.2399H11.5V1.3999Z"));
        fourStringsBtnRemarksTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 4.3999H0.5V7.2399H11.5V4.3999Z"));
        fourStringsBtnRemarksTabApp4.$("svg>path", 2)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 7.3999H0.5V10.2399H11.5V7.3999Z"));
    }

    // Иконка кнопки 'Все строки'
    public void btnAllStringsSvgAttrsApp4Checking() {
        allStringsBtnRemarksTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 1.2002H0.5V6.2002H11.5V1.2002Z"));
        allStringsBtnRemarksTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 6.2002H0.5V11.2002H11.5V6.2002Z"));
        allStringsBtnRemarksTabApp4.$("svg>path", 2)
                .shouldHave(
                        attribute("stroke", "#C7C7C7"),
                        attribute("d", "M11.5 11.2002H0.5V16.2002H11.5V11.2002Z"));
    }

    // Иконка кнопки 'Добавить замечание'
    public void btnAddRemarkSvgAttrsApp4Checking() {
        addRemarkBtnRemarksTabApp4.$("svg>path", 0)
                .shouldHave(
                        attribute("fill", "#EBEBEB"),
                        attribute("d", "M8 16.15a8 8 0 1 0 0-16 8 8 0 0 0 0 16Z"));
        addRemarkBtnRemarksTabApp4.$("svg>path", 1)
                .shouldHave(
                        attribute("fill", "#909090"),
                        attribute("d", "M9 4.15H7v8h2v-8Z"));
        addRemarkBtnRemarksTabApp4.$("svg>path", 2)
                .shouldHave(
                        attribute("fill", "#909090"),
                        attribute("d", "M12 7.15H4v2h8v-2Z"));
    }

    public String exportReportAgreementProcessForDocumentVersionLink() { // Отчет по процессу согласования версии документа url
        String docId = getDocIdFromDocsCard();
        String link = BASE_URL + "/documents/exportAgreeTasks/" + docId;
        return link;
    }

    public String exportReportAgreementResultForDocumentLink() { // Отчет о результатах согласования документа url
        String docId = getDocIdFromDocsCard();
        String link = BASE_URL + "/app4/agreement-process-milestone/export/report/" + docId;
        return link;
    }

    public String exportVersionsRemarksForDocumentLink() { // Экспортировать замечания к версии url
        String docId = getDocIdFromDocsCard();
        String link = BASE_URL + "/app4/remark/export?documentId=" + docId + "&documentVersion=1&exportType=EXCEL&allPermissions=true";
        return link;
    }

    public String exportgreementListForDocumentLink() { // Экспортировать лист согласования url
        String docId = getDocIdFromDocsCard();
        String link = BASE_URL + "/documents/exportApprovalSheet/" + docId + "?format=pdf";
        return link;
    }

    // сделать активной в json кнопку Остановить процесс / "stopProcess" на карточке документа и преобразовать обратно в строку JSON
    public String makeDisabledFalseInStopProcessBtnInDocsCard(String initialJsonString) {
        // заменить значение нужного ключа в json и преобразовать обратно в строку JSON
        String updatedJsonString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(initialJsonString);

            // Получаем массив documentGridList
            JsonNode documentGridListNode = rootNode.path("documentGridList");

            if (documentGridListNode.isArray()) {
                for (JsonNode documentNode : documentGridListNode) {
                    // Получаем узел buttonsConfig
                    JsonNode buttonsConfigNode = documentNode.path("buttonsConfig");

                    if (buttonsConfigNode.isObject()) {
                        // Приводим узел к ObjectNode для возможности модификации
                        ObjectNode buttonsConfigObject = (ObjectNode) buttonsConfigNode;

                        // Получаем узел stopProcess
                        JsonNode stopProcessNode = buttonsConfigObject.path("stopProcess");

                        if (stopProcessNode.isObject()) {
                            // Приводим узел к ObjectNode для возможности модификации
                            ObjectNode stopProcessObject = (ObjectNode) stopProcessNode;

                            // Меняем значение "disabled" на false
                            stopProcessObject.put("disabled", false);
                        }
                    }
                }
            }
            updatedJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Преобразуем обратно в строку JSON
        return updatedJsonString;
    }




}
