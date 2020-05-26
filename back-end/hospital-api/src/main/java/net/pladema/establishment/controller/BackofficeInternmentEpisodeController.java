package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.repository.documents.entity.InternmentEpisode;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/internmentepisodes")
public class BackofficeInternmentEpisodeController extends AbstractBackofficeController<InternmentEpisode, Integer>{

	public BackofficeInternmentEpisodeController(InternmentEpisodeRepository repository) {
		super(repository);
	}

}
