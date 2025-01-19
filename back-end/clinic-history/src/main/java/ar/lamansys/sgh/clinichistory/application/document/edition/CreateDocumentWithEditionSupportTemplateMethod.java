package ar.lamansys.sgh.clinichistory.application.document.edition;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EPreviousDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.SetNullIdsIpsVisitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class CreateDocumentWithEditionSupportTemplateMethod {

    private final UpdatePreviousDocument updatePreviousDocumentBo;
    private final SetNullIdsIpsVisitor setNullIdsDocumentElementsVisitor;

    @Transactional
    public Integer run(IEditableDocumentBo newDocument) {
        log.debug("Input parameter -> newDocument {}", newDocument);

        Long previousDocumentId = getPreviousDocumentId(newDocument);
        newDocument.setPreviousDocumentId(previousDocumentId);
        newDocument.setDocumentStatusId(EDocumentStatus.getDocumentStatusId(newDocument));

        if (!this.isFirstTimeDocumentCreation(previousDocumentId)) {
            IEditableDocumentBo previousDocument = getPreviousDocument(previousDocumentId);
            this.copyBasicInformation(newDocument, previousDocument);
            if (!this.isClosingDocumentForTheFirstTime(previousDocument)) {
                assertContextValid(newDocument);
            }
            updatePreviousDocumentBo.run(previousDocument);
        }

        this.setNullIdsDocumentElements(newDocument);

        Integer id = createNewDocument(newDocument);

        log.debug("Output -> new document with bussiness object id {} generated", id);
        return id;
    }

    private boolean isFirstTimeDocumentCreation(Long previousDocumentId) {
        return previousDocumentId == null;
    }

    private boolean isClosingDocumentForTheFirstTime(IEditableDocumentBo previousDocument) {
        return !EPreviousDocumentStatus.hasToBeValidated(previousDocument);
    }

    protected abstract Long getPreviousDocumentId(IEditableDocumentBo newDocument);

    protected abstract IEditableDocumentBo getPreviousDocument(Long previousDocumentId);

    protected abstract void setPatientInfo(IEditableDocumentBo newDocument);

    protected abstract void assertContextValid(IEditableDocumentBo newDocument);

    protected abstract Integer createNewDocument(IEditableDocumentBo newDocument);

    protected void setNullIdsDocumentElements(IEditableDocumentBo newDocument) {
        newDocument.getIpsComponents()
                .forEach(ipsBo -> ipsBo.accept(setNullIdsDocumentElementsVisitor));
    }

    protected void copyBasicInformation(IEditableDocumentBo newDocument, IEditableDocumentBo previousDocument) {
        this.setInitialDocument(newDocument, previousDocument);
        newDocument.setBusinessObjectId(previousDocument.getBusinessObjectId());
        newDocument.setPatientId(previousDocument.getPatientId());
        newDocument.setPerformedDate(previousDocument.getPerformedDate());
        newDocument.setCreatedBy(previousDocument.getCreatedBy());
        newDocument.setEncounterId(previousDocument.getEncounterId());
        this.setPatientInfo(newDocument);

        previousDocument.setPatientInfo(newDocument.getPatientInfo());
        previousDocument.setModificationReason(newDocument.getModificationReason());
    }

    private void setInitialDocument(IEditableDocumentBo newDocument, IEditableDocumentBo previousDocument) {
        Long initialDocumentId = previousDocument.getInitialDocumentId();
        newDocument.setInitialDocumentId(initialDocumentId != null ? initialDocumentId : previousDocument.getId());
    }

}
