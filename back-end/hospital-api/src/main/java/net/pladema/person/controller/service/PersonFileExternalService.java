package net.pladema.person.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto;

import java.util.List;

public interface PersonFileExternalService {

	List<PersonFileDto> getFiles(Integer personId);
}
