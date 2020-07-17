package net.pladema.clinichistory.documents.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;

@Getter
@Setter
@ToString
public class OnGenerateInternmentDocumentEvent extends OnGenerateDocumentEvent {

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private final String RELATIVE_DIRECTORY = "/institution/{institutionId}/internment/{sourceId}/{documentType}/";

    public OnGenerateInternmentDocumentEvent(Document document, Integer institutionId, Integer sourceId,
                                             EDocumentType documentType, Integer patientId) {
        super(document, institutionId, sourceId, documentType, patientId);
    }

    @Override
    public String getRelativeDirectory() {
        return RELATIVE_DIRECTORY
                .replace("{institutionId}", getInstitutionId().toString())
                .replace("{sourceId}", getSourceId().toString())
                .replace("{documentType}", getDocumentType()) +
                getUuid();
    }

    @Override
    public Short getSourceType() {
        return SourceType.INTERNACION;
    }
}

