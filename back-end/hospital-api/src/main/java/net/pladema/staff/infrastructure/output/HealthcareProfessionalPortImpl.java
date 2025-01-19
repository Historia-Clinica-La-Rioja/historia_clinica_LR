package net.pladema.staff.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherProfessionalBo;
import lombok.RequiredArgsConstructor;
import net.pladema.staff.application.ports.HealthcareProfessionalPort;

import net.pladema.staff.repository.HealthcareProfessionalRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HealthcareProfessionalPortImpl implements HealthcareProfessionalPort {

	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Override
	public MedicationRequestValidationDispatcherProfessionalBo getProfessionalDataNeededForMedicationRequestValidation(Integer healthcareProfessionalId) {
		List<MedicationRequestValidationDispatcherProfessionalBo> result = healthcareProfessionalRepository.fetchProfessionalDataNeededForMedicationRequestValidation(healthcareProfessionalId);
		if (result.isEmpty())
			return null; //TODO exception
		return result.get(0);
	}

}
