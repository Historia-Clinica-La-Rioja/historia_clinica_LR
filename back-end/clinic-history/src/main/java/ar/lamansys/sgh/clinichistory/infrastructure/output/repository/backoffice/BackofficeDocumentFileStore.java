package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.dto.DocumentFileDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeDocumentFileStore implements BackofficeStore<DocumentFileDto, Long> {
	private final DocumentFileRepository documentFileRepository;

	private final LocalDateMapper localDateMapper;

	public BackofficeDocumentFileStore(DocumentFileRepository documentFileRepository, LocalDateMapper localDateMapper) {
		this.documentFileRepository = documentFileRepository;
		this.localDateMapper = localDateMapper;
	}


	@Override
	public Page<DocumentFileDto> findAll(DocumentFileDto dto, Pageable pageable) {
		var page = documentFileRepository.findAll(createExample(dto), pageable);
		List<DocumentFileDto> documentFileDtoList = page.getContent().stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
		return new PageImpl<>(documentFileDtoList,
				pageable,
				documentFileRepository.count());
	}

	private Example<DocumentFile> createExample(DocumentFileDto dto) {
		var documentFile = new DocumentFile();
		if (dto.getId() != null)
			documentFile = documentFileRepository
				.findById(dto.getId())
				.get();
		documentFile.setFilename(dto.getFilename());
		documentFile.setSourceId(dto.getSourceId());
		documentFile.setTypeId(dto.getTypeId());
		return Example.of(documentFile);
	}

	@Override
	public List<DocumentFileDto> findAll() {
		return documentFileRepository.findAll().stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
	}

	@Override
	public List<DocumentFileDto> findAllById(List<Long> ids) {
		return documentFileRepository.findAllById(ids).stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
	}

	@Override
	public Optional<DocumentFileDto> findById(Long id) {
		return documentFileRepository.findById(id).map(this::mapToDocumentFileDto);
	}

	@Override
	public DocumentFileDto save(DocumentFileDto dto) {
		return documentFileRepository.findById(dto.getId())
				.map(documentFileRepository::save)
				.map(this::mapToDocumentFileDto)
				.get();
	}
	
	@Override
	public void deleteById(Long id) {
		documentFileRepository.deleteById(id);
	}

	@Override
	public Example<DocumentFileDto> buildExample(DocumentFileDto entity) {
		return Example.of(entity);
	}

	private DocumentFileDto mapToDocumentFileDto(DocumentFile entity) {
		return new DocumentFileDto(entity.getId(),
				entity.getSourceId(),
				entity.getSourceTypeId(),
				entity.getTypeId(),
				entity.getFilename(),
				localDateMapper.fromLocalDateTimeToZonedDateTime(entity.getCreatedOn()));
	}
}
