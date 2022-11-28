package net.pladema.staff.controller.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.ProfessionalProfessionBackofficeDto;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import net.pladema.staff.repository.entity.ProfessionalProfessions;

@Service
public class BackofficeProfessionalProfessionStore implements BackofficeStore<ProfessionalProfessionBackofficeDto, Integer> {

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
	private final ProfessionalProfessionRepository professionalProfessionRepository;
	private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

    public BackofficeProfessionalProfessionStore(HealthcareProfessionalRepository healthcareProfessionalRepository,
												 ProfessionalProfessionRepository professionalProfessionRepository,
												 HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository){
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
		this.professionalProfessionRepository = professionalProfessionRepository;
		this.healthcareProfessionalSpecialtyRepository = healthcareProfessionalSpecialtyRepository;
	}

    @Override
    public Page<ProfessionalProfessionBackofficeDto> findAll(ProfessionalProfessionBackofficeDto exampleDto, Pageable pageable) {
		return healthcareProfessionalRepository.findByPersonId(exampleDto.getPersonId())
				.map(healthcareProfessional -> {
					var example = new ProfessionalProfessions(healthcareProfessional.getId(), exampleDto.getProfessionalSpecialtyId());
					example.setDeleted(false);
					return example;
				})
				.map(professionalProfessions ->
					professionalProfessionRepository.findAll(
							Example.of(professionalProfessions),
							PageRequest.of(
									pageable.getPageNumber(),
									pageable.getPageSize(),
									Sort.unsorted()
							))
							 .map(this::buildDto)
				)
				.orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));
    }

    @Override
    public List<ProfessionalProfessionBackofficeDto> findAll() {
        return professionalProfessionRepository.findAll()
				.stream()
				.filter(professionalProfessions ->  !professionalProfessions.isDeleted())
				.map(professionalProfessions -> new ProfessionalProfessionBackofficeDto())
				.collect(Collectors.toList());
    }

    @Override
    public List<ProfessionalProfessionBackofficeDto> findAllById(List<Integer> ids) {
        return professionalProfessionRepository.findAllById(ids)
				.stream()
				.filter(professionalProfessions -> !professionalProfessions.isDeleted())
				.map(this::buildDto)
				.collect(Collectors.toList());
    }



	@Override
    public Optional<ProfessionalProfessionBackofficeDto> findById(Integer id) {
        return professionalProfessionRepository.findById(id)
				.map(this::buildDto);
    }

    @Override
    public ProfessionalProfessionBackofficeDto save(ProfessionalProfessionBackofficeDto dto) {
		if (dto.getHealthcareProfessionalId() == null)
			return create(dto);
        return professionalProfessionRepository.findByHealthcareProfessionalIdAndProfessionalSpecialtyId(dto.getHealthcareProfessionalId(),
						dto.getProfessionalSpecialtyId())
				.map(this::reactivate)
				.map(this::buildDto)
				.orElseGet( () -> create(dto));
    }

    private ProfessionalProfessionBackofficeDto create(ProfessionalProfessionBackofficeDto dto) {
		var result = (dto.getHealthcareProfessionalId() == null) ?
				createAll(dto) :
				professionalProfessionRepository
						.findById(dto.getHealthcareProfessionalId())
						.map(this::reactivate)
						.map(this::buildDto)
						.orElseGet( () -> createAll(dto));

		healthcareProfessionalSpecialtyRepository.findByUniqueKey(result.getId(), dto.getClinicalSpecialtyId())
				.map(healthcareProfessionalSpecialtyRepository::reactivate)
				.orElseGet( () -> healthcareProfessionalSpecialtyRepository
						.save(new HealthcareProfessionalSpecialty(result.getId(), dto.getClinicalSpecialtyId())));
		return result;
    }

	private ProfessionalProfessionBackofficeDto createAll(ProfessionalProfessionBackofficeDto dto) {
		var hp = healthcareProfessionalRepository.findByPersonId(dto.getPersonId())
				.orElseGet(() -> healthcareProfessionalRepository.save(new HealthcareProfessional(dto.getPersonId())));
		dto.setHealthcareProfessionalId(hp.getId());
		return buildDto(professionalProfessionRepository
				.findByHealthcareProfessionalIdAndProfessionalSpecialtyId(dto.getHealthcareProfessionalId(), dto.getProfessionalSpecialtyId())
				.map(this::reactivate)
				.orElseGet(() -> professionalProfessionRepository.save(buildEntity(dto))));

	}


	private ProfessionalProfessions reactivate(ProfessionalProfessions professionalProfessions) {
		if (professionalProfessions.isDeleted())
			return professionalProfessionRepository.reactivate(professionalProfessions);
		healthcareProfessionalRepository
				.findById(professionalProfessions.getHealthcareProfessionalId())
				.map(healthcareProfessionalRepository::reactivate);
		return professionalProfessions;
	}

	@Override
    public void deleteById(Integer id) {
		professionalProfessionRepository.findById(id)
				.ifPresent(ppr -> {
					professionalProfessionRepository.deleteById(id);
					healthcareProfessionalSpecialtyRepository.deleteByProfessionalProfessionId(id);
				});
    }

    @Override
    public Example<ProfessionalProfessionBackofficeDto> buildExample(ProfessionalProfessionBackofficeDto entity) {
        return null;
    }

	private ProfessionalProfessionBackofficeDto buildDto(ProfessionalProfessions professionalProfessions) {
		return healthcareProfessionalRepository.findById(professionalProfessions.getHealthcareProfessionalId())
				.map(hp ->
						new ProfessionalProfessionBackofficeDto(
								professionalProfessions.getId(),
								hp.getId(),
								hp.getPersonId(),
								professionalProfessions.getProfessionalSpecialtyId(),
								professionalProfessions.isDeleted())
				).orElseGet(null);
	}

	private ProfessionalProfessions buildEntity(ProfessionalProfessionBackofficeDto dto) {
		return new ProfessionalProfessions(dto.getHealthcareProfessionalId(), dto.getProfessionalSpecialtyId());
	}

}
