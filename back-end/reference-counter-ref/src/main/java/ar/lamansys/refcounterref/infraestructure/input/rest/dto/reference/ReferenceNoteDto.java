package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceNoteDto implements Serializable {

    private static final long serialVersionUID = 2393695582042238076L;

    private Integer id;

    private String description;

    public ReferenceNoteDto(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

}
