package net.pladema.person.infraestructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;
import net.pladema.person.service.PersonService;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharedPersonImpl implements SharedPersonPort {

	private final PersonService personService;

	@Override
	public String getCountryIsoCodeFromPerson(Integer personId) {
		return personService.getCountryIsoCodeFromPerson(personId);
	}

}
