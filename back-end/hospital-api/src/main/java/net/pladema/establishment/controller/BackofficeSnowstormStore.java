package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormSearchItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.controller.service.SnowstormExternalService;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackofficeSnowstormStore implements BackofficeStore<BackofficeSnowstormDto, Long> {

	private final SnowstormExternalService snowstormExternalService;
	private final SnomedService snomedService;

	private final String SNOWSTORM_EXCEPTION = "snowstorm.port.exception";
	
	public Page<BackofficeSnowstormDto> findAll(BackofficeSnowstormDto example, Pageable pageable, SnomedECL ecl) {
		if (!example.getTerm().isBlank() && example.getTerm().length() >= 3){
			try {
				List<BackofficeSnowstormDto> resultSearch = snowstormExternalService.getConcepts(example.getTerm(), ecl.toString())
						.getItems()
						.stream()
						.map(this::mapToBackofficeSnowstormDto)
						.collect(Collectors.toList());
				int listSize = resultSearch.size();
				int maxIndex = Math.min(pageable.getPageSize(), listSize);
				return new PageImpl<>(resultSearch.subList(0, maxIndex), pageable, pageable.getPageSize());
			} catch (SnowstormPortException e){
				throw new BackofficeValidationException(SNOWSTORM_EXCEPTION);
			}
		}
		return new PageImpl<>(Collections.emptyList());
	}


	@Override
	public Page<BackofficeSnowstormDto> findAll(BackofficeSnowstormDto example, Pageable pageable) {
		return new PageImpl<>(Collections.emptyList());
	}

	@Override
	public List<BackofficeSnowstormDto> findAll() {
		return new ArrayList<>();
	}

	@Override
	public List<BackofficeSnowstormDto> findAllById(List<Long> ids) {
		List<BackofficeSnowstormDto> result;
		try{
			result = snowstormExternalService.getConcepts(ids).getItems()
					.stream()
					.map(this::mapToBackofficeSnowstormDto)
					.sorted(Comparator.comparing(BackofficeSnowstormDto::getTerm,
							Comparator.nullsFirst(Comparator.naturalOrder())))
					.collect(Collectors.toList());
		} catch (SnowstormPortException e){
			throw new BackofficeValidationException(SNOWSTORM_EXCEPTION);
		}
		return result;
	}

	@Override
	public Optional<BackofficeSnowstormDto> findById(Long id) {
		BackofficeSnowstormDto result;
		try{
			result = mapToBackofficeSnowstormDto(snowstormExternalService.getConceptById(id.toString()));
		} catch (SnowstormPortException e){
			throw new BackofficeValidationException(SNOWSTORM_EXCEPTION);
		}
		return Optional.of(result);
	}

	@Override
	public BackofficeSnowstormDto save(BackofficeSnowstormDto entity) {
		return entity;
	}

	@Override
	public void deleteById(Long id) {

	}

	@Override
	public Example<BackofficeSnowstormDto> buildExample(BackofficeSnowstormDto entity) {
		return Example.of(entity);
	}

	private BackofficeSnowstormDto mapToBackofficeSnowstormDto (SharedSnowstormSearchItemDto item){
		BackofficeSnowstormDto result = new BackofficeSnowstormDto();
		result.setId(Long.parseLong(item.getConceptId()));
		result.setConceptId(item.getConceptId());
		result.setTerm(item.getPt());
		return result;
	}

	@Transactional
	public Integer saveSnowstormConcept(String conceptId) {
		try {
			String conceptPt = snowstormExternalService.getConceptById(conceptId).getPt();
			if (conceptPt.isEmpty())
				throw new BackofficeValidationException(SNOWSTORM_EXCEPTION);
			var snomedBo = new SnomedBo(conceptId, conceptPt);
			return snomedService.getSnomedId(snomedBo).orElseGet(() -> snomedService.createSnomedTerm(snomedBo));
		} catch (SnowstormPortException e) {
			throw new BackofficeValidationException(SNOWSTORM_EXCEPTION);
		}
	}

}
