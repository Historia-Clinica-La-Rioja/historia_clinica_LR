package net.pladema.person.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
@RequiredArgsConstructor
public class BackofficePersonStore implements BackofficeStore<Person, Integer> {


	private final PersonRepository personRepository;

	@Override
	public Page<Person> findAll(Person entity, Pageable pageable) {
		if ((entity.getIdentificationNumber() != null))
			entity.setIdentificationNumber(entity.getIdentificationNumber().replace(".", ""));
		List<Integer> activePersonIds = personRepository.findAllActive();
		List<Person> result =  personRepository.findAll(buildExample(entity), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted()))
				.filter(p -> activePersonIds.contains(p.getId())).toList();
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
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
