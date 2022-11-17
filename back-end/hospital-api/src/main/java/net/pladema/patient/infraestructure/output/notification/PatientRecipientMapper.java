package net.pladema.patient.infraestructure.output.notification;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.infraestructure.output.notification.PersonRecipient;
import net.pladema.person.infraestructure.output.notification.PersonRecipientMapper;

@Service
@AllArgsConstructor
public class PatientRecipientMapper implements RecipientMapper<PatientRecipient> {
	private final PatientRepository patientRepository;
	private final PersonRecipientMapper personRecipientMapper;
	@Override
	public Optional<RecipientBo> toRecipient(PatientRecipient patientRecipient) {
		return patientRepository.findById(patientRecipient.patientId)
				.map(Patient::getPersonId)
				.map(PersonRecipient::new)
				.flatMap(personRecipientMapper::toRecipient);
	}

}
