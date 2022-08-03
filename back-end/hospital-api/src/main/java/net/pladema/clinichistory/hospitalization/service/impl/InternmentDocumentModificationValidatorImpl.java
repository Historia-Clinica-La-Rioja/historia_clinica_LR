package net.pladema.clinichistory.hospitalization.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class InternmentDocumentModificationValidatorImpl implements InternmentDocumentModificationValidator {

	private final SharedDocumentPort sharedDocumentPort;
	private final InternmentEpisodeService internmentEpisodeService;

	@Override
	public void execute(Integer intermentEpisodeId, Long documentId, String reason, EDocumentType documentType) {
		log.debug("Input parameter intermentEpisodeId {}, documentId {}, reason {}, documentType{}",
				intermentEpisodeId, documentId, reason, documentType.getValue());

		if (reason == null)
			throw new InternmentDocumentException(InternmentDocumentEnumException.NULL_REASON, "Para llevar a cabo la acción el motivo es un campo requerido");

		DocumentReduceInfoDto document = sharedDocumentPort.getDocument(documentId);

		Integer currentUser = UserInfo.getCurrentAuditor();
		if (!document.getCreatedBy().equals(currentUser))
			throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_USER, "El documento únicamente puede ser intervenido por el usuario que lo ha creado");

		if (!document.getTypeId().equals(documentType.getId()))
			throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_ID, "No es posible llevar a cabo la acción dado que el id ingresado no coincide con el tipo de documento");

		if (!document.getSourceId().equals(intermentEpisodeId))
			throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_INTERNMENT_EPISODE_ID, "El id del episodio de internación ingresado no coincide con el id de episodio de internacion del documento");

		if (internmentEpisodeService.haveMedicalDischarge(intermentEpisodeId))
			throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_MEDICAL_DISCHARGE, "No es posible llevar a cabo la acción dado que se ha realizado un alta médica");

		if (ChronoUnit.HOURS.between(document.getCreatedOn(), LocalDateTime.now()) > 24)
			throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_DATE, "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación");
	}
}
