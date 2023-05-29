package net.pladema.patient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;

import lombok.ToString;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.APersonDto;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class APatientDto extends APersonDto{

	private Short typeId;
	
    @Length(max = 255)
    private String comments;
    
    private Short identityVerificationStatusId;

    private AAdditionalDoctorDto generalPractitioner;

    private AAdditionalDoctorDto pamiDoctor;

    private EAuditType auditType;

	@Nullable
	private String message;
}
