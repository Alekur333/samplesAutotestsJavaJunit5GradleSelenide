package aleks.kur.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestStatus {

    public static final String REVIEW_COMPLETED = "reviewCompleted";

    public static final String INCLUDED_IN_PLAN_PI = "includedInPlanPi";

    public static final String RETURNED_IN_CKI = "returnedInCki";

    public static final String SEND_CKI = "sendCki";

    public static final String DRAFT = "draft"; // id=1

    public static final String REJECTED_CKI = "rejectedCki";

    public static final String REJECTED_PKTB = "rejectedPktb";

    public static final String REJECTED_PKTB_CCT = "rejectedPktbCct";

    public static final String INCLUDED_PARTLY = "includedPartly";

    public static final String INCLUDED_IN_PROJECT_PLAN = "includedInProjectPlan";

    public static final String INCLUDED_IN_SECOND_TURN = "includedInSecondTurn";

    public static final String EXCLUDED = "excluded";

    private String code;
    private Long id;

    public RequestStatus() {};

    public RequestStatus(String code, long id) {
        setCode(code);
        setId(id);
    }


}
