package aleks.kur.tests.directories;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.Allure.step;

@Tags({@Tag("ui"), @Tag("documentType")})
@DisplayName("Проверки справочника 'Типы документов' через Ui")
public class TestDocumentTypes extends TestBaseUi {



    String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @BeforeEach
    public void authorization() {
        authApi.authorizationOnUibyApiPure(authCookie);
    }


    // Тесты карточки типа документа

    @Test
//    @RepeatedIfExceptionsTest(repeats = 1, exceptions = IOException.class)
//    @Retry
    @Tags({@Tag("smoke")})
    @DisplayName( "Должен быть атрибут 'Электронное согласование и подписание документа' в карточке типа документа")
    void shouldBeAgreementRequiredAttrInDocTypeCardTest() {
        step("Перейти на страницу справочника Типы документов", () -> {
            open("/#?page=reference&subpage=doctypes");
        });
        step("Редактировать первую строку типа", () -> {
            documentTypesPage.firstDocTypeOnTable.click();
            documentTypesPage.editDocTypeBtn.click();
        });
        step("Проверить наличие атрибута " + documentTypesPage.electronicApprovalAndSigningAttrName, () -> {
            documentTypesPage.agreementRequiredInDocTypeCheckBox.scrollIntoView(true).shouldHave(exactText(documentTypesPage.electronicApprovalAndSigningAttrName));
        });
    }

    @Test
//    @Disabled
    @Tags({@Tag("smoke")})
    @DisplayName("Редактирование атрибута 'Электронное согласование и подписание документа' сохраненяется для типа документа РА")
    void agreementRequiredKeepsForDocTypeTest() {
        step("Перейти на страницу справочника Типы документов", () -> {
            open("/#?page=reference&subpage=doctypes");
        });
        step("В строке поиска набрать 'Руководство администратора системы'", () -> {
            documentTypesPage.searchField.setValue("Руководство администратора системы");
        });
        step("Редактировать найденный тип", () -> {
            documentTypesPage.typeRasString.click();
            documentTypesPage.editDocTypeBtn.click();
            sleep(1500);
        });
        step("Если установлен атрибут " + documentTypesPage.electronicApprovalAndSigningAttrName + ", то", () -> {
            if (documentTypesPage.getAgreementRequiredAttrOfDocType(authCookie)) {
                step("Cнять его и сохранить", () -> {
                    documentTypesPage.agreementRequiredInDocTypeCheckBox.shouldBe(visible, Duration.ofSeconds(3)).scrollIntoView(true);
                    documentTypesPage.agreementRequiredInDocTypeCheckBox.hover().click();
                    documentTypesPage.saveDocTypeBtn.click();
                    sleep(1000);
                });
                step("Проверить в таблице типов, что в колонке 'Требуется согласование' значение 'Нет' для тестового типа", () -> {
                    documentTypesPage.agreementRequiredOfDocTypeOnTable.shouldHave(exactText("Нет"));
                });
                step("Редактировать тестовый тип.\n" +
                        "Проверить, что атрибут не установлен.\n" +
                        "Активировать атрибут " + documentTypesPage.electronicApprovalAndSigningAttrName + " и сохранить", () -> {
                    documentTypesPage.typeRasString.click();
                    documentTypesPage.editDocTypeBtn.click();
                    assertFalse(documentTypesPage.getAgreementRequiredAttrOfDocType(authCookie));
                    documentTypesPage.agreementRequiredInDocTypeCheckBox.shouldBe(visible, Duration.ofSeconds(3)).scrollIntoView(true);
                    documentTypesPage.agreementRequiredInDocTypeCheckBox.hover().click();
                    sleep(1000);
                    documentTypesPage.saveDocTypeBtn.click();
                });
                step("Проверить в таблице типов, что в колонке 'Требуется согласование' значение 'Да' для тестового типа", () -> {
                    documentTypesPage.agreementRequiredOfDocTypeOnTable.shouldHave(exactText("Да"));
                });
                step("Редактировать тип со снятым атрибутом.\n" +
                        "Проверить, что атрибут установлен.", () -> {
                    documentTypesPage.typeRasString.click();
                    documentTypesPage.editDocTypeBtn.click();
                    assertTrue(documentTypesPage.getAgreementRequiredAttrOfDocType(authCookie));
                });
            } else {
                step("Если не установлен атрибут " + documentTypesPage.electronicApprovalAndSigningAttrName + ", то", () -> {
                    step("активировать его и сохранить", () -> {
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.shouldBe(visible, Duration.ofSeconds(3)).scrollIntoView(true);
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.hover().click();
                        documentTypesPage.saveDocTypeBtn.click();
                        sleep(1000);
                    });
                    step("Проверить в таблице типов, что в колонке 'Требуется согласование' значение 'Да' для тестового типа", () -> {
                        documentTypesPage.agreementRequiredOfDocTypeOnTable.shouldHave(exactText("Да"));
                    });
                    step("Редактировать тестовый тип.\n" +
                            "Проверить, что атрибут установлен.\n" +
                            "Снять атрибут " + documentTypesPage.electronicApprovalAndSigningAttrName + " и сохранить", () -> {
                        documentTypesPage.typeRasString.click();
                        documentTypesPage.editDocTypeBtn.click();
                        assertTrue(documentTypesPage.getAgreementRequiredAttrOfDocType(authCookie));
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.shouldBe(visible, Duration.ofSeconds(3)).scrollIntoView(true);
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.hover().click();
                        sleep(1000);
                        documentTypesPage.saveDocTypeBtn.click();
                    });
                    step("Проверить в таблице типов, что в колонке 'Требуется согласование' значение 'Нет' для тестового типа", () -> {
                        documentTypesPage.agreementRequiredOfDocTypeOnTable.shouldHave(exactText("Нет"));
                    });
                    step("Редактировать тестовый тип.\n" +
                            "Активировать атрибут " + documentTypesPage.electronicApprovalAndSigningAttrName + " и сохранить", () -> {
                        documentTypesPage.typeRasString.click();
                        documentTypesPage.editDocTypeBtn.click();
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.shouldBe(visible, Duration.ofSeconds(3)).scrollIntoView(true);
                        documentTypesPage.agreementRequiredInDocTypeCheckBox.hover().click();
                        sleep(1000);
                        documentTypesPage.saveDocTypeBtn.click();
                    });
                });
            }
        });
    }




}
