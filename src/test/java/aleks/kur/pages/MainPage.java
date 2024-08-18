package aleks.kur.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static org.assertj.core.api.Assertions.assertThat;


public class MainPage {

    public SelenideElement

            // Кнопки
            userMenuBtn = $(byXpath("//*[@id='header-user-name-login']")), // кнопка меню пользователя
            logoutBtn = $(byXpath("//*[contains(@type, 'submit')]")), // кнопко выхода из аккаунта
            closeSidebarBtn = $(".button-close-sidebar"), // открыть-закрыть боковое меню
            closeSidebarBtnApp4 = $(".MuiDrawer-paperAnchorLeft .MuiBox-root button svg"), // открыть-закрыть боковое меню


    // Текстовые поля
    headerUserName =

            $(byCssSelector("*[id='header-username']")),  // имя пользователя в заголовке

    // Пункты бокового меню
    sideMenuBlock = $("#menu-region"),
    sideMenuBlockV4 = $("#menu-region"),
            sideMenuNotifications =
                    $(".js-notification-menu-item"), // боковое меню Уведомления
            sideMenuMessages =
//            $("a[href='/v4/messages']"), // боковое меню Сообщения v4
                    $("#main-accordion a[href='#?page=message']"), // боковое меню Сообщения v2
            sideMenuTasks =
                    $("a[href='#?page=tasks']"),  // боковое меню Задачи
            sideMenuReports =
//            $("a[href='#?page=reports']"), // боковое меню Отчеты app2
                    $("a[href='/v4/reports']"), // боковое меню Отчеты app4
            sideMenuPcDzo =
                    $("a[href='/v4/dzo']"), // боковое меню Отчеты app4
            sideMenuRequestAutomation =
                    $("a[href='#?page=request&owner=mine']"), // боковое меню Заявки (автоматизация)
            sideMenuRequestModification =
                    $(byText("Заявки (модификация)")).
                            parent(), // боковое меню Заявки (автоматизация)
    //            sideMenuRequestModification = $("a[href='#?page=deduction']"), // боковое меню Заявки (автоматизация)
    sideMenuPlanPiWorks =
            $("a[href='#?page=works']"), // боковое меню Заявки (автоматизация)
            sideMenuProjects = $("a[href='#programsMenu']"), // боковое меню Проекты
            sideMenuAgreements = $("a[href='#?page=agreement']"), // боковое меню Договоры
            sideMenuDocsCoordination =
//            $("a[href='#?page=approvalDocumentState']"), // боковое меню Согласование док-тов апп2
                    $("a[href='/v4/approval-documents']"), // боковое меню Отчеты app4
            sideMenuLimits = $("a[href='#limitsMenu']"), // боковое меню Лимиты
            sideMenuDirectories =
//            $("a[href='#?page=reference']"), // боковое меню Справочники app2
                    $("a[href='/v4/references']"), // боковое меню Справочники app4
            sideMenuSettings =
                    $("a[href='#?page=settings']"), // боковое меню Настройки
            sideMenuSettingsApp4 =
                    $("a[href='/v4/settings']"), // боковое меню Настройки app4
            sideMenuDocumentsTemplates =
                    $("a[href='#?page=documentsAndTemplates']"), // боковое меню Настройки
            sideMenuDocumentsTemplatesApp4 =
                    $("a[href='/v4/documents-and-templates']"), // боковое меню Настройки
            sideMenuLimitsFinancing =
                    $("[href='#?page=limits&subpage=financing']"), // боковое меню Лимиты По источникам финансирования
            sideMenuLimitsExecutors =
                    $("[href='#?page=limits&subpage=executors']"), // боковое меню Лимиты По исполнителям
            sideMenuLimitsCustomers =
                    $("[href='#?page=limits&subpage=customers']"), // боковое меню Лимиты по ФЗ
            sideMenuReportByKT =
                    $("a[href='#reportCMenu']"), // боковое меню Отчет по КТ
            sideMenuKtReportsStructure =
                    $("[href='#?page=reportCStructures']"), // боковое меню Отчет по КТ Структура отчета
            sideMenuKtReport =
                    $("[href='#?page=reportC&id=current']"), // боковое меню Отчет по КТ Отчет
            sideMenuKtArchive =
                    $("[href='#?page=reportCArchive']"), // боковое меню Отчет по КТ Архив
            sideMenuPiPlans =
                    $("a[href='#?page=plan_PI']"); // боковое меню Планы ПИ

    public ElementsCollection
            projectsOnSideMenu = $$("#programsMenu a"),
            limitsOnSideMenu = $$("#limitsMenu a"),
            reportByKtElemetsOnSideMenu = $$("#reportCMenu a");

    // получение имени пользователя из меню пользователя
    public String getUserName() {
        //String userName = userMenu.getText();
        String userName = headerUserName.getText();
        return userName;
    }

    // нажать кнопку меню пользователя
    public MainPage userMenu() {
        userMenuBtn.click();
        return this;
    }

    // нажать кнопку выхода из аккаунта
    public MainPage userLogout() {
        $(logoutBtn).click();
        return this;
    }

    // сравнение имени пользователя в заголовке с именем залогированной персоны
    public MainPage userNameIsEqualToLoginPerson(String userShortName) {
        $(byXpath("//*[contains(@id,'header-username')]")).shouldHave(text(userShortName));
        return this;
    }

    public MainPage shouldBeSideBarMenuNotifications() {
        sideMenuNotifications.click();
        assertThat(url()).contains("notification");
        return this;
    }

    public MainPage shouldBeSideBarMenuMessages() {
        sideMenuMessages.click();
        assertThat(url()).contains("message");
        return this;
    }

    public MainPage shouldBeSideBarMenuTasks() {
        sideMenuTasks.click();
        assertThat(url()).contains("tasks");
        return this;
    }

    public MainPage shouldBeSideBarMenuReports() {
        sideMenuReports.doubleClick();
        assertThat(url()).contains("reports");
        return this;
    }

    public MainPage fromSideBarMenuToRequestAutomation() {
        sideMenuRequestAutomation.click();
        return this;
    }

    public MainPage fromSideBarMenuToRequestModification() {
        sideMenuRequestModification.click();
        sleep(1000);
        sideMenuRequestModification.click();
        return this;
    }

    public MainPage fromSideBarMenuToPlanPiWorks() {
        sideMenuPlanPiWorks.click();
        return this;
    }

    public MainPage fromSideBarMenuOpenProgectsList() {
        sideMenuProjects.click();
        return this;
    }

    public MainPage onSideBarMenuProgectsListNotEmty() {
        projectsOnSideMenu.shouldHave(sizeGreaterThan(0));
        return this;
    }

    public MainPage fromSideBarMenuOpenLimitsList() {
        sideMenuLimits.click();
        return this;
    }

    public MainPage onSideBarMenuLimitsListNotEmty() {
        limitsOnSideMenu.shouldHave(sizeGreaterThan(0));
        return this;
    }

}
