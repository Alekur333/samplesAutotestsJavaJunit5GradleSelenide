package aleks.kur.pages;

import io.restassured.response.Response;
import lombok.val;
import ru.progredis.models.comments.Comment;
import ru.progredis.models.par.ParRobotRequest;
import ru.progredis.models.robot.RobotRequest;
import ru.progredis.models.robot.RobotResponse;

import java.util.Calendar;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.pages.RequestAutomationPage.faker;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class RobotPage {

    protected RequestRobotsPage requestRobotsPage = new RequestRobotsPage();

    public int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public int robotId;
    ParRobotRequest newPar;

    public String
            nameRobot = "Имя робота autoTest " + faker.lorem().characters(5),
            descriptionRobot = "Описание требуемой функциональности робота autoTest",
            qualitativePerformanceRobot = "Качественные показатели эффективности робота autoTest";

    public Comment
            newComment1 = new Comment(
            false,
            "новый комментарий 1",
            true),
            newComment1edited = new Comment(
                    false,
                    "Edited новый комментарий 1",
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
                    "добавление комментария с единственным полем text autoTest 1");

    public Comment editComment(int id, String text) {
        Comment editedComment = new Comment(
                id + 0,
                false,
                "Edited " + text,
                true);
        return editedComment;
    }

    public Comment[] newRobotComments = {newComment1, newComment2};
    public Comment[] editedRobotComments = {newComment1edited, newComment2, newComment3};

    // редакция коммента1, добавление нового коммента
    public Comment[] editedRobotComments(Comment editedComment, Comment Comment2, Comment newComment) {
        Comment[] editedRequestRobotsComments = {editedComment, Comment2, newComment};
        return editedRequestRobotsComments;
    }

    public RobotRequest robotNew = new RobotRequest(
            robotId + 0,
            nameRobot + "",
            descriptionRobot + "",
            1,
            1,
            1,
            2,
            2,
            5,
            950,
            9369,
            9963,
            9,
            2,
            qualitativePerformanceRobot + "",
            newRobotComments,
            newPar
    );

    // Робот Редакция
//    public RobotRequest robotEditedRequest(Integer robotId, Comment[] editedRobotComments) {
    public RobotRequest robotEditedFields = new RobotRequest(
            robotId + 0,
            "Edited " + nameRobot,
            "Edited " + descriptionRobot,
            2,
            2,
            4,
            3,
            1,
            1,
            350,
            3369,
            3963,
            18,
            3,
            "Edited " + qualitativePerformanceRobot,
            editedRobotComments,
            newPar
    );

    // Робот Редакция полей
    public RobotRequest robotPatchedAllFieldsRequest(Comment[] editedRobotComments) {
        RobotRequest robotEdited = new RobotRequest();
        robotEdited.setName(robotEditedFields.getName());
        robotEdited.setDescription(robotEditedFields.getDescription());
        robotEdited.setAsuNumberId(robotEditedFields.getAsuNumberId());
        robotEdited.setFilesCountId(robotEditedFields.getFilesCountId());
        robotEdited.setActionsCountId(robotEditedFields.getActionsCountId());
        robotEdited.setTextRecognitionId(robotEditedFields.getTextRecognitionId());
        robotEdited.setApplicationDescriptionId(robotEditedFields.getApplicationDescriptionId());
        robotEdited.setAdaptationTypeId(robotEditedFields.getAdaptationTypeId());
        robotEdited.setFreedUpWorkManHours(robotEditedFields.getFreedUpWorkManHours());
        robotEdited.setReleasedCosts(robotEditedFields.getReleasedCosts());
        robotEdited.setDiscountedIncome(robotEditedFields.getDiscountedIncome());
        robotEdited.setPaybackPeriod(robotEditedFields.getPaybackPeriod());
        robotEdited.setSupportFormatId(robotEditedFields.getSupportFormatId());
        robotEdited.setQualitativePerformance(robotEditedFields.getQualitativePerformance());
        robotEdited.setComments(editedRobotComments);
        return robotEdited;
    }

    public RobotRequest robotAllRequiredAttrs(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .qualitativePerformance(robotNew.getQualitativePerformance())
            .build();
    return robot;
    }

    public RobotRequest robotNoName(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
//            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoDescription(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
//            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoAsuNumber(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
//            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoFilesCount(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
//            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoActionsCount(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
//            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoTextRecognition(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
//            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoApplicationDescription(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
//            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoAdaptationType(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
//            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoFreedUpWorkManHours(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
//            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoReleasedCosts(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
//            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoDiscountedIncome(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
//            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoPaybackPeriod(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
//            .paybackPeriod(robotNew.getPaybackPeriod())
            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    public RobotRequest robotNoSupportFormat(Integer requestId) {
     RobotRequest.RobotRequestBuilder builderRobot = RobotRequest.builder();
     RobotRequest robot = builderRobot
            .requestId(requestId)
            .name(robotNew.getName())
            .description(robotNew.getDescription())
            .asuNumberId(robotNew.getAsuNumberId())
            .filesCountId(robotNew.getFilesCountId())
            .actionsCountId(robotNew.getActionsCountId())
            .textRecognitionId(robotNew.getTextRecognitionId())
            .applicationDescriptionId(robotNew.getApplicationDescriptionId())
            .adaptationTypeId(robotNew.getAdaptationTypeId())
            .freedUpWorkManHours(robotNew.getFreedUpWorkManHours())
            .releasedCosts(robotNew.getReleasedCosts())
            .discountedIncome(robotNew.getDiscountedIncome())
            .paybackPeriod(robotNew.getPaybackPeriod())
//            .supportFormatId(robotNew.getSupportFormatId())
            .build();
    return robot;
    }

    // новый ПАР Робота
    public ParRobotRequest newRobotPar() {
        ParRobotRequest newRobotPar = new ParRobotRequest();
        newRobotPar.setApprover(1755); // Козырев А.Б.
        newRobotPar.setCharacteristicsOfAutomationObjects("autoTest Характеристика объектов автоматизации");
        newRobotPar.setConclusionOnSoftwareRobot("autoTest Заключение на программного робота");
        newRobotPar.setInteractionOfRobot("autoTest Взаимодействие программного робота со смежными системами");
        newRobotPar.setListDocuments("autoTest Перечень документов, в которых описаны действия в аварийных ситуациях, направленные на...");
        newRobotPar.setRecommendationInformationProtection("autoTest Рекомендации по формированию требований к защите информации");
        newRobotPar.setRecommendationApplicationSoftware("autoTest Рекомендации по формированию требований к прикладному ПО...");
        return newRobotPar;
    }

    public RobotRequest robotPatchedByParRequest() {
        RobotRequest robotPatchedByParRequest = new RobotRequest();
        robotPatchedByParRequest.setName(robotNew.getName());
        robotPatchedByParRequest.setDescription(robotNew.getDescription());
        robotPatchedByParRequest.setAsuNumberId(robotNew.getAsuNumberId());
        robotPatchedByParRequest.setFilesCountId(robotNew.getFilesCountId());
        robotPatchedByParRequest.setActionsCountId(robotNew.getActionsCountId());
        robotPatchedByParRequest.setTextRecognitionId(robotNew.getTextRecognitionId());
        robotPatchedByParRequest.setApplicationDescriptionId(robotNew.getApplicationDescriptionId());
        robotPatchedByParRequest.setAdaptationTypeId(robotNew.getAdaptationTypeId());
        robotPatchedByParRequest.setFreedUpWorkManHours(robotNew.getFreedUpWorkManHours());
        robotPatchedByParRequest.setReleasedCosts(robotNew.getReleasedCosts());
        robotPatchedByParRequest.setDiscountedIncome(robotNew.getDiscountedIncome());
        robotPatchedByParRequest.setPaybackPeriod(robotNew.getPaybackPeriod());
        robotPatchedByParRequest.setSupportFormatId(robotNew.getSupportFormatId());
        robotPatchedByParRequest.setQualitativePerformance(robotNew.getQualitativePerformance());
        robotPatchedByParRequest.setComments(newRobotComments);
        robotPatchedByParRequest.setPar(newRobotPar());
        return robotPatchedByParRequest;
    }

//    public parRobotRequest editedRobotPar() {
//        newRobotPar().setApprover(1);
//    }

    // создать робота
    public Response createRobotApi(String authCookie, int requestRobotsId) {
        RobotRequest robotNewWithRequestId = new RobotRequest(
                requestRobotsId,
                robotNew.getName(),
                robotNew.getDescription(),
                robotNew.getAsuNumberId(),
                robotNew.getFilesCountId(),
                robotNew.getActionsCountId(),
                robotNew.getTextRecognitionId(),
                robotNew.getApplicationDescriptionId(),
                robotNew.getAdaptationTypeId(),
                robotNew.getFreedUpWorkManHours(),
                robotNew.getReleasedCosts(),
                robotNew.getDiscountedIncome(),
                robotNew.getPaybackPeriod(),
                robotNew.getSupportFormatId(),
                robotNew.getQualitativePerformance(),
                robotNew.getComments(),
                robotNew.getPar()
        );
        Response newRobotResponse =
                given()
                        .spec(requestSpec)
                        .body(robotNewWithRequestId)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/robot");
        return newRobotResponse;
    }


    // создать робота с произвольными полями
    public static Response createCustomRobotApi(String authCookie, RobotRequest robot) {
        Response customRobot =
                given()
                        .spec(requestSpec)
                        .body(robot)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/robot");
        return customRobot;
    }

    // создать в новой заявке робота и получить его id
    public Integer createRobotAndGetIdApi(String authCookie) {
        int requestId = requestRobotsPage.createRequestRobotsAndGetIdApi(authCookie, requestRobotsPage.requestRobotsNew);
        int newRobotId =
                createRobotApi(authCookie, requestId)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().body().path("id");
        requestRobotsPage.clearCasheRequestRobotsByIdApi(authCookie, requestId);
        return newRobotId;
    }

    // получить робота по id
    public Response getRobotByIdApi(String authCookie, int robotId) {
        Response getRobotByIdResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot/" + robotId);
        return getRobotByIdResponse;
    }

    // создать и получить робота по id
    public Response createAndGetRobotApi(String authCookie) {
        int newRobotId = createRobotAndGetIdApi(authCookie);
        Response getRobotResponse = getRobotByIdApi(authCookie, newRobotId);
        return getRobotResponse;
    }

    // редактировать поля Robot по id
    public Response patchRobotApi(String authCookie, int robotId, RobotRequest robotEdited) {
        Response patchedRobotResponse =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body(robotEdited)
                        .when()
                        .patch(BASE_URL + "/app4/robot/" + robotId);
        return patchedRobotResponse;
    }

    // создать и редактировать все поля Робота, кроме ПАР и документов
    public Response createAndPatchFieldsRobotByIdApi(String authCookie) {
        step("Создаем Робота, получаем его как класс, сохраняем его id", () -> {
        });
        Response getNewRobot = createAndGetRobotApi(authCookie);
        RobotResponse newRobotAsClass = getNewRobot.as(RobotResponse.class);
        int newRobotId = newRobotAsClass.getId();
        step("Редактируем 1 коммент Робота и добавляем новый коммент", () -> {
        });
        int comment1Id = newRobotAsClass.getComments()[0].getId();
        String comment1Text = newRobotAsClass.getComments()[0].getText();
        Comment editedComment1 = editComment(comment1Id, comment1Text);
        Comment comment2FromRobot = newRobotAsClass.getComments()[1];
        Comment[] editedRobotComments = editedRobotComments(editedComment1, comment2FromRobot, newComment3);
        step("Редактируем Робота и получаем тело ответа отредактированной заявки", () -> {
        });
        RobotRequest robotEdited = robotPatchedAllFieldsRequest(editedRobotComments);
        Response editedRobot = patchRobotApi(authCookie, newRobotId, robotEdited);
        return editedRobot;
    }

    // создать и заполнить все поля ПАР Робота
    public Response createAndFillRobotParFieldsApi(String authCookie) {
        step("Создаем Робота, получаем его как класс, сохраняем его id", () -> {
        });
        Response getNewRobot = createAndGetRobotApi(authCookie);
        RobotResponse newRobotAsClass = getNewRobot.as(RobotResponse.class);
        int newRobotId = newRobotAsClass.getId();
        step("Добавляем ПАР со всеми заполненными полями в Роботе", () -> {
        });
        Response robotWithPar = patchRobotApi(authCookie, newRobotId, robotPatchedByParRequest());
        return robotWithPar;
    }

    // получить ПАР Робота
    public Response robotParFileApi(String authCookie) {
        step("Создаем Робота, получаем его как класс, сохраняем его id", () -> {
        });
        Response getNewRobot = createAndGetRobotApi(authCookie);
        RobotResponse newRobotAsClass = getNewRobot.as(RobotResponse.class);
        int newRobotId = newRobotAsClass.getId();
        step("Добавляем ПАР со всеми заполненными полями в Роботе", () -> {
        });
        Response robotWithPar = patchRobotApi(authCookie, newRobotId, robotPatchedByParRequest());
        step("Получаем файл ПАР ", () -> {
        });
        val robotParFile = exportRobotParReportApi(authCookie, newRobotId);
        return robotParFile;
    }

    public static Response deleteRobotApi(String authCookie, Integer id) {
        Response deleteRobot =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .delete(BASE_URL + "/app4/robot/" + id);
        return deleteRobot;
    }

    public Response postAndDeleteRobotApi(String authCookie) {
        int newRobotId = createRobotAndGetIdApi(authCookie);
        Response deletedRequestRobots = deleteRobotApi(authCookie, newRobotId);
        return deletedRequestRobots;
    }

    // восстановление робота
    public Response restoreRobotApi(String authCookie, Integer id) {
        Response restoredRobot =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
//                        .body(requestRobotsEdited)
                        .when()
                        .post(BASE_URL + "/app4/robot/" + id + "/restore");
        return restoredRobot;
    }


    // получить список Роботов
    public static Response getRobotsListApi(String authCookie, Integer page, Integer size, String sort, Boolean deleted, Integer year, String search) {
        Response getRobotsList =
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
                        .get(BASE_URL + "/app4/robot/all");
        return getRobotsList;
    }


    Response includeRobotInNewPlanPiWorkApi(String authCookie, Integer robotId) {
        Response robotIncludedInNewPlanPiWork =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .post(BASE_URL + "/app4/robot/" + robotId + "/work");
        return robotIncludedInNewPlanPiWork;
    }

    Response rejectRobotApi(String authCookie, Integer robotId) {
        Response rejectRobot =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .body("{\"text\": \"reject\"}")
                        .when()
                        .post(BASE_URL + "/app4/robot/" + robotId + "/reject");
        return rejectRobot;
    }

    public static Response filterValuesByColumnNameOfRobotsListApi(String authCookie, String columnName) {
        Response rejectRobot =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .queryParam("columnName", columnName)
                        .when()
                        .get(BASE_URL + "/app4/robot/all/filter-value");
        return rejectRobot;
    }

    public Response exportRobotParReportApi(String authCookie, int robotId) {
        Response robotParReport =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/app4/robot/"+ robotId + "/exportParReport");
        return robotParReport;
    }



}
