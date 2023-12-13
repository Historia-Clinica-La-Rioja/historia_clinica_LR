package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.dto.CareLineProblemDto;
import net.pladema.establishment.repository.CareLineProblemRepository;
import net.pladema.establishment.repository.entity.CareLineProblem;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.controller.service.SnowstormExternalService;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
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

	private final SnomedService snomedService;

	private final SnowstormExternalService snowstormExternalService;

	private final FeatureFlagsService featureFlagsService;

	public BackofficeCareLineProblemStore(CareLineProblemRepository careLineProblemRepository,
										  SnomedService snomedService,
										  SnowstormExternalService snowstormExternalService,
										  FeatureFlagsService featureFlagsService){
		this.careLineProblemRepository=careLineProblemRepository;
		this.snomedService=snomedService;
		this.snowstormExternalService=snowstormExternalService;
		this.featureFlagsService=featureFlagsService;
	}


	@Override
	public Page<CareLineProblemDto> findAll(CareLineProblemDto entity, Pageable pageable) {
		return careLineProblemRepository.findByCareLineId(entity.getCareLineId(), pageable);
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
	public CareLineProblemDto save(CareLineProblemDto dto) {
		Integer snomedId;
		/* If cached concepts search is active, the id of Snomed entity will be on conceptSctid field */
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			snomedId = Integer.valueOf(dto.getConceptSctid());
		/* If not, search the concept term on snowstorm to check if already exists */
		else {
			try {
				String conceptPt = snowstormExternalService.getConceptById(dto.getConceptSctid()).getPt();
				var snomedBo = new SnomedBo(dto.getConceptSctid(), conceptPt);
				snomedId = snomedService.getSnomedId(snomedBo).orElseGet(() -> snomedService.createSnomedTerm(snomedBo));
			} catch (SnowstormPortException e) {
				throw new RuntimeException(e);
			}
		}
		dto.setSnomedId(snomedId);
		Optional<CareLineProblem> careLineProblem = careLineProblemRepository.findByCareLineIdAndSnomedId(dto.getCareLineId(), dto.getSnomedId());
		/* Check if problem is associated to a care line already */
		if(careLineProblem.isPresent()){
			if (careLineProblem.get().isDeleted()) {
				careLineProblem.get().setDeleted(false);
				dto.setId(careLineProblemRepository.save(careLineProblem.get()).getId());
			} else {
				throw new BackofficeValidationException("care-line.problem.exists");
			}
		} else {
			dto.setId(careLineProblemRepository.save(mapToEntity(dto)).getId());
		}
		return dto;
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
