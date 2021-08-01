package ar.lamansys.immunization.domain.consultation;

import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ImmunizePatientBo {

    @ToString.Include
    private final Integer patientId;

    @ToString.Include
    private final Integer institutionId;

    @ToString.Include
    private final Integer clinicalSpecialtyId;

    private final List<ImmunizationInfoBo> immunizations;

    public ImmunizePatientBo(Integer patientId, Integer institutionId,
                             Integer clinicalSpecialtyId,
                             List<ImmunizationInfoBo> immunizations) {
        this.patientId = patientId;
        this.institutionId = institutionId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
        this.immunizations = immunizations;
    }
}
