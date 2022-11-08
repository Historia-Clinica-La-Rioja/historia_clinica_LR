package net.pladema.person.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonFileService {

	List<Integer> addFiles(MultipartFile[] files, Integer personId, Integer institutionId);

	void deleteFiles(List<Integer> filesIds);

}
