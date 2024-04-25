package ar.lamansys.sgh.clinichistory.application.document.draft;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CreateDocumentWithDraftSupport {

    private final Function<Integer, Long> getLastDraftDocumentId;
    private final Function<Long, IDocumentBo> getLastDraftDocument;
    private final DiscardPreviousDocument discardPreviousDocument;
    private final SetNullIdsDocumentElements setNullIdsDocumentElements;
    private final Function<IDocumentBo, Integer> createNewDocument;
    private final Function<IDocumentBo, Boolean> setPatientInfo;

    @Transactional
    public Integer run(IDocumentBo newDocument) {
        log.debug("Input parameter -> newDocument {}", newDocument);

        this.setPreviousDocumentId(newDocument);
        this.discardPreviousDocument(newDocument);
        setNullIdsDocumentElements.run(newDocument);
        Integer id = createNewDocument.apply(newDocument);

        log.debug("Output -> new document id {} generated", id);
        return id;
    }

    private void setPreviousDocumentId(IDocumentBo newDocument) {
        Long lastDocumentId = getLastDraftDocumentId.apply(newDocument.getEncounterId());
        newDocument.setPreviousDocumentId(lastDocumentId);
    }

    private void discardPreviousDocument(IDocumentBo newDocument) {
        Long previousDocumentId = newDocument.getPreviousDocumentId();
        if (previousDocumentId != null) {
            IDocumentBo previousDocument = getLastDraftDocument.apply(newDocument.getPreviousDocumentId());
            setPatientInfo.apply(previousDocument);
            setInitialDocument(newDocument, previousDocument);
            discardPreviousDocument.run(previousDocument);
        }
    }

    private void setInitialDocument(IDocumentBo newDocument, IDocumentBo previousDocument) {
        Long initialDocumentId = previousDocument.getInitialDocumentId();
        newDocument.setInitialDocumentId(initialDocumentId != null ? initialDocumentId : previousDocument.getId());
    }

}
