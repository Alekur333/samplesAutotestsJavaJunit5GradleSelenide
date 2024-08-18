package aleks.kur.models.robot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@JsonDeserialize(as = Request.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RobotAttribute {

    private Integer id;
    private String name;
    private Boolean deleted;
    private Boolean blocked;
    private Integer coefficient;
/*
        "id": 1,
        "deleted": false,
        "blocked": false,
        "name": "0-2",
        "coefficient": 100
*/
/*

    public RobotAttribute() {
    }

    // Used for Robots effects
    public RobotAttribute(Integer id, String name, Boolean deleted, Boolean blocked, Integer coefficient) {
        setId(id);
        setName(name);
        setDeleted(deleted);
        setBlocked(blocked);
        setCoefficient(coefficient);
     }

    // Used for Robots supportFormat
    public RobotAttribute(Integer id, String name, Boolean deleted, Boolean blocked) {
        setId(id);
        setName(name);
        setDeleted(deleted);
        setBlocked(blocked);
     }
*/

}
