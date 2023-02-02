package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentFileBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentFileStorageImpl implements DocumentFileStorage {

    private final DocumentFileRepository documentRepository;

    public DocumentFileStorageImpl(DocumentFileRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Optional<DocumentFileBo> findById(Long id) {
        return documentRepository.findById(id)
                .map(this::mapToDocumentFileBo);
    }

	@Override
	public List<Long> getIdsByDocumentsIds(List<Long> ids) {
		return documentRepository.findAllById(ids).stream().map(DocumentFile::getId).collect(Collectors.toList());
	}

	private DocumentFileBo mapToDocumentFileBo(DocumentFile documentFile) {
        return new DocumentFileBo(
                documentFile.getId(),
                documentFile.getSourceId(),
                documentFile.getSourceTypeId(),
                documentFile.getTypeId(),
                documentFile.getFilepath(),
                documentFile.getFilename(),
                documentFile.getUuidfile(),
                documentFile.getChecksum());
    }
}
