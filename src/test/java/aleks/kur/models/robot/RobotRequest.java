package aleks.kur.models.robot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.progredis.models.comments.Comment;
import ru.progredis.models.par.ParRobotRequest;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RobotRequest {

//    private Integer id;

    private Integer requestId;
    private String name;
    private String description;
    private Integer asuNumberId; // Справочник Количество ИС, используемых в работе
    private Integer filesCountId; // Справочник Количество файлов, обрабатываемых роботом
    private Integer actionsCountId; // Справочник Число предполагаемых элементарных действий
    private Integer textRecognitionId; // Справочник Необходимость распознавания текста в картинке
    private Integer applicationDescriptionId; // Справочник Описание клиентских приложений
    private Integer adaptationTypeId; // Справочник Вид адаптации
    private Integer freedUpWorkManHours; // Эффекты - Высвобождаемые трудозатраты в год, ч/ч. Формат: "кол часов" + 2 цифры после запятой на конце без знаков
    private Integer releasedCosts; // Эффекты - Высвобождаемые трудозатраты в год, тыс.руб. Формат: "кол часов" + 3 цифры после запятой на конце без знаков
    private Integer discountedIncome; // Эффекты - Дисконтированный доход, тыс.руб. Формат: "кол часов" + 3 цифры после запятой на конце без знаков
    private Integer paybackPeriod; // Эффекты - Дисконтированный доход, тыс.руб. Формат: "кол часов" + 3 цифры после запятой на конце без знаков
    private Integer supportFormatId; // Справочник Формат сопровождения
    private String qualitativePerformance; // Качественные показатели эффективности
    private Comment[] comments;
    private ParRobotRequest par;


}
