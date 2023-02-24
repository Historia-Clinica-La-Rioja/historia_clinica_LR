package net.pladema.person.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import net.pladema.person.service.GetPersonFileService;

import net.pladema.person.service.PersonFileService;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPersonFileServiceImpl implements GetPersonFileService {

	private final PersonFileService personFileService;

	@Override
	public StoredFileBo run(Integer fileId) {

		log.debug("Input parameters -> fileId {}", fileId);

		StoredFileBo result = personFileService.getFile(fileId);
		return result;

	}
}
