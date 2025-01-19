package ar.lamansys.sgh.clinichistory.domain.document;

import java.time.LocalDateTime;

public interface IDocumentHeaderBo {
    Integer getInstitutionId();

    Integer getEncounterId();

    void setId(Long documentId);

    Long getId();

    Integer getCreatedBy();

    LocalDateTime getCreatedOn();

    Integer getClinicalSpecialtyId();

    String getDocumentStatusId();
}
