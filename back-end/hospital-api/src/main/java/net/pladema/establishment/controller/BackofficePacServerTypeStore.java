package net.pladema.establishment.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.pladema.establishment.controller.dto.PacServerTypeDto;
import net.pladema.establishment.controller.dto.EPacServerType;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficePacServerTypeStore implements BackofficeStore<PacServerTypeDto, Short> {

	@Override
	public Page<PacServerTypeDto> findAll(PacServerTypeDto example, Pageable pageable) {
		List<PacServerTypeDto> list = getPacServerType();
		return new PageImpl<>(list, pageable, list.size());
	}

	private List<PacServerTypeDto> getPacServerType() {
		return Stream.of(EPacServerType.values())
				.map(pst -> new PacServerTypeDto(pst.getId(), pst.getDescription()))
				.collect(Collectors.toList());
	}

	@Override
	public List<PacServerTypeDto> findAll() {
		return getPacServerType();
	}

	@Override
	public List<PacServerTypeDto> findAllById(List<Short> ids) {
		if (ids.isEmpty())
			return getPacServerType();
		return getPacServerType().stream()
				.filter(dto -> ids.contains(dto.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<PacServerTypeDto> findById(Short id) {
		return getPacServerType().stream()
				.filter(dto -> id.equals(dto.getId())).findFirst();
	}

	@Override
	public PacServerTypeDto save(PacServerTypeDto entity) { return null; }

	@Override
	public void deleteById(Short id) {}

	@Override
	public Example<PacServerTypeDto> buildExample(PacServerTypeDto entity) {
		return Example.of(entity);
	}
}
