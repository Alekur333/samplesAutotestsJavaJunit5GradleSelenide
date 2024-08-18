package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.progredis.models.comments.Comment;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRobotsRequest {

    //    private Integer id;
    private String shortName;
    private String name;
    private Integer year;
    private Integer asuId;
    private Integer companyFzId;
    private Integer responsiblePersonFzId;
    private Integer[] measuresProgramsIds;
    private Comment[] comments;

}
