package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CloseEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CloseEpicrisisDraftServiceImpl implements CloseEpicrisisDraftService {

	private final SharedDocumentPort sharedDocumentPort;
	private final EpicrisisService epicrisisService;
	private final CreateEpicrisisService createEpicrisisService;

	@Override
	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldEpicrisisId, EpicrisisBo newEpicrisis) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldEpicrisisId {}, newEpicrisis {} ", intermentEpisodeId, oldEpicrisisId, newEpicrisis);
		EpicrisisBo oldEpicrisis = epicrisisService.getDocument(oldEpicrisisId);
		newEpicrisis.setInitialDocumentId(oldEpicrisis.getInitialDocumentId() != null ? oldEpicrisis.getInitialDocumentId() : oldEpicrisis.getId());

		sharedDocumentPort.deleteDocument(oldEpicrisis.getId(), DocumentStatus.ERROR);

		var result = createEpicrisisService.execute(newEpicrisis, false);

		log.debug("Output -> {}", result.getId());
		return result.getId();
	}

}
