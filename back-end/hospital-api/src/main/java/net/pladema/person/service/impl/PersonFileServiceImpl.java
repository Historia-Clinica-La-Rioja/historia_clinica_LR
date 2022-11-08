package net.pladema.person.service.impl;

import ar.lamansys.sgx.shared.files.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.person.controller.service.exceptions.CreatePersonFileException;
import net.pladema.person.controller.service.exceptions.CreatePersonFileExceptionEnum;
import net.pladema.person.repository.PersonFileRepository;
import net.pladema.person.repository.entity.PersonFile;

import net.pladema.person.service.PersonFileService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PersonFileServiceImpl implements PersonFileService {

	private final FileService fileService;
	private final PersonFileRepository personFileRepository;

	private static final String RELATIVE_DIRECTORY = "/person/{personId}/document-file/";
	private final String OUTPUT = "Output -> {}";

	@Override
	@Transactional
	public List<Integer> addFiles(MultipartFile[] files, Integer personId, Integer institutionId) throws CreatePersonFileException {
		log.debug("Input parameters ->  institutionId {} personId {}, ",institutionId, personId);

		validateInputs(institutionId, personId, files);

		List<Integer> result = Arrays.stream(files).mapToInt(file -> {
					String newFileName = fileService.createFileName(FilenameUtils.getExtension(file.getOriginalFilename()));
					String partialPath = buildPartialPath(personId, newFileName);
					String uuid = newFileName.split("\\.")[0];
					try {
						fileService.transferMultipartFile(partialPath, uuid, "PACIENTE", file);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return savePersonFile(partialPath, file, institutionId, personId);
				})
				.boxed()
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	private Integer savePersonFile(String completePath, MultipartFile file, Integer institutionId, Integer personId) {
		PersonFile personFile = new PersonFile(
				completePath,
				file.getContentType(),
				file.getSize(),
				file.getOriginalFilename(),
				institutionId,
				personId);
		Integer result = personFileRepository.save(personFile).getId();
		log.debug(OUTPUT, result);
		return result;
	}

	private String buildPartialPath(Integer personId, String relativeFilePath){
		log.debug("Input parameters -> personId {}, relativeFilePath {}", personId, relativeFilePath);
		String result = RELATIVE_DIRECTORY
				.replace("{personId}", personId.toString())
				.concat(relativeFilePath);
		log.debug(OUTPUT, result);
		return result;
	}


	private void validateInputs(Integer institutionId, Integer personId, MultipartFile[] files) throws CreatePersonFileException {
		if (institutionId == null)
			throw new CreatePersonFileException(CreatePersonFileExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
		if (personId == null)
			throw new CreatePersonFileException(CreatePersonFileExceptionEnum.NULL_PERSON_ID, "El id del paciente es obligatorio");
		if (files.length == 0)
			throw new CreatePersonFileException(CreatePersonFileExceptionEnum.NULL_FILES, "Es obligatorio al menos un archivo");
	}
}
