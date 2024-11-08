package net.pladema.person.controller;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import lombok.RequiredArgsConstructor;
import net.pladema.person.controller.dto.InstitutionUserPersonDto;
import net.pladema.person.repository.domain.InstitutionUserPersonBo;

import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.user.repository.UserPersonRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackofficeInstitutionUserPersonStore implements BackofficeStore<InstitutionUserPersonDto, Integer> {

	private final UserPersonRepository userPersonRepository;

	private final UserRepository userRepository;

	@Override
	public Page<InstitutionUserPersonDto> findAll(InstitutionUserPersonDto example, Pageable pageable) {
		List<InstitutionUserPersonDto> list = userPersonRepository.findAllByInstitutionIdAndUserIds(example.getInstitutionId())
				.stream()
				.map(this::mapToInstitutionPersonDto)
				.collect(Collectors.toList());
		return new PageImpl<>(list, pageable, list.size());
	}

	private InstitutionUserPersonDto mapToInstitutionPersonDto(InstitutionUserPersonBo institutionUserPersonBo) {
		return new InstitutionUserPersonDto(institutionUserPersonBo.getUserId(),
				institutionUserPersonBo.getPersonId(),
				institutionUserPersonBo.getInstitutionId(),
				institutionUserPersonBo.getCompleteName(),
				institutionUserPersonBo.getCompleteLastName(),
				institutionUserPersonBo.getIdentificationNumber());
	}

	@Override
	public List<InstitutionUserPersonDto> findAll() {
		return null;
	}

	@Override
	public List<InstitutionUserPersonDto> findAllById(List<Integer> ids) {
		return userPersonRepository.findByUserIds(ids)
				.stream()
				.map(this::mapToInstitutionPersonDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<InstitutionUserPersonDto> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public InstitutionUserPersonDto save(InstitutionUserPersonDto entity) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Example<InstitutionUserPersonDto> buildExample(InstitutionUserPersonDto entity) {
		return null;
	}
}
