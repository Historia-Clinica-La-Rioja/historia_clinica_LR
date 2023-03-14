package net.pladema.person.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.person.service.DeletePersonFileService;

import net.pladema.person.service.PersonFileService;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DeletePersonFileServiceImpl implements DeletePersonFileService {

	private final PersonFileService personFileService;

	public void run(List<Integer> fileIds) {
		log.debug("Input parameters -> fileIds {}", fileIds);

		personFileService.deleteFiles(fileIds);
	}
}
