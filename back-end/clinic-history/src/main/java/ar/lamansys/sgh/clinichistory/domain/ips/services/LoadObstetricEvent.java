package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObstetricEventRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObstetricEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoadObstetricEvent {

	private static final Logger LOG = LoggerFactory.getLogger(LoadObstetricEvent.class);

	public static final String OUTPUT = "Output -> {}";

	private final ObstetricEventRepository obstetricEventRepository;

	private final DocumentService documentService;

	private final LoadNewborns loadNewborns;

	public LoadObstetricEvent (ObstetricEventRepository obstetricEventRepository,
							   DocumentService documentService,
							   LoadNewborns loadNewborns){
		this.obstetricEventRepository = obstetricEventRepository;
		this.documentService = documentService;
		this.loadNewborns = loadNewborns;
	}

	public ObstetricEventBo run (Long documentId, Optional<ObstetricEventBo> obstetricEventBo){
		LOG.debug("Input parameters -> documentId {}, obstetricEvent {}", documentId, obstetricEventBo);
		obstetricEventBo.ifPresent(oe -> {
			if (!oe.hasNotNullValues())
				return;
			ObstetricEvent entity = new ObstetricEvent();
			entity.setGestationalAge(oe.getGestationalAge());
			entity.setPregnancyTerminationType(oe.getPregnancyTerminationType() != null ? oe.getPregnancyTerminationType().getId() : null);
			entity.setPreviousPregnancies(oe.getPreviousPregnancies());
			entity.setCurrentPregnancyEndDate(oe.getCurrentPregnancyEndDate());

			oe.setId(obstetricEventRepository.save(entity).getId());

			loadNewborns.run(oe.getNewborns(), oe.getId());

			documentService.createDocumentObstetricEvent(documentId, oe.getId());
		});
		LOG.debug(OUTPUT, obstetricEventBo);
		return obstetricEventBo.orElse(null);
	}

}
