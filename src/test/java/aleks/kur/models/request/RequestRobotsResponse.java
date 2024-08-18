package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.progredis.models.comments.Comment;
import ru.progredis.models.general.IdFullShort;
import ru.progredis.models.measuresPrograms.MeasuresPrograms;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRobotsResponse {

    private Integer id;
    private String shortName;
    private String name;
    private Integer year;
    private RequestKind kind;
    private RequestStatus status;
//    private String asuShortName;
    private IdFullShort asu;
    private IdFullShort companyFz;
    private IdFullShort fzResponsiblePerson;
    private MeasuresPrograms[] measuresPrograms;
    private Comment[] comments;
    private Boolean deleted;

}
