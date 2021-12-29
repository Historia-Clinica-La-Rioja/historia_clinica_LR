package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceSummaryNoteDto implements Serializable {

    private static final long serialVersionUID = 2393695582042238076L;

    private Integer id;

    private String description;

}
