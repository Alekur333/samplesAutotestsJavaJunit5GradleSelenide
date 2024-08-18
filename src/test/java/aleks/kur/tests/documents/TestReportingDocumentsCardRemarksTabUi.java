package aleks.kur.tests.documents;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.pages.DocumentsPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.refresh;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки карточки документа вкладки 'Замечания' документов группы Отчетные")
@Tags({@Tag("ui"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments"), @Tag("documentsCardAgreementProgressTab")})
//@Disabled
public class TestReportingDocumentsCardRemarksTabUi extends TestBaseUi {

    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUiApp4();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String contractsName = contractsPage.contractsName;
    String docName = documentsPage.docName;
    String decimalNumber = documentsPage.decimalNumber;
    String docNameUpdated = documentsPage.docNameUpdated;
    int docTypeId = 45; // Техническое задание на систему (ТЗ)

    @Test
    @DisplayName("Есть кнопка Фильтр на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeFilterBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.filterBtnRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть панель поиска на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeSearchPanelOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.searchPanelRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть чек-бокс 'Только не снятые замечания' на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeOnlyActualCheckBoxOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.onlyActualCheckBoxRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть дропдаун выбора версий на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeVersionsFilterOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.versionsFilterRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть кнопка экспорта на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeExportBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.exportBtnRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть кнопка 'Четыре строки' на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeFourStringsBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.fourStringsBtnRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть кнопка 'Все строки' на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeAllStringsBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.allStringsBtnRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть кнопка 'Добавить замечание' на вкладке 'Замечания' отправленного на согласование документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeAddRemarkBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            refresh();
            documentsPage.addRemarkBtnRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть таблица замечаний на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeRemarksTableOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Проверить, что есть элемент на вкладке 'Замечания'", () -> {
            documentsPage.remarksTabApp4.click();
            documentsPage.remarksTableRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Должно быть точное количество колонок в таблице на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldHaveExactNumberOfColumnsRemarksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("В таблице задач документа должно быть 12 колонок", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
            documentsPage.columnsOnRemarksTableOnRemarksTabApp4.shouldHave(size(12), Duration.ofSeconds(5));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Имена колонок соответствуют требованиям в таблице на вкладке 'Замечания' нового документа апп4 группы Отчетные\n")
//    @Disabled
    void shouldBeRequiredColumnsOnRemarksTableOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Перейти на вкладку 'Замечания', свернуть боковую панель", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
        });
        step("Проверить наличие колонки '№' и ее порядковый номер", () -> {
            documentsPage.remarkNumberColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(exactText("№"));
        });
        step("Проверить наличие колонки 'Статус' и ее порядковый номер", () -> {
            documentsPage.remarkStatusColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Статус"));
        });
        step("Проверить наличие колонки 'Версия' и ее порядковый номер", () -> {
            documentsPage.docVersionColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Версия"));
        });
        step("Проверить наличие колонки 'Автор' и ее порядковый номер", () -> {
            documentsPage.remarkAuthorColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Автор"));
        });
        step("Проверить наличие колонки 'Раздел документа' и ее порядковый номер", () -> {
            documentsPage.docChapterColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Раздел документа"));
        });
        step("Проверить наличие колонки 'Замечание' и ее порядковый номер", () -> {
            documentsPage.remarksTextColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Замечание"));
        });
        step("Проверить наличие колонки 'Комментарии проверяющих' и ее порядковый номер", () -> {
            documentsPage.remarksCommentColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldBe(empty);
        });
        step("Проверить наличие колонки 'Обработка' и ее порядковый номер", () -> {
            documentsPage.remarkProcessingColumnHeaderOnRemarksTableOnRemarksTabApp4.shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Обработка"));
        });
        step("Проверить наличие колонки 'Подтверждение' и ее порядковый номер", () -> {
            documentsPage.remarkApprovingColumnHeaderOnRemarksTableOnRemarksTabApp4.scrollIntoView(true).shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Подтверждение"));
        });
        step("Проверить наличие колонки 'Комментарий разработчика' и ее порядковый номер", () -> {
            documentsPage.docsAuthorCommentColumnHeaderOnRemarksTableOnRemarksTabApp4.scrollIntoView(true).shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Комментарий разработчика"));
        });
        step("Проверить наличие колонки 'Комментарий автора' и ее порядковый номер", () -> {
            documentsPage.authorsCommentColumnHeaderOnRemarksTableOnRemarksTabTabApp4.scrollIntoView(true).shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Комментарий автора"));
        });
        step("Проверить наличие колонки 'Согласованное решение' и ее порядковый номер", () -> {
            documentsPage.agreedDecisionColumnHeaderOnRemarksTableOnRemarksTabApp4.scrollIntoView(true).shouldBe(visible, Duration.ofSeconds(5))
                    .shouldHave(exactText("Согласованное решение"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Кнопка Фильтр имеет иконку и имя на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldHaveIconAndNameFilterBtnOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Элемент должен иметь иконку и имя", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
            documentsPage.filterBtnRemarksTabApp4.shouldHave(exactText(" Фильтр"));
            documentsPage.filterBtnRemarksTabApp4.$("svg").shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Иконки кнопок должны иметь точные атрибуты svg на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldHaveExactIconBtnsOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку 'Замечания'", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
            refresh();
        });
        step("Иконка кнопки 'Фильтр' имеет точные атрибуты svg", () -> {
            documentsPage.btnFilterSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Экспортировать' имеет точные атрибуты svg", () -> {
            documentsPage.btnExportSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Четыре строки' имеет точные атрибуты svg", () -> {
            documentsPage.btnFourStringsSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Все строки' имеет точные атрибуты svg", () -> {
            documentsPage.btnAllStringsSvgAttrsApp4Checking();
        });
        step("Иконка кнопки 'Добавить замечание' имеет точные атрибуты svg", () -> {
            documentsPage.btnAddRemarkSvgAttrsApp4Checking();
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Кнопка Фильтр вызывет панель фильтров на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldBeFilterPanelByFilterBtnOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Перейти на вкладку 'Замечания'", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
        });
        step("Кнопка Фильтр вызывет панель фильтров", () -> {
            documentsPage.filterBtnRemarksTabApp4.click();
            documentsPage.filterPanelRemarksTabApp4.shouldBe(visible);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Панель фильтров имеет необходимые опции на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @Disabled
    void shouldHaveRequiredOptionsFilterPanelOnRemarksTabOfNewReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Перейти на вкладку 'Замечания' и открыть опции фильтров", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
            documentsPage.filterBtnRemarksTabApp4.click();
            documentsPage.filtersDropDownRemarksTabApp4.click();
        });
        step("Дропдаун фильтров имеет точное количество опций", () -> {
            documentsPage.filterDropDownOptionsRemarksTabApp4.shouldHave(size(4));
        });
        step("Дропдаун фильтров имеет опцию Версия документа", () -> {
            documentsPage.docVersionFilterDropDownRemarksTabApp4
                    .shouldHave(exactText("Версия документа"))
                    .shouldHave(attribute("data-value", "documentVersion"));
        });
        step("Дропдаун фильтров имеет опцию Автор", () -> {
            documentsPage.authorFilterDropDownRemarksTabApp4
                    .shouldHave(exactText("Автор"))
                    .shouldHave(attribute("data-value", "author"));
        });
        step("Дропдаун фильтров имеет опцию Подразделение", () -> {
            documentsPage.authorDepartmentFilterDropDownRemarksTabApp4
                    .shouldHave(exactText("Подразделение"))
                    .shouldHave(attribute("data-value", "authorDepartment"));
        });
        step("Дропдаун фильтров имеет опцию Статус", () -> {
            documentsPage.statusFilterDropDownRemarksTabApp4
                    .shouldHave(exactText("Статус"))
                    .shouldHave(attribute("data-value", "status"));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    static Stream<Arguments> shouldBeModalByDropDownOptionsFilterOnRemarksTabOfNewReportingDocumentApp4Test() {
        DocumentsPage documentsPage = new DocumentsPage();
        return Stream.of(
                Arguments.of(documentsPage.docVersionFilterDropDownRemarksTabApp4, "Версия документа"),
                Arguments.of(documentsPage.authorFilterDropDownRemarksTabApp4, "Автор"),
                Arguments.of(documentsPage.authorDepartmentFilterDropDownRemarksTabApp4, "Подразделение"),
                Arguments.of(documentsPage.statusFilterDropDownRemarksTabApp4, "Статус")
        );
    }
    @MethodSource
    @ParameterizedTest(name = "Фильтр {1} вызывает окно выбора на вкладке 'Замечания' нового документа апп4 группы Отчетные")
//    @DisplayName("Фильтр вызывает окно выбора на вкладке 'Замечания' нового документа апп4 группы Отчетные")
    void shouldBeModalByDropDownOptionsFilterOnRemarksTabOfNewReportingDocumentApp4Test(SelenideElement locator, String filterName) {
        step("Создать в договоре новый документ по id типа и открыть его карточку в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
        });
        step("Перейти на вкладку 'Замечания' и открыть опции фильтров", () -> {
            mainPage.closeSidebarBtnApp4.click();
            documentsPage.remarksTabApp4.click();
            documentsPage.filterBtnRemarksTabApp4.click();
            documentsPage.filtersDropDownRemarksTabApp4.click();
        });
        step("Выбрать опцию и проверить появление модального окна с именем", () -> {
            locator.click();
            modalWindowsPage.checkBoxTableModalHeader.shouldHave(exactText(filterName));
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }

    @Test
    @DisplayName("Есть необходимые элементы в окне добавления замечания на вкладке 'Замечания' отправленного на согласование документа апп4 группы Отчетные")
    void shouldBeRequiredElementsOnAddRemarkModalOnRemarksTabOfAgreengReportingDocumentApp4Test() {
        step("Создать в договоре новый документ по id типа и открыть его карточку на вкладке 'Замечания' в апп4", () -> {
            documentsPage.createNewDocumentInNewContractSpoByDocTypeIdAndOpenDocsCard(authCookie, contractsName, docName, docTypeId);
            mainPage.closeSidebarBtnApp4.click();
        });
        step("Запустить процесс согласования по кнопке в строке версии", () -> {
            documentsPage.firstVersionOnDocumentVersionsTableOnMainTabApp4.hover();
            documentsPage.startProcessBtnOnVersionStringOnMainTabApp4.click();
        });
        step("Перейти на вкладку Замечания, обновить страницу, открыть окно", () -> {
            documentsPage.remarksTabApp4.click();
            refresh();
            documentsPage.addRemarkBtnRemarksTabApp4.click();
        });
        step("Есть заголовок", () -> {
            String addRemarkModalHeader = "Замечание № к документу \"" + docName + "\" (версия 1)";
            remarksPage.addRemarkModalHeader.shouldHave(exactText(addRemarkModalHeader));
        });
        step("Есть информационная панель", () -> {
            remarksPage.addRemarkModalInfoPanel.shouldBe(visible);
        });
        step("Есть элемент № раздела", () -> {
            remarksPage.addRemarkModalChapterNumber.shouldBe(visible);
        });
        step("Есть элемент Название раздела документа", () -> {
            remarksPage.addRemarkModalChapterName.shouldBe(visible);
        });
        step("Есть элемент Замечание", () -> {
            remarksPage.addRemarkModalRemarkText.shouldBe(visible);
        });
        step("Есть элемент Оригинальная формулировка", () -> {
            remarksPage.addRemarkModalOriginalFormulation.shouldBe(visible);
        });
        step("Есть элемент Предлагаемая формулировка", () -> {
            remarksPage.addRemarkModalProposedFormulation.shouldBe(visible);
        });
        step("Есть кнопка выбора файлов", () -> {
            remarksPage.addRemarkModalUploadFilesBtn
                    .shouldHave(exactText("Выберите файлы..."))
                    .shouldBe(enabled);
        });
        step("Есть кнопка Отмена", () -> {
            remarksPage.addRemarkModalCancelBtn
                    .shouldHave(exactText("Отмена"))
                    .shouldBe(enabled);
        });
        step("Есть кнопка Сохранить и добавить следующее замечание", () -> {
            remarksPage.addRemarkModalSaveAndAddNextBtn
                    .shouldHave(exactText("Сохранить и добавить следующее замечание"))
                    .shouldBe(disabled);
        });
        step("Есть кнопка Сохранить", () -> {
            remarksPage.addRemarkModalSaveBtn
                    .shouldHave(exactText("Сохранить"))
                    .shouldBe(disabled);
        });
        step("Удалить тестовый договор через api", () -> {
            documentsPage.deleteContractByDocIdOnDocsCardApi(authCookie);
        });
    }


}
