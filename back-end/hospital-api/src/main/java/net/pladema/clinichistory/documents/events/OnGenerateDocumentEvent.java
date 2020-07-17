package net.pladema.clinichistory.documents.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.EDocumentType;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
@ToString
public abstract class OnGenerateDocumentEvent extends ApplicationEvent {

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

    public String getTemplateName(){
        return documentType.getTemplate();
    }

    public String getDocumentType(){
        return documentType.getValue();
    }

    public Short getDocumentTypeId(){
        return documentType.getId();
    }
}

