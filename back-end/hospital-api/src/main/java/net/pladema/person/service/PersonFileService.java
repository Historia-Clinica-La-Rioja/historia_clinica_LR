package net.pladema.person.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonFileService {

	List<Integer> addFiles(MultipartFile[] files, Integer personId, Integer institutionId);

	void deleteFiles(List<Integer> filesIds);

	StoredFileBo getFile(Integer fileId);

	List<PersonFileDto> getFiles(Integer personId);

}
