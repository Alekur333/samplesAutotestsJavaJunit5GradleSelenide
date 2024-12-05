package aleks.kur.tests.contract;

import com.codeborne.selenide.CollectionCondition;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.progredis.models.contract.ContractCalendarPlanWorkGet;
import ru.progredis.models.contract.ContractCalendarPlanWorkPost;
import ru.progredis.pages.*;
import ru.progredis.tests.TestBaseUi;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.progredis.pages.ContractsPage.*;
import static ru.progredis.pages.DirectoriesPage.getAgreementSubTypesDirectoryApi;
import static ru.progredis.pages.RequestAutomationPage.faker;

@DisplayName("Проверки вкладки 'Ввод в ОЭ/ПЭ' карточки договора")
@Tags({@Tag("ui"), @Tag("api"), @Tag("contract"), @Tag("regress"), @Tag("putInOePeTab")})
public class TestContractComissioningOePeTabCalendarPlanWorksBlock extends TestBaseUi {
    @BeforeEach
    public void authorization() {
        authApiPage.authorizationApiOnUi();
    }

    static String authCookieSa;
    static int
            contractPpoWithNoWorkId,
            contractPpoWith1WorkId,
            contractPpoWith2WorksId;
    static List<Integer> contractSubtypesId;

    @BeforeAll
    public static void dataPreparation() {
        authCookieSa = AuthApiPage.getAuthCookie(login, passwd);
        contractPpoWithNoWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 0, PlanPiWorksPage.asuForPlanPiWorksId);
        contractPpoWith1WorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        contractPpoWith2WorksId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 2, PlanPiWorksPage.asuForPlanPiWorksId);
        contractSubtypesId = getAgreementSubTypesDirectoryApi(authCookieSa, new HashMap<>()).then().extract().path("id");

    }

    static ContractsPage contractsPage = new ContractsPage();
    static String contractsName = contractsPage.contractsName;

    @Tags({@Tag("ui")})
    @CsvSource(value = {"9,_", "36,_", "строка,не", "'/}[.,',не"})
    @ParameterizedTest(name = "Тест #{index} -> Значение \"{0}\", как {1}валидное, {1}возможно вставить в поле \"№ п/п\" при заполнении календарного плана вкладки 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    void onlyValidDataAcceptedInOrderedNumberFieldOfCalendarPlanWorkOnContractComissioningOePeTabTest(String value, String isValid) {
        step("Создать договор с работой через api", () -> {
        });
        contractsPage.fillRequiredFieldsForStageOfCalendarPlanOnTabCommissioningTableUi(contractPpoWith1WorkId, 6, value);
        step("кнопка Сохранить должна быть " + isValid + "активна", () -> {
            switch (isValid) {
                case "_" -> {
                    contractsPage.orderedNumberCellOnTabCommissioningTable.$("input").shouldHave(
                            attribute("type", "number"),
                            attribute("min", "1"),
                            attribute("max", "99"));
                    contractsPage.saveBtnOnTabCommissioning.shouldBe(enabled);
                }
                case "не" -> {
                    contractsPage.orderedNumberCellOnTabCommissioningTable.$("input").shouldHave(
                            attribute("type", "number"),
                            attribute("min", "1"),
                            attribute("max", "99"));
                    contractsPage.saveBtnOnTabCommissioning.shouldBe(disabled);
                }
            }
        });
    }

    @Tags({@Tag("ui")})
    @CsvSource(value = {"type,text", "maxlength,255", "placeholder,Указывайте название работы из соответствующего пункта таблицы Приложения №2 к договору"})
    @ParameterizedTest(name = "Тест #{index} -> Значение атрибута \"{0}\" равно ожидаемому \"{1}\" у поля \"Наименование работ\" при заполнении календарного плана вкладки 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    void calendarPlanWorkNamesFieldHaveProperAttrsOnContractComissioningOePeTabTest(String attrName, String attrValueExpected) {
        step("Добавить строку этапа в календарном плане тест-договора", () -> {
            contractsPage.openContractsCardCommissioningTab(contractPpoWith1WorkId);
            mainPage.closeSidebarBtn.click();
            switchTo().frame(contractsPage.calendarPlanIframe);
            contractsPage.calendarPlanEditBtn.click();
            contractsPage.addBtnOnTabCommissioning.click();

        });
        step("Проверить, что значение атрибута \"" + attrName + "\" равно ожидаемому \"" + attrValueExpected + "\"", () -> {
            String attrValueActual = contractsPage.nameCellOnTabCommissioningTable.$("input").getAttribute(attrName);
            Assertions.assertEquals(attrValueExpected, attrValueActual);
        });
    }

    @Tags({@Tag("ui")})
    @Test
    @DisplayName("Установлен по умолчанию атрибут “Требуется ОЭ” при заполнении календарного плана вкладки 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    void requiredOeAttributeChekedByDefaultForCalendarPlanWorkOnContractComissioningOePeTabTest() {
        // https://wk.progredis.ru/functionality/user/Contracts/DO3/Requirements/OE-PE-Tab
        step("Добавить строку этапа в календарном плане тест-договора", () -> {
            contractsPage.openContractsCardCommissioningTab(contractPpoWith1WorkId);
            mainPage.closeSidebarBtn.click();
            switchTo().frame(contractsPage.calendarPlanIframe);
            contractsPage.calendarPlanEditBtn.click();
            contractsPage.addBtnOnTabCommissioning.click();
        });
        step("Проверить, что элемент input имеет атрибут cheked для поля 'Требуется ОЭ'", () -> {
            contractsPage.requiredOeCellOnTabCommissioningTable.shouldHave(attribute("checked"));
        });
    }

    @Tags({@Tag("ui")})
    @CsvSource(value = {"Автозаполняется, одна", "Выбирается, несколько"})
    @ParameterizedTest(name = "Тест #{index} -> {0} поле “АСУ”, если у работ договора {1} АСУ, при заполнении календарного плана вкладки 'Ввод в ОЭ/ПЭ' карточки договора ППО")
    void asuSelectionDependsOnAsuNumberForCalendarPlanWorkOnContractComissioningOePeTabTest(String result, String amount) {
        switch (amount) {
            case "одна" -> {
                step("Добавить строку этапа в календарном плане тест-договора", () -> {
                    contractsPage.openContractsCardCommissioningTab(contractPpoWith1WorkId);
                    contractsPage.addStageInCalendarPlanOnContractComissioningOePeTab();
                });
                step("Проверить, что поле 'Асу' заполнено и не радактируется", () -> {
                    contractsPage.asuCellOnTabCommissioningTable.shouldHave(cssClass("Mui-disabled"));
                    contractsPage.asuCellOnTabCommissioningTable.$("div.MuiSelect-select").shouldHave(text("АСУ ПИ"));
                });
            }
            case "несколько" -> {
                step("Добавить строку этапа в календарном плане тест-договора", () -> {
                    contractsPage.openContractsCardCommissioningTab(contractPpoWith2WorksId);
                    contractsPage.addStageInCalendarPlanOnContractComissioningOePeTab();
                });
                step("Проверить, что поле 'Асу' не заполнено и имеет 2 АСУ для выбора", () -> {
                    contractsPage.asuCellOnTabCommissioningTable.click();
                    contractsPage.asuListOnTabCommissioningTable.$$("li").shouldHave(CollectionCondition.size(2));
                });
            }
        }
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени запроса на добавление работ КП договора вкладки Ввод в ОЭ/ПЭ api")
    void postContractCalendarPlanWorkBaseChecksApiTest() {
        step("Создать договор ППО", () -> {
        });
        int contractPpoOneWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Добавить 1й в справочнике подтип договору", () -> {
            ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoOneWorkId, contractSubtypesId.get(0))
                    .then().assertThat().statusCode(200);
            Thread.sleep(1500);
        });
        step("Проверить ответ на запрос", () -> {
            ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractPpoOneWorkId, "1", contractCalendarPlanWorkName, PlanPiWorksPage.asuForPlanPiWorksId[0],
                            currentDateYyyyMmDdHyphen, currentDateYyyyMmDdHyphenPlusMonthes(6), true)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(201)
                    .header("Content-Type", equalTo("application/json"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/contractCalendarPlanWorksPostSchema.json"));
        });
        step("Удалить тестдоговор ", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractPpoOneWorkId);
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Не должны сохраняться работы календарного плана без установленного подтипа у договора для вкладки Ввод в ОЭ/ПЭ api")
    void contractCalendarPlanWorksNotSavedWithoutContractSubtypeApiTest() {
        step("Создать договор ППО", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Добавить работу КП договора, проверить код и сообщение об отсутствии подтипа договора", () -> {
            Response postContractCalendarPlanWorks = ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractId, "1",
                    contractCalendarPlanWorkName, PlanPiWorksPage.asuForPlanPiWorksId[0],
                    currentDateYyyyMmDdHyphen, currentDateYyyyMmDdHyphenPlusMonthes(6), true);
            postContractCalendarPlanWorks.then().log().ifValidationFails().assertThat()
                    .statusCode(500)
                    .body(equalTo("Agreement sub type is required"));
        });
        step("Проверить, что в ответе на запрос работ нет работ КП", () -> {
            Response getContractCalendarPlanWorks = ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractId);
            getContractCalendarPlanWorks.then().log().ifValidationFails().assertThat()
                    .statusCode(200)
                    .body(equalTo("[]"));
        });
        step("Удалить тестдоговор ", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }

    static Stream<Arguments> postContractCalendarPlanWorkFieldsSavingCheckApiTest() throws InterruptedException {
        // Создать договор ППО
        int contractPpoOneWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        // Добавить 1й в справочнике подтип договору"
        ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoOneWorkId, contractSubtypesId.get(0))
                .then().assertThat().statusCode(200);
        Thread.sleep(1500);
        // получить ответ на добавление работ КП договора и собрать данные для проверок
        String
                number = "99",
                name = contractCalendarPlanWorkName,
                currentDate = currentDateYyyyMmDdHyphen,
                currentDatePlus6Monthes = currentDateYyyyMmDdHyphenPlusMonthes(6);
        int asuId = PlanPiWorksPage.asuForPlanPiWorksId[0];
        boolean needOE = true;
        Response calendarPlanPostResponse = ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractPpoOneWorkId, number, name, asuId,
                currentDate, currentDatePlus6Monthes, needOE);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = calendarPlanPostResponse.as(ContractCalendarPlanWorkGet[].class);
        String
                numberAct = calendarPlanPostResponseModel[0].getNumber(),
                nameAct = calendarPlanPostResponseModel[0].getName(),
                startDateAct = calendarPlanPostResponseModel[0].getStartDate(),
                endDateAct = calendarPlanPostResponseModel[0].getEndDate();
        int asuIdAct = calendarPlanPostResponseModel[0].getAsu().getId();
        boolean needOeAct = calendarPlanPostResponseModel[0].getNeedOE();
        return Stream.of(
                Arguments.of("Номер работы", number, numberAct),
                Arguments.of("Наименование работы", name, nameAct),
                Arguments.of("АСУ", asuId, asuIdAct),
                Arguments.of("Дата начала", currentDate, startDateAct),
                Arguments.of("Дата окончания", currentDatePlus6Monthes, endDateAct),
                Arguments.of("Требуется ОЭ", needOE, needOeAct)
        );
    }

    @ParameterizedTest(name = "Тест #{index} -> Проверка сохранения поля \"{0}\" в запросе на добавление работ КП договора вкладки Ввод в ОЭ/ПЭ api")
    @MethodSource
    @Tags({@Tag("api")})
    void postContractCalendarPlanWorkFieldsSavingCheckApiTest(String name, Object expectedValue, Object actualValue) {
        step("Проверить ответ на запрос", () -> {
            Assertions.assertEquals(expectedValue, actualValue, "Значение '" + actualValue + "' поля '" + name + "' в ответе не соответствует ожидаемому - " + expectedValue);
        });
    }

    static Stream<Arguments> contractCalendarPlanWorkSholdHaveRequiredFieldsApiTest() throws InterruptedException {
        // Создать договор ППО
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        // Добавить 1й в справочнике подтип договору"
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0))
                .then().assertThat().statusCode(200);
        Thread.sleep(1500);
        // получить ответ на добавление работ КП договора и собрать данные для проверок
        String
                numberData = "9",
                planWorkNameData = contractCalendarPlanWorkName,
                startDateData = currentDateYyyyMmDdHyphen,
                endDateData = currentDateYyyyMmDdHyphenPlusMonthes(6);
        int asuIdData = PlanPiWorksPage.asuForPlanPiWorksId[0];
        boolean needOEData = true;
        ContractCalendarPlanWorkPost workWithoutNumber = newCalendarPlanWorkWithoutNumber(contractId, planWorkNameData, asuIdData, startDateData, endDateData, needOEData);
        ContractCalendarPlanWorkPost workWithoutName = newCalendarPlanWorkWithoutName(contractId, numberData, asuIdData, startDateData, endDateData, needOEData);
        ContractCalendarPlanWorkPost workWithoutAsu = newCalendarPlanWorkWithoutAsu(contractId, numberData, planWorkNameData, startDateData, endDateData, needOEData);
        ContractCalendarPlanWorkPost workWithoutStartDate = newCalendarPlanWorkWithoutStartDate(contractId, numberData, planWorkNameData, asuIdData, endDateData, needOEData);
        ContractCalendarPlanWorkPost workWithoutEndDate = newCalendarPlanWorkWithoutEndDate(contractId, numberData, planWorkNameData, asuIdData, startDateData, needOEData);
        return Stream.of(
                Arguments.of("Номер работы", workWithoutNumber, "number"),
                Arguments.of("Наименование работы", workWithoutName, "name"),
                Arguments.of("АСУ", workWithoutAsu, "asu_id"),
                Arguments.of("Дата начала", workWithoutStartDate, "start_date"),
                Arguments.of("Дата окончания", workWithoutEndDate, "end_date")
        );
    }

    @ParameterizedTest(name = "Тест #{index} -> Должно быть обязательным поле \"{0}\" в запросе на добавление работ КП договора вкладки Ввод в ОЭ/ПЭ api")
    @MethodSource
    @Tags({@Tag("api")})
    void contractCalendarPlanWorkSholdHaveRequiredFieldsApiTest(String name, ContractCalendarPlanWorkPost work, String failedFieldName) {
        step("Проверить ответ на запрос", () -> {
            ContractCalendarPlanWorkPost[] works = {work};
            Response workWithoutRequiredField = postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
            String responseBody;
            if (failedFieldName.equals("number")) {
                responseBody = "";
            } else {
                responseBody = "constraint [" + failedFieldName + "\" of relation \"agreement_schedule]";
            }
            workWithoutRequiredField.then().assertThat()
                    .statusCode(500)
                    .body(containsString(responseBody));
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени запроса на получение работ КП договора вкладки Ввод в ОЭ/ПЭ api")
    void getContractCalendarPlanWorksBaseChecksApiTest() {
        step("Создать договор ППО", () -> {
        });
        int contractPpoOneWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Добавить 1й в справочнике подтип договору", () -> {
            ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoOneWorkId, contractSubtypesId.get(0))
                    .then().log().ifValidationFails().assertThat().statusCode(200);
            Thread.sleep(1500);
        });
        step("Добавить работу КП договора", () -> {
            ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractPpoOneWorkId, "1", contractCalendarPlanWorkName, PlanPiWorksPage.asuForPlanPiWorksId[0],
                            currentDateYyyyMmDdHyphen, currentDateYyyyMmDdHyphenPlusMonthes(6), true)
                    .then().log().ifValidationFails().assertThat().statusCode(201);
        });
        step("Проверить ответ на запрос", () -> {
            ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractPpoOneWorkId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .header("Content-Type", equalTo("application/json"))
                    .header("cache-control", equalTo("no-cache, no-store, max-age=0, must-revalidate"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/contractCalendarPlanWorksSchema.json"));
        });
        step("Удалить тестдоговор ", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractPpoOneWorkId);
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Работы календарного плана договора в ответе на запрос упорядочиваются по номеру работы для вкладки Ввод в ОЭ/ПЭ api")
    void contractCalendarPlanWorksShouldBeSortedByNumberApiTest() {
        step("Создать договор ППО", () -> {
        });
        int contractPpoOneWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        step("Добавить 1й в справочнике подтип договору", () -> {
            ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoOneWorkId, contractSubtypesId.get(0))
                    .then().log().ifValidationFails().assertThat().statusCode(200);
            Thread.sleep(500);
        });
        List<String> workNumbersList = List.of("35", "18", "27");
        List<String> workNumbersListSorted = workNumbersList.stream().sorted().toList();
        String expectedList = workNumbersListSorted.toString();
        step("Добавить 3 работы КП договора с разными номерами от 1 до 99", () -> {
            for (String number : workNumbersList) {
                String calendarPlanWorkName = "Работа КП договора autotest " + faker.lorem().characters(5);
                ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractPpoOneWorkId, number, calendarPlanWorkName, PlanPiWorksPage.asuForPlanPiWorksId[0],
                                currentDateYyyyMmDdHyphen, currentDateYyyyMmDdHyphenPlusMonthes(6), true)
                        .then().log().ifValidationFails().assertThat().statusCode(201);
            }
        });
        step("Проверить ответ на запрос работ КП, что номера работ сортированы по возрастанию", () -> {
            List<String> numbersActualList = ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractPpoOneWorkId)
                    .then().log().ifValidationFails()
                    .assertThat().statusCode(200).extract().path("number");
            Assertions.assertEquals(expectedList, numbersActualList.toString());
        });
        step("Удалить тестдоговор ", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractPpoOneWorkId);
        });
    }

    static Stream<Arguments> canEditContractCalendarPlanWorkWhenItsNotProcessedApiTest() throws InterruptedException {
        // Создать договор ППО
        int contractPpoOneWorkId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        // Добавить 1й в справочнике подтип договору
        ContractsPage.putContractSubtypeApi(authCookieSa, contractPpoOneWorkId, contractSubtypesId.get(0))
                .then().assertThat().statusCode(200);
        Thread.sleep(1000);
        // Создать работу КП
        ContractsPage.postContractCalendarPlanSingleWorkApi(authCookieSa, contractPpoOneWorkId, "1", contractCalendarPlanWorkName, PlanPiWorksPage.asuForPlanPiWorksId[0],
                        currentDateYyyyMmDdHyphen, currentDateYyyyMmDdHyphenPlusMonthes(6), true)
                .then().log().ifValidationFails().assertThat().statusCode(201);
        // подготовить тестданные
        Response works = ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractPpoOneWorkId);
        ContractCalendarPlanWorkPost[]
                worksEditedNumber = works.as(ContractCalendarPlanWorkPost[].class),
                worksEditedName = works.as(ContractCalendarPlanWorkPost[].class),
                worksEditedAsu = works.as(ContractCalendarPlanWorkPost[].class),
                worksEditedStartDate = works.as(ContractCalendarPlanWorkPost[].class),
                worksEditedEndDate = works.as(ContractCalendarPlanWorkPost[].class),
                worksEditedNeedOE = works.as(ContractCalendarPlanWorkPost[].class);
        worksEditedNumber[0].setNumber("99");
        worksEditedName[0].setName("editedName");
        worksEditedAsu[0].getAsu().setId(PlanPiWorksPage.asuForPlanPiWorksId[1]);
        worksEditedAsu[0].getAsu().setName("Подсистема сводной бухгалтерской отчетности в рамках ЕК АСУФР");
        worksEditedAsu[0].getAsu().setShortName("СБО");
        worksEditedStartDate[0].setStartDate(currentDateYyyyMmDdHyphenPlusOrMinusMonthes(-1));
        worksEditedEndDate[0].setEndDate(currentDateYyyyMmDdHyphenPlusOrMinusMonthes(3));
        worksEditedNeedOE[0].setNeedOE(false);
        return Stream.of(
                Arguments.of("Номер работы", worksEditedNumber, contractPpoOneWorkId),
                Arguments.of("Наименование работы", worksEditedName, contractPpoOneWorkId),
                Arguments.of("АСУ", worksEditedAsu, contractPpoOneWorkId),
                Arguments.of("Дата Начало", worksEditedAsu, contractPpoOneWorkId),
                Arguments.of("Дата Окончание", worksEditedEndDate, contractPpoOneWorkId),
                Arguments.of("Требуется ОЭ", worksEditedNeedOE, contractPpoOneWorkId)
        );
    }

    @ParameterizedTest(name = "Тест #{index} -> Можно редактировать поле \"{0}\" календарного плана договора, пока работы КП не в процессе, на вкладке Ввод в ОЭ/ПЭ api")
    @MethodSource
    @Tags({@Tag("api")})
    void canEditContractCalendarPlanWorkWhenItsNotProcessedApiTest(String attrName, ContractCalendarPlanWorkPost[] editedWorks, int contractId) {
        step("Редактировать работу КП", () -> {
            ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, editedWorks)
                    .then().log().ifValidationFails().assertThat().statusCode(201);
        });
        step("Проверить сохранение редактированного атрибута", () -> {
            Response getEditedWorks = ContractsPage.getContractCalendarPlanWorksApi(authCookieSa, contractId)
                    .then().log().ifValidationFails().assertThat().statusCode(200).extract().response();
            ContractCalendarPlanWorkPost[] getEditedWorksAsPostClass = getEditedWorks.as(ContractCalendarPlanWorkPost[].class);
            Assertions.assertEquals(editedWorks[0], getEditedWorksAsPostClass[0]);
        });
    }

    @Test
    @Tags({@Tag("baseChecksApi"), @Tag("api")})
    @DisplayName("Проверка кода, валидация схемы ответа, времени получения запроса на старт процесса по работе календарного плана договора на вкладке Ввод в ОЭ/ПЭ api")
    void startProcessForContractCalendarPlanWorkBaseChecksApiTest() throws InterruptedException {
        step("Создать новый договор ППО с подтипом через api", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        step("Создать работу КП договора", () -> {
        });
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 1);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = workResp.as(ContractCalendarPlanWorkGet[].class);
        int workId = calendarPlanPostResponseModel[0].getId();
        step("Запустить процесс по работе и проверить ответ на запрос", () -> {
            ProcessPage.startProcessForContractCalendarPlanWork(authCookieSa, workId)
                    .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .header("Content-Type", equalTo("application/json"))
                    .time(lessThan(1500L))
                    .body(matchesJsonSchemaInClasspath("schemas/contractCalendarPlanWorkStartProcessSchema.json"));
        });
        step("Удалить договор", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }

    @Test
    @Tags({@Tag("ui")})
    @DisplayName("Можно удалить работу календарного плана договора, если она не была в процессе, на вкладке Ввод в ОЭ/ПЭ api")
    void canBeDeletedNotProcessedContractCalendarPlanWorkApiTest() throws InterruptedException {
        step("Создать новый договор ППО с подтипом", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        step("Создать 2 работы КП договора", () -> {
            ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 2);
            Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
            workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        });
        step("Открыть карточку договора на вкладке Ввод в ОЭ/ПЭ и удалить по кнопке 2ю работу", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            switchTo().frame(contractsPage.calendarPlanIframe);
            contractsPage.calendarPlanEditBtn.click();
            contractsPage.deleteCalendarPlanWorkBtnOnTabCommissioning.get(1).shouldBe(enabled).click();
            contractsPage.saveBtnOnTabCommissioning.click();
        });
        step("Проверить, что работа удалена и не отображается в списке", () -> {
            contractsPage.contractCalendarPlanWorks.shouldHave(CollectionCondition.size(1), Duration.ofSeconds(7));
        });
        step("Удалить договор", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }

    @Test
    @Tags({@Tag("ui")})
    @DisplayName("Нельзя удалить работу календарного плана договора, если она была в процессе, на вкладке Ввод в ОЭ/ПЭ api")
    void canNotBeDeletedProcessedContractCalendarPlanWorkUiTest() throws InterruptedException {
        step("Создать новый договор ППО с подтипом", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        step("Создать 2 работы КП договора", () -> {
        });
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 2);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = workResp.as(ContractCalendarPlanWorkGet[].class);
        step("Запустить процесс по первой работе", () -> {
            int work1Id = calendarPlanPostResponseModel[0].getId();
            ProcessPage.startProcessForContractCalendarPlanWork(authCookieSa, work1Id)
                    .then().log().ifValidationFails().assertThat().statusCode(200);
        });
        step("Проверить, что кнопка Удалить неактивна и имеет валидную подсказку на первой работе в процессе", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            switchTo().frame(contractsPage.calendarPlanIframe);
            contractsPage.calendarPlanEditBtn.click();
            contractsPage.deleteCalendarPlanWorkBtnOnTabCommissioning.get(0).shouldHave(
                    cssClass("disabled"),
                    attribute("title", "Для удаления этапа необходимо остановить процесс. Обратитесь к бизнес-администратору для остановки процесса."));
        });
        step("Удалить договор", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }

    static Stream<Arguments> distinctAttrsShouldBeUneditableForProcessedContractCalendarPlanWorkUiTest() throws InterruptedException {
        // Создать новый договор ППО с подтипом
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        // Создать работу КП договора и запустить по ней процесс
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 1);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = workResp.as(ContractCalendarPlanWorkGet[].class);
        int workId = calendarPlanPostResponseModel[0].getId();
        ProcessPage.startProcessForContractCalendarPlanWork(authCookieSa, workId)
                .then().log().ifValidationFails().assertThat().statusCode(200);
        // собрать данные для проверок
        authApiPage.authorizationApiOnUiApp4();
        contractsPage.openContractsCardCommissioningTab(contractId);
        switchTo().frame(contractsPage.calendarPlanIframe);
        contractsPage.calendarPlanEditBtn.click();
        String
                orderedNumberCellAttrs = contractsPage.orderedNumberCellOnTabCommissioningTable.getAttribute("class"),
                nameCellAttrs = contractsPage.nameCellOnTabCommissioningTable.getAttribute("class"),
                asuCellAttrs = contractsPage.asuCellOnTabCommissioningTable.getAttribute("class"),
                startDateCellAttrs = contractsPage.startDateCellOnTabCommissioningTable.getAttribute("class"),
                endDateCellAttrs = contractsPage.endDateCellOnTabCommissioningTable.getAttribute("class"),
                requiredOeCellAttrs = contractsPage.requiredOeCellOnTabCommissioningTable.parent().getAttribute("class");
        return Stream.of(
                Arguments.of("enabled", "№ п/п", orderedNumberCellAttrs),
                Arguments.of("enabled", "Наименование работ", nameCellAttrs),
                Arguments.of("disabled", "АСУ", asuCellAttrs),
                Arguments.of("enabled", "Начало", startDateCellAttrs),
                Arguments.of("enabled", "Окончание", endDateCellAttrs),
                Arguments.of("disabled", "Требуется ОЭ", requiredOeCellAttrs)
        );
    }

    @ParameterizedTest(name = "Тест №{index} -> Поле \"{1}\" \"{0}\", когда идет процесс по работе КП договора")
    @MethodSource
    @Tags({@Tag("ui")})
    @DisplayName("Только атрибуты “АСУ” и “Требуется ОЭ” задизейблены, когда идет процесс по работе календарного плана договора, на вкладке Ввод в ОЭ/ПЭ")
    void distinctAttrsShouldBeUneditableForProcessedContractCalendarPlanWorkUiTest(String isEnabled, String name, String elementAttrs) throws InterruptedException {
        step("Проверить, что только поле “АСУ” и “Требуется ОЭ” имеют класс Mui-disabled при редактировании работы КП в процессе", () -> {
            switch (isEnabled) {
                case "enabled":
                    Assertions.assertFalse(elementAttrs.contains("Mui-disabled"), "Элемент " + name + " имеет класс Mui-disabled. Атрибуты элемента: " + elementAttrs);
                    break;
                case "disabled":
                    Assertions.assertTrue(elementAttrs.contains("Mui-disabled"), "Элемент " + name + " не имеет класс Mui-disabled. Атрибуты элемента: " + elementAttrs);
                    break;
            }
        });
    }

    @Test
    @Tags({@Tag("ui")})
    @DisplayName("Должен быть id процесса у работы календарного плана договора, когда она в процессе, на вкладке Ввод в ОЭ/ПЭ ui")
    void shouldBeProcessIdForContractCalendarPlanWorkUiTest() throws InterruptedException {
        step("Создать новый договор ППО с подтипом", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        step("Создать 2 работы КП договора", () -> {
        });
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 2);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = workResp.as(ContractCalendarPlanWorkGet[].class);
        step("Запустить процесс по первой работе", () -> {
            int work1Id = calendarPlanPostResponseModel[0].getId();
            ProcessPage.startProcessForContractCalendarPlanWork(authCookieSa, work1Id)
                    .then().log().ifValidationFails().assertThat().statusCode(200);
        });
        step("Проверить, что отображается id процесса на первой работе и не отображается для работы без процесса", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            switchTo().frame(contractsPage.calendarPlanIframe);
            String processIdProcessedWork = contractsPage.contractCalendarPlanWorkProcessIdOnTabCommissioning.get(0).getText();
            String processIdUnprocessedWork = contractsPage.contractCalendarPlanWorkProcessIdOnTabCommissioning.get(1).getText();
            Assertions.assertTrue(processIdProcessedWork.matches("^\\d+$"), "Нет значения в ячейке 'ID процесса' у работы КП в процессе");
            Assertions.assertTrue(processIdUnprocessedWork.isBlank(), "Есть значение в ячейке 'ID процесса' у работы КП без процесса");
        });
        step("Удалить договор", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }

    @Test
    @Tags({@Tag("ui")})
    @DisplayName("Открывается модальное окно с текущими задачами по клику на id процесса работы календарного плана, на вкладке Ввод в ОЭ/ПЭ договора ППО ui")
    void shouldBeTasksModalOnProcessIdForContractCalendarPlanWorkUiTest() throws InterruptedException {
        step("Создать новый договор ППО с подтипом", () -> {
        });
        int contractId = contractsPage.createContractWithPlanPiWorksAndGetIdApi(authCookieSa, contractsPage.contractsName, 3, 1, 1, PlanPiWorksPage.asuForPlanPiWorksId);
        ContractsPage.putContractSubtypeApi(authCookieSa, contractId, contractSubtypesId.get(0)).then().assertThat().statusCode(200);
        Thread.sleep(1000);
        step("Создать работу КП договора", () -> {
        });
        ContractCalendarPlanWorkPost[] works = ContractsPage.createdSeveralCalendarPlanWorksArr(contractId, 1);
        Response workResp = ContractsPage.postContractCalendarPlanWithPreparedWorksApi(authCookieSa, works);
        workResp.then().log().ifValidationFails().assertThat().statusCode(201);
        ContractCalendarPlanWorkGet[] calendarPlanPostResponseModel = workResp.as(ContractCalendarPlanWorkGet[].class);
        step("Запустить процесс по работе", () -> {
            int workId = calendarPlanPostResponseModel[0].getId();
            ProcessPage.startProcessForContractCalendarPlanWork(authCookieSa, workId)
                    .then().log().ifValidationFails().assertThat().statusCode(200);
        });
        step("Проверить, что есть задачи в модальном окне по id процесса", () -> {
            contractsPage.openContractsCardCommissioningTab(contractId);
            switchTo().frame(contractsPage.calendarPlanIframe);
            contractsPage.contractCalendarPlanWorkProcessIdOnTabCommissioning.get(0).click();
            switchTo().defaultContent();
            switchTo().frame(contractsPage.processTasksModalForCalendarPlanWorkframe);
            contractsPage.processTaskRowsOnModalForCalendarPlanWork.shouldHave(CollectionCondition.sizeGreaterThan(0));
        });
        step("Удалить договор", () -> {
            contractsPage.deleteContractApi(authCookieSa, contractId);
        });
    }


}
