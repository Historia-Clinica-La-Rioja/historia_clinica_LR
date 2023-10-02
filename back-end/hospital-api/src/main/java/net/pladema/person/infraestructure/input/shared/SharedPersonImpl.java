package net.pladema.person.infraestructure.input.shared;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.person.service.PersonService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedPersonImpl implements SharedPersonPort {

	private final PersonService personService;

	@Override
	public String getCountryIsoCodeFromPerson(Integer personId) {
		return personService.getCountryIsoCodeFromPerson(personId);
	}

	@Override
	public String getPersonFullNameById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		return personService.findPerson(personId)
				.map(person -> FhirString.joining(person.getFirstName(), person.getMiddleNames(), person.getLastName(), person.getOtherLastNames()))
				.orElseThrow(()-> new NotFoundException("person-not-found", String.format("La persona con id %s no existe", personId)));
	}

	@Override
	public String getCompletePersonNameById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		return personService.getCompletePersonNameById(personId);
	}

}
