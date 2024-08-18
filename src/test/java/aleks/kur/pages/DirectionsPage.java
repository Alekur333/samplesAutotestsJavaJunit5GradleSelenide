package aleks.kur.pages;

import com.codeborne.selenide.*;
import com.codeborne.selenide.ElementsCollection;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.tests.TestBaseApi.requestSpec;
//import static ru.progredis.tests.TestBaseApi.requestData;

public class DirectionsPage {

    public Map<String, String> requestData = new HashMap<>();

    // Справочник Направления

    public SelenideElement
            directionsPageHeader = $(".work-space-content-body h3"),  // заголовок страницы
            shortNameColomn = $x("//th/span[text()= 'Краткое наименование']").parent(),  // заголовок колонка Краткое наименование
            fullNameColomn = $x("//th/span[text()= 'Полное наименование']").parent(),  // заголовок колонка Полное наименование
            descriptionColomn = $x("//th/span[text()= 'Описание']").parent(),  // заголовок колонка Описание

    addDirectionBtn = $(".link-add"),  // кнопка Добавить
            editDirectionBtn = $(".link-edit"),  // кнопка Редактировать
            deleteDirectionBtn = $(".link-delete"),  // кнопка Удалить
            exportCsvDirectionsBtn = $(".link-export-zip"),  // кнопка Экспорт CSV
            exportXlsDirectionsBtn = $(".link-export"),  // кнопка Экспорт XSL
            importDirectionsBtn = $(".link-import-zip"),  // кнопка Импорт

    searchFieldOnDirectionsPage = $("input[placeholder='Поиск']"),  // поле поиска

    // карточка Направление
    directionShortNameField = $("#shortName"),
            directionFullNameField = $("#fullName"),
            directionDescriptionField = $("#description"),
            firstAsuInDirection = $x("//h6[text()='АСУ']").parent().$("div>div>div"), // первый АСУ в направлении

    saveDirectionBtn = $("input.btn-success").parent(),  // кнопка Сохранить
            cancelDirectionBtn = $(".btn-default").parent(),  // кнопка Отмена
            deleteDirectionOnModalBtn = $(".modal-footer .btn-success"),  // кнопка Удалить в модалке подтверждения
            addAsuInDirectionBtn = $(".link-add"),  // кнопка добавить АСУ

    // Модалка выбора АСУ
    searchFieldInAsuModal = $(".modal-body input[placeholder='Поиск']"),  // поле поиска
            firstAsuForDirectionCheck = $(".modal-body td"),  // чек в первой строке АСУ
            chooseAsuForDirectionBtn = $(".modal-footer .btn-success")  // кнопка Выбрать на модалке
                    ;

    public ElementsCollection directionsTableColomns = $$("tr th"); // коллекция колонок таблицы
    public ElementsCollection directionsTableDataStrings = $$("tbody tr"); // коллекция строк с данными таблицы

    public String directionsPageLink = "#?page=reference&subpage=directions"; // страница Направления

    public String directionShortName = "directionShortNameAutoTest_" + faker.lorem().characters(3);
    public String directionFullName = "directionFullNameAutoTest_" + faker.lorem().characters(3);
    public String directionDescription = "directionDescriptionAutoTest_" + faker.lorem().characters(3);
    public String directionShortNameUpdated = "UpdatedDirectionShortNameAutoTest_" + faker.lorem().characters(3);
    public String directionFullNameUpdated = "UpdatedDirectionFullNameAutoTest_" + faker.lorem().characters(3);
    public String directionDescriptionUpdated = "UpdatedDirectionDescriptionAutoTest_" + faker.lorem().characters(3);
    public String directionShortNameMax = "directionShortName__" + faker.lorem().characters(235);
    public String directionFullNameMax = "directionFullName___" + faker.lorem().characters(235);
    public String directionDescriptionMax = "test_Description____" + faker.lorem().characters(980);



    public int createDirectionAndGetIdApi(String authCookie, String fullName, String shortName, String description) {
        requestData.put("fullName", fullName);
        requestData.put("shortName",shortName);
        requestData.put("description",description);
        requestData.put("approvalRoutes",null); // array of id
        requestData.put("reviewOrganizerId","0"); // int
        requestData.put("asuIdList",null); // array
        int directionId =
                given()
                        .spec(requestSpec)
                        .body(requestData)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/directions")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract().response().body().path("id");
        return directionId;
    }

    public void getDirectionByIdApi(String authCookie, int directionId) {
        given()
                .spec(requestSpec)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/app4/directions/" + directionId)
                .then()
                .log().all()
                .statusCode(200);
    }

    public void deleteDirectionApi(String authCookie, int directionId) {
        given()
                .spec(requestSpec)
                .body("[]")
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .post(BASE_URL + "/app4/directions/delete/" + directionId)
                .then()
                .log().all()
                .statusCode(204);
    }

    public int getDirectionsAmountOnUi() {
        int directionsAmount = directionsTableDataStrings.size();
        System.out.println("directionsAmount = " + directionsAmount);
        return directionsAmount;
    }

}
