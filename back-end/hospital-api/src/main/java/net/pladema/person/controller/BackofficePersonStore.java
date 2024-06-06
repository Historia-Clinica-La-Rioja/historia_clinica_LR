package net.pladema.person.controller;

import lombok.RequiredArgsConstructor;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.pladema.person.repository.BackofficePersonDto;
import net.pladema.person.repository.PersonExtendedRepository;
import net.pladema.person.repository.entity.PersonExtended;

@Service
@RequiredArgsConstructor
public class BackofficePersonStore implements BackofficeStore<BackofficePersonDto, Integer> {


	private final PersonRepository personRepository;

	private final PersonExtendedRepository personExtendedRepository;

	@Override
	public Page<BackofficePersonDto> findAll(BackofficePersonDto entity, Pageable pageable) {
		if ((entity.getIdentificationNumber() != null))
			entity.setIdentificationNumber(entity.getIdentificationNumber().replace(".", ""));

		return personRepository.findAll(buildExample(mapToEntity(entity)), pageable)
				.map(BackofficePersonDto::new);
	}

	private Person mapToEntity(BackofficePersonDto dto){
		return new Person(dto.getId(),
				dto.getFirstName(),
				dto.getMiddleNames(),
				dto.getLastName(),
				dto.getOtherLastNames(),
				dto.getIdentificationTypeId(),
				dto.getIdentificationNumber(),
				dto.getGenderId(),
				dto.getBirthDate());
	}

	@Override
	public List<BackofficePersonDto> findAll() {
		List<Integer> activePersonIds = personRepository.findAllActive();
		return personRepository.findAll()
				.stream().filter(p -> activePersonIds.contains(p.getId()))
				.map(BackofficePersonDto::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<BackofficePersonDto> findAllById(List<Integer> ids) {
		return personRepository.findAllById(ids).stream().map(BackofficePersonDto::new).collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficePersonDto> findById(Integer id) {
		return personRepository.findById(id).map(person -> {
			PersonExtended pex = personExtendedRepository.getById(person.getId());
			BackofficePersonDto result = new BackofficePersonDto(person);
			result.setEmail(pex.getEmail());
			return result;
		});
	}


	@Override
	public BackofficePersonDto save(BackofficePersonDto entity) {
		Person p = personRepository.save(mapToEntity(entity));
		BackofficePersonDto result = new BackofficePersonDto(p);
		if(entity.getEmail() != null){
			personExtendedRepository.setEmail(entity.getEmail(), entity.getId());
			result.setEmail(entity.getEmail());
		}
		return result;
	}

	@Override
	public void deleteById(Integer id) {
		personRepository.deleteById(id);
	}

	@Override
	public Example<BackofficePersonDto> buildExample(BackofficePersonDto entity) {
		return null;
	}

	public Example<Person> buildExample(Person entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matching().withMatcher("identificationNumber",
				ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("identificationNumber", ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("firstName", x -> x.ignoreCase().contains())
				.withMatcher("lastName", x -> x.ignoreCase().contains());
		return Example.of(entity, customExampleMatcher);
	}

}
