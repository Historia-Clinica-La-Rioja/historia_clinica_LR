package ar.lamansys.immunization.domain.consultation;

import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import lombok.Getter;

import java.util.List;

@Getter
public class ImmunizePatientBo {

    private final Integer patientId;

    private final Integer institutionId;

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
