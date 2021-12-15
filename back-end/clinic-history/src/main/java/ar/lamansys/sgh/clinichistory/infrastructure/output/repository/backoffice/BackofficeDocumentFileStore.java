package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeDocumentFileStore implements BackofficeStore<DocumentFile, Long> {
	private final DocumentFileRepository documentFileRepository;

	public BackofficeDocumentFileStore(DocumentFileRepository documentFileRepository) {
		this.documentFileRepository = documentFileRepository;
	}


	@Override
	public Page<DocumentFile> findAll(DocumentFile entity, Pageable pageable) {
		return documentFileRepository.findAll(Example.of(entity), pageable);
	}

	@Override
	public List<DocumentFile> findAll() {
		return documentFileRepository.findAll();
	}

	@Override
	public List<DocumentFile> findAllById(List<Long> ids) {
		return documentFileRepository.findAllById(ids);
	}

	@Override
	public Optional<DocumentFile> findById(Long id) {
		return documentFileRepository.findById(id);
	}

	@Override
	public DocumentFile save(DocumentFile dto) {
		return documentFileRepository.save(dto);
	}
	
	@Override
	public void deleteById(Long id) {
		documentFileRepository.deleteById(id);
	}

	@Override
	public Example<DocumentFile> buildExample(DocumentFile entity) {
		return Example.of(entity);
	}


}
