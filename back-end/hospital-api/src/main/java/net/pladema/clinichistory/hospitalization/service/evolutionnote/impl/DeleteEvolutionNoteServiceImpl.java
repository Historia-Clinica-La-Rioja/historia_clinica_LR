package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.DeleteEvolutionNoteService;

import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;

import net.pladema.permissions.repository.enums.ERole;

import net.pladema.user.application.port.UserRoleStorage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteEvolutionNoteServiceImpl implements DeleteEvolutionNoteService {

	private final InternmentEpisodeService internmentEpisodeService;
	private final InternmentDocumentModificationValidator internmentDocumentModificationValidator;
	private final SharedDocumentPort sharedDocumentPort;
	private final UserRoleStorage userRoleStorage;

	@Override
	@Transactional
	public void execute(Integer intermentEpisodeId, Long evolutionNoteId, String reason) {
		log.debug("Input parameters -> intermentEpisodeId {}, evolutionNoteId {}, reason {}",
				intermentEpisodeId, evolutionNoteId, reason);
		assertContextValid(intermentEpisodeId, evolutionNoteId, reason);
		sharedDocumentPort.deleteDocument(evolutionNoteId, DocumentStatus.ERROR);
		sharedDocumentPort.updateDocumentModificationReason(evolutionNoteId, reason);
	}

	private void assertContextValid(Integer intermentEpisodeId, Long evolutionNoteId, String reason) {
		Integer userId = UserInfo.getCurrentAuditor();

		Boolean isNursingProfessional = userRoleStorage.getRolesByUser(userId).stream()
				.filter(userRoleBo -> (
						userRoleBo.getRoleId() == ERole.ENFERMERO.getId() &&
								userRoleBo.getRoleId() != ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId() &&
								userRoleBo.getRoleId() != ERole.ESPECIALISTA_MEDICO.getId())
				).collect(Collectors.toList()).stream().findAny().isPresent();
		EDocumentType documentType = isNursingProfessional ? EDocumentType.NURSING_EVOLUTION_NOTE : EDocumentType.EVALUATION_NOTE;

		internmentDocumentModificationValidator.execute(intermentEpisodeId, evolutionNoteId, reason, documentType);

		if (internmentEpisodeService.haveEpicrisis(intermentEpisodeId))
			throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_EPICRISIS, "No se puede eliminar el documento nota de evolución debido a que existe una epicrisis");
	}
}
