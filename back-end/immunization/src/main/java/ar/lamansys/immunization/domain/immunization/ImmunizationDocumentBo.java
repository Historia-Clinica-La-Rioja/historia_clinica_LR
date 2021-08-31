package ar.lamansys.immunization.domain.immunization;

import lombok.Getter;

import java.util.List;

@Getter
public class ImmunizationDocumentBo {

    private final Long id;

    private final Integer patientId;

    private final Integer encounterId;

    private final Integer institutionId;

    private final Integer doctorId;

    private final Integer clinicalSpecialtyId;

    private final List<ImmunizationInfoBo> immunizations;

    public ImmunizationDocumentBo(Long id, Integer patientId,
                                  Integer encounterId,
                                  Integer institutionId,
                                  Integer doctorId,
                                  Integer clinicalSpecialtyId,
                                  List<ImmunizationInfoBo> immunizations) {
        this.id = id;
        this.patientId = patientId;
        this.encounterId = encounterId;
        this.institutionId = institutionId;
        this.doctorId = doctorId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
        this.immunizations = immunizations;
    }
}
