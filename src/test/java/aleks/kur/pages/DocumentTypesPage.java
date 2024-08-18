package aleks.kur.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static ru.progredis.filters.CustomLogFilter.customLogFilter;
import static ru.progredis.tests.TestBase.*;

public class DocumentTypesPage {
    // Справочник Типы документов

    public String electronicApprovalAndSigningAttrName = "Электронное согласование и подписание документа";


    public SelenideElement
            // Таблица типов
            searchField = $("input[placeholder='Поиск']"), // поле для ввода поиска
            firstDocTypeOnTable = $("tbody tr"), // первая строка таблицы
            agreementRequiredOfDocTypeOnTable = $x("//tr/td[5]"), // значение в колонке Требуется согласование

    editDocTypeBtn = $(".link-edit"), // кнопка Редактировать

    typeRasString = $(byText("Руководство администратора системы")).parent(), // строка Руководство администратора системы в таблице

    // Карточка типа
    agreementRequiredInDocTypeCheckBox = $("[for='agreementRequiredCheckBox']"), // атрибут Требуется согласование

    saveDocTypeBtn = $(".footer-wrap .btn-success")

     ;



    // Api методы

    // Получить значение атрибута "Электронное согласование и подписание документа"
    public boolean getAgreementRequiredAttrOfDocType(String authCookie) {
        boolean agreementRequiredAttrOfDocType =
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .log().all()
                        .contentType("application/json")
//                        .body(TestBaseApi.requestData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get("/documenttypes?code=RAS")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .response().body().path("items[0].agreementRequired");
//        System.out.println(agreementRequiredAttrOfDocType);
        return agreementRequiredAttrOfDocType;
    }

}
