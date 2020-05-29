package net.pladema.patient.controller.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.APersonDto;

@Getter
@Setter
@NoArgsConstructor
public class APatientDto extends APersonDto{

	private Short typeId;
	
    @Length(max = 255)
    private String comments;
    
    private Short identityVerificationStatusId;
    
    @Length(max = 255)
    private String medicalCoverageName;
    
    @Length(max = 150)
    private String medicalCoverageAffiliateNumber;

    private AAdditionalDoctorDto generalPractitioner;

    private AAdditionalDoctorDto pamiDoctor;
}
