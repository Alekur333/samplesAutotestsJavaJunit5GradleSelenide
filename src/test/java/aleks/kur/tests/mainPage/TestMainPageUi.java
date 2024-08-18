package aleks.kur.tests.mainPage;

import org.junit.jupiter.api.*;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


@Tags({@Tag("ui"), @Tag("mainPage")})
@DisplayName("Проверки элементов главной страницы (боковое меню, заголовок)")
public class TestMainPageUi extends TestBaseUi {

        @BeforeEach
        public void authorization() {
            authApi.authorizationApiOnUi();
        }

        @Test
        @Tags({@Tag("smoke")})
        @DisplayName("Открывается страница 'Раздел уведомлений' из бокового меню")
        void sideBarMenuToNotificationsPageTest() {
            open("");
            mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
            mainPage.shouldBeSideBarMenuNotifications();
            notificationsPage.shouldBeNotificationsPage();
        }

        @Test
        @Tags({@Tag("smoke")})
        @DisplayName("Открывается страница 'Сообщения' из бокового меню")
        void sideBarMenuToMessagesPageTest() {
            open("");
            mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
            mainPage.shouldBeSideBarMenuMessages();
            messagesPage.shouldBeMessagesPage();
        }

        @Test
        @Tags({@Tag("smoke")})
        @DisplayName("Открывается страница 'Задачи' из бокового меню")
        void sideBarMenuToTasksPageTest() {
            open("");
            mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
            mainPage.shouldBeSideBarMenuTasks();
            tasksPage.shouldBeTasksPage();
        }

