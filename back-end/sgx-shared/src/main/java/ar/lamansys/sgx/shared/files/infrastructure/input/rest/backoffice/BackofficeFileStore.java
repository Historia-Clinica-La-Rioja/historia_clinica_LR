package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto.FileInfoDto;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficeFileStore implements BackofficeStore<FileInfoDto, Long> {
	private final FileInfoRepository fileInfoRepository;

	private final FileService fileService;
	private final LocalDateMapper localDateMapper;

	public BackofficeFileStore(FileInfoRepository fileInfoRepository, FileService fileService,
							   LocalDateMapper localDateMapper) {
		this.fileInfoRepository = fileInfoRepository;
		this.fileService = fileService;
		this.localDateMapper = localDateMapper;
	}


	@Override
	public Page<FileInfoDto> findAll(FileInfoDto dto, Pageable pageable) {
		var example = createExample(dto);
		var page = fileInfoRepository.findAll(example, pageable);
		List<FileInfoDto> fileInfoDtoList = page.getContent().stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
		return new PageImpl<>(fileInfoDtoList,
				pageable,
				fileInfoRepository.count());
	}

	private Example<FileInfo> createExample(FileInfoDto dto) {
		var fileInfo = new FileInfo();
		if (dto.getId() != null) {
			fileInfo = fileInfoRepository
					.findById(dto.getId())
					.get();
			return Example.of(fileInfo);
		}
		fileInfo.setName(dto.getName());
		fileInfo.setSource(dto.getSource());
		fileInfo.setRelativePath(dto.getRelativePath());
		fileInfo.setUuidfile(dto.getUuidfile());
		return Example.of(fileInfo);
	}

	@Override
	public List<FileInfoDto> findAll() {
		return fileInfoRepository.findAll().stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
	}

	@Override
	public List<FileInfoDto> findAllById(List<Long> ids) {
		return fileInfoRepository.findAllById(ids).stream().map(this::mapToDocumentFileDto).collect(Collectors.toList());
	}

	@Override
	public Optional<FileInfoDto> findById(Long id) {
		return fileInfoRepository.findById(id).map(this::mapToDocumentFileDto);
	}

	@Override
	public FileInfoDto save(FileInfoDto dto) {
		return fileInfoRepository.findById(dto.getId())
				.map(fileInfo -> update(fileInfo, dto))
				.map(this::mapToDocumentFileDto)
				.get();
	}

	private FileInfo update(FileInfo fileInfo, FileInfoDto dto) {
		var path = fileService.buildCompletePath(dto.getRelativePath());
		fileService.validateFileExists(path);
		fileInfo.setRelativePath(path.relativePath);
		return fileInfoRepository.save(fileInfo);
	}

	@Override
	public void deleteById(Long id) {
		fileInfoRepository.deleteById(id);
	}

	@Override
	public Example<FileInfoDto> buildExample(FileInfoDto entity) {
		return Example.of(entity);
	}

	private FileInfoDto mapToDocumentFileDto(FileInfo entity) {
		return new FileInfoDto(entity.getId(),
				entity.getName(),
				entity.getRelativePath(),
				entity.getOriginalPath(),
				entity.getUuidfile(),
				entity.getContentType(),
				entity.getSize(),
				entity.getSource(),
				entity.getGeneratedBy(),
				localDateMapper.fromLocalDateTimeToZonedDateTime(entity.getCreatedOn()));
	}
}
