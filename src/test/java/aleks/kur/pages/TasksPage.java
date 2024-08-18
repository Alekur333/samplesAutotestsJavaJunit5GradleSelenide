package aleks.kur.pages;

import com.codeborne.selenide.Condition;
import io.restassured.response.Response;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static ru.progredis.helpers.DriverSettings.BASE_URL;
import static ru.progredis.tests.TestBaseApi.requestSpec;

public class TasksPage {
    public void shouldBeTasksPage() {
        $(".work-space-content-header h3").shouldHave(Condition.exactText("Задачи"));
    }

    // список задач Чужие-Новые
    public Response getNotMyTasksListApi(String authCookie, int limit, int offset) {
        Response tasksList =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/tasks?completed=false&my=false&deleted=false&sort=id-&limit="+limit + "&offset=" + offset);
        return tasksList;
    }

    // задача по id
    public Response getPersonTaskByIdApi(String authCookie, int taskId) {
        Response taskCard =
                given()
                        .spec(requestSpec)
                        .cookie("PLAY_SESSION", authCookie)
                        .when()
                        .get(BASE_URL + "/tasks/" + taskId);
        return taskCard;
    }


}
