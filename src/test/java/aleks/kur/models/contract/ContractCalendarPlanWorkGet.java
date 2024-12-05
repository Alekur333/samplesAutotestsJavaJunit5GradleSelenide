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
public class ContractCalendarPlanWorkGet {
    private Integer id;
    private Integer agreementId;
    private IdFullShort asu;
    private String number;
    private String name;
    private String startDate;
    private String endDate;
    private Object documentKit;
    private Integer processId;
    private Boolean movingProcess;
    private Boolean deleted;
    private Object managerSpk;
    private String esppNumber;
    private Boolean warn;
    private Boolean needOE;
    private Object ptkProvided;
    private Object telegramUploaded;
    private Object[] processes;
    private Object buttonsConfig;

}
