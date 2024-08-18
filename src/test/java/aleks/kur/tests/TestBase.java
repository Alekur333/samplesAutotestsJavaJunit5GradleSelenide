package aleks.kur.tests;

import com.codeborne.selenide.ElementsCollection;
import org.aeonbits.owner.ConfigFactory;
import ru.progredis.config.UsersConfig;
import ru.progredis.pages.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;

public class TestBase {

    protected static AuthUiPage authUiPage = new AuthUiPage();
    protected MainPage mainPage = new MainPage();
    protected static NotificationsPage notificationsPage = new NotificationsPage();
    protected MessagesPage messagesPage = new MessagesPage();
    protected TasksPage tasksPage = new TasksPage();
    protected ReportsPage reportsPage = new ReportsPage();
    protected ReportByKtPage reportByKtPage = new ReportByKtPage();
    protected RequestAutomationPage requestAutoPage = new RequestAutomationPage();
    protected static RequestRobotsPage requestRobotsPage = new RequestRobotsPage();
    protected static RobotPage robotPage = new RobotPage();
    protected RequestModificationPage requestModPage = new RequestModificationPage();
    protected PlanPiWorksPage planPiWorksPage = new PlanPiWorksPage();
    protected PiPlansPage piPlansPage = new PiPlansPage();
    protected ContractsPage contractsPage = new ContractsPage();
    protected DocsCoordinationPage docsCoordinationPage = new DocsCoordinationPage();
    protected DirectoriesPage directoriesPage = new DirectoriesPage();
    protected PcDzoCoordinationRoutePage pcDzoCoordinationRoutePage = new PcDzoCoordinationRoutePage();
    protected static PcDzoPage pcDzoPage = new PcDzoPage();
    protected SettingsPage settingsPage = new SettingsPage();
    protected DocumentsTemlatesPage documentsTemlatesPage = new DocumentsTemlatesPage();
    protected ModalWindowsPage modalWindowsPage = new ModalWindowsPage();
    protected static AuthApiPage authApi = new AuthApiPage();
    protected DocumentTypesPage documentTypesPage = new DocumentTypesPage();
    protected DirectionsPage directionsPage = new DirectionsPage();
    protected AsuPage asuPage = new AsuPage();
    protected DocumentsPage documentsPage = new DocumentsPage();
    protected FilesPage filesPage = new FilesPage();
    public static RemarksPage remarksPage = new RemarksPage();
    public static AgreementCorrectionPage agreementCorrectionPage = new AgreementCorrectionPage();
    public static UsersPage usersPage = new UsersPage();
    public static IntegrationPage integrationPage = new IntegrationPage();
    public static OrganizationPage organizationPage = new OrganizationPage();
//    public static ProcessPage processPage = new ProcessPage();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter dtfDot = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public LocalDateTime now = LocalDateTime.now();
    public static int currentYear = LocalDateTime.now().getYear();
    public LocalDate today = LocalDate.now();
    public String currentDateDdMmYyyy = dtf.format(now);
    public String currentDateDdMmYyyyDots = dtfDot.format(now);
    public String currentDateDdMmYyyyPlus30Days = dtf.format(now.plusDays(30));
    public static long anyYearsAgoFromNowMillisDate(int yearsAgo) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneYearAgo = now.minusYears(yearsAgo);
        // Преобразуем в Instant (мгновение времени)
        Instant instant = oneYearAgo.toInstant();
        System.out.println("instant = " + instant);
        // Получаем Unix-время в миллисекундах
        long someYearsAgoMillis = instant.toEpochMilli();
        return someYearsAgoMillis;
    }
    public LocalDate dayBeforeOfPeriodForProcessImmediateStart = LocalDate.of(today.getYear(), 1, 31);
    public LocalDate dayAfterOfPeriodForProcessImmediateStart = LocalDate.of(today.getYear(), 5, 1);

    public static String attachFile = "some_document.xlsx";
    public static String attachFile1 = "some document1.xlsx";

//    String user = System.getProperty("user");

    public static String setUser() {
        String user = System.getProperty("user");
        System.out.println("user= " + user);
        if (Objects.isNull(user)) {
            user = "saKurochkin";
//            user = "saTechP";
            return user;
        } else {
//            System.setProperty("currentUser", user);
            return user;
        }
    }

    String currentUser = System.setProperty("currentUser", setUser());

    public static UsersConfig usersConfig =
            ConfigFactory.create(UsersConfig.class, System.getProperties());

    public static String login = usersConfig.login();
    public static String passwd = usersConfig.password();
    public static String userMainRole = usersConfig.mainRole();
    public static String userShortName = usersConfig.shortName();
    public static String session = usersConfig.session();


    public static void reloadPageToCatchElement(int elements, ElementsCollection elementsCollection,
                                                int maxTimesReloads, int millisecBetweenReload) {
        System.out.println("elements = " + elements);
        int i, maxPageReloads = maxTimesReloads;
        for (i = 1; i <= maxPageReloads; i++) {
            if (elements < 1) {
                sleep(millisecBetweenReload);
                refresh();
                sleep(2000);
                elements = elementsCollection.size();
                System.out.println("elements = " + elements);
            } else {
                break;
            }
        }
    }

}
