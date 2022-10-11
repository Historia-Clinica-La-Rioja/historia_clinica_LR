package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import net.pladema.establishment.controller.dto.CareLineProblemDto;
import net.pladema.establishment.repository.CareLineProblemRepository;
import net.pladema.establishment.repository.entity.CareLineProblem;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.controller.service.SnowstormExternalService;

import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeCareLineProblemStore implements BackofficeStore<CareLineProblemDto, Integer> {

	private final CareLineProblemRepository careLineProblemRepository;

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	private final SnomedRepository snomedRepository;

	private final SnowstormExternalService snowstormExternalService;

	public BackofficeCareLineProblemStore(CareLineProblemRepository careLineProblemRepository,
										  SnomedRelatedGroupRepository snomedRelatedGroupRepository,
										  SnomedRepository snomedRepository,
										  SnowstormExternalService snowstormExternalService){
		this.careLineProblemRepository=careLineProblemRepository;
		this.snomedRelatedGroupRepository=snomedRelatedGroupRepository;
		this.snomedRepository=snomedRepository;
		this.snowstormExternalService=snowstormExternalService;
	}


	@Override
	public Page<CareLineProblemDto> findAll(CareLineProblemDto entity, Pageable pageable) {
		List<CareLineProblemDto> result = careLineProblemRepository.findByCareLineId(entity.getCareLineId())
				.stream()
				.map(this::mapToCareLineProblemDto)
				.collect(Collectors.toList());
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<CareLineProblemDto> findAll() {
		return careLineProblemRepository.findAll()
				.stream()
				.map(this::mapToCareLineProblemDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<CareLineProblemDto> findAllById(List<Integer> ids) {

		return careLineProblemRepository.findAllById(ids)
				.stream()
				.map(this::mapToCareLineProblemDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<CareLineProblemDto> findById(Integer id) {
		return careLineProblemRepository.findById(id).map(this::mapToCareLineProblemDto);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@Transactional
	public CareLineProblemDto save(CareLineProblemDto entity) {
		Optional<Integer> snomedId = snomedRepository.findLatestIdBySctid(entity.getConceptSctid())
				.or(() -> snomedRelatedGroupRepository.getSnomedIdById(Integer.parseInt(entity.getConceptSctid())));
		/* Check if Snowstorm concept exists in snomed cache */
		if(snomedId.isPresent()){
			entity.setSnomedId(snomedId.get());
			Optional<CareLineProblem> careLineProblem = careLineProblemRepository.findByCareLineIdAndSnomedId(entity.getCareLineId(), entity.getSnomedId());
			/* Check if problem is associated to a care line already */
			if(careLineProblem.isPresent()){
				if (careLineProblem.get().isDeleted()) {
					careLineProblem.get().setDeleted(false);
					return mapToCareLineProblemDto(careLineProblemRepository.save(careLineProblem.get()));
				} else {
					throw new BackofficeValidationException("care-line.problem.exists");
				}
			} else {
				return mapToCareLineProblemDto(careLineProblemRepository.save(mapToEntity(entity)));
			}
		} else {
		/* If not, save concept in Snomed cache first */
			try {
				var concept = snowstormExternalService.getConceptById(entity.getConceptSctid());
				Snomed snomed = snomedRepository.save(new Snomed(concept.getConceptId(), concept.getPt(), concept.getConceptId(), concept.getPt()));
				entity.setSnomedId(snomed.getId());
				return mapToCareLineProblemDto(careLineProblemRepository.save(mapToEntity(entity)));
			} catch (SnowstormPortException e){
				throw new BackofficeValidationException(e.getMessage());
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void deleteById(Integer id) {
		careLineProblemRepository.deleteById(id);
	}

	@Override
	public Example<CareLineProblemDto> buildExample(CareLineProblemDto entity) {
		return Example.of(entity);
	}

	private CareLineProblemDto mapToCareLineProblemDto(CareLineProblem entity){
		CareLineProblemDto result = new CareLineProblemDto();
		result.setId(entity.getId());
		result.setCareLineId(entity.getCareLineId());
		result.setSnomedId(entity.getSnomedId());
		return result;
	}

	private CareLineProblem mapToEntity(CareLineProblemDto dto){
		CareLineProblem result = new CareLineProblem();
		result.setId(dto.getId());
		result.setCareLineId(dto.getCareLineId());
		result.setSnomedId(dto.getSnomedId());
		return result;
	}

}
