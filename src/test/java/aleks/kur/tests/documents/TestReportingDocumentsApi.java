package aleks.kur.tests.documents;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import ru.progredis.pages.AuthApiPage;
import ru.progredis.tests.TestBaseApi;

import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверки документов группы Отчетные Api")
@Tags({@Tag("api"), @Tag("document"), @Tag("regress"), @Tag("reportingDocuments")})
public class TestReportingDocumentsApi extends TestBaseApi {

    String
            authCookie = AuthApiPage.getAuthCookie(login, passwd),
            contractsName = contractsPage.contractsName,
            docName = documentsPage.docName,
            decimalNumber = documentsPage.decimalNumber;

    int
            docTypeId = 45, // Техническое задание на систему (ТЗ)
            contractsId = contractsPage.createContractSpoDraftAndGetIdApi(authCookie, contractsName), // Создать тестовый договор через api
            fileId = filesPage.uploadFileAndGetIdApi(authCookie, documentsPage.testUploadFile), // загрузить файл и получить id
            docId = documentsPage.createDocumentInContractAndGetIdApi(authCookie, docName, decimalNumber, docTypeId, contractsId, fileId); // Добавить документ ТЗ в тестовый договор через api

    @Test
//    @Disabled
    @DisplayName("Атрибут документа movingProcess:true после старта процесса согласования api")
    void docGetsAttrMovingProcessTrueAfterProcessStartsApiTest() {
        step("Создать документ и получить его id", () -> {
            step("Запустить процесс согласования документа по id", () -> {
                documentsPage.startProcessOfReportingDocumentApi(authCookie, docId);
            });
        });
        step("Проверить, что атрибут документа movingProcess:true", () -> {
            assertThat(documentsPage.getAttrOfDocumentMovingProcessApi(authCookie, docId)).isEqualTo(true);
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }


    @Test
//    @Disabled
    @DisplayName("Атрибут документа movingProcess:false после прекращения процесса согласования api")
    void docGetsAttrMovingProcessFalseAfterProcessStopsApiTest() {
        step("Создать документ и получить его id", () -> {
            step("Запустить процесс согласования документа по id", () -> {
                documentsPage.startProcessOfReportingDocumentApi(authCookie, docId);
            });
        });
        step("Остановить процесс согласования", () -> {
            documentsPage.stopProcessOfReportingDocumentApi(authCookie, docId);
        });
        step("Проверить, что атрибут документа movingProcess:false", () -> {
            assertThat(documentsPage.getAttrOfDocumentMovingProcessApi(authCookie, docId)).isEqualTo(false);
        });
        step("Удалить тестовый договор через api", () -> {
            contractsPage.deleteContractApi(authCookie, contractsId);
        });
    }


}
