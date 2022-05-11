package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.DeleteAnamnesisService;

import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteAnamnesisServiceImpl implements DeleteAnamnesisService {

	private final InternmentEpisodeService internmentEpisodeService;
	private final InternmentDocumentModificationValidator internmentDocumentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;

	@Override
	@Transactional
	public void execute(Integer intermentEpisodeId, Long anamnesisId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, anamnesisId {}, reason {}",
				intermentEpisodeId, anamnesisId, reason);
		assertContextValid(intermentEpisodeId, anamnesisId, reason);
		sharedDocumentPort.deleteDocument(anamnesisId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(anamnesisId, reason);
		internmentEpisodeService.deleteAnamnesisDocumentId(intermentEpisodeId);
	}

	private void assertContextValid(Integer intermentEpisodeId, Long anamnesisId, String reason) {
		internmentDocumentModificationValidator.execute(intermentEpisodeId, anamnesisId, reason, EDocumentType.ANAMNESIS);
		if (internmentEpisodeService.haveEvolutionNoteAfterAnamnesis(intermentEpisodeId))
			throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_EVOLUTION_NOTE, "No es posible eliminar el documento evaluación de ingreso dado que existe una nota de evolución creada posteriormente");
		if (internmentEpisodeService.havePhysicalDischarge(intermentEpisodeId))
			throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_PHYSICAL_DISCHARGE, "No es posible eliminar el documento evaluación de ingreso dado que se ha realizado el alta física del paciente");
	}

}
