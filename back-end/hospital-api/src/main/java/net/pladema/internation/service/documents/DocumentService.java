package net.pladema.internation.service.documents;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.core.entity.DocumentInmunization;
import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentVitalSign;

public interface DocumentService {

    Document create(Document document);

    void createDocumentHealthCondition(Long documentId, Integer healthConditionId);

    DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);

    void createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId);

    DocumentInmunization createInmunization(Long documentId, Integer inmunizationId);
}
