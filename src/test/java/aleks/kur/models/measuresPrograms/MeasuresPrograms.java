package aleks.kur.models.measuresPrograms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasuresPrograms {

    private Long id;
    private MeasuresProgram measuresProgram;

    public MeasuresPrograms() {};

//    public MeasuresPrograms(long id, String name, String shortName) {
//        setId(id);
//        setName(name);
//        setShortName(shortName);
//    }


}
