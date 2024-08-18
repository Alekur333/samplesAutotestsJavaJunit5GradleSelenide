package aleks.kur.tests.contract;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки вкладки 'Ввод в ОЭ/ПЭ' карточки договора")
@Tags({@Tag("ui"), @Tag("contract"), @Tag("regress"), @Tag("putInOePeTab")})
public class TestContractPutInOePeTab extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApi.authorizationApiOnUi();
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);
    String contractsName = contractsPage.contractsName;

    @Test
    @DisplayName("В карточке договора ППО есть вкладка 'Ввод в ОЭ/ПЭ' ")
    void contractPpoShouldHavePutInOePeTab() {
        // Вкладка “Ввод в ОЭ/ПЭ” есть только у договоров вида ППО типов “Договор”, “Ордер-заказ” и “Заказ-наряд”.
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Создать новый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractPpoDraftAndGetIdApi(authCookie, contractsName);
        step("Открыть карточку тестового договора", () -> {
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить наличие вкладки 'Ввод в ОЭ/ПЭ'", () -> {
            contractsPage.putInOePeTab.shouldBe(visible).shouldHave(exactText("Ввод в ОЭ/ПЭ"));
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("В карточке договора СПО нет вкладки 'Ввод в ОЭ/ПЭ' ")
    void contractSpoShouldNotHavePutInOePeTab() {
        // Вкладка “Ввод в ОЭ/ПЭ” есть только у договоров вида ППО типов “Договор”, “Ордер-заказ” и “Заказ-наряд”.
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Создать новый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName);
        step("Открыть карточку тестового договора", () -> {
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить наличие вкладки 'Ввод в ОЭ/ПЭ'", () -> {
            contractsPage.putInOePeTab.shouldNot(exist);
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("Есть кнопка 'Собрать предложения в комиссию по приемке ПО' для СА в меню 'Действия' карточки договора")
    void shouldBeFormSoftwareAcceptanceCommitteeBtnForSaOnActionsMenu() {
        step("Создать новый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName);
        step("Открыть карточку тестового договора от СА", () -> {
            sleep(2000);
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить наличие кнопки 'Собрать предложения в комиссию по приемке ПО' ", () -> {
            contractsPage.actionsBtn.click();
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.should(exist);
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

    @Test
    @DisplayName("Не активна кнопка 'Собрать предложения в комиссию по приемке ПО', если в договоре нет процедуры приемки, для СА в меню 'Действия' карточки договора")
    void shouldBeDesabledFormSoftwareAcceptanceCommitteeBtnForSaOnActionsMenuWhenNoAcceptanceProcedure() {
        step("Создать новый договор через api", () -> {
        });
        int contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName);
        step("Открыть карточку тестового договора от СА", () -> {
            contractsPage.openContractsCardMainInfoTab(contractsId);
        });
        step("Проверить не активность кнопки 'Собрать предложения в комиссию по приемке ПО' ", () -> {
            contractsPage.actionsBtn.click();
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.parent().shouldHave(attribute("disabled"));
        });
        step("У кнопки должа быть подсказка 'Для формирования комиссии по приемке требуется загрузка ТЗ, ЧТЗ или ПР'", () -> {
            contractsPage.FormSoftwareAcceptanceCommitteeBtn.parent().parent().
                    shouldHave(attribute("title", "Для формирования комиссии по приемке требуется загрузка ТЗ, ЧТЗ или ПР"));
        });
        step("Удалить тестовый договор черз api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }

}
