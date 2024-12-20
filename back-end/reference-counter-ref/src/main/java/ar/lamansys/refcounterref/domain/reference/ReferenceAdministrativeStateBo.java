package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ReferenceAdministrativeStateBo {

	private Integer id;
	private Integer referenceId;
	private EReferenceAdministrativeState state;
	private String reason;
	private String professionalName;
	private LocalDateTime createdOn;

}
