package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.controller.dto.BackofficeHealthcareProfessionalCompleteDto;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;

@Service
public class BackofficeHealthcareProfessionalStore implements BackofficeStore<BackofficeHealthcareProfessionalCompleteDto, Integer> {

    private final HealthcareProfessionalRepository healthcareProfessionalRepository;
    private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

    public BackofficeHealthcareProfessionalStore(HealthcareProfessionalRepository healthcareProfessionalRepository,
                                                 HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository){
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.healthcareProfessionalSpecialtyRepository = healthcareProfessionalSpecialtyRepository;
    }

    @Override
    public Page<BackofficeHealthcareProfessionalCompleteDto> findAll(BackofficeHealthcareProfessionalCompleteDto example, Pageable pageable) {
        HealthcareProfessional hp = buildHealthcareProfessionalEntity(example);

        ExampleMatcher customExampleMatcher = ExampleMatcher.matching().
                withMatcher("licenseNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        return healthcareProfessionalRepository.findAll(
                Example.of(hp, customExampleMatcher),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.unsorted()
                )
        ).map(this::buildHealthcareProfessionalDto);
    }

    @Override
    public List<BackofficeHealthcareProfessionalCompleteDto> findAll() {
        return healthcareProfessionalRepository.findAll().stream()
                .map(this::buildHealthcareProfessionalDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BackofficeHealthcareProfessionalCompleteDto> findAllById(List<Integer> ids) {
        return healthcareProfessionalRepository.findAllById(ids).stream()
                .map(this::buildHealthcareProfessionalDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BackofficeHealthcareProfessionalCompleteDto> findById(Integer id) {
        return toHealthcareProfessionalCreateDto(healthcareProfessionalRepository.findById(id));
    }

    @Override
    public BackofficeHealthcareProfessionalCompleteDto save(BackofficeHealthcareProfessionalCompleteDto dto) {
        if (dto.getId() != null) {
            return update(dto);
        }
        return create(dto);
    }

    private BackofficeHealthcareProfessionalCompleteDto update(BackofficeHealthcareProfessionalCompleteDto dto) {
        HealthcareProfessional hp = buildHealthcareProfessionalEntity(dto);
        hp.setId(dto.getId());
        healthcareProfessionalRepository.save(hp);
        return dto;
    }

    private BackofficeHealthcareProfessionalCompleteDto create(BackofficeHealthcareProfessionalCompleteDto dto) {
        HealthcareProfessional hp = healthcareProfessionalRepository.save(buildHealthcareProfessionalEntity(dto));
        dto.setId(hp.getId());
        healthcareProfessionalSpecialtyRepository.save(buildHealthcareProfessionalSpecialty(dto));
        return dto;
    }

    @Override
    public void deleteById(Integer id) {
        healthcareProfessionalRepository.deleteById(id);
    }

    @Override
    public Example<BackofficeHealthcareProfessionalCompleteDto> buildExample(BackofficeHealthcareProfessionalCompleteDto entity) {
        return null;
    }

    private HealthcareProfessional buildHealthcareProfessionalEntity(BackofficeHealthcareProfessionalCompleteDto dto){
        HealthcareProfessional hp = new HealthcareProfessional();
        hp.setPersonId(dto.getPersonId());
        hp.setLicenseNumber(dto.getLicenseNumber());
        hp.setDeleted(dto.isDeleted());
        return hp;
    }

    private HealthcareProfessionalSpecialty buildHealthcareProfessionalSpecialty(BackofficeHealthcareProfessionalCompleteDto dto){
        HealthcareProfessionalSpecialty hps = new HealthcareProfessionalSpecialty();
        hps.setClinicalSpecialtyId(dto.getClinicalSpecialtyId());
        hps.setProfessionalProfessionsId(dto.getId());
        return hps;
    }


    private Optional<BackofficeHealthcareProfessionalCompleteDto> toHealthcareProfessionalCreateDto(Optional<HealthcareProfessional> byId) {
        return byId.map(this::buildHealthcareProfessionalDto);
    }

    private BackofficeHealthcareProfessionalCompleteDto buildHealthcareProfessionalDto(HealthcareProfessional byId) {
		BackofficeHealthcareProfessionalCompleteDto dto = new BackofficeHealthcareProfessionalCompleteDto();
		dto.setPersonId(byId.getPersonId());
		dto.setId(byId.getId());
		if(byId.getLicenseNumber().isBlank())
			dto.setLicenseNumber("Número de matrícula no definido");
		else
			dto.setLicenseNumber(byId.getLicenseNumber());
		return dto;
    }

}
