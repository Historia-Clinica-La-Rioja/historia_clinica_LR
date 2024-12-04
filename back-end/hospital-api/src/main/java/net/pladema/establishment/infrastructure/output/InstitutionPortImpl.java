package net.pladema.establishment.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherInstitutionBo;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.application.port.InstitutionPort;

import net.pladema.establishment.repository.InstitutionRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InstitutionPortImpl implements InstitutionPort {

	private final InstitutionRepository institutionRepository;

	@Override
	public MedicationRequestValidationDispatcherInstitutionBo getInstitutionDataNeededForMedicationRequestValidation(Integer institutionId) {
		return institutionRepository.fetchInstitutionDataNeededForMedicationRequestValidation(institutionId);
	}

}
