package net.pladema.emergencycare.domain;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
public class EmergencyCareDocumentHeaderBo {

    public static final String EMERGENCY_CARE_DESCRIPTION = "Guardia";

    private IDocumentHeaderBo baseDocumentHeader;
    @Getter
    private String sourceTypeName = EMERGENCY_CARE_DESCRIPTION;
    @Getter
    private String clinicalSpecialtyName;
    @Getter
    private String institutionName;
    @Getter
    private String professionalName;
    @Getter
    private BedBo bed;

    public Integer getInstitutionId() {
        return baseDocumentHeader.getInstitutionId();
    }

    public Integer getEncounterId() {
        return baseDocumentHeader.getEncounterId();
    }

    public Long getDocumentId() {
        return baseDocumentHeader.getId();
    }

    public Integer getCreatedBy() {
        return baseDocumentHeader.getCreatedBy();
    }

    public LocalDateTime getCreatedOn() {
        return baseDocumentHeader.getCreatedOn();
    }

    public Integer getClinicalSpecialtyId() {
        return baseDocumentHeader.getClinicalSpecialtyId();
    };
}
