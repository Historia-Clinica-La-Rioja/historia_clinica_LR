package net.pladema.internation.service.documents;

import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentVitalSign;

public interface DocumentService {

    void createHealthConditionIndex(Long documentId, Integer healthConditionId);

    DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);
}
