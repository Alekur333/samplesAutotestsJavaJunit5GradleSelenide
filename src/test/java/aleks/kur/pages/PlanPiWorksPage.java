package aleks.kur.pages;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

public class PlanPiWorksPage {

    public void shouldBePlanPiWorksPage() {

        $(".work-space-content-header h3").shouldHave(Condition.exactText("Работы планов ПИ"));
    }
}
