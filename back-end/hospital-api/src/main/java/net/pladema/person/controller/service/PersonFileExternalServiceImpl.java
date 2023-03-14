package net.pladema.person.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.person.service.PersonFileService;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PersonFileExternalServiceImpl implements PersonFileExternalService{

	private final PersonFileService personFileService;

	@Override
	public List<PersonFileDto> getFiles(Integer personId) {
		log.debug("Input parameters -> personId {}, ", personId);
		return personFileService.getFiles(personId);
	}
}
