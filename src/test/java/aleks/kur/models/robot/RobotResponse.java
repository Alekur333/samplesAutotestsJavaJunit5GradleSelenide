package aleks.kur.models.robot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.progredis.models.comments.Comment;
import ru.progredis.models.par.ParRobotResponse;
import ru.progredis.models.request.RequestRobotsResponseInRobot;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RobotResponse {

    private Integer id;
    private RequestRobotsResponseInRobot request;
    private String name;
    private String description;
    private RobotAttribute asuNumber; // Справочник Количество ИС, используемых в работе
    private RobotAttribute filesCount; // Справочник Количество файлов, обрабатываемых роботом
    private RobotAttribute actionsCount; // Справочник Число предполагаемых элементарных действий
    private RobotAttribute textRecognition; // Справочник Необходимость распознавания текста в картинке
    private RobotAttribute applicationDescription; // Справочник Описание клиентских приложений
    private RobotAttribute adaptationType; // Справочник Вид адаптации
    private Double freedUpWorkManHours; // Эффекты - Высвобождаемые трудозатраты в год, ч/ч. Формат: "кол часов" + 2 цифры после запятой на конце без знаков
    private Long releasedCosts; // Эффекты - Высвобождаемые трудозатраты в год, тыс.руб. Формат: "кол часов" + 3 цифры после запятой на конце без знаков
    private Long discountedIncome; // Эффекты - Дисконтированный доход, тыс.руб. Формат: "кол часов" + 3 цифры после запятой на конце без знаков
    private Float paybackPeriod; // Эффекты - Срок окупаемости, лет
    private RobotAttribute supportFormat; // Справочник Формат сопровождения
    private String qualitativePerformance; // Качественные показатели эффективности
    private Comment[] comments;
    private ParRobotResponse par;
    private Boolean deleted;


}
