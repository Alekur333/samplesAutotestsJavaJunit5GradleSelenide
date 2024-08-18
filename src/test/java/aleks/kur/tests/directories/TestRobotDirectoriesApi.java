package aleks.kur.tests.directories;

import org.junit.jupiter.api.*;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.RequestRobotsPage.deleteAllTestRequestRobotsByNameApi;

@Tags({@Tag("directories"), @Tag("robot"), @Tag("api")})
@DisplayName("Проверки справочников Роботы api")
public class TestRobotDirectoriesApi extends TestBaseApi {

    @AfterAll
    static void finish() {
        String authCookie = AuthApiPage.getAuthCookie(login, passwd);
        deleteAllTestRequestRobotsByNameApi(authCookie);
    }

    String authCookie = AuthApiPage.getAuthCookie(login, passwd);

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Статусы роботов' api")
    void getRobotStatusesDirectoryBaseChecksApiTest() {
        directoriesPage.getRobotStatusesDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotStatusesDirectoryScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Базовая стоимость робота' api")
    void getRobotBasicCostDirectoryBaseChecksApiTest(){
        directoriesPage.getRobotBasicCostDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCostsDirectoriesScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Стоимость работы по разработке управляющего модуля' api")
    void getWorkRobotsControlModuleCostDirectoryBaseChecksApiTest(){
        directoriesPage.getWorkRobotsControlModuleCostDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCostsDirectoriesScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Количество ИС, используемых в работе»' api")
    void getUsedInWorkAsuNumberRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getUsedInWorkAsuNumberCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Количество файлов, обрабатываемых роботом»' api")
    void getFilesNumberProcessedByRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getFilesNumberProcessedByRobotCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Число предполагаемых элементарных действий»' api")
    void getElementaryActionsNumberRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getElementaryActionsNumberRobotCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Необходимость распознавания текста в картинке»' api")
    void getTextRecognitionRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getTextRecognitionRobotCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Описание клиентских приложений»' api")
    void getClientsAppsDescriptionRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getClientsAppsDescriptionRobotCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Коэффициент «Вид адаптации»' api")
    void getAdaptationTypeRobotCoefficientDirectoryBaseChecksApiTest(){
        directoriesPage.getAdaptationTypeRobotCoefficientDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotCoefficientsScheme.json"));
    }

    @Test
//    @Disabled
    @Tags({@Tag("baseChecksApi")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени ответа справочника робота 'Форматы сопровождения' api")
    void getSupportFormatsDirectoryBaseChecksApiTest(){
        directoriesPage.getSupportFormatsDirectoryApi(authCookie)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", is("application/json"))
                .time(lessThan(1500L))
                .body(matchesJsonSchemaInClasspath("schemas/robotSupportFormatsScheme.json"));
    }


}
