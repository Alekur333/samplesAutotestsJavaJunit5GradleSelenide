package aleks.kur.models.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdFullShort {

    private Integer id;
    private String name;
    private String shortName;

//    public IdFullShort() {};

//    public IdFullShort(long id, String name, String shortName) {
//        setId(id);
//        setName(name);
//        setShortName(shortName);
//    }


}
