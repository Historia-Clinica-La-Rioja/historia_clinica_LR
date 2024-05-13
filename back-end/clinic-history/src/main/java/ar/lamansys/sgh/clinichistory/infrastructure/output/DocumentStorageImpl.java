package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentStorageImpl implements DocumentStorage {

    private final DocumentRepository documentRepository;

    @Override
    public Optional<DocumentBo> getDocumentBo(Long documentId) {
        return documentRepository.findById(documentId)
                .map(this::getMinimalDocumentBo);
    }

    private DocumentBo getMinimalDocumentBo(Document document) {
        var result = new DocumentBo();
        result.setId(document.getId());
        result.setPerformedDate(document.getCreatedOn());
        return result;
    }
}
