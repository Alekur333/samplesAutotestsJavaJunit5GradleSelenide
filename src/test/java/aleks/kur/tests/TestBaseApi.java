package aleks.kur.tests;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.with;
import static ru.progredis.filters.CustomLogFilter.customLogFilter;
import static ru.progredis.helpers.BaseUrlSetting.getBaseUrl;
import static ru.progredis.helpers.DriverSettings.BASE_URL;

public class TestBaseApi extends TestBase {
//    public static String BASE_URL = getBaseUrl();

    @BeforeAll
    public static void beforeApiTest() {
        RestAssured.baseURI = BASE_URL;
    }

    public static Map<String, String> requestData = new HashMap<>();

    static Header connection = new Header("Connection", "keep-alive");
    static Header host = new Header("Host", getBaseUrl());
    static Header accept = new Header("Accept", "*/*");
    static Header acceptEncoding = new Header("Accept-Encoding", "gzip, deflate");
    //    static Header contentLength = new Header("Content-Length", "0");
    static Headers headers = new Headers(connection, host, accept, acceptEncoding);

    public static RequestSpecification requestSpec = with()
//            .baseUri("https://")
//            .basePath("/api")
            .contentType(ContentType.JSON)
            .headers(headers)
            .filter(customLogFilter().withCustomTemplates())
            .log().all();

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
//            .expectBody(containsString("success"))
            .build();



}
