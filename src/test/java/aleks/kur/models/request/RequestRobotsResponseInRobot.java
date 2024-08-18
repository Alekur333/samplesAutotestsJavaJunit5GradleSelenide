package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

//@JsonDeserialize(as = Request.class)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRobotsResponseInRobot {

    private Integer id;
    private String name;
    private Integer year;
    private String asuShortName;

//    @JsonProperty("code")
//    private String kindCode;

    public RequestRobotsResponseInRobot() {
    }

    //  Used in Robot
    public RequestRobotsResponseInRobot(int id, String name, int year, String asuShortName) {
        setId(id);
        setName(name);
        setYear(year);
        setAsuShortName(asuShortName);
    }

}
