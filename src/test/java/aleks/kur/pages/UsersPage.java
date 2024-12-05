package aleks.kur.pages;

import io.restassured.response.Response;
import ru.progredis.helpers.UserService;
import ru.progredis.models.user.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class UsersPage {

    static final String USERS_FILE_PATH = "src/test/resources/dataJsons/users.json";
    static UserService userService = new UserService(USERS_FILE_PATH);
    public static User
            akurochkinSA = userService.findUserByLogin("akurochkin_SA@progredis.ru"),
            akurochkinBA = userService.findUserByLogin("akurochkin_BA@progredis.ru"),
            executor1_Progredis = userService.findUserByLogin("executor_1@progredis.ru"),
            executorProjectManager1_Progredis = userService.findUserByLogin("executorProjectManager1@progredis.ru"),
            contractManager1 = userService.findUserByLogin("contractManager1@progredis.ru");


    // запрос пользователя по логину api
    public Response getUserByLoginApi(String authCookie, String userLogin) {
        Response user =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .queryParam("q", userLogin)
                        .get(BASE_URL + "/persons");
        return user;
    }

    // запрос пользователей по коду роли api
    public Response getUsersByRoleCodeApi(String authCookie, String roleCode) {
        Response user =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .queryParam("roleCode", roleCode)
                        .get(BASE_URL + "/persons");
        return user;
    }

    // "Определяем id 1го юзера по коду роли"
    public int getIdFirstPersonInRole(String authCookie, String roleCode) {
        return getUsersByRoleCodeApi(authCookie, roleCode)
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().body().path("id[0]");
    }


    // "Определяем id юзера по логину"
    public int getIdInRoleByLogin(String authCookie, String userLogin) {
        int personId = getUserByLoginApi(authCookie, userLogin)
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().body().path("id[0]");
        return personId;
    }


}
