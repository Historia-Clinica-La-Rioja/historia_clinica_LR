package net.pladema.establishment.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.exceptions.BackofficeMedicalCoverageEnumException;
import net.pladema.establishment.controller.exceptions.BackofficeMedicalCoverageException;
import net.pladema.patient.controller.dto.BackofficeCoverageDto;
import net.pladema.patient.controller.dto.EMedicalCoverageType;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceRepository;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.patient.repository.entity.PrivateHealthInsurance;
import net.pladema.person.repository.HealthInsuranceRepository;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackofficeMedicalCoverageStore implements BackofficeStore<BackofficeCoverageDto, Integer> {

    private final MedicalCoverageRepository medicalCoverageRepository;

    private final PrivateHealthInsuranceRepository privateHealthInsuranceRepository;

    private final HealthInsuranceRepository healthInsuranceRepository;

    @Override
    public Page<BackofficeCoverageDto> findAll(BackofficeCoverageDto coverage, Pageable pageable) {
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        List<BackofficeCoverageDto> result = medicalCoverageRepository.findAll(Example.of(new MedicalCoverage(coverage.getId(), coverage.getName(),coverage.getCuit(), coverage.getType()), customExampleMatcher)).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        if(coverage.getType()!=null)
            result = result.stream().filter(backofficeCoverage -> backofficeCoverage.getType().equals(coverage.getType())).collect(Collectors.toList());
        if(coverage.getEnabled()!=null)
            result = result.stream().filter(backofficeCoverage -> backofficeCoverage.getEnabled().equals(coverage.getEnabled())).collect(Collectors.toList());

        int minIndex = pageable.getPageNumber() * pageable.getPageSize();
        int maxIndex = minIndex + pageable.getPageSize();
        return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
    }

    @Override
    public List<BackofficeCoverageDto> findAll() {
        return medicalCoverageRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BackofficeCoverageDto> findAllById(List<Integer> ids) {
        return medicalCoverageRepository.findAllById(ids).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BackofficeCoverageDto> findById(Integer id) {
        return medicalCoverageRepository.findById(id)
                .map(this::mapToDto);
    }

    @Override
    public BackofficeCoverageDto save(BackofficeCoverageDto dto) {
        if (dto.getId() != null) {
            return update(dto);
        }
        return create(dto);
    }

	private BackofficeCoverageDto update(BackofficeCoverageDto dto) {
		if (dto.getRnos() != null && healthInsuranceRepository.existsByRnosAndDiferentId(dto.getRnos(),dto.getId()))
			throw new BackofficeValidationException("medical-coverage.rnos-duplicated");
		if(medicalCoverageRepository.existsByCUITandDiferentId(dto.getCuit(),dto.getId()))
			throw new BackofficeValidationException("medical-coverage.cuit-duplicated");
		dto.setCuit(setAndValidateCuit(dto.getCuit()));
		BackofficeCoverageDto result = null;
		switch (EMedicalCoverageType.map(dto.getType())){
			case PREPAGA: result =  mapToDto(updatePrivateHealthInsurance(dto));
				break;
			case OBRASOCIAL: result = mapToDto(updateHealthInsurance(dto));
				break;
			case ART: result = mapToDto(updateART(dto));
		}
		return result;
	}

	private MedicalCoverage updatePrivateHealthInsurance(BackofficeCoverageDto dto) {
		return privateHealthInsuranceRepository.findById(dto.getId()).map(privateHealthInsurance -> {
			privateHealthInsurance.setName(dto.getName());
			privateHealthInsurance.setCuit(dto.getCuit());
			return (MedicalCoverage) medicalCoverageRepository.save(privateHealthInsurance);
		}).orElseThrow(() -> new BackofficeMedicalCoverageException(BackofficeMedicalCoverageEnumException.UNEXISTED_MEDICAL_COVERAGE, String.format("La prepaga con id %s no existe", dto.getId())));
	}

	private MedicalCoverage updateHealthInsurance(BackofficeCoverageDto dto) {
		return medicalCoverageRepository.findById(dto.getId()).map(medicalCoverage -> {
            HealthInsurance healthInsurance = (HealthInsurance) medicalCoverage;
            healthInsurance.setAcronym(dto.getAcronym());
            healthInsurance.setRnos(dto.getRnos());
            healthInsurance.setName(dto.getName());
            healthInsurance.setCuit(dto.getCuit());
            return medicalCoverageRepository.save(healthInsurance);
        }).orElseThrow(() -> new BackofficeMedicalCoverageException(BackofficeMedicalCoverageEnumException.UNEXISTED_MEDICAL_COVERAGE, String.format("La obra social con id %s no existe", dto.getId())));
    }

	private MedicalCoverage updateART(BackofficeCoverageDto dto) {
		return medicalCoverageRepository.findById(dto.getId()).map(medicalCoverage -> {
			medicalCoverage.setName(dto.getName());
			medicalCoverage.setCuit(dto.getCuit());
			return medicalCoverageRepository.save(medicalCoverage);
		}).orElseThrow(() -> new BackofficeMedicalCoverageException(BackofficeMedicalCoverageEnumException.UNEXISTED_MEDICAL_COVERAGE, String.format("La cobertura ART con id %s no existe", dto.getId())));
	}

    private BackofficeCoverageDto create(BackofficeCoverageDto dto) {
        if (dto.getRnos() != null && healthInsuranceRepository.existsByRnos(dto.getRnos()))
            throw new BackofficeValidationException("medical-coverage.rnos-duplicated");
        if (medicalCoverageRepository.existsByCUIT(dto.getCuit()))
            throw new BackofficeValidationException("medical-coverage.cuit-duplicated");
        dto.setCuit(setAndValidateCuit(dto.getCuit()));
        MedicalCoverage entity = null;
		switch (EMedicalCoverageType.map(dto.getType())){
			case PREPAGA: entity =  new PrivateHealthInsurance(dto.getId(),dto.getName(), dto.getCuit(), dto.getType());
				break;
			case OBRASOCIAL: entity = new HealthInsurance(dto.getId(),dto.getName(), dto.getCuit(),dto.getRnos(),dto.getAcronym(), dto.getType());
				break;
			case ART : entity = new MedicalCoverage(dto.getId(), dto.getName(), dto.getCuit(), dto.getType());
        }
        entity = medicalCoverageRepository.save(entity);
        return mapToDto(entity);
    }

    private String setAndValidateCuit(String cuit){
        String result =  Arrays.stream(cuit.split("-")).collect(Collectors.joining());
        if(!(StringUtils.isNumeric(result)))
            throw new BackofficeValidationException("medical-coverage.invalid-cuit");
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        medicalCoverageRepository.deleteById(id);
    }

    @Override
    public Example<BackofficeCoverageDto> buildExample(BackofficeCoverageDto entity) {
        return Example.of(entity);
    }

    private BackofficeCoverageDto mapToDto(MedicalCoverage entity) {
		switch (EMedicalCoverageType.map(entity.getType())) {
			case PREPAGA:
				return privateHealthInsuranceRepository.findById(entity.getId()).map(insurance -> new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(), EMedicalCoverageType.PREPAGA.getId(), null, null, !insurance.isDeleted())).get();
			case OBRASOCIAL:
				return healthInsuranceRepository.findById(entity.getId()).map(healthInsurance -> new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(), EMedicalCoverageType.OBRASOCIAL.getId(), healthInsurance.getRnos(), healthInsurance.getAcronym(), !healthInsurance.isDeleted()))
						.orElse(new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(),EMedicalCoverageType.OBRASOCIAL.getId(), null,null, true));
			default:
				return medicalCoverageRepository.findById(entity.getId()).map(medicalCoverage -> new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(), EMedicalCoverageType.ART.getId(), null, null, !medicalCoverage.isDeleted())).get();
        }
    }

}
