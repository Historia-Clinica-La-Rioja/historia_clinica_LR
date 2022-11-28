package net.pladema.clinichistory.external.infraestructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalClinicalHistoryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedExternalClinicalHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.external.service.ExternalClinicalHistoryService;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedExternalClinicalHistoryImpl implements SharedExternalClinicalHistory {

	private final ExternalClinicalHistoryService externalClinicalHistoryService;

	@Override
	public Integer createExternalClinicalHistory(ExternalClinicalHistoryDto externalClinicalHistoryDto) {
		log.debug("Input parameter -> externalClinicalHistoryDto {} ", externalClinicalHistoryDto);
		return externalClinicalHistoryService.save(mapToBo(externalClinicalHistoryDto));
	}

	private ExternalClinicalHistoryBo mapToBo(ExternalClinicalHistoryDto externalClinicalHistoryDto) {
		return ExternalClinicalHistoryBo.builder()
				.patientGender(externalClinicalHistoryDto.getPatientGender())
				.patientDocumentType(externalClinicalHistoryDto.getPatientDocumentType())
				.patientDocumentNumber(externalClinicalHistoryDto.getPatientDocumentNumber())
				.notes(externalClinicalHistoryDto.getNotes())
				.consultationDate(externalClinicalHistoryDto.getConsultationDate())
				.institution(externalClinicalHistoryDto.getInstitution())
				.professionalName(externalClinicalHistoryDto.getProfessionalName())
				.professionalSpecialty(externalClinicalHistoryDto.getProfessionalSpecialty())
				.build();
	}

}
