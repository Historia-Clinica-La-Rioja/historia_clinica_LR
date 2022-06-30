package net.pladema.person.infraestructure.output.notification;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;
import net.pladema.person.repository.PersonRepository;

@Service
@AllArgsConstructor
public class PersonRecipientMapper implements RecipientMapper<PersonRecipient> {
	private final PersonRepository personRepository;

	@Override
	public Optional<RecipientBo> toRecipient(PersonRecipient personRecipient) {
		return personRepository.getPersonRecipient(personRecipient.personId)
				.map(personRecipientVo -> new RecipientBo(
						personRecipientVo.firstName,
						personRecipientVo.lastName,
						personRecipientVo.email,
						personRecipientVo.phonePrefix + personRecipientVo.phoneNumber
				));
	}
}
