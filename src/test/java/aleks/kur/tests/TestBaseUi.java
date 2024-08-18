package aleks.kur.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ru.progredis.helpers.AllureAttachments;
import ru.progredis.helpers.DriverSettings;
import ru.progredis.pages.*;

import java.net.MalformedURLException;

import static com.codeborne.selenide.Selenide.*;


public class TestBaseUi extends TestBase {

    @BeforeAll
    public static void beforeTest() throws MalformedURLException {


        WebDriverManager.chromedriver().setup();
        DriverSettings.configure();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    public void tearDownAttaches() {

        AllureAttachments.addScreenshotAs("Last screenshot");
        AllureAttachments.addPageSource();
        AllureAttachments.addBrowserConsoleLogs();
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @AfterAll
    static void finish() {
        Selenide.closeWebDriver();
    }

}
