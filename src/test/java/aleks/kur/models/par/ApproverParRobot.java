package aleks.kur.models.par;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproverParRobot {

    private Integer id; // id Утверждающее лицо
    private String shortName;
    private String departmentName;
    private String position;
}
