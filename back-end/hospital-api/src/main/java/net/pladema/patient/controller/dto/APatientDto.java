package net.pladema.patient.controller.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.APersonDto;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class APatientDto extends APersonDto{

	private Short typeId;
	
    @Length(max = 255)
    private String comments;
    
    private Short identityVerificationStatusId;

    private AAdditionalDoctorDto generalPractitioner;

    private AAdditionalDoctorDto pamiDoctor;

    private Short auditTypeId;

	@Nullable
	private String message;
}
