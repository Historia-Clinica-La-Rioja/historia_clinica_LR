package net.pladema.staff.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;

@Getter
@Setter
@NoArgsConstructor
public class ProfessionalRegistrationNumberVo {

    private Integer healthcareProfessionalId;

    private BasicPersonalDataVo basicPersonalDataVo;

    private ProfessionalLicenseNumber professionalLicenseNumber;

    public ProfessionalRegistrationNumberVo(Integer healthcareProfessionalId, String firstName, String lastName, String nameSelfDetermination, ProfessionalLicenseNumber professionalLicenseNumber) {
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.basicPersonalDataVo = new BasicPersonalDataVo(firstName, lastName, null, nameSelfDetermination);
        this.professionalLicenseNumber = professionalLicenseNumber;
    }
}
