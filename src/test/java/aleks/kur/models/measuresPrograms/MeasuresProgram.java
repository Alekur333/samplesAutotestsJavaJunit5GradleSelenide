package aleks.kur.models.measuresPrograms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasuresProgram {

    private String code;
    private Integer id;
    private String name;
    private Integer number;

    public MeasuresProgram() {};

//    public MeasuresProgram(long id, String name, String shortName) {
//        setId(id);
//        setName(name);
//        setShortName(shortName);
//    }


}
