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
        List<BackofficeCoverageDto> result = medicalCoverageRepository.findAll(Example.of(new MedicalCoverage(coverage.getId(), coverage.getName(),coverage.getCuit()), customExampleMatcher)).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
        if (dto.getRnos() != null && healthInsuranceRepository.existsByRnos(dto.getRnos()))
            throw new BackofficeValidationException("medical-coverage.rnos-duplicated");
        if(medicalCoverageRepository.existsByCUITandDiferentId(dto.getCuit(),dto.getId()))
                throw new BackofficeValidationException("medical-coverage.cuit-duplicated");
        String cuit = setAndValidateCuit(dto.getCuit());
        return medicalCoverageRepository.findById(dto.getId())
                .map(medicalCoverage -> privateHealthInsuranceRepository.findById(medicalCoverage.getId())
                        .map(privateHealthInsurance -> {
                            privateHealthInsurance.setPlan(dto.getPlan());
                            privateHealthInsurance.setName(dto.getName());
                            privateHealthInsurance.setCuit(cuit);
                            return (MedicalCoverage) medicalCoverageRepository.save(privateHealthInsurance);
                        })
                        .orElseGet(() -> {
                            HealthInsurance healthInsurance = (HealthInsurance) medicalCoverage;
                            healthInsurance.setAcronym(dto.getAcronym());
                            healthInsurance.setRnos(dto.getRnos());
                            healthInsurance.setName(dto.getName());
                            healthInsurance.setCuit(cuit);
                            return medicalCoverageRepository.save(healthInsurance);
                        })).map(this::mapToDto)
                .orElseThrow(() -> new BackofficeMedicalCoverageException(BackofficeMedicalCoverageEnumException.UNEXISTED_MEDICAL_COVERAGE, String.format("La cobertura medica con id %s no existe", dto.getId())));
    }
    private BackofficeCoverageDto create(BackofficeCoverageDto dto) {
        if (dto.getRnos() != null && healthInsuranceRepository.existsByRnos(dto.getRnos()))
            throw new BackofficeValidationException("medical-coverage.rnos-duplicated");
        if (medicalCoverageRepository.existsByCUIT(dto.getCuit()))
            throw new BackofficeValidationException("medical-coverage.cuit-duplicated");
        String cuit = setAndValidateCuit(dto.getCuit());
        MedicalCoverage entity = (dto.getType()== EMedicalCoverageType.OBRASOCIAL.getId())
                ? new HealthInsurance(dto.getId(),dto.getName(), cuit,dto.getRnos(),dto.getAcronym())
                : new PrivateHealthInsurance(dto.getId(),dto.getName(), cuit, dto.getPlan());
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
        return privateHealthInsuranceRepository.findById(entity.getId()).map(insurance -> new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(),EMedicalCoverageType.PREPAGA.getId(), null, null, insurance.getPlan()))
                .orElseGet(() -> healthInsuranceRepository.findById(entity.getId())
                        .map(healthInsurance -> new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(),EMedicalCoverageType.OBRASOCIAL.getId(), healthInsurance.getRnos(), healthInsurance.getAcronym(), null))
                        .orElse(new BackofficeCoverageDto(entity.getId(), entity.getName(), entity.getCuit(),EMedicalCoverageType.OBRASOCIAL.getId(), null,null,null)));
    }

}
