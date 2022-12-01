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

import net.pladema.establishment.controller.dto.EPacServerProtocol;
import net.pladema.establishment.controller.dto.PacServerProtocolDto;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficePacServerProtocolStore implements BackofficeStore<PacServerProtocolDto, Short> {

	@Override
	public Page<PacServerProtocolDto> findAll(PacServerProtocolDto example, Pageable pageable) {
		List<PacServerProtocolDto> list = getPacServerProtocol();
		return new PageImpl<>(list, pageable, list.size());
	}

	private List<PacServerProtocolDto> getPacServerProtocol() {
		return Stream.of(EPacServerProtocol.values())
				.map(psp -> new PacServerProtocolDto(psp.getId(), psp.getDescription()))
				.collect(Collectors.toList());
	}

	@Override
	public List<PacServerProtocolDto> findAll() {
		return getPacServerProtocol();
	}

	@Override
	public List<PacServerProtocolDto> findAllById(List<Short> ids) {
		if (ids.isEmpty())
			return getPacServerProtocol();
		return getPacServerProtocol().stream()
				.filter(dto -> ids.contains(dto.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<PacServerProtocolDto> findById(Short id) {
		return getPacServerProtocol().stream()
				.filter(dto -> id.equals(dto.getId())).findFirst();
	}

	@Override
	public PacServerProtocolDto save(PacServerProtocolDto entity) { return null; }

	@Override
	public void deleteById(Short id) {}

	@Override
	public Example<PacServerProtocolDto> buildExample(PacServerProtocolDto entity) {
		return Example.of(entity);
	}
}
