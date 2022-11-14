package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateEpicrisisDraftServiceImpl implements UpdateEpicrisisDraftService {

	private final EpicrisisService epicrisisService;
	private final SharedDocumentPort sharedDocumentPort;
	private final DateTimeProvider dateTimeProvider;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DocumentFactory documentFactory;
	private final EpicrisisValidator epicrisisValidator;


	@Override
	@Transactional
	public Long run(Integer intermentEpisodeId, Long oldEpicrisisId, EpicrisisBo newEpicrisis) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldEpicrisisId {}, newEpicrisis {} ", intermentEpisodeId, oldEpicrisisId, newEpicrisis);
		EpicrisisBo oldEpicrisis = epicrisisService.getDocument(oldEpicrisisId);
		newEpicrisis.setInitialDocumentId(oldEpicrisis.getPreviousDocumentId());
		epicrisisValidator.assertContextValid(newEpicrisis);
		epicrisisValidator.assertEpicrisisValid(newEpicrisis);
		newEpicrisis.setConfirmed(false);
		
		sharedDocumentPort.deleteDocument(oldEpicrisis.getId(), DocumentStatus.ERROR);

		newEpicrisis.getMainDiagnosis().setId(null);
		Optional.ofNullable(newEpicrisis.getDiagnosis()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getPersonalHistories()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getFamilyHistories()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getAllergies()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getImmunizations()).ifPresent(list->list.forEach(d->d.setId(null)));

		// create new document
		newEpicrisis.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newEpicrisis.getEncounterId()).toLocalDate());
		newEpicrisis.setPerformedDate(dateTimeProvider.nowDateTime());
		newEpicrisis.setId(documentFactory.run(newEpicrisis, newEpicrisis.isConfirmed()));
		internmentEpisodeService.updateEpicrisisDocumentId(newEpicrisis.getEncounterId(), newEpicrisis.getId());
		log.debug("Output -> {}", newEpicrisis.getId());
		return newEpicrisis.getId();
	}
	
}
