package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.mapper.BackofficeEpisodeDocumentTypeStore;

import net.pladema.staff.repository.entity.EpisodeDocumentType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/episodedocumenttypes")
public class BackofficeEpisodeDocumentTypeController extends AbstractBackofficeController<EpisodeDocumentType, Integer> {

	public BackofficeEpisodeDocumentTypeController(BackofficeEpisodeDocumentTypeStore backofficeEpisodeDocumentTypeStore) {
		super(backofficeEpisodeDocumentTypeStore);
	}
}
