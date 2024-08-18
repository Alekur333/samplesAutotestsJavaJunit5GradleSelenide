package aleks.kur.pages;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.*;

public class MessagesPage {

    public void shouldBeMessagesPage() {
        $(".b-message h3").shouldHave(Condition.exactText("Сообщения")); // v2
//        $("[data-testid='@app4/header/Typography']").shouldHave(Condition.exactText("Сообщения")); // v4
    }
}
