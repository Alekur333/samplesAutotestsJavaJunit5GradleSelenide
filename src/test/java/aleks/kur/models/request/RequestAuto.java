package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.progredis.models.general.IdFullShort;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestAuto {

    private Integer id;
    private Integer year;
    private Boolean is2020;
    private String name;
    private String shortName;
//    private String systemObjectCode;
    private RequestKind kind;
    private RequestStatus status;
    private IdFullShort asu;
    private Integer asuId;
    private IdFullShort companyFz;
    private Integer companyFzId;
    private IdFullShort fzResponsiblePerson;
    private Integer responsiblePersonFzId;

//    @JsonProperty("code")
//    private String kindCode;

    public RequestAuto() {};

    // Used for POST RequestAutomation
    public RequestAuto(String name, String shortName, int year, boolean is2020,
                       RequestKind kind, RequestStatus status) {
        setName(name);
        setShortName(shortName);
//        setSystemObjectCode(systemObjectCode);
        setYear(year);
        setIs2020(is2020);
        setKind(kind);
        setStatus(status);
    }

//     Used for PUT RequestAutoAutomation
    public RequestAuto(int id, String name, String shortName, int year, boolean is2020,
                       RequestKind kind, RequestStatus status) {
        setId(id);
        setName(name);
        setShortName(shortName);
//        setSystemObjectCode(systemObjectCode);
        setYear(year);
        setIs2020(is2020);
        setKind(kind);
        setStatus(status);
    }

    // Used for POST newRequestRobots
    public RequestAuto(String name, String shortName, int year, int asuId,
                       int companyFzId, int responsiblePersonFzId) {
        setName(name);
        setShortName(shortName);
        setYear(year);
        setAsuId(asuId);
        setCompanyFzId(companyFzId);
        setResponsiblePersonFzId(responsiblePersonFzId);
    }

}
