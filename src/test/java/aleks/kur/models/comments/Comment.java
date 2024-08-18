package aleks.kur.models.comments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    private Boolean deleted;
    private Integer id;
    private String text;
    private Boolean updated;

    public Comment() {
    }

    ;

    // for creation of new robots request
    public Comment(boolean deleted, String text, boolean updated) {
        setDeleted(deleted);
        setText(text);
        setUpdated(updated);
    }

    // for edit
    public Comment(int id, boolean deleted, String text, boolean updated) {
        setId(id);
        setDeleted(deleted);
        setText(text);
        setUpdated(updated);
    }

    // for edding to request
    public Comment(String text) {
        setText(text);
    }


}
