package net.pladema.electronicjointsignature.professionalsstatus.infrastructure.input.dto;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentElectronicSignatureProfessionalStatusDto {

	private String professionalCompleteName;

	private EElectronicSignatureStatus status;

	private DateDto date;

}
