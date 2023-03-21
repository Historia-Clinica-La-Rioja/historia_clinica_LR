package net.pladema.user.infrastructure.output.notification;

import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;


import net.pladema.person.infraestructure.output.notification.PersonRecipient;
import net.pladema.person.infraestructure.output.notification.PersonRecipientMapper;
import net.pladema.user.repository.UserPersonRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRecipientMapper implements RecipientMapper<UserRecipient> {

	private final UserPersonRepository userPersonRepository;
	private final PersonRecipientMapper personRecipientMapper;

	@Override
	public Optional<RecipientBo> toRecipient(UserRecipient userRecipient) {
		return userPersonRepository.getPersonIdByUserId(userRecipient.userId)
				.map(PersonRecipient::new)
				.flatMap(personRecipientMapper::toRecipient);
	}

}
