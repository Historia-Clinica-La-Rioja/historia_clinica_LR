package ar.lamansys.sgh.clinichistory.application.document.draft;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;

import java.util.Optional;
import java.util.function.Function;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CreateDocumentWithDraftSupport {

    private final Function<Integer, Long> getLastDraftDocumentId;
    private final Function<Long, IDocumentBo> getLastDraftDocument;
    private final DiscardPreviousDocument discardPreviousDocument;
    private final IpsVisitor setNullIdsDocumentElementsVisitor;
    private final Function<IDocumentBo, Integer> createNewDocument;
    private final Function<Integer, BasicPatientDto> setPatientInfo;

    @Transactional
    public Integer run(IDocumentBo newDocument) {
        log.debug("Input parameter -> newDocument {}", newDocument);

        this.setPreviousDocumentId(newDocument);

        Long previousDocumentId = newDocument.getPreviousDocumentId();
        if (previousDocumentId != null) {
            IDocumentBo previousDocument = getLastDraftDocument.apply(previousDocumentId);
            this.copyBasicInformation(newDocument, previousDocument);
            discardPreviousDocument.run(previousDocument);
        }

        this.setNullIdsDocumentElements(newDocument);
        Integer id = createNewDocument.apply(newDocument);

        log.debug("Output -> new document id {} generated", id);
        return id;
    }

    private void setNullIdsDocumentElements(IDocumentBo documentBo) {
        documentBo.getIpsComponents()
                .forEach(ipsBo -> ipsBo.accept(setNullIdsDocumentElementsVisitor));
    }

    private void setPreviousDocumentId(IDocumentBo newDocument) {
        Long lastDocumentId = getLastDraftDocumentId.apply(newDocument.getEncounterId());
        newDocument.setPreviousDocumentId(lastDocumentId);
    }

    private void copyBasicInformation(IDocumentBo newDocument, IDocumentBo previousDocument) {
        this.setInitialDocument(newDocument, previousDocument);
        newDocument.setBusinessObjectId(previousDocument.getBusinessObjectId());
        newDocument.setPatientId(previousDocument.getPatientId());
        this.setPatientInfo(newDocument);
        previousDocument.setPatientInfo(newDocument.getPatientInfo());
    }

    private void setInitialDocument(IDocumentBo newDocument, IDocumentBo previousDocument) {
        Long initialDocumentId = previousDocument.getInitialDocumentId();
        newDocument.setInitialDocumentId(initialDocumentId != null ? initialDocumentId : previousDocument.getId());
    }

    private void setPatientInfo(IDocumentBo iDocumentBo) {
        Optional.ofNullable(setPatientInfo.apply(iDocumentBo.getPatientId()))
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresent(iDocumentBo::setPatientInfo);
    }

}
