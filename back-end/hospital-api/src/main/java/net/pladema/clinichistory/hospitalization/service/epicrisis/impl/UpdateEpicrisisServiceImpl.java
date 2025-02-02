package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateEpicrisisServiceImpl implements UpdateEpicrisisService {

	private final InternmentDocumentModificationValidator documentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;
	private final EpicrisisValidator epicrisisValidator;
	private final InternmentEpisodeService internmentEpisodeService;
	private final DateTimeProvider dateTimeProvider;
	private final DocumentFactory documentFactory;
	private final EpicrisisService epicrisisService;

	@Override
	@Transactional
	public Long execute(Integer intermentEpisodeId, Long oldEpicrisisId, EpicrisisBo newEpicrisis) {
		log.debug("Input parameters -> intermentEpisodeId {}, oldEpicrisisId {}, newEpicrisis {} ", intermentEpisodeId, oldEpicrisisId, newEpicrisis);
		epicrisisValidator.assertContextValid(newEpicrisis);
		EpicrisisBo oldEpicrisis = epicrisisService.getDocument(oldEpicrisisId);
		newEpicrisis.setInitialDocumentId(oldEpicrisis.getInitialDocumentId() != null ? oldEpicrisis.getInitialDocumentId() : oldEpicrisis.getId());
		newEpicrisis.setPerformedDate(dateTimeProvider.nowDateTime());
		documentModificationValidator.execute(intermentEpisodeId, oldEpicrisis.getId(), newEpicrisis.getModificationReason(), EDocumentType.EPICRISIS);
		epicrisisValidator.assertEpicrisisValid(newEpicrisis);
		sharedDocumentPort.updateDocumentModificationReason(oldEpicrisis.getId(), newEpicrisis.getModificationReason());
		sharedDocumentPort.deleteDocument(oldEpicrisis.getId(), DocumentStatus.ERROR);

		newEpicrisis.getMainDiagnosis().setId(null);
		Optional.ofNullable(newEpicrisis.getDiagnosis()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getPersonalHistories().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getFamilyHistories().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getAllergies().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getImmunizations()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(newEpicrisis.getOtherProblems()).ifPresent(list->list.forEach(d -> d.setId(null)));
		if (newEpicrisis.getExternalCause() != null) newEpicrisis.getExternalCause().setId(null);
		if (newEpicrisis.getObstetricEvent() != null) newEpicrisis.getObstetricEvent().setId(null);

		// create new document
		newEpicrisis.setPatientInternmentAge(internmentEpisodeService.getEntryDate(newEpicrisis.getEncounterId()).toLocalDate());
		newEpicrisis.setId(documentFactory.run(newEpicrisis, true));
		internmentEpisodeService.updateEpicrisisDocumentId(newEpicrisis.getEncounterId(), newEpicrisis.getId());
		log.debug("Output -> {}", newEpicrisis.getId());
		return newEpicrisis.getId();
	}

}
