package aleks.kur.models.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.progredis.models.general.IdFullShort;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractCalendarPlanWorkPost {
    private Integer id;
    private Integer agreementId;
    private IdFullShort asu;
    private String number;
    private String name;
    private String startDate;
    private String endDate;
    private Boolean needOE;
    private Boolean deleted;

}
