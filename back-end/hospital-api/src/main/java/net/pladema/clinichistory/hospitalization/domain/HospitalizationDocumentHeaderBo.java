package net.pladema.clinichistory.hospitalization.domain;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HospitalizationDocumentHeaderBo {

    public static final String HOSPITALIZATION_DESCRIPTION = "Internación";

    private IDocumentHeaderBo baseDocumentHeader;
    private String sourceTypeName = HOSPITALIZATION_DESCRIPTION;
    private String clinicalSpecialtyName;
    private String institutionName;
    private String professionalName;
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
}
