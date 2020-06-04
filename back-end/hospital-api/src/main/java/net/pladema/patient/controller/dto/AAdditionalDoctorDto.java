package net.pladema.patient.controller.dto;

import javax.annotation.Nullable;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.AdditionalDoctorBo;

@Getter
@Setter
@NoArgsConstructor
public class AAdditionalDoctorDto {

    @Nullable
	private Integer id;
	
    @Length(max = 80)
    private String fullName;

    @Length(max = 15)
    private String phoneNumber;

    private boolean generalPractitioner;
    
    public AAdditionalDoctorDto(AdditionalDoctorBo additionalDoctorBo){
    	this.id = additionalDoctorBo.getId();
    	this.fullName = additionalDoctorBo.getFullName();
    	this.phoneNumber = additionalDoctorBo.getPhoneNumber();
    	this.generalPractitioner = additionalDoctorBo.getGeneralPractitioner();
    }
    
}
