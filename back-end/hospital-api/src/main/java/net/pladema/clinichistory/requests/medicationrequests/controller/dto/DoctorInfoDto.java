package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.staff.controller.dto.ProfessionalDto;

@Getter
@Setter
@NoArgsConstructor
public class DoctorInfoDto {

    private Integer id;

    private String firstName;

    private String lastName;

	private String nameSelfDetermination;

    public static DoctorInfoDto from(ProfessionalDto professionalDto) {
        if (professionalDto == null)
            return null;
        DoctorInfoDto result = new DoctorInfoDto();
        result.setId(professionalDto.getId());
        result.setFirstName(professionalDto.getFirstName());
        result.setLastName(professionalDto.getLastName());
		result.setNameSelfDetermination(professionalDto.getNameSelfDetermination());
        return result;
    }
}
