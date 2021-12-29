package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceSummaryProcedureDto implements Serializable {

    private static final long serialVersionUID = 2806473937158573659L;

    private SharedSnomedDto snomed;

}
