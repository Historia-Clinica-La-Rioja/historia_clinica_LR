package ar.lamansys.sgh.clinichistory.domain.document.event;

import ar.lamansys.sgh.clinichistory.domain.document.event.exceptions.GenerateDocumentEventEnumException;
import ar.lamansys.sgh.clinichistory.domain.document.event.exceptions.GenerateDocumentEventException;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@ToString
public class OnGenerateDocumentEvent extends ApplicationEvent {

    protected static final String PDF_EXTENSION = ".pdf";

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private final String RELATIVE_DIRECTORY = "/institution/{institutionId}/{encounterType}/{encounterId}/{documentType}/";

    private final IDocumentBo documentBo;

    private final String uuid;

    private final EDocumentType documentType;

    private final ESourceType eSourceType;

    public OnGenerateDocumentEvent(IDocumentBo documentBo) throws GenerateDocumentEventException {
        super(documentBo);
        validData(documentBo);
        this.documentBo = documentBo;
        this.documentType = EDocumentType.map(documentBo.getDocumentType());
        this.eSourceType = ESourceType.map(documentBo.getDocumentSource());
        this.uuid = UUID.randomUUID().toString();
    }

    private void validData(IDocumentBo documentBo) throws GenerateDocumentEventException {
        if (documentBo == null)
            throw new GenerateDocumentEventException(GenerateDocumentEventEnumException.DOCUMENT_NULL, "El documento es obligatorio");
        if (documentBo.getId() == null)
            throw new GenerateDocumentEventException(GenerateDocumentEventEnumException.DOCUMENT_ID_NULL, "El id del documento es obligatorio");
        if (documentBo.getInstitutionId() == null)
            throw new GenerateDocumentEventException(GenerateDocumentEventEnumException.INSTITUTION_ID_NULL, "El id de la institución es obligatorio");
        if (documentBo.getEncounterId() == null)
            throw new GenerateDocumentEventException(GenerateDocumentEventEnumException.ENCOUNTER_ID_NULL, "El id del encuentro asociado al documento es obligatorio");
        if (documentBo.getPatientId() == null)
            throw new GenerateDocumentEventException(GenerateDocumentEventEnumException.PATIENT_ID_NULL, "El id del paciente es obligatorio");
    }

    public String getTemplateName() {
        return documentType.getTemplate();
    }

    public String getDocumentType() {
        return documentType.getValue();
    }

    public Short getDocumentTypeId() {
        return documentType.getId();
    }

    public String getRelativeDirectory() {
        return RELATIVE_DIRECTORY
                .replace("{institutionId}", documentBo.getInstitutionId().toString())
                .replace("{encounterType}", eSourceType.getValue() + "")
                .replace("{encounterId}", documentBo.getEncounterId().toString())
                .replace("{documentType}", getDocumentType()) +
                getUuid() + PDF_EXTENSION;
    }
    public Short getSourceType() {
        return eSourceType.getId();
    }

    public String buildDownloadName() {
        String name = documentType + "_" + documentBo.getId();
        return buildDownloadName(name);
    }

    private String buildDownloadName(String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        name = name + "_" + formattedDateTime + ".pdf";
        return name;
    }

    public Integer getPatientId() {
        return documentBo.getPatientId();
    }

    public Integer getEncounterId(){
        return documentBo.getEncounterId();
    }

    public LocalDateTime getPerformedDate() { return documentBo.getPerformedDate(); }
}

