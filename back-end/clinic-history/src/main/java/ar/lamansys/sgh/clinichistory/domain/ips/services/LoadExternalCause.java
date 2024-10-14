package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;

import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ExternalCauseRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ExternalCause;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class LoadExternalCause {

	private static final Logger LOG = LoggerFactory.getLogger(LoadExternalCause.class);

	public static final String OUTPUT = "Output -> {}";

	private final ExternalCauseRepository externalCauseRepository;

	private final DocumentService documentService;

	private final SnomedService snomedService;

	public LoadExternalCause(ExternalCauseRepository externalCauseRepository,
							 DocumentService documentService,
							 SnomedService snomedService)
	{
		this.externalCauseRepository = externalCauseRepository;
		this.documentService = documentService;
		this.snomedService = snomedService;
	}

	public ExternalCauseBo run (Long documentId, Optional<ExternalCauseBo> externalCauseBo) {
		LOG.debug("Input parameters -> documentId {}, externalCauseBo {}", documentId, externalCauseBo);
		externalCauseBo.ifPresent(ec -> {
			if (!ec.hasNotNullValues())
				return;
			ExternalCause externalCause = new ExternalCause();
			if (ec.getSnomed() != null){
				Integer snomedId = snomedService.getSnomedId(ec.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(ec.getSnomed()));
				externalCause.setSnomedId(snomedId);
			}
			if (ec.getExternalCauseType() != null)
				externalCause.setExternalCauseTypeId(ec.getExternalCauseType().getId());
			if(ec.getEventLocation() != null)
				externalCause.setEventLocation(ec.getEventLocation().getId());

			var externalCauseId = externalCauseRepository.save(externalCause).getId();
			ec.setId(externalCauseId);

			documentService.createDocumentExternalCause(documentId, externalCause.getId());
		});
		LOG.debug(OUTPUT, externalCauseBo);
		return externalCauseBo.orElse(null);
	}

}
