package aleks.kur.pages;

import io.restassured.response.Response;
import lombok.val;
import ru.progredis.models.comments.Comment;
import ru.progredis.models.request.RequestRobotsRequest;
import ru.progredis.models.request.RequestRobotsResponse;
import ru.progredis.models.robot.RobotRequest;

import java.util.Calendar;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.UsersPage.*;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class RequestRobotsPage {

    protected RequestAutomationPage requestAutoPage = new RequestAutomationPage();
    protected DocumentsPage documentsPage = new DocumentsPage();
    protected FilesPage filesPage = new FilesPage();

    public static int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    public int
            asuId = 2699,
            asuIdEdited = 2831,
            companyFzId = 540,
            companyFzIdEdited = 2,
            responsiblePersonFzId = 1881,
            responsiblePersonFzIdEdited = 1304;
    public String
            requestsFullName = requestAutoPage.requestsFullName,
            requestsShortName = requestAutoPage.requestsShortName;

    final public static String validRolesHaveAccessToRequestRobotsList =
            fzFromNewRequestRobotsFz_1 + "\n" +
                    respForRequests_1 + "\n" +
                    respGvcForRequestRobots_1 + "\n" +
                    respGvcForRequestRobotsAllocation_1 + "\n" +
                    respPktbForRequestRobots_1 + "\n" +
                    respPktbForRequestRobotsAllocation_1 + "\n" +
                    testAuditor1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    final public static String invalidRolesNotHaveAccessToRequestRobotsList =
            FCI_1 + "\n" +
                    executor_1;

    final public static String validRolesToActWithRequestRobots =
            fzFromNewRequestRobotsFz_1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;

    final public static String validRolesToStartProcessRequestRobots =
            fzFromNewRequestRobotsFz_1 + "\n" +
                    akurochkin_BA + "\n" +
                    akurochkin_SA;
    final public static String invalidRolesToStartProcessRequestRobots =
            respForRequests_1 + "\n" +
                    respGvcForRequestRobots_1 + "\n" +
                    respGvcForRequestRobotsAllocation_1 + "\n" +
                    respPktbForRequestRobots_1 + "\n" +
                    respPktbForRequestRobotsAllocation_1 + "\n" +
                    testAuditor1;

    final public static String validRolesToStopProcessRequestRobots =
            akurochkin_BA + "\n" +
                    akurochkin_SA;

    final public static String invalidRolesToStopProcessRequestRobots =
            fzFromNewRequestRobotsFz_1 + "\n" +
                    respForRequests_1 + "\n" +
                    respGvcForRequestRobots_1 + "\n" +
                    respGvcForRequestRobotsAllocation_1 + "\n" +
                    respPktbForRequestRobots_1 + "\n" +
                    respPktbForRequestRobotsAllocation_1 + "\n" +
                    testAuditor1 + "\n" +
                    executor_1;

    public Comment
            newComment1 = new Comment(
            false,
            "новый комментарий 1",
            true),
            newComment2 = new Comment(
                    false,
                    "новый комментарий 2",
                    true),
            newComment3 = new Comment(
                    false,
                    "новый комментарий 3",
                    true),
            addComment1 = new Comment(
                    "добавление комментария в заявку autoTest 1");

    public Comment editComment(int id, String text) {
        Comment editedComment = new Comment(
                id + 0,
                false,
                "Edited " + text,
                true);
        return editedComment;
    }

    public Integer[] newMeasuresProgramsIds = {2}; // Программа мероприятий по повышению операционной эффективности и оптимизации расходов ОАО «РЖД»
    public Integer[] editedMeasuresProgramsIds = {2, 3}; // Программа мероприятий по повышению операционной эффективности и оптимизации расходов ОАО «РЖД»
    public Comment[] newRequestRobotsComments = {newComment1, newComment2};

    // редакция коммента1, добавление нового коммента
    public Comment[] editedRequestRobotsComments(Comment editedComment) {
        Comment[] editedRequestRobotsComments = {editedComment, newComment3};
        return editedRequestRobotsComments;
    }

    // Заявка роботизация новая
    public RequestRobotsRequest.RequestRobotsRequestBuilder builderRequestRobotsFull = RequestRobotsRequest.builder();
    public RequestRobotsRequest requestRobotsNew = builderRequestRobotsFull
            .name(requestsFullName)
            .shortName(requestsShortName)
            .year(currentYear)
            .asuId(asuId)
            .companyFzId(companyFzId)
            .responsiblePersonFzId(responsiblePersonFzId)
            .measuresProgramsIds(newMeasuresProgramsIds)
            .comments(newRequestRobotsComments)
            .build();

    public RequestRobotsRequest.RequestRobotsRequestBuilder builderRequestRobotsOnlyNames = RequestRobotsRequest.builder();
    public RequestRobotsRequest requestRobotsOnlyNames = builderRequestRobotsOnlyNames
            .name(requestsFullName)
            .shortName(requestsShortName)
            .build();

    public RequestRobotsRequest.RequestRobotsRequestBuilder builderRequestRobotsNoFullName = RequestRobotsRequest.builder();
    public RequestRobotsRequest requestRobotsNoFullName = builderRequestRobotsNoFullName
            .shortName(requestsShortName)
            .build();

    public RequestRobotsRequest.RequestRobotsRequestBuilder builderRequestRobotsNoShortName = RequestRobotsRequest.builder();
    public RequestRobotsRequest requestRobotsNoShortName = builderRequestRobotsNoShortName
            .shortName(requestsShortName)
            .build();

    // Заявка роботизация Редакция
    public RequestRobotsRequest requestRobotsEdited = new RequestRobotsRequest(
            "Edited " + requestsFullName,
            "Edited " + requestsShortName,
            currentYear + 1,
            asuIdEdited + 0,
            companyFzIdEdited + 0,
            responsiblePersonFzIdEdited + 0,
            editedMeasuresProgramsIds,
            newRequestRobotsComments
    );

    public RequestRobotsRequest requestRobotsEdit(Comment editedComment1) {
        RequestRobotsRequest requestRobotsEdited = new RequestRobotsRequest(
                "Edited " + requestsFullName,
                "Edited " + requestsShortName,
                currentYear + 1,
                asuIdEdited + 0,
                companyFzIdEdited + 0,
                responsiblePersonFzIdEdited + 0,
                editedMeasuresProgramsIds,
                editedRequestRobotsComments(editedComment1)
        );
        return requestRobotsEdited;
    }

    // сбросить кэш заявки по id
    public void clearCasheRequestRobotsByIdApi(String authCookie, Integer requestId) {
//        Response clearCasheRequestRobots =
        given()
                .spec(requestSpec)
                .cookie("PLAY_SESSION", authCookie)
                .when()
                .get(BASE_URL + "/app4/robot-request/evictCache/" + requestId);
//        return clearCasheRequestRobots;
    }

    // создать заявку роботизация
    public Response createRequestRobotsApi(String authCookie, RequestRobotsRequest request) {
        Response newRequestAutoResponse =
                given()
                        .spec(requestSpec)
                        .body(request)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/robot-request");
        return newRequestAutoResponse;
    }

    // создать заявку роботизация и получить ее id
    public Integer createRequestRobotsAndGetIdApi(String authCookie, RequestRobotsRequest requestRobotsNew) {
        int newRequestRobotsId =
                createRequestRobotsApi(authCookie, requestRobotsNew)
//                createRequestRobotsApi(authCookie, requestRobotsNew)
                        .then()
                        .log().ifError()
                        .extract().response().body().path("id");
        return newRequestRobotsId;
    }

    // создать и получить заявку по id
    public Response createAndGetRequestRobotsByIdApi(String authCookie, RequestRobotsRequest requestRobotsNew) {
        int newRequestId = createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        Response getRequestByIdResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/" + newRequestId);
        return getRequestByIdResponse;
    }

    // создать и получить заявку с роботом со всеми обязательными атрибутами для процесса
    @FunctionalInterface
    public interface RobotRequestCreator {
        RobotRequest create(int requestId);
    }
    public Response createAndGetRequestRobotsReadyToProcessApi(String authCookie, RequestRobotsRequest requestRobots, RobotRequestCreator creator) {
        // создать заявку
        int requestId = createRequestRobotsAndGetIdApi(authCookie, requestRobots);
        // Создать объект RobotRequest с помощью переданного метода
        RobotRequest robot = creator.create(requestId);
        // добавить заявке Робот с нужными атрибутам
        Response robotResponse = RobotPage.createCustomRobotApi(authCookie, robot);
        int robotId = robotResponse.then().extract().body().path("id");
        // добавить 'Паспорт робота' роботу
        val docTypeId = documentsPage.getDocTypeIdByTypeName(authCookie, "Паспорт робота");
        int docId = documentsPage.createDocumentInRobotAndGetIdApi(authCookie, docTypeId, robotId);
        // вернуть тело готовой заявки
        Response requestRobotsResponse = getRequestRobotsByIdApi(authCookie, requestId);
        return requestRobotsResponse;
    }

    // получить заявку по id
    public Response getRequestRobotsByIdApi(String authCookie, int requestId) {
        Response getRequestByIdResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/" + requestId);
        return getRequestByIdResponse;
    }

    // редактировать заявку по id
    public Response editRequestRobotsByIdApi(String authCookie, int requestId, RequestRobotsRequest requestRobotsEdited) {
        Response editRequestByIdResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(requestRobotsEdited)
                        .when()
                        .patch(BASE_URL + "/app4/robot-request/" + requestId);
        return editRequestByIdResponse;
    }

    // создать и редактировать заявку по id
    public Response createAndPatchFieldsRequestRobotsByIdApi(String authCookie, RequestRobotsRequest requestRobotsNew) {

        step("Получаем id новой заявки и первого комментария", () -> {
        });
        Response newRequest = createRequestRobotsApi(authCookie, requestRobotsNew);
        newRequest
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
        RequestRobotsResponse newRequestRobotsAsClass = newRequest.as(RequestRobotsResponse.class);
        int newRequestId = newRequestRobotsAsClass.getId();
        int newComment1Id = newRequestRobotsAsClass.getComments()[0].getId();
        String newComment1Text = newRequestRobotsAsClass.getComments()[0].getText();

        step("Редактируем заявку и получаем тело ответа отредактированной заявки", () -> {
        });
        Comment editedComment1 = editComment(newComment1Id, newComment1Text);
        Comment[] editedRequestRobotsComments = editedRequestRobotsComments(editedComment1);
        RequestRobotsRequest requestRobotsEdited = requestRobotsEdit(editedComment1);
        Response editedRequestRobots = editRequestRobotsByIdApi(authCookie, newRequestId, requestRobotsEdited);
        editedRequestRobots
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
        return editedRequestRobots;
    }

    // удалить заявку
    public static Response deleteRequestRobotsApi(String authCookie, Integer id) {
        Response deleteRequestRobots =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .delete(BASE_URL + "/app4/robot-request/" + id);
        return deleteRequestRobots;
    }

    // создать и удалить заявку
    public Response postAndDeleteRequestRobotsByIdApi(String authCookie, RequestRobotsRequest requestRobotsNew) {
        int newRequestId = createRequestRobotsAndGetIdApi(authCookie, requestRobotsNew);
        Response deletedRequestRobots = deleteRequestRobotsApi(authCookie, newRequestId);
        return deletedRequestRobots;
    }

    // восстановить заявку
    public Response restoreRequestRobotsApi(String authCookie, Integer id) {
        Response restoredRequestRobots =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
//                        .body(requestRobotsEdited)
                        .when()
                        .post(BASE_URL + "/app4/robot-request/" + id + "/restore");
        return restoredRequestRobots;
    }

    // добавить комментарии к заявке
    public Response addCommentToRequestRobots(String authCookie, Integer RequestRobotsId) {
        Response addCommentToRequestRobots =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(addComment1)
                        .when()
                        .post(BASE_URL + "/app4/robot-request/comment/" + RequestRobotsId);
        return addCommentToRequestRobots;
    }

    // получить список заявок
    public static Response getRequestRobotsListApi(String authCookie, Integer page, Integer size, String sort, Boolean deleted, Integer year, String search) {
        Response getRequestRobotsList =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("sort", sort)
                        .queryParam("deleted", deleted)
                        .queryParam("year", year)
                        .queryParam("search", search)
                        .when()
                        .get(BASE_URL + "/app4/robot-request");
        return getRequestRobotsList;
    }

    // получить список роботов заявки
    public Response getRobotsListOfRequestRobotsById(String authCookie, Integer RequestRobotsId, Integer page, Integer size, Boolean deleted) {
        Response robotsOfRequest =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("deleted", deleted)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/" + RequestRobotsId + "/robot/list");
        return robotsOfRequest;
    }

    // удалить все заявки роботов, содержащие в имени autoTest
    public static void deleteAllTestRequestRobotsByNameApi(String authCookie) {
        List requestIdsToDeleteByName = getRequestRobotsListApi(
                authCookie, null, null, "id,ASC", null, null, null)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .extract().response().path("items.findAll{ it.shortName =~ /^.*autoTest.*$/ }.id");
//        System.out.println(requestIdsToDeleteByName);
        int idsSize = requestIdsToDeleteByName.size();
//        System.out.println(idsSize);
        int i;
        for (i = 0; i < idsSize; i++) {
            System.out.println("id to del " + requestIdsToDeleteByName.get(i));
            deleteRequestRobotsApi(authCookie, (Integer) requestIdsToDeleteByName.get(i));
        }
    }

    // Действия процесса заявок
    // Запуск процесса по id заявки
    public static Response startProcessRequestRobots(String authCookie, Integer requestId) {
        Response startProcessRequestRobots =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/startProcess/" + requestId);
        return startProcessRequestRobots;
    }

    // Остановка процесса по id заявки
    public static Response stopProcessRequestRobots(String authCookie, Integer requestId) {
        Response startProcessRequestRobots =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/startProcess/" + requestId);
        return startProcessRequestRobots;
    }

    // Действие «Скачать заявку»
    public static byte[] exportRequestRobotsByIdAsByteArray(String authCookie, Integer requestId) {
        byte[] export =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/export/" + requestId)
                        .asByteArray();
        return export;
    }
    public static Response exportRequestRobotsByIdAsResp(String authCookie, Integer requestId) {
        Response export =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/export/" + requestId);
        return export;
    }

    // действие «Экспорт в Excel» 1й страницы списка из 2х заявок роботов текущего года
    public static Response exportRequestRobotsListResp(String authCookie) {
        Response export =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .queryParam("sort", "id,DESC")
                        .queryParam("year", currentYear)
                        .when()
                        .get(BASE_URL + "/app4/robot-request/exportGrid/");
        return export;
    }

    // действие «Экспорт в Excel», получение списка отложенных отчетов,
    // запрос файла отчета по id последнего из списка
    public static Response dounloadFileByActionExportRequestRobotsList(String authCookie) {
        exportRequestRobotsListResp(authCookie).then().log().ifValidationFails().statusCode(200);
        val lastReportFile = ReportsPage.dounloadLastDelayedReportFile(authCookie);
        return lastReportFile;
    }

    // получить значения фильтров по полям списка заявок роботов
    public static Response filterValuesRequestRobotsList(String authCookie, String filterName) {
        Response filterValues =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
//                        .queryParam("search", "")
//                        .queryParam("sort", "shortName,ASC")
                        .when()
                        .get(BASE_URL + "/app4/robot-request/filter/" + filterName);
        return filterValues;
    }


    // получить значения фильтров по полям списка роботов в заявке
    public static Response filterValuesRobotsListOfRequest(String authCookie, String columnName, int requestId) {
        Response filterValues =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("columnName", columnName)
//                        .queryParam("search", "")
//                        .queryParam("sort", "shortName,ASC")
                        .when()
                        .get(BASE_URL + "/app4/robot-request/" + requestId + "/robot/list/filter-value");
        return filterValues;
    }




}
