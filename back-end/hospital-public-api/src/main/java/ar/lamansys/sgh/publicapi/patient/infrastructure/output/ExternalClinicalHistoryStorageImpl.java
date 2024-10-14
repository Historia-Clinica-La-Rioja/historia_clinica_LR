package ar.lamansys.sgh.publicapi.patient.infrastructure.output;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalClinicalHistoryStorage;
import ar.lamansys.sgh.publicapi.patient.application.port.out.exceptions.ExternalClinicalHistoryStorageException;
import ar.lamansys.sgh.publicapi.patient.application.port.out.exceptions.ExternalClinicalHistoryStorageExceptionEnum;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalClinicalHistoryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedExternalClinicalHistory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalClinicalHistoryStorageImpl implements ExternalClinicalHistoryStorage {

	private final SharedExternalClinicalHistory sharedExternalClinicalHistory;

	@Override
	public Integer create(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		assertContextValid(externalClinicalHistoryBo);
		return sharedExternalClinicalHistory.createExternalClinicalHistory(mapToDto(externalClinicalHistoryBo));
	}

	private void assertContextValid(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		if (externalClinicalHistoryBo.getPatientGender() == null)
			throw new ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum.NULL_GENDER_ID, "Es obligatorio ingresar identificador de género");
		if (externalClinicalHistoryBo.getPatientDocumentType() == null)
			throw new ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum.NULL_DOCUMENT_TYPE_ID, "Es obligatorio ingresar identificador de tipo de documento");
		if (externalClinicalHistoryBo.getPatientDocumentNumber() == null || externalClinicalHistoryBo.getPatientDocumentNumber().isBlank())
			throw new ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum.NULL_DOCUMENT_NUMBER, "Es obligatorio ingresar número de documento");
		if (externalClinicalHistoryBo.getNotes() == null || externalClinicalHistoryBo.getNotes().isBlank())
			throw new ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum.NULL_NOTES, "Es obligatorio ingresar notas");
		if (externalClinicalHistoryBo.getConsultationDate() == null)
			throw new ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum.NULL_CONSULTATION_DATE, "Es obligatorio ingresar una fecha de consulta");
	}

	private ExternalClinicalHistoryDto mapToDto(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		return ExternalClinicalHistoryDto.builder()
				.patientGender(externalClinicalHistoryBo.getPatientGender())
				.patientDocumentType(externalClinicalHistoryBo.getPatientDocumentType())
				.patientDocumentNumber(externalClinicalHistoryBo.getPatientDocumentNumber())
				.notes(externalClinicalHistoryBo.getNotes())
				.consultationDate(externalClinicalHistoryBo.getConsultationDate())
				.institution(externalClinicalHistoryBo.getInstitution())
				.professionalName(externalClinicalHistoryBo.getProfessionalName())
				.professionalSpecialty(externalClinicalHistoryBo.getProfessionalSpecialty())
				.build();
	}
}
