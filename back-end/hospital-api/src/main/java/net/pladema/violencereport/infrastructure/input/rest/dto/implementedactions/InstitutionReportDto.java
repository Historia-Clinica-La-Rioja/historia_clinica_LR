package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EInstitutionReportPlace;
import net.pladema.violencereport.domain.enums.EInstitutionReportReason;

import javax.validation.constraints.NotNull;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class InstitutionReportDto {

	@NotNull(message = "{value.mandatory}")
	private Boolean reportWasDoneByInstitution;

	private List<EInstitutionReportReason> reportReasons;

	private List<EInstitutionReportPlace> institutionReportPlaces;

	private String otherInstitutionReportPlace;

}