        @Test
        @Tags({@Tag("smoke")})
        @DisplayName("Открывается страница 'Отчеты' из бокового меню")
        void sideBarMenuToReportsPageTest() {
            open("");
            mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
            mainPage.shouldBeSideBarMenuReports();
            reportsPage.shouldBeReportsPage();
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("requestAuto")})
        @DisplayName("Открывается страница 'Заявки (автоматизация)' из бокового меню")
        void sideBarMenuToRequestAutomationPageTest() {
            step("Страница 'Заявки (автоматизация)' открывается", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.fromSideBarMenuToRequestAutomation();
                requestAutoPage.shouldBeRequestAutoPage();
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("requestMod")})
        @DisplayName("Открывается страница 'Заявки (модификация)' из бокового меню")
        void sideBarMenuToRequestModificationPageTest() {
            step("Страница 'Заявки (модификация)' открывается", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.fromSideBarMenuToRequestModification();
                requestModPage.shouldBeRequestsModificationPage();
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("planPiWorks")})
        @DisplayName("Открывается страница 'Работы планов ПИ' из бокового меню")
        void sideBarMenuToPlanPiWorksPageTest() {
            step("Страница 'Работы планов ПИ' открывается", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.fromSideBarMenuToPlanPiWorks();
                planPiWorksPage.shouldBePlanPiWorksPage();
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("projects")})
        @DisplayName("Раскрывается меню Проекты из бокового меню")
        void sideBarMenuHasProjectsListTest() {
            step("Список проектов раскрывается и не пустой", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.fromSideBarMenuOpenProgectsList();
                mainPage.onSideBarMenuProgectsListNotEmty();
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("limits")})
        @DisplayName("Раскрывается меню Лимиты из бокового меню")
        void sideBarMenuHasLimitsListTest() {
            step("Меню лимитов раскрывается и не пустой", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.fromSideBarMenuOpenLimitsList();
                mainPage.onSideBarMenuLimitsListNotEmty();
            });
            step("Меню лимитов содержит пункт 'По источникам финансирования' ", () -> {
                mainPage.sideMenuLimitsFinancing.shouldHave(text("По источникам финансирования"));
            });
            step("Меню лимитов содержит пункт 'По исполнителям' ", () -> {
                mainPage.sideMenuLimitsExecutors.shouldHave(text("По исполнителям"));
            });
            step("Меню лимитов содержит пункт 'Лимиты по ФЗ' ", () -> {
                mainPage.sideMenuLimitsCustomers.shouldHave(text("Лимиты по ФЗ"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("reportByKT")})
        @DisplayName("Раскрывается меню Отчет по КТ из бокового меню")
        void sideBarMenuHasReportByKtListTest() {
            step("Меню Отчет по КТ раскрывается и имеет 3 пункта", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuReportByKT.click();
                mainPage.reportByKtElemetsOnSideMenu.shouldHave(size(3));
            });
            step("Меню Отчет по КТ содержит пункт 'Структура отчета' ", () -> {
                mainPage.sideMenuKtReportsStructure.shouldHave(text("Структура отчета"));
            });
            step("Меню Отчет по КТ содержит пункт 'Отчет' ", () -> {
                mainPage.sideMenuKtReport.shouldHave(text("Отчет"));
            });
            step("Меню Отчет по КТ содержит пункт 'Архив' ", () -> {
                mainPage.sideMenuKtArchive.shouldHave(text("Архив"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("reportByKT")})
        @DisplayName("Открывается страница Структура отчета по КТ из бокового меню")
        void sideBarMenuToReportByKtStructurePageTest() {
            step("Открыть меню Отчет по КТ и перейти в раздел Структура отчета", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuReportByKT.click();
                mainPage.sideMenuKtReportsStructure.click();
            });
            step("Проверить url и заголовок страницы Структура отчета", () -> {
                assertThat(url()).contains("reportCStructures");
                reportByKtPage.headerNameLocator.shouldHave(exactText("Настройка отчета по КТ"), Duration.ofSeconds(40));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("reportByKT")})
        @DisplayName("Открывается страница текущего отчета по КТ из бокового меню")
        void sideBarMenuToReportByKtPageTest() {
            step("Открыть меню Отчет по КТ и перейти в раздел Отчет", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuReportByKT.click();
                mainPage.sideMenuKtReport.click();
            });
            step("Проверить url и заголовок страницы отчета", () -> {
                assertThat(url()).contains("reportC&id=current");
                reportByKtPage.headerNameLocator.shouldHave(text("Отчет по КТ за период"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("reportByKT")})
        @DisplayName("Открывается страница архива отчета по КТ из бокового меню")
        void sideBarMenuToReportByKtArchivePageTest() {
            step("Открыть меню Отчет по КТ и перейти в раздел Архив", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuReportByKT.click();
                mainPage.sideMenuKtArchive.click();
            });
            step("Проверить url и заголовок страницы архива", () -> {
                assertThat(url()).contains("reportCArchive");
                reportByKtPage.headerNameLocator.shouldHave(text("Архив отчета по КТ, на период"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("piPlans")})
        @DisplayName("Открывается страница 'Планы ПИ' из бокового меню")
        void sideBarMenuToPiPlansPageTest() {
            step("Перейти на страницу Планы ПИ из бокового меню ", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuPiPlans.click();
            });
            step("Проверить url и заголовок страницы Планы ПИ", () -> {
                assertThat(url()).contains("plan_PI");
                piPlansPage.headerNameLocator.shouldHave(exactText("Планы ПИ"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("agreements")})
        @DisplayName("Открывается страница 'Договоры' из бокового меню")
        void sideBarMenuToAgreementsPageTest() {
            step("Перейти на страницу из бокового меню ", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuAgreements.click();
            });
            step("Проверить url и заголовок страницы", () -> {
                assertThat(url()).contains("agreement");
                contractsPage.headerNameLocator.shouldHave(exactText("Договоры"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("coordination")})
        @DisplayName("Открывается страница 'Согласование документов' из бокового меню")
        void sideBarMenuToDocsCoordinationPageTest() {
            step("Перейти на страницу из бокового меню ", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuDocsCoordination.click();
            });
            step("Проверить url и заголовок страницы", () -> {
//            assertThat(url()).contains("approvalDocumentState"); // апп2
                assertThat(url()).contains("approval-documents"); // апп4
                docsCoordinationPage.headerNameLocator.shouldHave(exactText("Согласование документов"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("directories")})
        @DisplayName("Открывается страница 'Справочники' из бокового меню")
        void sideBarMenuToDirectoriesPageTest() {
            step("Перейти на страницу из бокового меню ", () -> {
                open("");
                mainPage.sideMenuBlock.shouldBe(visible, Duration.ofSeconds(5));
                mainPage.sideMenuDirectories.click();
            });
            step("Проверить url и заголовок страницы", () -> {
                assertThat(url()).contains("references");
                directoriesPage.headerNameLocator.shouldHave(exactText("Справочники"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("settings")})
        @DisplayName("Открывается страница 'Настройки' из бокового меню")
        void sideBarMenuToSettingsPageTest() {
            open("");
            step("Перейти на страницу из бокового меню ", () -> {
                if ($$("a[href='#?page=settings']").size() > 0) {
                    mainPage.sideMenuSettings.click();
                    step("Проверить url и заголовок страницы app2", () -> {
                        assertThat(url()).contains("page=settings");
                        settingsPage.headerNameLocator.shouldHave(exactText("Настройки"));
                    });
                } else {
                    mainPage.sideMenuSettingsApp4.click();
                    step("Проверить url и заголовок страницы app4", () -> {
                        assertThat(url()).contains("/v4/settings");
                        settingsPage.headerNameLocatorApp4.shouldHave(exactText("Настройки"));
                    });
                }
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("documentsTemplates")})
        @DisplayName("Открывается страница 'Документы и шаблоны' из бокового меню")
        void sideBarMenuToDocumentsTemplatesPageTest() {
            open("");
            //app2 версия
//        step("Перейти на страницу из бокового меню ", () -> {
//            mainPage.sideMenuDocumentsTemplates.click();
//        });
//        step("Проверить url и заголовок страницы", () -> {
//            assertThat(url()).contains("documentsAndTemplates");
//            documentsTemlatesPage.headerNameLocator.shouldHave(exactText("Документы и шаблоны"));
//        });

            //app4 версия
            step("Перейти на страницу из бокового меню ", () -> {
                mainPage.sideMenuDocumentsTemplatesApp4.click();
            });
            step("Проверить url и заголовок страницы", () -> {
                assertThat(url()).contains("/v4/documents-and-templates");
                documentsTemlatesPage.headerNameLocatorApp4.shouldHave(exactText("Документы и шаблоны"));
            });
        }

        @Test
        @Tags({@Tag("smoke"), @Tag("pcDzo")})
        @DisplayName("Открывается страница 'ПЦ ДЗО' из бокового меню")
        void sideBarMenuToPcDzoPageUiTest() {
            open("");

            step("Перейти на страницу из бокового меню ", () -> {
                mainPage.sideMenuPcDzo.click();
                step("Проверить url и заголовок страницы app4", () -> {
                    assertThat(url()).contains("/v4/dzo");
                    pcDzoPage.headerNameLocatorPcDzoPage.shouldHave(exactText("ПЦ дочерних и зависимых обществ"));
                });
            });
        }

}
