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

@Service
@RequiredArgsConstructor
public class BackofficePersonStore implements BackofficeStore<Person, Integer> {


	private final PersonRepository personRepository;

	@Override
	public Page<Person> findAll(Person entity, Pageable pageable) {
		if ((entity.getIdentificationNumber() != null))
			entity.setIdentificationNumber(entity.getIdentificationNumber().replace(".", ""));

		return personRepository.findAll(buildExample(entity), pageable);
	}

	@Override
	public List<Person> findAll() {
		List<Integer> activePersonIds = personRepository.findAllActive();
		return personRepository.findAll()
				.stream().filter(p -> activePersonIds.contains(p.getId())).collect(Collectors.toList());
	}

	@Override
	public List<Person> findAllById(List<Integer> ids) {
		return personRepository.findAllById(ids);
	}

	@Override
	public Optional<Person> findById(Integer id) {
		return personRepository.findById(id);
	}


	@Override
	public Person save(Person entity) {
		return personRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		personRepository.deleteById(id);
	}

	@Override
	public Example<Person> buildExample(Person entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matching().withMatcher("identificationNumber",
				ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("identificationNumber", ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("firstName", x -> x.ignoreCase().contains())
				.withMatcher("lastName", x -> x.ignoreCase().contains());
		return Example.of(entity, customExampleMatcher);
	}

}
