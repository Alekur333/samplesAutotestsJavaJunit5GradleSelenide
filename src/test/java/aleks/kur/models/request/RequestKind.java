package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestKind {

    //Автоматизация
    public static String PPO = "PPO";
    // Заявки на автоматизацию, идущие по процессу 2019.04
    public static String PPOBPM = "PPOBPM"; // id=7
    //СПО
    public static String LPO = "LPO";
    //СПО (с 2021)
    public static String LPO2021 = "LPO2021";
    //Сопровождение
    public static String OTHER = "OTHER";
    //Инф. услуги до 2019
    public static String INFO = "INFO";
    //Инф. услуги
    public static String INFO_NEW = "INFO_NEW";
    //Оборудование
    public static String SOME = "SOME";
    //Оборудование (с 2021)
    public static String SOME2021 = "SOME2021";
    //Роботизация
    public static String ROUTINE_AUTOMATIZATION = "RoutineAutomatization";

//    private Request kind;
//    @Column(name = "code")
    private String code;
    private Long id;
    private String name;
    private String shortName;

//    name=Автоматизация бизнес-процессов, shortName=Автоматизация

    public RequestKind() {};

    public RequestKind(String code, long id, String name, String shortName) {
        setCode(code);
        setId(id);
        setName(name);
        setShortName(shortName);
    }


}
