package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;

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
}
