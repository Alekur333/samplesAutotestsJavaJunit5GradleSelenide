package aleks.kur.pages;

import com.codeborne.selenide.Condition;
import ru.progredis.tests.TestBase;

import static com.codeborne.selenide.Selenide.*;

public class NotificationsPage extends TestBase {

    // Тестовые данные

    // Шаблоны уведомлений
    static int templateForEventStartProcessId = 24; // Процесс запущен - Успешно
    static int templateForEventStartProcessErrorId = 25; // Процесс запущен - Ошибка
    static int templateForEventStopProcessId = 83; // Процесс остановлен - Успешно
    static int templateForEventDocumentApprovalId = 175; // Процесс согласования документа запущен - Успешно
    public static int[] notogicationsToSwitchEvent =
            {
                    templateForEventStartProcessId,
                    templateForEventStartProcessErrorId,
                    templateForEventStopProcessId,
                    templateForEventDocumentApprovalId
            };

    public void shouldBeNotificationsPage() {
        $(".work-space-content-header h3").shouldHave(Condition.exactText("Раздел уведомлений"));
    }

    public void switchOffNotificationsActivity(String authCookie) {
        int i;
        int lenghtOfNotifList = notogicationsToSwitchEvent.length;
        for (i = 0; i < lenghtOfNotifList; i++) {
            String notificationsId = String.valueOf(notogicationsToSwitchEvent[i]);
            System.out.println(notificationsId);
            settingsPage.switchNotificationsActivity(authCookie, notificationsId, "false", "false");
        }
    }

    public void switchOnNotificationsActivity(String authCookie) {
        int i, lenghtOfNotifList;
        lenghtOfNotifList = notogicationsToSwitchEvent.length;
        for (i = 0; i < lenghtOfNotifList; i++) {
            String notificationsId = String.valueOf(notogicationsToSwitchEvent[i]);
            System.out.println(notificationsId);
            settingsPage.switchNotificationsActivity(authCookie, notificationsId, "true", "true");
        }
    }

}
