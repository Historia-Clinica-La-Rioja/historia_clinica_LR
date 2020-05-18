package net.pladema.internation.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.documents.InternmentDocument;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class OnGenerateDocumentEvent extends ApplicationEvent {

    private InternmentDocument document;
    private Integer institutionId;
    private Integer internmentEpisodeId;
    private Short documentType;
    private String templateName;
    private Integer patiendId;

    public OnGenerateDocumentEvent(InternmentDocument document, Integer institutionId, Integer internmentEpisodeId,
                                   Short documentType, String templateName, Integer patientId) {
        super(document);
        this.document = document;
        this.institutionId = institutionId;
        this.internmentEpisodeId = internmentEpisodeId;
        this.documentType = documentType;
        this.templateName = templateName;
        this.patiendId = patientId;
    }
}
