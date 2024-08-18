package aleks.kur.models.par;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParRobotRequest {

    private Integer approver; // id Утверждающее лицо
    private String characteristicsOfAutomationObjects; // Характеристика объектов автоматизации
    private String conclusionOnSoftwareRobot; // Заключение на программного робота
    private String interactionOfRobot; // Взаимодействие программного робота со смежными системами
    private String listDocuments; // Перечень документов, в которых описаны действия в аварийных ситуациях, направленные на...
    private String recommendationInformationProtection; // Рекомендации по формированию требований к защите информации
    private String recommendationApplicationSoftware; // Рекомендации по формированию требований к прикладному ПО, используемому программным роботом

}
