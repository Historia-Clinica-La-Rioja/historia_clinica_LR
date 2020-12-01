package net.pladema.clinichistory.documents.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@ToString
public abstract class OnGenerateDocumentEvent extends ApplicationEvent {

    protected static final String PDF_EXTENSION = ".pdf";

    private Document document;

    private String uuid;

    private Integer institutionId;

    private Integer sourceId;

    private EDocumentType documentType;

    private Integer patientId;

    public OnGenerateDocumentEvent(Document document, Integer institutionId, Integer sourceId,
                                   EDocumentType documentType, Integer patientId) {
        super(sourceId);
        this.document = document;
        this.institutionId = institutionId;
        this.sourceId = sourceId;
        this.documentType = documentType;
        this.patientId = patientId;
        this.uuid = UUID.randomUUID().toString();
    }

    public abstract String getRelativeDirectory();

    public abstract Short getSourceType();

    public String getTemplateName() {
        return documentType.getTemplate();
    }

    public String getDocumentType() {
        return documentType.getValue();
    }

    public Short getDocumentTypeId() {
        return documentType.getId();
    }

    public String buildDownloadName() {
        String name = documentType + "_" + document.getId();
        return buildDownloadName(name);
    }

    private String buildDownloadName(String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        name = name + "_" + formattedDateTime + ".pdf";
        return name;
    }

}

